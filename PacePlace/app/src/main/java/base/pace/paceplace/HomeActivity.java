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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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

    ImageView mHomeImageView, mEventImageView, mUserProfileImageView, mLogoutImageView;
    private int mPrimaryColor, mWhiteColor;
    private int mSelectedMenu = 1;
    ArrayList<CourseDetail> mCourseList = new ArrayList<>();
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
            case 3:
                setTitle(R.string.user_registration);
                break;
        }

        setViews();
        setOnClickListeners();
    }

    private void setCourseDetails() {
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
                        if (receivedJSONObject != null) {
                            if (receivedJSONObject.getmResponse()) {
                                Log.i(TAG, "In getting response");
                                // TODO : Set mCourseList variable before calling setDefaultCourseDetailFragment()
                                try {
                                    JSONArray courseDetailsJsonArray = receivedJSONObject.getmJsonArrayResponse();
                                    if (courseDetailsJsonArray != null) {
                                        for (int i = 0; i < courseDetailsJsonArray.length(); i++) {
                                            JSONObject obj = courseDetailsJsonArray.getJSONObject(i);
                                            //CourseDetail cd = setCourseDetailsFromResponse(obj);
                                            mCourseList.add(setCourseDetailsFromResponse(obj));
                                        }
                                    } else {
                                        // No data found
                                        generateToastMessage(R.string.course_no_data_found);
                                    }

                                    setDefaultCourseDetailFragment();
                                } catch (JSONException e) {
                                    generateToastMessage(R.string.course_issue_in_response_json);
                                    e.printStackTrace();
                                }
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
                    setColorsToMenu(mPrimaryColor, mWhiteColor, mWhiteColor, mWhiteColor);
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
                    setColorsToMenu(mWhiteColor, mPrimaryColor, mWhiteColor, mWhiteColor);
                    // TODO : Call the webservice to get event list
                    mSelectedMenu = 2;
                }
            }
        });

        mLogoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedMenu != 4) {
                    setColorsToMenu(mWhiteColor, mWhiteColor, mWhiteColor, mPrimaryColor);
                    mSelectedMenu = 4;
                }
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra(PacePlaceConstants.LOGIN_ACTIVITY_MESSAGE, PacePlaceConstants.LOGOUT);
                startActivity(intent);
            }
        });
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
    }

    private void setDefaultCourseDetailFragment() {
        CourseListFragment courseListFragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        mHomeImageView.setColorFilter(mPrimaryColor);
        bundle.putSerializable(PacePlaceConstants.COURSE_LIST, mCourseList);
        courseListFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.home_fagement_view_RL, courseListFragment).commit();
    }

    private CourseDetail setCourseDetailsFromResponse(JSONObject jsonObject){
        return new CourseDetail(
                jsonObject.optString("course_name"),
                jsonObject.opt("course_rating")!=null?jsonObject.optString("course_rating"):"",
                jsonObject.optString("firstname"),
                "0", // Prof Rating
                jsonObject.optString("si_cd_course_day.static_combo_value"),
                jsonObject.optString("course_time"),
                jsonObject.optString("location_name"),
                "Room : 007",
                jsonObject.optString("course_startdate"),
                jsonObject.optString("course_enddate"),
                jsonObject.optString("address_line1"),
                jsonObject.optString("city"),
                jsonObject.optString("course_desc"),
                jsonObject.optString("email"),
                jsonObject.optString("si_ci_graduation_type.static_combo_value"),
                jsonObject.optString("si_ci_subject.static_combo_value"),
                jsonObject.optString("static_combo_value"),

                jsonObject.opt("course_day")!=null?jsonObject.optInt("course_day"):0,
                jsonObject.opt("credit")!=null?jsonObject.optInt("credit"):0,
                jsonObject.opt("number_of_raters")!=null?jsonObject.optInt("number_of_raters"):0,
                jsonObject.opt("seat_available")!=null?jsonObject.optInt("seat_available"):0,
                jsonObject.opt("seat_capacity")!=null?jsonObject.optInt("seat_capacity"):0
        );
    }


    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
