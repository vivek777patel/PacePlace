package base.pace.paceplace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import base.pace.paceplace.course.CourseListFragment;
import base.pace.paceplace.course.CourseRegistrationFragment;
import base.pace.paceplace.event.EventFragment;
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

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPrimaryColor = Color.parseColor(getResources().getString(R.color.colorPrimary));
        mWhiteColor = Color.parseColor(getResources().getString(R.color.colorWhite));

        Intent intent = getIntent();
        mLoggedInUserInfo = (UserInfo) intent.getSerializableExtra(PacePlaceConstants.USER_INFO);

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
                setColorsToMenu(mPrimaryColor, mWhiteColor, mWhiteColor, mWhiteColor);
                setDefaultCourseDetailFragment();
            }
        });

        mEventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMenu = 2;
                setTitle(R.string.events);
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
                setColorsToMenu(mWhiteColor, mWhiteColor, mPrimaryColor, mWhiteColor);
                setUserDetailsFragment();
            }
        });

        mLogoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMenu = 4;
                setColorsToMenu(mWhiteColor, mWhiteColor, mWhiteColor, mPrimaryColor);
                clearPreferenceValue();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra(PacePlaceConstants.LOGIN_ACTIVITY_MESSAGE, PacePlaceConstants.LOGOUT);
                startActivity(intent);
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

    private void setViews() {
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
        getFragmentManager().beginTransaction().replace(R.id.home_fagement_view_RL, eventFragment).commit();
    }

    private void setUserDetailsFragment() {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        mUserProfileImageView.setColorFilter(mPrimaryColor);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PacePlaceConstants.REGISTER, PacePlaceConstants.UPDATE);
        bundle.putSerializable(PacePlaceConstants.USER_INFO, mLoggedInUserInfo);
        userProfileFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.home_fagement_view_RL, userProfileFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);//Menu Resource, Menu
        MenuItem registerMenu = menu.findItem(R.id.register_courses);
        /*MenuItem backMenu = menu.findItem(R.id.action_back);
        backMenu.setOnMenuItemClickListener(goToCourseDetailMenu());*/
        registerMenu.setOnMenuItemClickListener(goToRegistrationPage());
        return true;
    }

    private MenuItem.OnMenuItemClickListener goToRegistrationPage() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CourseRegistrationFragment courseRegistrationFragement = new CourseRegistrationFragment();
                Bundle b = new Bundle();
                b.putInt(PacePlaceConstants.USER_ID, mLoggedInUserInfo.getmUserId());
                courseRegistrationFragement.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.home_fagement_view_RL, courseRegistrationFragement).commit();
                return false;
            }
        };

    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getUserInfo(UserInfo userInfo) {
        Log.i(TAG, "Getting User Info");
        mLoggedInUserInfo = userInfo;
    }
}
