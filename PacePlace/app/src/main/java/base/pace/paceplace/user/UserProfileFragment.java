package base.pace.paceplace.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import base.pace.paceplace.LoginActivity;
import base.pace.paceplace.R;
import base.pace.paceplace.httpclient.UserDetailsHttpClient;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.util.PacePlaceConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {

    public static final String TAG = "UserProfileFragment";

    TextView mMandatoryTextView;
    AppCompatEditText mFirstNameEditText, mLastNameEditText, mEmailEditText, mPasswordEditText, mContactEditText, mDobEditText, mConfirmPasswordEditText;
    Button mRegisterButton, mClearButton;

    AppCompatSpinner mGenderSelectSpinner, mAccountTypeSpinner, mGraduationTypeSpinner, mSubjectSelectSpinner, mStudentTypeSpinner;
    //adding map for data
    Map<String, ArrayList<String>> mStaticInfo = new HashMap<>();
    private String mOperationType = PacePlaceConstants.INSERT;
    UserInfo mUserInfo;
    private int mUserId = 0;
    Calendar myCalendar = Calendar.getInstance();
    private UpdateUserInfoObject mUpdateUserInfoObject;

    AVLoadingIndicatorView mAVLoadingIndicatorView;
    String mUserInfoString="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, vg, false);

        mAVLoadingIndicatorView = view.findViewById(R.id.user_registration_avi);

        mMandatoryTextView = view.findViewById(R.id.mandatory_field_text_view);
        mEmailEditText = view.findViewById(R.id.emailEditText);
        mPasswordEditText = view.findViewById(R.id.passEditText);
        mConfirmPasswordEditText = view.findViewById(R.id.confirmPassEditText);
        mFirstNameEditText = view.findViewById(R.id.firstNameEditText);
        mLastNameEditText = view.findViewById(R.id.lastNameEditText);
        mContactEditText = view.findViewById(R.id.contactEditText);
        mDobEditText = view.findViewById(R.id.dobEditText);
        mGenderSelectSpinner = view.findViewById(R.id.genderSpinnerSelect);
        mGraduationTypeSpinner = view.findViewById(R.id.graduationTypeSpinnerSelect);
        mAccountTypeSpinner = view.findViewById(R.id.accountTypeSpinnerSelect);
        mSubjectSelectSpinner = view.findViewById(R.id.subjecTypeSpinnerSelect);
        mStudentTypeSpinner = view.findViewById(R.id.studentTypeSpinnerSelect);

        mRegisterButton = view.findViewById(R.id.registerButton);
        mClearButton = view.findViewById(R.id.clearButton);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearViews();
            }
        });
        mAVLoadingIndicatorView.bringToFront();

        Bundle args = getArguments();
        if (args != null) {
            mUserInfoString = args.getString(PacePlaceConstants.REGISTER);
            mUserInfo = (UserInfo) args.getSerializable(PacePlaceConstants.USER_INFO);
            if (mUserInfoString!= null && mUserInfo != null) {
                mEmailEditText.setEnabled(Boolean.FALSE);
                mRegisterButton.setText(R.string.btn_save_txt);
                mEmailEditText.setText(mUserInfo.getmEmail());
                mFirstNameEditText.setText(mUserInfo.getmFirstName());
                mLastNameEditText.setText(mUserInfo.getmLastName());
                mContactEditText.setText(mUserInfo.getmContact());
                mDobEditText.setText(mUserInfo.getmDob());
                mPasswordEditText.setText(mUserInfo.getmPassword());
                mConfirmPasswordEditText.setText(mUserInfo.getmPassword());
                mOperationType = PacePlaceConstants.UPDATE;
                mUserId = mUserInfo.getmUserId();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    myCalendar.setTime(sdf.parse(mUserInfo.getmDob()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        mDobEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        getStaticInfo();

        return view;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(compareDates())
            mDobEditText.setText(sdf.format(myCalendar.getTime()));
        else
            generateToastMessage(R.string.dob_validation);
    }

    private Boolean compareDates() {
        Calendar currentDate = Calendar.getInstance();
        if (myCalendar.getTime().before(currentDate.getTime()))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public void configureSpinner(AppCompatSpinner studentTypeSpinner, String typeSpinner) {
        ArrayList<String> finalString = new ArrayList<>();
        finalString.add(typeSpinner);
        ArrayList<String> list = mStaticInfo.get(typeSpinner);
        finalString.addAll(list);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_layout, finalString);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        studentTypeSpinner.setAdapter(dataAdapter);
    }

    private void getStaticInfo() {

        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.smoothToShow();
        UserDetailsHttpClient.getInstance().getStaticInfo(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    if (response.body() != null) {
                        Log.i(TAG, "Response");
                        JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                        if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                            JSONObject responseDataJSON = receivedJSONObject.getJSONObject(PacePlaceConstants.DATA);
                            Iterator<?> keys = responseDataJSON.keys();
                            while (keys.hasNext()) {
                                String key = (String) keys.next();
                                ArrayList<String> list = new ArrayList<>();
                                JSONArray jsonArray = (JSONArray) responseDataJSON.get(key);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray innerJsonArray = jsonArray.getJSONArray(i);
                                    list.add(innerJsonArray.get(1).toString());
                                }
                                mStaticInfo.put(key, list);
                            }
                            configureSpinner(mGenderSelectSpinner, PacePlaceConstants.GENDER);
                            configureSpinner(mGraduationTypeSpinner, PacePlaceConstants.GRADUATION_TYPE);
                            configureSpinner(mAccountTypeSpinner, PacePlaceConstants.ACCOUNT_TYPE);
                            configureSpinner(mSubjectSelectSpinner, PacePlaceConstants.SUBJECT);
                            configureSpinner(mStudentTypeSpinner, PacePlaceConstants.STUDENT_TYPE);

                            if (null != mUserInfo) {
                                setSpinnerValue(mGenderSelectSpinner, mUserInfo.getmGender());
                                setSpinnerValue(mGraduationTypeSpinner, mUserInfo.getmGraduationType());
                                setSpinnerValue(mAccountTypeSpinner, mUserInfo.getmAccountType());
                                setSpinnerValue(mSubjectSelectSpinner, mUserInfo.getmSubject());
                                setSpinnerValue(mStudentTypeSpinner, mUserInfo.getmStudentType());
                            }


                        } else {
                            generateToastMessage(R.string.static_issue_in_response_json);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    generateToastMessage(R.string.static_issue_in_response_json);
                }
                finally {
                    mAVLoadingIndicatorView.smoothToHide();
                    mAVLoadingIndicatorView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.static_issue_in_response_json);
                mAVLoadingIndicatorView.smoothToHide();
                mAVLoadingIndicatorView.setVisibility(View.GONE);
            }
        });
    }

    private void setSpinnerValue(AppCompatSpinner spinner, String value) {
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(value);
        spinner.setSelection(spinnerPosition);
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
                String gender = mGenderSelectSpinner.getSelectedItem().toString();
                String graduationType = mGraduationTypeSpinner.getSelectedItem().toString();
                String accountType = mAccountTypeSpinner.getSelectedItem().toString();
                String subjectType = mSubjectSelectSpinner.getSelectedItem().toString();
                String studentType = mStudentTypeSpinner.getSelectedItem().toString();

                int genderSelectedPos = mGenderSelectSpinner.getSelectedItemPosition();
                int graduationTypeSelectedPos = mGraduationTypeSpinner.getSelectedItemPosition();
                int accountTypeSelectedPos = mAccountTypeSpinner.getSelectedItemPosition();
                int subjectTypeSelectedPos = mSubjectSelectSpinner.getSelectedItemPosition();
                int studentTypeSelectedPos = mStudentTypeSpinner.getSelectedItemPosition();


                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty() || dob.isEmpty()
                        || genderSelectedPos == 0 || graduationTypeSelectedPos == 0 || accountTypeSelectedPos == 0 || subjectTypeSelectedPos == 0 || studentTypeSelectedPos == 0) {
                    generateToastMessage(R.string.all_fields_required);
                    if (email.isEmpty()) {
                        mEmailEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    if (password.isEmpty()) {
                        mPasswordEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    if (confirmPassword.isEmpty()) {
                        mConfirmPasswordEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    if (firstName.isEmpty()) {
                        mFirstNameEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    if (lastName.isEmpty()) {
                        mLastNameEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    if (contact.isEmpty()) {
                        mContactEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    if (dob.isEmpty()) {
                        mDobEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }

                    return;
                }
                if (!password.equals(confirmPassword)) {
                    generateToastMessage(R.string.conf_pass_and_pass_required);
                    return;
                }

                //UserInfo registrationInfo = new UserInfo(email, password, firstName, lastName, contact, dob);
                UserInfo registrationInfo = new UserInfo(email, password, firstName, lastName, contact, dob,
                        gender, graduationType, studentType, subjectType, accountType);
                validateAndStoreUser(registrationInfo);

                // To hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != imm) {
                    imm.hideSoftInputFromWindow(mEmailEditText.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
                }
            }
        });
    }

    private void validateAndStoreUser(final UserInfo registrationInfo) {

        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.smoothToShow();
        UserDetailsHttpClient.getInstance().registerUser(registrationInfo.getmEmail(), registrationInfo.getmPassword(), registrationInfo.getmFirstName(),registrationInfo.getmLastName(),
                registrationInfo.getmContact(), registrationInfo.getmDob(),mOperationType, String.valueOf(mUserId),
                registrationInfo.getmGender(), registrationInfo.getmGraduationType(), registrationInfo.getmStudentType(), registrationInfo.getmSubject(), registrationInfo.getmAccountType(), new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                                Log.i(TAG, "Response");
                                JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                                if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                                    if (mOperationType.equalsIgnoreCase(PacePlaceConstants.INSERT)) {
                                        generateToastMessage(R.string.register_success);
                                        clearViews();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        generateToastMessage(R.string.user_profile_updated_success);
                                        registrationInfo.setmUserId(mUserId);
                                        mUpdateUserInfoObject.getUserInfo(registrationInfo);
                                    }
                                } else {
                                    generateToastMessage(R.string.register_failed);
                                }
                            } else {
                                generateToastMessage(R.string.register_failed);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            generateToastMessage(R.string.register_failed);
                        }
                        finally {
                            mAVLoadingIndicatorView.setVisibility(View.GONE);
                            mAVLoadingIndicatorView.smoothToHide();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                        generateToastMessage(R.string.register_failed);
                        mAVLoadingIndicatorView.setVisibility(View.GONE);
                        mAVLoadingIndicatorView.smoothToHide();
                    }
                });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            if(activity instanceof UpdateUserInfoObject)
                mUpdateUserInfoObject = (UpdateUserInfoObject) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface UpdateUserInfoObject {
        void getUserInfo(UserInfo userInfo);
    }

    private void clearViews() {
        // To clear the text views
        //To check whether its edit screen or new registration screen
        if(mUserInfoString.isEmpty())
            mEmailEditText.setText("");

        mPasswordEditText.setText("");
        mConfirmPasswordEditText.setText("");
        mFirstNameEditText.setText("");
        mLastNameEditText.setText("");
        mContactEditText.setText("");
        mDobEditText.setText("");

        // Change it to white in case any validation error came earlier
        mEmailEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        mPasswordEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        mConfirmPasswordEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        mFirstNameEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        mLastNameEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        mContactEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        mDobEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));

        configureSpinner(mGenderSelectSpinner, PacePlaceConstants.GENDER);
        configureSpinner(mGraduationTypeSpinner, PacePlaceConstants.GRADUATION_TYPE);
        configureSpinner(mAccountTypeSpinner, PacePlaceConstants.ACCOUNT_TYPE);
        configureSpinner(mSubjectSelectSpinner, PacePlaceConstants.SUBJECT);
        configureSpinner(mStudentTypeSpinner, PacePlaceConstants.STUDENT_TYPE);
    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }


}
