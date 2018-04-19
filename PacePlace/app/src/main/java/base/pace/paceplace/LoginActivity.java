package base.pace.paceplace;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import base.pace.paceplace.singup.SignUpActivity;
import base.pace.paceplace.util.CommonWSInvoke;
import base.pace.paceplace.util.CustomVolleyRequestQueue;
import base.pace.paceplace.util.PacePlaceConstants;
import base.pace.paceplace.util.WebServiceResponse;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";


    AppCompatEditText mEmailEditText, mPasswordEditText;
    Button mLoginButton;
    TextView mSignupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configureViews();
        configureClickListeners();
    }

    private void configureViews() {
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
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
                    return;
                }

                validateUserCredentials(email, password);

                // To clear the text views
                mEmailEditText.setText("");
                mPasswordEditText.setText("");

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
        Thread threadA = new Thread() {
            public void run() {
                CommonWSInvoke threadB = new CommonWSInvoke(getApplicationContext());
                WebServiceResponse jsonObject = null;
                try {
                    jsonObject = threadB.execute(PacePlaceConstants.LOGIN,email,password).get(10, TimeUnit.SECONDS);
                } catch (InterruptedException|ExecutionException|TimeoutException e) {
                    e.printStackTrace();
                }
                final WebServiceResponse receivedJSONObject = jsonObject;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"Response is: " + receivedJSONObject);
                        if(receivedJSONObject.getmResponse())
                            generateToastMessage(R.string.login_success);
                        else
                            generateToastMessage(R.string.login_failed);
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
