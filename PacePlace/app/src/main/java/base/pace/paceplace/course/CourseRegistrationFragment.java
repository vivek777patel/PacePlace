package base.pace.paceplace.course;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import base.pace.paceplace.R;
import base.pace.paceplace.httpclient.CourseDetailsHttpClient;
import base.pace.paceplace.util.CommonWSJSONArrayInvoke;
import base.pace.paceplace.util.PacePlaceConstants;
import base.pace.paceplace.util.WebServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRegistrationFragment extends Fragment {

    public final String TAG = "CourseRegistrationFrg";

    CourseRegistrationListViewAdapter mAdapter;
    ArrayList<CourseDetail> mCourseLists = new ArrayList<>();
    ArrayList<Integer> mAddedCourses = new ArrayList();
    Map<Integer, CourseDetail> mAddedCourseDetailMap = new HashMap<>();
    ListView mListView;
    Button mSaveRegistration, mResetRegistration;

    private SwipeRefreshLayout mSwipeContainer;
    private int mLoggedInUserId;

    AVLoadingIndicatorView mAVLoadingIndicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_registration_fragement, vg, false);
        mListView = view.findViewById(R.id.courses_registration_listview);
        mSaveRegistration = view.findViewById(R.id.save_registration_button);
        mResetRegistration = view.findViewById(R.id.reset_button);
        mSwipeContainer = view.findViewById(R.id.course_registration_swipe_container);
        mAVLoadingIndicatorView = view.findViewById(R.id.course_registration_avi);

        Bundle b = getArguments();
        mLoggedInUserId = b.getInt(PacePlaceConstants.USER_ID);
        mAVLoadingIndicatorView.bringToFront();
        mSwipeContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        mCourseLists.clear();
                        getCoursesForRegistration();
                    }
                }
        );

        getCoursesForRegistration();
        return view;
    }

    private void getCoursesForRegistration() {
        CourseDetailsHttpClient.getInstance().getCourseForRegistration(String.valueOf(mLoggedInUserId), new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    if (response.body() != null) {
                        JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                        if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                            // TODO : Set mCourseList variable before calling setDefaultCourseDetailFragment()
                            try {
                                JSONArray courseDetailsJsonArray = (JSONArray) receivedJSONObject.get(PacePlaceConstants.DATA);
                                if (courseDetailsJsonArray != null) {
                                    for (int i = 0; i < courseDetailsJsonArray.length(); i++) {
                                        JSONObject obj = courseDetailsJsonArray.getJSONObject(i);
                                        mCourseLists.add(setCourseDetailsFromResponse(obj));
                                    }
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    // No data found
                                    generateToastMessage(R.string.course_no_data_found);
                                }

                            } catch (JSONException e) {
                                generateToastMessage(R.string.course_register_issue_in_response_json);
                                e.printStackTrace();
                            }
                        } else {
                            generateToastMessage(R.string.course_register_issue_in_response_json);
                        }
                    } else {
                        generateToastMessage(R.string.course_register_issue_in_response_json);
                    }
                    mSwipeContainer.setRefreshing(false);
                    mAVLoadingIndicatorView.smoothToHide();

                } catch (JSONException e) {
                    generateToastMessage(R.string.course_register_issue_in_response_json);
                    e.printStackTrace();
                }
                mAVLoadingIndicatorView.smoothToHide();
                mSwipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.course_register_issue_in_response_json);
                mAVLoadingIndicatorView.smoothToHide();
                mSwipeContainer.setRefreshing(false);
            }
        });
    }

    public CourseDetail setCourseDetailsFromResponse(JSONObject jsonObject) {
        return new CourseDetail(
                jsonObject.opt("course_id") != null ? jsonObject.optInt("course_id") : 0,
                jsonObject.optString("course_name"),
                jsonObject.optString("firstname"),
                jsonObject.optString("si_cd_course_day.static_combo_value"),
                jsonObject.optString("course_time"),
                jsonObject.optString("location_name"),
                jsonObject.optString("address_line1"),
                jsonObject.opt("course_det_id") != null ? jsonObject.optInt("course_det_id") : 0
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureCourseList();
        mSaveRegistration.setOnClickListener(saveRegistrationData());
        mResetRegistration.setOnClickListener(resetRegistrationData());
    }

    private void configureCourseList() {

        mAdapter = new CourseRegistrationListViewAdapter(getActivity(), mCourseLists, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                CourseDetail displayCourseInfo = (CourseDetail) mAdapter.getItem(position);
                //int courseId = displayCourseInfo.getmCourseId();
                int courseId = displayCourseInfo.getmCourseDetId();

                if (mAddedCourses.contains(courseId)) {
                    //mAddedCourses.remove(courseId);
                    mAddedCourses.remove(Integer.valueOf(courseId));
                    mAddedCourseDetailMap.remove(courseId);
                    displayCourseInfo.setmCourseSelectedIndicator(false);
                } else {
                    mAddedCourses.add(courseId);
                    mAddedCourseDetailMap.put(courseId, displayCourseInfo);
                    displayCourseInfo.setmCourseSelectedIndicator(true);
                    // Log.i("REG",""+mAddedCourses);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mListView.setAdapter(mAdapter);
        mListView.setItemsCanFocus(false);
    }

    private View.OnClickListener resetRegistrationData() {
        View.OnClickListener resetCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CourseDetail courseData : mCourseLists) {
                    if (courseData.getmCourseSelectedIndicator()) {
                        courseData.setmCourseSelectedIndicator(false);
                    }
                }
                mAddedCourses.clear();
                mAdapter.notifyDataSetChanged();
            }
        };
        return resetCL;
    }

    private View.OnClickListener saveRegistrationData() {
        View.OnClickListener resetCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedData();
            }
        };
        return resetCL;
    }

    private JSONArray arrayListToJSONArray() {
        JSONArray finalJSONArray = new JSONArray();
        try {
            for (Map.Entry<Integer, CourseDetail> entry : mAddedCourseDetailMap.entrySet()) {
                CourseDetail value = entry.getValue();
                JSONObject jo = new JSONObject();
                jo.put(PacePlaceConstants.USER_ID, mLoggedInUserId);
                jo.put(PacePlaceConstants.COURSE_DET_ID, value.getmCourseDetId());
                finalJSONArray.put(jo);
            }
            //ja.put();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalJSONArray;
    }

    private void saveSelectedData() {
        mAVLoadingIndicatorView.smoothToShow();
        Thread threadA = new Thread() {
            public void run() {
                CommonWSJSONArrayInvoke threadB = new CommonWSJSONArrayInvoke(getActivity());
                WebServiceResponse jsonObject = null;
                try {
                    jsonObject = threadB.execute(PacePlaceConstants.URL_SAVE_REGISTERED_COURSES, PacePlaceConstants.COURSES_REGISTRATION,
                            arrayListToJSONArray()).get(30, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
                final WebServiceResponse receivedJSONObject = jsonObject;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Response is: " + receivedJSONObject);
                        if (null != receivedJSONObject) {
                            if (receivedJSONObject.getmResponse()) {
                                // TODO : After data is saved remove from current list or reload the page and clear the selected list
                                Log.i(TAG, "DATA Saved successfully");
                                generateToastMessage(R.string.course_registration_success);
                                mAddedCourses.clear();
                                mAddedCourseDetailMap.clear();
                                getCoursesForRegistration();
                            } else {
                                generateToastMessage(R.string.register_failed);
                            }
                        } else {
                            generateToastMessage(R.string.register_failed);
                        }
                        mAVLoadingIndicatorView.smoothToHide();
                    }
                });
            }
        };
        threadA.start();
    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }

}
