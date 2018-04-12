package base.pace.paceplace.singup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import base.pace.paceplace.R;

public class SignUpActivity extends AppCompatActivity{

    public static final String TAG = "SignUpActivity";

    EditText mEmailEditText,mPasswordEditText;
    Button mLoginButton;
    TextView mSignupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configureViews();
        configureClickListeners();
    }

    private void configureViews(){
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mLoginButton = findViewById(R.id.loginButton);
        mSignupTextView = findViewById(R.id.signupTextView);
        mEmailEditText.setText("Testing");
    }

    public void configureClickListeners(){

    }

    private void validateUserCredentials(String email, String password){

    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
