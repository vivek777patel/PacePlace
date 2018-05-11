package base.pace.paceplace.course;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import base.pace.paceplace.R;
import base.pace.paceplace.httpclient.CourseDetailsHttpClient;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.util.PacePlaceConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseListFragment extends Fragment {

    public static final String TAG = "CourseListFragment";

    CourseListViewAdapter mAdapter;
    ArrayList<CourseDetail> mCourseLists = new ArrayList<>();
    ListView mListView;
    String mOverAllCourseRatings, mOverAllProfessorRatings, mStudentCourseRatings, mStudentCourseProfRatings;
    int mOverAllCourseRaters,mOverAllCourseRatersPrev, mOverAllProfRaters,mOverAllProfRatersPrev;
    Float mStudentProfRating = 0f, mStudentCourseRating = 0f, mOverallCourseRatings = 0f, mOverallProfRatings = 0f;
    RatingBar mStudentProfessorRatingBar, mProfessorOverAllRatingBar, mStudentCourseRatingBar, mCourseOverAllRatingBar;
    TextView popTitleTextView, mProfOverAllRatingTextView, mCourseOverAllRatingTextView, mStudentProfessorRatingTextView, mStudentCourseRatingTextView;
    ImageView closePopupBtn;
    Button saveAllRatings;
    UserInfo mSelectedUserInfo;
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    private Boolean mRatingUpdated = Boolean.FALSE;
    private int mSelectedPosition = 0;

    CourseDetail mSelectedCourseDetails;

    DecimalFormat decimalFormat = new DecimalFormat("##.0");

    private static PopupWindow mPopupWindow;
    private static LayoutInflater mLayoutInflater;
    private SwipeRefreshLayout mSwipeContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        View view = inflater.inflate(R.layout.courses_fragement, vg, false);
        mListView = view.findViewById(R.id.coursesListView);
        mSwipeContainer = view.findViewById(R.id.swipeContainer);
        mAVLoadingIndicatorView = view.findViewById(R.id.course_detail_avi);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        mSelectedUserInfo = (UserInfo) args.get(PacePlaceConstants.USER_INFO);
        configureCourseList();
        setCourseDetails();
        mSwipeContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        mCourseLists.clear();
                        setCourseDetails();
                    }
                }
        );
    }

    private void setCourseDetails() {
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.bringToFront();
        mAVLoadingIndicatorView.smoothToShow();

        CourseDetailsHttpClient.getInstance().getUserCourseDetails(String.valueOf(mSelectedUserInfo.getmUserId()), new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    if (response.body() != null) {
                        JSONObject receivedJSONObject = new JSONObject(response.body().toString());
                        if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                            JSONArray courseDetailsJsonArray = receivedJSONObject.getJSONArray(PacePlaceConstants.DATA);
                            if (courseDetailsJsonArray != null && courseDetailsJsonArray.length() > 0) {
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
                            generateToastMessage(R.string.course_issue_in_response_json);
                        }
                    } else {
                        generateToastMessage(R.string.course_issue_in_response_json);
                    }
                } catch (JSONException e) {
                    generateToastMessage(R.string.course_issue_in_response_json);
                    e.printStackTrace();
                } finally {
                    mAVLoadingIndicatorView.setVisibility(View.GONE);
                    mAVLoadingIndicatorView.smoothToHide();
                    mSwipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.course_issue_in_response_json);
                mAVLoadingIndicatorView.setVisibility(View.GONE);
                mAVLoadingIndicatorView.smoothToHide();
                mSwipeContainer.setRefreshing(false);
            }
        });

    }

    private void configureCourseList() {
        mAdapter = new CourseListViewAdapter(getActivity(), mCourseLists, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                mSelectedCourseDetails = (CourseDetail) mAdapter.getItem(position);
                mSelectedPosition = position;
                bringPopup(mSelectedCourseDetails, v);
            }
        });

        mListView.setAdapter(mAdapter);
        mListView.setItemsCanFocus(false);

    }

    public void bringPopup(CourseDetail courseInfo, View v) {

        View popWindow = mLayoutInflater.inflate(R.layout.course_rating_page, null);

        mOverAllCourseRatings = courseInfo.getmCourseRatings();
        mOverAllProfessorRatings = courseInfo.getmCourseProfRatings();
        mOverallCourseRatings = Float.valueOf(courseInfo.getmCourseRatings());
        mOverallProfRatings = Float.valueOf(courseInfo.getmCourseProfRatings());

        mOverAllCourseRaters = courseInfo.getmNoOfCourseRater();
        mOverAllProfRaters = courseInfo.getmNoOfProfRater();

        mOverAllCourseRatersPrev = courseInfo.getmNoOfCourseRater();
        mOverAllProfRatersPrev = courseInfo.getmNoOfProfRater();

        mStudentProfRating = 0f;
        mStudentCourseRating = 0f;

        mStudentCourseRatings = courseInfo.getmStudentCourseRatings();
        mStudentCourseProfRatings = courseInfo.getmStudentCourseProfRatings();

        closePopupBtn = popWindow.findViewById(R.id.close_popup);
        popTitleTextView = popWindow.findViewById(R.id.course_title_bar);


        mCourseOverAllRatingTextView = popWindow.findViewById(R.id.course_over_text_view);
        mProfOverAllRatingTextView = popWindow.findViewById(R.id.prof_over_text_view);

        mStudentCourseRatingTextView = popWindow.findViewById(R.id.rating_bar_course_text_view);
        mStudentProfessorRatingTextView = popWindow.findViewById(R.id.rating_bar_prof_text_view);

        mStudentCourseRatingBar = popWindow.findViewById(R.id.rating_bar_course);
        mStudentProfessorRatingBar = popWindow.findViewById(R.id.rating_bar_prof);

        mProfessorOverAllRatingBar = popWindow.findViewById(R.id.prof_overall_rate_bar);
        mCourseOverAllRatingBar = popWindow.findViewById(R.id.course_overall_rate_bar);

        saveAllRatings = popWindow.findViewById(R.id.save_ratings);

        //Set Data to popup Layout
        popTitleTextView.setText(String.valueOf(courseInfo.getmCourseName()));
        mCourseOverAllRatingTextView.setText(String.valueOf(mOverAllCourseRatings));
        mProfOverAllRatingTextView.setText(String.valueOf(mOverAllProfessorRatings));

        mProfessorOverAllRatingBar.setRating(Float.parseFloat(mOverAllProfessorRatings));
        mCourseOverAllRatingBar.setRating(Float.parseFloat(mOverAllCourseRatings));

        // Setting the default/given rate
        mStudentCourseRatingBar.setRating(Float.valueOf(mStudentCourseRatings));
        mCourseOverAllRatingBar.setRating(Float.valueOf(mOverAllCourseRatings));
        mStudentCourseRatingTextView.setText(mStudentCourseRatings);

        mStudentProfessorRatingBar.setRating(Float.valueOf(mStudentCourseProfRatings));
        mProfessorOverAllRatingBar.setRating(Float.valueOf(mOverAllProfessorRatings));
        mStudentProfessorRatingTextView.setText(mStudentCourseProfRatings);

        // Allowing to give rating only once
        if (Float.valueOf(mStudentCourseRatings) != 0.0) {
            mStudentCourseRatingBar.setEnabled(Boolean.FALSE);
        }
        if (Float.valueOf(mStudentCourseProfRatings) != 0.0) {
            mStudentProfessorRatingBar.setEnabled(Boolean.FALSE);
        }

        mStudentCourseRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mStudentCourseRatingTextView.setText(String.valueOf(rating));
                mRatingUpdated = Boolean.TRUE;
                int courseOverallRatersPlus = mOverAllCourseRaters;
                // First time rating for overall this course
                if (Float.parseFloat(mOverAllCourseRatings) == 0.0) {
                    mCourseOverAllRatingBar.setRating(rating);
                    mCourseOverAllRatingTextView.setText(String.valueOf(rating));
                    mStudentCourseRating = rating;
                    mOverAllCourseRaters = 1;
                    mOverallCourseRatings = rating;
                    return;
                }

                // First time rating for user to selected course --> Increment raters only if the user is giving first time rating
                if (Float.parseFloat(mStudentCourseRatings) == 0) {
                    courseOverallRatersPlus += 1;
                }

                mOverallCourseRatings = Float.parseFloat(decimalFormat.format(((Float.parseFloat(mOverAllCourseRatings) * mOverAllCourseRatersPrev) + rating) / courseOverallRatersPlus));

                mCourseOverAllRatingBar.setRating(mOverallCourseRatings);
                mCourseOverAllRatingTextView.setText(String.valueOf(mOverallCourseRatings));

                mStudentCourseRating = rating;
                mOverAllCourseRaters = courseOverallRatersPlus;
                mStudentCourseRatings = "1";
            }
        });
        mStudentProfessorRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mStudentProfessorRatingTextView.setText(String.valueOf(rating));
                mRatingUpdated = Boolean.TRUE;
                int profOverallRatersPlus = mOverAllProfRaters;
                // First time rating for overall this course
                if (Float.parseFloat(mOverAllProfessorRatings) == 0.0) {
                    mProfessorOverAllRatingBar.setRating(rating);
                    mProfOverAllRatingTextView.setText(String.valueOf(rating));
                    mStudentProfRating = rating;
                    mOverAllProfRaters = 1;
                    mOverallProfRatings = rating;
                    return;
                }
                // First time rating for user to selected course professor  --> Increment raters only if the user is giving first time rating
                if (Float.parseFloat(mStudentCourseProfRatings) == 0) {
                    profOverallRatersPlus += 1;
                }

                mOverallProfRatings = Float.parseFloat(decimalFormat.format((Float.parseFloat(mOverAllProfessorRatings) * mOverAllProfRatersPrev + rating) / (profOverallRatersPlus)));
                mProfessorOverAllRatingBar.setRating(mOverallProfRatings);
                mProfOverAllRatingTextView.setText(String.valueOf(mOverallProfRatings));
                mStudentProfRating = rating;
                mOverAllProfRaters = profOverallRatersPlus;
                mStudentCourseProfRatings = "1";
            }
        });


        //instantiate popup window
        mPopupWindow = new PopupWindow(popWindow, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //display the popup window
        mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 80);


        //close the popup window on button click
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        saveAllRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, mStudentCourseRating + "-" + mOverAllCourseRaters + "=" + mOverallCourseRatings);
                Log.i(TAG, mStudentProfRating + "-" + mOverAllProfRaters + "=" + mOverallProfRatings);
                // Hit REST only when rating is updated
                if (mRatingUpdated)
                    saveRatings(mStudentCourseRating, mOverAllCourseRaters, mOverallCourseRatings,
                            mStudentProfRating, mOverAllProfRaters, mOverallProfRatings);
                else
                    mPopupWindow.dismiss();

            }
        });

    }

    private void saveRatings(final float mStudentCourseRating, final int mOverallCourseRaters, final float mOverallCourseRatings,
                             final float mStudentProfRating, final int mOverallProfRaters, final float mOverallProfRatings) {
        mAVLoadingIndicatorView.smoothToShow();
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        CourseDetailsHttpClient.getInstance().saveUserRatings(
                String.valueOf(mStudentCourseRating), String.valueOf(mOverallCourseRaters), String.valueOf(mOverallCourseRatings),
                String.valueOf(mStudentProfRating), String.valueOf(mOverallProfRaters), String.valueOf(mOverallProfRatings),
                String.valueOf(mSelectedCourseDetails.getmCourseId()),
                String.valueOf(mSelectedCourseDetails.getmStudentCourseId()),
                String.valueOf(mSelectedCourseDetails.getmProfRateId()),
                String.valueOf(mSelectedCourseDetails.getmCourseProfessorId()), new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                                JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                                if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                                    // To avoid additional Rest Call
                                    setCourseDetails();
                                } else {
                                    generateToastMessage(R.string.rating_failed);
                                }
                            } else {
                                generateToastMessage(R.string.rating_failed);
                            }
                            mPopupWindow.dismiss();
                        } catch (JSONException e) {
                            generateToastMessage(R.string.rating_failed);
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
                        generateToastMessage(R.string.rating_failed);
                        mAVLoadingIndicatorView.smoothToHide();
                        mAVLoadingIndicatorView.setVisibility(View.GONE);
                        mSwipeContainer.setRefreshing(false);
                    }
                });
    }

    private CourseDetail setCourseDetailsFromResponse(JSONObject jsonObject) {
        return new CourseDetail(
                jsonObject.optString("course_name"),
                jsonObject.opt("course_rating") != null ? jsonObject.optString("course_rating") : "0",
                jsonObject.optString("firstname"),
                jsonObject.opt("professor_ratings") != null ? jsonObject.optString("professor_ratings") : "0", // Prof Rating
                jsonObject.optString("si_cd_course_day.static_combo_value"),
                jsonObject.optString("course_time"),
                jsonObject.optString("location_name"),
                jsonObject.optString("address_line1"),
                jsonObject.optString("course_startdate"),
                jsonObject.optString("course_enddate"),
                jsonObject.optString("address_line1"),
                jsonObject.optString("city"),
                jsonObject.optString("course_desc"),
                jsonObject.optString("email"),
                jsonObject.optString("si_ci_graduation_type.static_combo_value"),
                jsonObject.optString("si_ci_subject.static_combo_value"),
                jsonObject.optString("static_combo_value"),

                jsonObject.opt("course_day") != null ? jsonObject.optInt("course_day") : 0,
                jsonObject.opt("credit") != null ? jsonObject.optInt("credit") : 0,
                jsonObject.opt("number_of_raters") != null ? jsonObject.optInt("number_of_raters") : 0,
                jsonObject.opt("seat_available") != null ? jsonObject.optInt("seat_available") : 0,
                jsonObject.opt("seat_capacity") != null ? jsonObject.optInt("seat_capacity") : 0,
                jsonObject.opt("professor_raters") != null ? jsonObject.optInt("professor_raters") : 0,//number_of_prof_raters
                jsonObject.opt("student_course_rating") != null ? jsonObject.optString("student_course_rating") : "0",//STUDENT_COURSE_RATINGS
                jsonObject.opt("student_professor_rating") != null ? jsonObject.optString("student_professor_rating") : "0",//STUDENT_PROF_COURSE_RATINGS

                jsonObject.opt("professor_user_id") != null ? jsonObject.optInt("professor_user_id") : 0,//professor user id

                jsonObject.opt("course_id") != null ? jsonObject.optInt("course_id") : 0,
                jsonObject.opt("scm_id") != null ? jsonObject.optInt("scm_id") : 0,
                jsonObject.opt("prof_rate_id") != null ? jsonObject.optInt("prof_rate_id") : 0
        );

    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }
}
