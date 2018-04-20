package base.pace.paceplace.singup;

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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import base.pace.paceplace.LoginActivity;
import base.pace.paceplace.R;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.util.CommonWSInvoke;
import base.pace.paceplace.util.PacePlaceConstants;
import base.pace.paceplace.util.WebServiceResponse;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

    AppCompatEditText mFirstNameEditText, mLastNameEditText, mEmailEditText, mPasswordEditText, mContactEditText, mDobEditText, mConfirmPasswordEditText;
    Button mRegisterButton, mClearButton;
    TextView mSignupTextView;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        configureViews();
        configureClickListeners();
    }

    private void configureViews() {
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passEditText);
        mConfirmPasswordEditText = findViewById(R.id.confirmPassEditText);
        mFirstNameEditText = findViewById(R.id.firstNameEditText);
        mLastNameEditText = findViewById(R.id.lastNameEditText);
        mContactEditText = findViewById(R.id.contactEditText);
        mDobEditText = findViewById(R.id.dobEditText);

        mRegisterButton = findViewById(R.id.registerButton);
        mClearButton = findViewById(R.id.clearButton);
    }

    public void configureClickListeners() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String confirmPassword = mConfirmPasswordEditText.getText().toString();
                String firstName = mFirstNameEditText.getText().toString();
                String lastName = mLastNameEditText.getText().toString();
                String contact = mContactEditText.getText().toString();
                String dob = mDobEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    generateToastMessage(R.string.email_pass_required);
                    return;
                }
                if (confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
                    generateToastMessage(R.string.conf_pass_and_pass_required);
                    return;
                }

                UserInfo registrationInfo = new UserInfo(email, password, firstName, lastName, contact, dob);
                validateAndStoreUser(registrationInfo);

                // To hide keyboard
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
            }
        });
    }

    private void validateAndStoreUser(final UserInfo registrationInfo) {
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
                    jsonObject = threadB.execute(PacePlaceConstants.URL_REGISTER, PacePlaceConstants.REGISTER,
                            registrationInfo.getmEmail(), registrationInfo.getmPassword(), registrationInfo.getmFirstName(),
                            registrationInfo.getmLastName(), registrationInfo.getmContact(), registrationInfo.getmDob()
                    ).get(10, TimeUnit.SECONDS);
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
                                generateToastMessage(R.string.register_success);
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                generateToastMessage(R.string.register_failed);
                            }
                        } else {
                            generateToastMessage(R.string.register_failed);
                        }
                        // To clear the text views
                        mEmailEditText.setText("");
                        mPasswordEditText.setText("");
                        mConfirmPasswordEditText.setText("");
                        mFirstNameEditText.setText("");
                        mLastNameEditText.setText("");
                        mContactEditText.setText("");
                        mDobEditText.setText("");
                        mProgressDialog.dismiss();
                    }
                });
            }
        };
        threadA.start();
    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
