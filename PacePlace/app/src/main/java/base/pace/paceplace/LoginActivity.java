package base.pace.paceplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import base.pace.paceplace.httpclient.UserDetailsHttpClient;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.singup.SignUpActivity;
import base.pace.paceplace.util.PacePlaceConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";


    AppCompatEditText mEmailEditText, mPasswordEditText;
    Button mLoginButton;
    TextView mSignupTextView,mEmailPasswordInvalidTextView;
    SharedPreferences mSharedPreference;
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i  = getIntent();
        if(i.getStringExtra(PacePlaceConstants.LOGIN_ACTIVITY_MESSAGE) != null){
            if(i.getStringExtra(PacePlaceConstants.LOGIN_ACTIVITY_MESSAGE).equalsIgnoreCase(PacePlaceConstants.LOGOUT))
                generateToastMessage(R.string.logout_success);
        }

        configureViews();
        configureClickListeners();
        checkForPreferences();

    }

    private void configureViews() {
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mEmailPasswordInvalidTextView = findViewById(R.id.emailPasswordInvalidTextView);
        mLoginButton = findViewById(R.id.loginButton);
        mSignupTextView = findViewById(R.id.signupTextView);
        mAVLoadingIndicatorView = findViewById(R.id.avi);
        mSharedPreference = getApplicationContext().getSharedPreferences(PacePlaceConstants.LOGIN, Context.MODE_PRIVATE);
        mAVLoadingIndicatorView.bringToFront();
    }

    private void checkForPreferences(){
        String userId = mSharedPreference.getString(PacePlaceConstants.USER_ID, "");
        String password = mSharedPreference.getString(PacePlaceConstants.PASSWORD, "");
        if(!userId.isEmpty() && !password.isEmpty()){
            validateUserCredentials(userId, password);
        }

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
        mAVLoadingIndicatorView.smoothToShow();
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        UserDetailsHttpClient.getInstance().login(email,password, new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    if (response.body() != null){
                        Log.i(TAG,"Response");
                        JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                        if(null!=receivedJSONObject){
                            if ((Boolean)receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                                //generateToastMessage(R.string.login_success);
                                UserInfo userInfo = setUserInfo((JSONObject)receivedJSONObject.getJSONObject(PacePlaceConstants.DATA));
                                setSharedPreference(email, password);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra(PacePlaceConstants.USER_INFO, userInfo);
                                startActivity(intent);
                            }
                            else{
                                generateToastMessage(R.string.login_failed);
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
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    generateToastMessage(R.string.login_issue_in_response_json);
                }
                finally {
                    mAVLoadingIndicatorView.smoothToHide();
                    mAVLoadingIndicatorView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.login_issue_in_response_json);
                mAVLoadingIndicatorView.smoothToHide();
                mAVLoadingIndicatorView.setVisibility(View.GONE);
            }
        });
    }

    private void setSharedPreference(String email, String password){
        Log.i(TAG,"In Set Shared Preference");
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(PacePlaceConstants.USER_ID, email);
        editor.putString(PacePlaceConstants.PASSWORD, password);
        editor.apply();
    }

    private UserInfo setUserInfo(JSONObject receivedJSONObject){
        UserInfo userInfo = new UserInfo();
        try {
            userInfo.setmUserId(Integer.parseInt(receivedJSONObject.get("user_id").toString()));
            userInfo.setmEmail(receivedJSONObject.get("email").toString());
            userInfo.setmAccountType(receivedJSONObject.get("account_type").toString());
            userInfo.setmDob(receivedJSONObject.get("dob").toString());
            //userInfo.setmEndDate(receivedJSONObject.get("end_date").toString());
            userInfo.setmFirstName(receivedJSONObject.get("firstname").toString());
            userInfo.setmGender(receivedJSONObject.get("gender").toString());
            userInfo.setmGraduationType(receivedJSONObject.get("graduation_type").toString());
            userInfo.setmLastName(receivedJSONObject.get("last_name").toString());
            userInfo.setmContact(receivedJSONObject.get("mobile").toString());
            userInfo.setmStatus(receivedJSONObject.get("status_id").toString());
            userInfo.setmStudentType(receivedJSONObject.get("student_type").toString());
            userInfo.setmSubject(receivedJSONObject.get("subject").toString());
            userInfo.setmPassword(receivedJSONObject.get("password").toString());
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
