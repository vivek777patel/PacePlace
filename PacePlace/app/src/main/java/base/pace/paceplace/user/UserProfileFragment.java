package base.pace.paceplace.user;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import base.pace.paceplace.LoginActivity;
import base.pace.paceplace.R;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.util.CommonWSInvoke;
import base.pace.paceplace.util.PacePlaceConstants;
import base.pace.paceplace.util.WebServiceResponse;

public class UserProfileFragment extends Fragment{

    public static final String TAG = "UserProfileFragment";

    AppCompatEditText mFirstNameEditText, mLastNameEditText, mEmailEditText, mPasswordEditText, mContactEditText, mDobEditText, mConfirmPasswordEditText;
    Button mRegisterButton, mClearButton;
    ProgressDialog mProgressDialog;
    AppCompatSpinner mGenderSelectSpinner,mAccountTypeSpinner, mGraduationTypeSpinner, mSubjectSelectSpinner, mStudentTypeSpinner;
    //adding map for datad
    Map<String,ArrayList<String>> mStaticInfo = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {

        ArrayList<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        mStaticInfo.put(PacePlaceConstants.GENDER,gender);
        ArrayList<String> gradType = new ArrayList<>();
        gradType.add("Bachelors");
        gradType.add("Masters");
        gradType.add("Doctorate");
        mStaticInfo.put(PacePlaceConstants.GRADUATION_TYPE,gradType);
        ArrayList<String> accnt_type = new ArrayList<>();
        accnt_type.add("Student");
        accnt_type.add("Professor");
        mStaticInfo.put(PacePlaceConstants.ACCOUNT_TYPE,accnt_type);
        ArrayList<String> studntType = new ArrayList<>();
        studntType.add("Local");
        studntType.add("International");
        mStaticInfo.put(PacePlaceConstants.STUDENT_TYPE,studntType);
        ArrayList<String>  sbjct= new ArrayList<>();
        sbjct.add("Computer Science");
        sbjct.add("Master in Business Adminstration");
        sbjct.add("Information System");
        mStaticInfo.put(PacePlaceConstants.SUBJECT,sbjct);

        View view = inflater.inflate(R.layout.fragment_user_profile, vg, false);
        mEmailEditText = view.findViewById(R.id.emailEditText);
        mPasswordEditText = view.findViewById(R.id.passEditText);
        mConfirmPasswordEditText = view.findViewById(R.id.confirmPassEditText);
        mFirstNameEditText = view.findViewById(R.id.firstNameEditText);
        mLastNameEditText = view.findViewById(R.id.lastNameEditText);
        mContactEditText = view.findViewById(R.id.contactEditText);
        mDobEditText = view.findViewById(R.id.dobEditText);
        mGenderSelectSpinner = view.findViewById(R.id.genderSpinnerSelect);
        mGraduationTypeSpinner = view.findViewById(R.id.graduationTypeSpinnerSelect) ;
        mAccountTypeSpinner = view.findViewById(R.id.accountTypeSpinnerSelect);
        mSubjectSelectSpinner = view.findViewById(R.id.subjecTypeSpinnerSelect);
        mStudentTypeSpinner = view.findViewById(R.id.studentTypeSpinnerSelect);

        mRegisterButton = view.findViewById(R.id.registerButton);
        mClearButton = view.findViewById(R.id.clearButton);

        configureSpinner(mGenderSelectSpinner,PacePlaceConstants.GENDER);
        configureSpinner(mGraduationTypeSpinner,PacePlaceConstants.GRADUATION_TYPE);
        configureSpinner(mAccountTypeSpinner,PacePlaceConstants.ACCOUNT_TYPE);
        configureSpinner(mSubjectSelectSpinner,PacePlaceConstants.SUBJECT);
        configureSpinner(mStudentTypeSpinner,PacePlaceConstants.STUDENT_TYPE);

        return view;
    }
        public void configureSpinner(AppCompatSpinner studentTypeSpinner,String typeSpinner) {
        ArrayList<String> list = mStaticInfo.get(typeSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_layout, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        studentTypeSpinner.setAdapter(dataAdapter);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureClickListeners();
    }

    private void configureClickListeners() {
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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(null!=imm) {
                    imm.hideSoftInputFromWindow(mEmailEditText.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
                }
            }
        });
    }

    private void validateAndStoreUser(final UserInfo registrationInfo) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        Thread threadA = new Thread() {
            public void run() {
                CommonWSInvoke threadB = new CommonWSInvoke(getActivity());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Response is: " + receivedJSONObject);
                        if (null != receivedJSONObject) {
                            if (receivedJSONObject.getmResponse()) {
                                generateToastMessage(R.string.register_success);
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
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
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }


}
