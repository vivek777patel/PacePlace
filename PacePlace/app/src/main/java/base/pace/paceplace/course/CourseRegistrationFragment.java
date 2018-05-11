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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import base.pace.paceplace.R;
import base.pace.paceplace.httpclient.CourseDetailsHttpClient;
import base.pace.paceplace.util.PacePlaceConstants;
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
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.bringToFront();

        CourseDetailsHttpClient.getInstance().getCourseForRegistration(String.valueOf(mLoggedInUserId), new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    if (response.body() != null) {
                        JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                        if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                            JSONArray courseDetailsJsonArray = (JSONArray) receivedJSONObject.get(PacePlaceConstants.DATA);
                            if (courseDetailsJsonArray != null) {
                                mCourseLists.clear();
                                for (int i = 0; i < courseDetailsJsonArray.length(); i++) {
                                    JSONObject obj = courseDetailsJsonArray.getJSONObject(i);
                                    mCourseLists.add(setCourseDetailsFromResponse(obj));
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                // No data found
                                generateToastMessage(R.string.course_no_data_found);
                            }
                        } else {
                            generateToastMessage(R.string.course_register_issue_in_response_json);
                        }
                    } else {
                        generateToastMessage(R.string.course_register_issue_in_response_json);
                    }
                } catch (JSONException e) {
                    generateToastMessage(R.string.course_register_issue_in_response_json);
                    e.printStackTrace();
                } finally {
                    mAVLoadingIndicatorView.smoothToHide();
                    mAVLoadingIndicatorView.setVisibility(View.GONE);
                    mSwipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.course_register_issue_in_response_json);
                mSwipeContainer.setRefreshing(false);
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

    private JsonArray arrayListToJSONArray() {
        JsonArray finalJSONArray = new JsonArray();
        try {
            for (Map.Entry<Integer, CourseDetail> entry : mAddedCourseDetailMap.entrySet()) {
                CourseDetail value = entry.getValue();
                JsonObject jo = new JsonObject();
                jo.addProperty(PacePlaceConstants.USER_ID, mLoggedInUserId);
                jo.addProperty(PacePlaceConstants.COURSE_DET_ID, value.getmCourseDetId());
                finalJSONArray.add(jo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalJSONArray;
    }

    private void saveSelectedData() {
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.smoothToShow();

        CourseDetailsHttpClient.getInstance().saveCourseRegistration(String.valueOf(arrayListToJSONArray()), new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String val = response.body();
                    JSONObject receivedJSONObject = new JSONObject(val);
                    if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                        JSONObject resultJson = receivedJSONObject.getJSONObject(PacePlaceConstants.DATA);
                        String data = resultJson.optString(PacePlaceConstants.DATA);
                        if (data.equalsIgnoreCase(getResources().getString(R.string.course_registration_success))) {
                            generateToastMessage(R.string.course_registration_success);
                            mAddedCourses.clear();
                            mAddedCourseDetailMap.clear();
                            getCoursesForRegistration();
                        } else {
                            generateToastMessage(R.string.course_register_issue_in_response_json);
                        }
                    } else {
                        generateToastMessage(R.string.course_register_issue_in_response_json);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    generateToastMessage(R.string.course_register_issue_in_response_json);
                } finally {
                    mAVLoadingIndicatorView.setVisibility(View.GONE);
                    mAVLoadingIndicatorView.smoothToHide();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.course_register_issue_in_response_json);
                mAVLoadingIndicatorView.setVisibility(View.GONE);
                mAVLoadingIndicatorView.smoothToHide();
            }
        });

    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }

}
