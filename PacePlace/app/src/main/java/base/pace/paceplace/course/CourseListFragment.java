package base.pace.paceplace.course;

import android.app.Fragment;
import android.content.Context;
import android.nfc.Tag;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import base.pace.paceplace.HomeActivity;
import base.pace.paceplace.R;
import base.pace.paceplace.util.PacePlaceConstants;

public class CourseListFragment extends Fragment {

    public static final String TAG = "CourseListFragment";

    CourseListViewAdapter mAdapter;
    ArrayList<CourseDetail> mCourseLists = new ArrayList<>();
    ListView  mListView;
    String courseRatings, profRatings, studentCourseRatings, studentCourseProfRatings;
    int courseOverallRaters,profOverallRaters;
    int saveCourseOverallRaters,saveProfOverallRaters;
    Float saveProfRating,saveCourseRating, updatedCourseRatings, updatedProfRatings;
    RatingBar prof_rates,prof_overall_rates,course_rates,course_overall_rates;
    TextView popTitleTextView,course_description,prof_overall_num,course_overall_num,prof_rates_text_view,course_rates_text_view;
    ImageView closePopupBtn;
    Button saveAllRatings;
    boolean changedProfRatings,changedCourseRatings = false;

        DecimalFormat decimalFormat = new DecimalFormat("##.0");

    private static LayoutInflater  mLayoutInflater;


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
        mAdapter = new CourseListViewAdapter(getActivity(), mCourseLists,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                CourseDetail displayCourseInfo = (CourseDetail) mAdapter.getItem(position);
                bringPopup(displayCourseInfo,v);
            }
        });

        mListView.setAdapter(mAdapter);
        mListView.setItemsCanFocus(false);
    }

    private static PopupWindow popupWindow;
    public void bringPopup(CourseDetail  courseInfo, View v){



        Log.i("Rede","Click");

        View popWindow = mLayoutInflater.inflate(R.layout.course_rating_page,null);

        //find feilds by id

        /*courseRatings = courseInfo.getmCourseRatings();
        profRatings = courseInfo.getmCourseProfRatings();
        courseOverallRaters = courseInfo.getmNoOfCourseRater();
        profOverallRaters = courseInfo.getmNoOfProfRater()
        studentCourseRatings = courseInfo.getmStudentCourseRatings();
        studentCourseProfRatings = courseInfo.getmStudentCourseProfRatings()*/

        // trial values
        courseRatings = "4.0";
        profRatings = "3.7";
        courseOverallRaters = 15;
        profOverallRaters = 10;
        studentCourseRatings = "3.0";
        studentCourseProfRatings = "0.0";

        closePopupBtn = (ImageView) popWindow.findViewById(R.id.close_popup);
        popTitleTextView = (TextView) popWindow.findViewById(R.id.course_title_bar);
        course_overall_num = (TextView)  popWindow.findViewById(R.id.course_over_text_view);
        prof_overall_num = (TextView) popWindow.findViewById(R.id.prof_over_text_view);
        course_rates_text_view = (TextView) popWindow.findViewById(R.id.rating_bar_course_text_view);
        prof_rates_text_view = (TextView) popWindow.findViewById(R.id.rating_bar_prof_text_view);
        course_rates = (RatingBar) popWindow.findViewById(R.id.rating_bar_course);
        prof_rates = (RatingBar) popWindow.findViewById(R.id.rating_bar_prof);
        prof_overall_rates = (RatingBar) popWindow.findViewById(R.id.prof_overall_rate_bar);
        course_overall_rates = (RatingBar) popWindow.findViewById(R.id.course_overall_rate_bar);
        saveAllRatings = (Button) popWindow.findViewById(R.id.save_ratings);

        //Set Data to popup Layout
        popTitleTextView.setText(String.valueOf(courseInfo.getmCourseName()));
        course_overall_num.setText(String.valueOf(courseRatings));
        prof_overall_num.setText(String.valueOf(profRatings));
        prof_overall_rates.setRating(Float.parseFloat(profRatings));
        course_overall_rates.setRating(Float.parseFloat(courseRatings));


        course_rates.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            Log.i(TAG,String.valueOf(rating));
            course_rates_text_view.setText(String.valueOf(rating));

                int courseOverallRatersPlus = courseOverallRaters;
                if (Float.parseFloat(studentCourseProfRatings)==0){
                    courseOverallRatersPlus+=1;
                }
            if (Float.parseFloat(courseRatings)==0.0){
                course_overall_rates.setRating(rating);
                course_overall_num.setText(String.valueOf(rating));
                saveCourseRating = rating;
                saveCourseOverallRaters = 1;
            }
            else{
                updatedCourseRatings  =(Float) Float.parseFloat(decimalFormat.format(((Float.parseFloat(courseRatings)*courseOverallRaters)+rating)/courseOverallRatersPlus));
                prof_overall_rates.setRating(updatedCourseRatings);
                course_overall_num.setText(String.valueOf(updatedCourseRatings));
                saveCourseRating = rating;
                saveCourseOverallRaters = courseOverallRatersPlus;

            }
            }
        });
        prof_rates.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.i(TAG,String.valueOf(rating));
                prof_rates_text_view.setText(String.valueOf(rating));

                int profOverallRatersPlus = profOverallRaters;
                if (Float.parseFloat(studentCourseProfRatings)==0){
                    profOverallRatersPlus+=1;
                }
                if (Float.parseFloat(profRatings)==0.0){
                    prof_overall_rates.setRating(rating);
                    prof_overall_num.setText(String.valueOf(rating));
                    saveProfRating = rating;
                    saveProfOverallRaters = 1;

                }
                else{

                    updatedProfRatings =(Float) Float.parseFloat(decimalFormat.format((Float.parseFloat(profRatings)*profOverallRaters+rating)/(profOverallRatersPlus)));
                    prof_overall_rates.setRating(updatedProfRatings );
                    prof_overall_num.setText(String.valueOf(updatedProfRatings));
                    saveProfRating = rating;
                    saveProfOverallRaters = profOverallRatersPlus;
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
                Log.i(TAG,saveCourseRating+"-"+saveCourseOverallRaters+"="+updatedCourseRatings);
                Log.i(TAG,saveProfRating+"-"+saveProfOverallRaters+"="+updatedProfRatings);
                popupWindow.dismiss();
            }
        });

    }

}
