package base.pace.paceplace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import base.pace.paceplace.course.CourseDetail;
import base.pace.paceplace.course.CourseListFragment;
import base.pace.paceplace.util.PacePlaceConstants;

public class HomeActivity extends AppCompatActivity {

    ImageView mHomeImageView,mEventImageView,mLogoutImageView;
    private int mPrimaryColor, mWhiteColor;
    private int mSelectedMenu=1;
    ArrayList<CourseDetail> mCourseList;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPrimaryColor = Color.parseColor(getResources().getString(R.color.colorPrimary));
        mWhiteColor = Color.parseColor(getResources().getString(R.color.colorWhite));

        setViews();
        setFragment();
        setOnClickListeners();
    }
    private void setOnClickListeners(){
        mHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedMenu!=1){
                    setColorsToMenu(mPrimaryColor,mWhiteColor,mWhiteColor);
                    // TODO : Call the webservice to get course list
                    CourseListFragment courseListFragment = new CourseListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PacePlaceConstants.COURSE_LIST, mCourseList);
                    courseListFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.home_fagement_view_RL,courseListFragment).commit();
                }
            }
        });

        mEventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedMenu!=2){
                    setColorsToMenu(mWhiteColor,mPrimaryColor,mWhiteColor);
                    // TODO : Call the webservice to get event list
                }
            }
        });
    }

    private void setColorsToMenu(int homeColor,int eventColor,int logoutColor){
        mHomeImageView.setColorFilter(homeColor);
        mEventImageView.setColorFilter(eventColor);
        mLogoutImageView.setColorFilter(logoutColor);
    }
    private void setViews(){
        mHomeImageView = findViewById(R.id.homeImageView);
        mEventImageView = findViewById(R.id.eventImageView);
        mLogoutImageView = findViewById(R.id.logoutImageView);
    }
    private void setFragment(){
        Intent intent = getIntent();
        mCourseList = (ArrayList<CourseDetail>) intent.getSerializableExtra(PacePlaceConstants.COURSE_LIST);
        CourseListFragment courseListFragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        mHomeImageView.setColorFilter(mPrimaryColor);
        bundle.putSerializable(PacePlaceConstants.COURSE_LIST, mCourseList);
        courseListFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.home_fagement_view_RL,courseListFragment).commit();
    }
}
