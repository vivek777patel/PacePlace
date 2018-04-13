package base.pace.paceplace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;
/*
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
*/

import org.json.JSONObject;

import base.pace.paceplace.singup.SignUpActivity;
import base.pace.paceplace.util.PacePlaceConstants;
import cz.msebera.android.httpclient.Header;


public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";



    EditText mEmailEditText, mPasswordEditText;
    Button mLoginButton;
    TextView mSignupTextView;

    ProgressDialog pDialog;

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
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.please_wait));
        pDialog.setCancelable(false);

        RequestParams params = new RequestParams();
        params.put(PacePlaceConstants.EMAIL, email);
        params.put(PacePlaceConstants.PASSWORD, password);
        invokeWS(params);

    }

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        pDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        Log.e(TAG,"URL : "+PacePlaceConstants.URL_LOGIN);
        client.post(PacePlaceConstants.URL_LOGIN, params, new JsonHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            //public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                // called when response HTTP status is "200 OK"
                Log.e(TAG,"SSSSSSSSSSSSS");
                Log.e(TAG,"SSSSSSSSSSSSS : "+jsonObject);
                generateToastMessage(R.string.login_success);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            //public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                generateToastMessage(R.string.login_failed);
                Log.e(TAG,"EEEEEEEE :: "+ PacePlaceConstants.URL_LOGIN);
                Log.e(TAG,"EEEEEEEE : "+statusCode);
                Log.e(TAG,"EEEEEEEE : "+errorResponse);
                throwable.printStackTrace();
            }
        });
        pDialog.hide();
    }


    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
