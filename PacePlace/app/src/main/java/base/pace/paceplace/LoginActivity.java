package base.pace.paceplace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import base.pace.paceplace.course.CourseDetail;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.singup.SignUpActivity;
import base.pace.paceplace.util.CommonWSInvoke;
import base.pace.paceplace.util.PacePlaceConstants;
import base.pace.paceplace.util.WebServiceResponse;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";


    AppCompatEditText mEmailEditText, mPasswordEditText;
    Button mLoginButton;
    TextView mSignupTextView,mEmailPasswordInvalidTextView;

    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i  = getIntent();
        if(i.getStringExtra(PacePlaceConstants.LOGIN_ACTIVITY_MESSAGE) != null){
            if(i.getStringExtra(PacePlaceConstants.LOGIN_ACTIVITY_MESSAGE).toString().equalsIgnoreCase(PacePlaceConstants.LOGOUT))
                generateToastMessage(R.string.logout_success);
        }

        configureViews();
        configureClickListeners();
    }

    private void configureViews() {
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mEmailPasswordInvalidTextView = findViewById(R.id.emailPasswordInvalidTextView);
        mLoginButton = findViewById(R.id.loginButton);
        mSignupTextView = findViewById(R.id.signupTextView);
    }

    public void configureClickListeners() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                // Validation of the entered data
                if (email.isEmpty() || password.isEmpty()) {
                    generateToastMessage(R.string.email_pass_required);
                    mEmailPasswordInvalidTextView.setText(R.string.email_pass_required);
                    mEmailPasswordInvalidTextView.setVisibility(View.VISIBLE);
                    return;
                }

                validateUserCredentials(email, password);

                // To hide keyboard
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);

            }
        });
        mSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

    }

    public void openSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void validateUserCredentials(String email, String password) {

        invokeWS(email, password);
    }

    public void invokeWS(final String email, final String password) {
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
                    jsonObject = threadB.execute(PacePlaceConstants.URL_LOGIN,PacePlaceConstants.LOGIN,email,password).get(10, TimeUnit.SECONDS);
                } catch (InterruptedException|ExecutionException|TimeoutException e) {
                    e.printStackTrace();
                }
                final WebServiceResponse receivedJSONObject = jsonObject;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"Response is: " + receivedJSONObject);
                        if(null!=receivedJSONObject){
                            if(receivedJSONObject.getmResponse()) {
                                generateToastMessage(R.string.login_success);
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
                                startActivity(intent);
                            }
                            else{
                                generateToastMessage(R.string.login_failed);
                                try {
                                    mEmailPasswordInvalidTextView.setText(receivedJSONObject.getmJsonObjectResponse().get(PacePlaceConstants.ERROR).toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mEmailPasswordInvalidTextView.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            generateToastMessage(R.string.login_failed);
                            mEmailPasswordInvalidTextView.setText(R.string.login_failed);
                            mEmailPasswordInvalidTextView.setVisibility(View.VISIBLE);
                        }
                        // To clear the text views
                        mEmailEditText.setText("");
                        mPasswordEditText.setText("");
                        mProgressDialog.dismiss();
                    }
                });
            }
        };
        threadA.start();
    }

    private UserInfo setUserInfo(WebServiceResponse receivedJSONObject){
        UserInfo userInfo = new UserInfo();
        try {
            userInfo.setmUserId(Integer.parseInt(receivedJSONObject.getmJsonObjectResponse().get("user_id").toString()));
            userInfo.setmEmail(receivedJSONObject.getmJsonObjectResponse().get("email").toString());
            userInfo.setmAccountType(receivedJSONObject.getmJsonObjectResponse().get("account_type").toString());
            userInfo.setmDob(receivedJSONObject.getmJsonObjectResponse().get("dob").toString());
            //userInfo.setmEndDate(receivedJSONObject.getmJsonObjectResponse().get("end_date").toString());
            userInfo.setmFirstName(receivedJSONObject.getmJsonObjectResponse().get("firstname").toString());
            userInfo.setmGender(receivedJSONObject.getmJsonObjectResponse().get("gender").toString());
            userInfo.setmGraduationType(receivedJSONObject.getmJsonObjectResponse().get("graduation_type").toString());
            userInfo.setmLastName(receivedJSONObject.getmJsonObjectResponse().get("last_name").toString());
            userInfo.setmContact(receivedJSONObject.getmJsonObjectResponse().get("mobile").toString());
            userInfo.setmStatus(receivedJSONObject.getmJsonObjectResponse().get("status_id").toString());
            userInfo.setmStudentType(receivedJSONObject.getmJsonObjectResponse().get("student_type").toString());
            userInfo.setmSubject(receivedJSONObject.getmJsonObjectResponse().get("subject").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
