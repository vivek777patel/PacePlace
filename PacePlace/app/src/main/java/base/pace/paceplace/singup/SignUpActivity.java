package base.pace.paceplace.singup;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import base.pace.paceplace.R;
import base.pace.paceplace.login.RegistrationInfo;

public class SignUpActivity extends AppCompatActivity{

    public static final String TAG = "SignUpActivity";

    AppCompatEditText mFirstNameEditText,mLastNameEditText,mEmailEditText, mPasswordEditText
            ,mContactEditText,mDobEditText, mConfirmPasswordEditText;
    Button mRegisterButton,mClearButton;
    TextView mSignupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        configureViews();
        configureClickListeners();
    }

    private void configureViews(){
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mConfirmPasswordEditText = findViewById(R.id.confirmPassEditText);
        mFirstNameEditText = findViewById(R.id.firstNameEditText);
        mLastNameEditText = findViewById(R.id.lastNameEditText);
        mContactEditText = findViewById(R.id.contactEditText);
        mDobEditText = findViewById(R.id.dobEditText);

        mRegisterButton = findViewById(R.id.registerButton);
        mClearButton = findViewById(R.id.clearButton);
    }

    public void configureClickListeners(){
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
                if(confirmPassword.isEmpty() || !password.equals(confirmPassword)){
                    generateToastMessage(R.string.conf_pass_and_pass_required);
                    return;
                }

                RegistrationInfo registrationInfo = new RegistrationInfo(email,password,firstName,lastName,contact,dob);
                validateAndStoreUser(registrationInfo);

                mEmailEditText.setText("");
                mPasswordEditText.setText("");
                mConfirmPasswordEditText.setText("");
                mFirstNameEditText.setText("");
                mLastNameEditText.setText("");
                mContactEditText.setText("");
                mDobEditText.setText("");

                // To hide keyboard
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
            }
        });
    }

    private void validateAndStoreUser(RegistrationInfo registrationInfo){

    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
