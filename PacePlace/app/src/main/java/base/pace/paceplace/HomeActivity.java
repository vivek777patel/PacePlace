package base.pace.paceplace;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import base.pace.paceplace.course.CourseListFragment;
import base.pace.paceplace.course.CourseRegistrationFragment;
import base.pace.paceplace.event.EventFragment;
import base.pace.paceplace.event.EventGenerateFragment;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.user.UserProfileFragment;
import base.pace.paceplace.util.PacePlaceConstants;

public class HomeActivity extends AppCompatActivity implements UserProfileFragment.UpdateUserInfoObject {

    private final String TAG = "HomeActivity";

    ImageView mHomeImageView, mEventImageView, mUserProfileImageView, mLogoutImageView;
    private int mPrimaryColor, mWhiteColor;
    private int mSelectedMenu = 1;
    private UserInfo mLoggedInUserInfo;
    SharedPreferences mSharedPreference;
    TextView mRegisterCourseTextView, mPostEventTextView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPrimaryColor = Color.parseColor(getResources().getString(R.color.colorPrimary));
        mWhiteColor = Color.parseColor(getResources().getString(R.color.colorWhite));

        Intent intent = getIntent();
        mLoggedInUserInfo = (UserInfo) intent.getSerializableExtra(PacePlaceConstants.USER_INFO);
        getSupportActionBar().setElevation(0);
        setViews();
        setOnClickListeners();

        switch (mSelectedMenu) {
            case 1:
                setTitle(R.string.user_courses);
                setDefaultCourseDetailFragment();
                break;
            case 2:
                setTitle(R.string.events);
                break;
            case 3:
                setTitle(R.string.user_registration);
                break;
        }


    }

    private void setOnClickListeners() {
        mHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMenu = 1;
                setTitle(R.string.user_courses);
                setColorToRegistrationMenu();
                setColorsToMenu(mPrimaryColor, mWhiteColor, mWhiteColor, mWhiteColor);
                setDefaultCourseDetailFragment();
            }
        });

        mEventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMenu = 2;
                setTitle(R.string.events);
                setColorToRegistrationMenu();
                setColorsToMenu(mWhiteColor, mPrimaryColor, mWhiteColor, mWhiteColor);
                // TODO : Call the webservice to get event list
                setEventFragment();

            }
        });

        mUserProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMenu = 3;
                setTitle(R.string.user_registration);
                setColorToRegistrationMenu();
                setColorsToMenu(mWhiteColor, mWhiteColor, mPrimaryColor, mWhiteColor);
                setUserDetailsFragment();
            }
        });

        mLogoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMenu = 4;
                setColorsToMenu(mWhiteColor, mWhiteColor, mWhiteColor, mPrimaryColor);
                setColorToRegistrationMenu();
                clearPreferenceValue();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra(PacePlaceConstants.LOGIN_ACTIVITY_MESSAGE, PacePlaceConstants.LOGOUT);
                startActivity(intent);
            }
        });
        mPostEventTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorsToMenu(mWhiteColor, mWhiteColor, mWhiteColor, mWhiteColor);
                setColorToRegistrationMenu();
                EventGenerateFragment eventGenerateFragment = new EventGenerateFragment();
                Bundle bundle = new Bundle();
                mPostEventTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                bundle.putSerializable(PacePlaceConstants.USER_INFO, mLoggedInUserInfo);
                eventGenerateFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.home_fagement_view_RL, eventGenerateFragment).commit();
            }
        });
        mRegisterCourseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorsToMenu(mWhiteColor, mWhiteColor, mWhiteColor, mWhiteColor);
                setColorToRegistrationMenu();
                CourseRegistrationFragment courseRegistrationFragment = new CourseRegistrationFragment();
                Bundle b = new Bundle();
                mRegisterCourseTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                b.putInt(PacePlaceConstants.USER_ID, mLoggedInUserInfo.getmUserId());
                courseRegistrationFragment.setArguments(b);
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.home_fagement_view_RL, courseRegistrationFragment).commit();
            }
        });
    }

    private void clearPreferenceValue() {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(PacePlaceConstants.USER_ID, "");
        editor.putString(PacePlaceConstants.PASSWORD, "");
        editor.apply();
    }

    private void setColorsToMenu(int homeColor, int eventColor, int userProfileColor, int logoutColor) {
        mHomeImageView.setColorFilter(homeColor);
        mEventImageView.setColorFilter(eventColor);
        mUserProfileImageView.setColorFilter(userProfileColor);
        mLogoutImageView.setColorFilter(logoutColor);
    }

    private void setColorToRegistrationMenu(){
        mRegisterCourseTextView.setTextColor(getResources().getColor(R.color.colorListSmall));
        mPostEventTextView.setTextColor(getResources().getColor(R.color.colorListSmall));

    }
    private void setViews() {
        mPostEventTextView = findViewById(R.id.register_event_text_view);
        mRegisterCourseTextView = findViewById(R.id.register_course_text_view);
        mHomeImageView = findViewById(R.id.homeImageView);
        mEventImageView = findViewById(R.id.eventImageView);
        mUserProfileImageView = findViewById(R.id.userProfileImageView);
        mLogoutImageView = findViewById(R.id.logoutImageView);
        mSharedPreference = getApplicationContext().getSharedPreferences(PacePlaceConstants.LOGIN, Context.MODE_PRIVATE);
    }

    private void setDefaultCourseDetailFragment() {
        CourseListFragment courseListFragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        mHomeImageView.setColorFilter(mPrimaryColor);
        bundle.putSerializable(PacePlaceConstants.USER_INFO, mLoggedInUserInfo);
        courseListFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.home_fagement_view_RL, courseListFragment).commit();
    }

    private void setEventFragment(){
        EventFragment eventFragment = new EventFragment();
        mEventImageView.setColorFilter(mPrimaryColor);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.home_fagement_view_RL, eventFragment).commit();
    }

    private void setUserDetailsFragment() {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        mUserProfileImageView.setColorFilter(mPrimaryColor);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PacePlaceConstants.REGISTER, PacePlaceConstants.UPDATE);
        bundle.putSerializable(PacePlaceConstants.USER_INFO, mLoggedInUserInfo);
        userProfileFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.home_fagement_view_RL, userProfileFragment).commit();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Fragment currentlyActiveFragment = getFragmentManager().findFragmentById(R.id.home_fagement_view_RL);

        if(currentlyActiveFragment instanceof CourseListFragment){
            setColorsToMenu(mPrimaryColor, mWhiteColor, mWhiteColor, mWhiteColor);
            setColorToRegistrationMenu();
        }
        else if(currentlyActiveFragment instanceof EventFragment){
            setColorsToMenu(mWhiteColor, mPrimaryColor, mWhiteColor, mWhiteColor);
            setColorToRegistrationMenu();
        }
        else if(currentlyActiveFragment instanceof UserProfileFragment){
            setColorsToMenu(mWhiteColor, mWhiteColor, mPrimaryColor, mWhiteColor);
            setColorToRegistrationMenu();
        }
        else if(currentlyActiveFragment instanceof CourseRegistrationFragment){
            setColorsToMenu(mWhiteColor, mWhiteColor, mWhiteColor, mWhiteColor);
            setColorToRegistrationMenu();
            mRegisterCourseTextView.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        else if(currentlyActiveFragment instanceof EventGenerateFragment){
            setColorsToMenu(mWhiteColor, mWhiteColor, mWhiteColor, mWhiteColor);
            setColorToRegistrationMenu();
            mPostEventTextView.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public void getUserInfo(UserInfo userInfo) {
        mLoggedInUserInfo = userInfo;
    }
}
