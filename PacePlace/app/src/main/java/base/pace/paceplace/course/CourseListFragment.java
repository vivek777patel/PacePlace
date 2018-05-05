package base.pace.paceplace.course;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import base.pace.paceplace.LoginActivity;
import base.pace.paceplace.R;
import base.pace.paceplace.util.CommonWSInvoke;
import base.pace.paceplace.util.PacePlaceConstants;
import base.pace.paceplace.util.WebServiceResponse;

public class CourseListFragment extends Fragment {

    public static final String TAG = "CourseListFragment";

    CourseListViewAdapter mAdapter;
    ArrayList<CourseDetail> mCourseLists = new ArrayList<>();
    ListView mListView;
    String courseRatings, profRatings, studentCourseRatings, studentCourseProfRatings;
    int courseOverallRaters, profOverallRaters;
    int mOverallCourseRaters, mOverallProfRaters;
    Float mStudentProfRating, mStudentCourseRating, mOverallCourseRatings, mOverallProfRatings;
    RatingBar prof_rates, prof_overall_rates, course_rates, course_overall_rates;
    TextView popTitleTextView, prof_overall_num, course_overall_num, prof_rates_text_view, course_rates_text_view;
    ImageView closePopupBtn;
    Button saveAllRatings;

    private int mSelectedPosition = 0;

    CourseDetail mSelectedCourseDetails;

    DecimalFormat decimalFormat = new DecimalFormat("##.0");

    private static LayoutInflater mLayoutInflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        View view = inflater.inflate(R.layout.courses_fragement, vg, false);
        mListView = view.findViewById(R.id.coursesListView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureCourseList();
    }

    private void configureCourseList() {
        Bundle args = getArguments();
        mCourseLists = (ArrayList<CourseDetail>) args.get(PacePlaceConstants.COURSE_LIST);
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

    private static PopupWindow popupWindow;

    public void bringPopup(CourseDetail courseInfo, View v) {

        View popWindow = mLayoutInflater.inflate(R.layout.course_rating_page, null);

        courseRatings = courseInfo.getmCourseRatings();
        profRatings = courseInfo.getmCourseProfRatings();
        courseOverallRaters = courseInfo.getmNoOfCourseRater();
        profOverallRaters = courseInfo.getmNoOfProfRater();
        studentCourseRatings = courseInfo.getmStudentCourseRatings();
        studentCourseProfRatings = courseInfo.getmStudentCourseProfRatings();


        closePopupBtn = popWindow.findViewById(R.id.close_popup);
        popTitleTextView = popWindow.findViewById(R.id.course_title_bar);
        course_overall_num = popWindow.findViewById(R.id.course_over_text_view);
        prof_overall_num = popWindow.findViewById(R.id.prof_over_text_view);
        course_rates_text_view = popWindow.findViewById(R.id.rating_bar_course_text_view);
        prof_rates_text_view = popWindow.findViewById(R.id.rating_bar_prof_text_view);
        course_rates = popWindow.findViewById(R.id.rating_bar_course);
        prof_rates = popWindow.findViewById(R.id.rating_bar_prof);
        prof_overall_rates = popWindow.findViewById(R.id.prof_overall_rate_bar);
        course_overall_rates = popWindow.findViewById(R.id.course_overall_rate_bar);
        saveAllRatings = popWindow.findViewById(R.id.save_ratings);

        //Set Data to popup Layout
        popTitleTextView.setText(String.valueOf(courseInfo.getmCourseName()));
        course_overall_num.setText(String.valueOf(courseRatings));
        prof_overall_num.setText(String.valueOf(profRatings));
        prof_overall_rates.setRating(Float.parseFloat(profRatings));
        course_overall_rates.setRating(Float.parseFloat(courseRatings));


        course_rates.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.i(TAG, String.valueOf(rating));
                course_rates_text_view.setText(String.valueOf(rating));

                int courseOverallRatersPlus = courseOverallRaters;
                if (Float.parseFloat(studentCourseProfRatings) == 0) {
                    courseOverallRatersPlus += 1;
                }
                if (Float.parseFloat(courseRatings) == 0.0) {
                    course_overall_rates.setRating(rating);
                    course_overall_num.setText(String.valueOf(rating));
                    mStudentCourseRating = rating;
                    mOverallCourseRaters = 1;
                    mOverallCourseRatings = rating;
                } else {
                    mOverallCourseRatings = Float.parseFloat(decimalFormat.format(((Float.parseFloat(courseRatings) * courseOverallRaters) + rating) / courseOverallRatersPlus));
                    prof_overall_rates.setRating(mOverallCourseRatings);
                    course_overall_num.setText(String.valueOf(mOverallCourseRatings));
                    mStudentCourseRating = rating;
                    mOverallCourseRaters = courseOverallRatersPlus;

                }
            }
        });
        prof_rates.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.i(TAG, String.valueOf(rating));
                prof_rates_text_view.setText(String.valueOf(rating));

                int profOverallRatersPlus = profOverallRaters;
                if (Float.parseFloat(studentCourseProfRatings) == 0) {
                    profOverallRatersPlus += 1;
                }
                if (Float.parseFloat(profRatings) == 0.0) {
                    prof_overall_rates.setRating(rating);
                    prof_overall_num.setText(String.valueOf(rating));
                    mStudentProfRating = rating;
                    mOverallProfRaters = 1;
                    mOverallProfRatings = rating;
                } else {

                    mOverallProfRatings = Float.parseFloat(decimalFormat.format((Float.parseFloat(profRatings) * profOverallRaters + rating) / (profOverallRatersPlus)));
                    prof_overall_rates.setRating(mOverallProfRatings);
                    prof_overall_num.setText(String.valueOf(mOverallProfRatings));
                    mStudentProfRating = rating;
                    mOverallProfRaters = profOverallRatersPlus;
                }
            }
        });


        //instantiate popup window
        popupWindow = new PopupWindow(popWindow, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //display the popup window
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 80);


        //close the popup window on button click
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        saveAllRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, mStudentCourseRating + "-" + mOverallCourseRaters + "=" + mOverallCourseRatings);
                Log.i(TAG, mStudentProfRating + "-" + mOverallProfRaters + "=" + mOverallProfRatings);
                saveRatings(mStudentCourseRating, mOverallCourseRaters, mOverallCourseRatings,
                        mStudentProfRating, mOverallProfRaters, mOverallProfRatings);

            }
        });

    }

    private void saveRatings(final float mStudentCourseRating, final int mOverallCourseRaters, final float mOverallCourseRatings,
                             final float mStudentProfRating, final int mOverallProfRaters, final float mOverallProfRatings){

        Thread threadA = new Thread() {
            public void run() {
                CommonWSInvoke threadB = new CommonWSInvoke(getActivity());
                WebServiceResponse jsonObject = null;
                try {
                    jsonObject = threadB.execute(PacePlaceConstants.URL_SAVE_RATINGS, PacePlaceConstants.RATINGS,
                            String.valueOf(mStudentCourseRating), String.valueOf(mOverallCourseRaters),String.valueOf(mOverallCourseRatings),
                            String.valueOf(mStudentProfRating), String.valueOf(mOverallProfRaters),String.valueOf(mOverallProfRatings),
                            String.valueOf(mSelectedCourseDetails.getmCourseId()),
                            String.valueOf(mSelectedCourseDetails.getmStudentCourseId()),
                            String.valueOf(mSelectedCourseDetails.getmProfRateId()),
                            String.valueOf(mSelectedCourseDetails.getmCourseProfessorId()))
                            .get(10, TimeUnit.SECONDS);
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
                                // TODO : write response code
                                mCourseLists.get(mSelectedPosition).setmCourseRatings(String.valueOf(mOverallCourseRatings));
                                mCourseLists.get(mSelectedPosition).setmCourseProfRatings(String.valueOf(mOverallProfRatings));
                                mAdapter.notifyDataSetChanged();
                            } else {
                                generateToastMessage(R.string.rating_failed);
                            }
                        } else {
                            generateToastMessage(R.string.rating_failed);
                        }

                        popupWindow.dismiss();
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
