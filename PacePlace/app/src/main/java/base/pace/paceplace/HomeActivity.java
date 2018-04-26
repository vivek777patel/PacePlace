package base.pace.paceplace;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import base.pace.paceplace.course.CourseDetail;
import base.pace.paceplace.course.CourseListFragment;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.util.CommonWSInvoke;
import base.pace.paceplace.util.PacePlaceConstants;
import base.pace.paceplace.util.WebServiceResponse;

public class HomeActivity extends AppCompatActivity {

    private final String TAG = "HomeActivity";

    ImageView mHomeImageView, mEventImageView, mLogoutImageView;
    private int mPrimaryColor, mWhiteColor;
    private int mSelectedMenu = 1;
    ArrayList<CourseDetail> mCourseList;
    private UserInfo mLoggedInUserInfo;
    ProgressDialog mProgressDialog;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPrimaryColor = Color.parseColor(getResources().getString(R.color.colorPrimary));
        mWhiteColor = Color.parseColor(getResources().getString(R.color.colorWhite));

        Intent intent = getIntent();
        mLoggedInUserInfo = (UserInfo) intent.getSerializableExtra(PacePlaceConstants.USER_INFO);

        switch (mSelectedMenu) {
            case 1:
                setTitle(R.string.user_courses);
                setCourseDetails();
                break;
            case 2:
                setTitle(R.string.events);
                break;
        }

        setViews();
//        setFragment();
        setOnClickListeners();
    }

    private void setCourseDetails() {
        /*Log.i(TAG,"SSSSS : "+mLoggedInUserInfo.getmEmail());*/
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        Thread threadA = new Thread() {
            public void run() {
                CommonWSInvoke threadB = new CommonWSInvoke(getApplicationContext());
                WebServiceResponse jsonObject = null;
                try {
                    jsonObject = threadB.execute(PacePlaceConstants.URL_COURSES, PacePlaceConstants.COURSES, String.valueOf(mLoggedInUserInfo.getmUserId())).get(10, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
                final WebServiceResponse receivedJSONObject = jsonObject;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Response is: " + receivedJSONObject);
                        if (null != receivedJSONObject) {
                            if (receivedJSONObject.getmResponse()) {
                                Log.i(TAG,"In getting response");
                                setFragment();
                                    /*generateToastMessage(R.string.login_success);
                                    UserInfo userInfo = setUserInfo(receivedJSONObject);
                                    ArrayList<CourseDetail> mCourseList = new ArrayList<>();
                                    mCourseList.add(new CourseDetail("Algo", "Ratings:-4/4","Suzzana","Ratings:-4/4","Wenesday","6pm - 9pm",
                                            "163 William Street", "Room:1420","Sept,6 2017","Dec,20 2017"));
                                    mCourseList.add(new CourseDetail("Mobile Web Content","Ratings:-4/4","Haik","Ratings:-4/4","Wenesday","6pm - 9pm",
                                            "163 William Street", "Room:1420","Sept,6 2017","Dec,20 2017"));
                                    mCourseList.add(new CourseDetail("Project 1","Ratings:-3/4","Yuri","Ratings:-3/4","Wenesday","6pm - 9pm",
                                            "163 William Street", "Room:1420","Sept,6 2017","Dec,20 2017"));
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.putExtra(PacePlaceConstants.USER_INFO, userInfo);
                                    intent.putExtra(PacePlaceConstants.COURSE_LIST, mCourseList);
                                    startActivity(intent);*/
                            } else {
                                generateToastMessage(R.string.login_failed);
                            }
                        } else {
                            generateToastMessage(R.string.login_failed);
                        }

                        mProgressDialog.dismiss();
                    }
                });
            }
        };
        threadA.start();

    }

    private void setOnClickListeners() {
        mHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedMenu != 1) {
                    setColorsToMenu(mPrimaryColor, mWhiteColor, mWhiteColor);
                    // TODO : Call the webservice to get course list
                    CourseListFragment courseListFragment = new CourseListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PacePlaceConstants.COURSE_LIST, mCourseList);
                    courseListFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.home_fagement_view_RL, courseListFragment).commit();
                }
            }
        });

        mEventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedMenu != 2) {
                    setColorsToMenu(mWhiteColor, mPrimaryColor, mWhiteColor);
                    // TODO : Call the webservice to get event list
                    mSelectedMenu = 2;
                }
            }
        });

        mLogoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedMenu != 3) {
                    setColorsToMenu(mWhiteColor, mWhiteColor, mPrimaryColor);
                    mSelectedMenu = 3;
                }
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra(PacePlaceConstants.LOGIN_ACTIVITY_MESSAGE, PacePlaceConstants.LOGOUT);
                startActivity(intent);
            }
        });
    }

    private void setColorsToMenu(int homeColor, int eventColor, int logoutColor) {
        mHomeImageView.setColorFilter(homeColor);
        mEventImageView.setColorFilter(eventColor);
        mLogoutImageView.setColorFilter(logoutColor);
    }

    private void setViews() {
        mHomeImageView = findViewById(R.id.homeImageView);
        mEventImageView = findViewById(R.id.eventImageView);
        mLogoutImageView = findViewById(R.id.logoutImageView);
    }

    private void setFragment() {
        Intent intent = getIntent();
        mCourseList = (ArrayList<CourseDetail>) intent.getSerializableExtra(PacePlaceConstants.COURSE_LIST);
        CourseListFragment courseListFragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        mHomeImageView.setColorFilter(mPrimaryColor);
        bundle.putSerializable(PacePlaceConstants.COURSE_LIST, mCourseList);
        courseListFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.home_fagement_view_RL, courseListFragment).commit();
    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
