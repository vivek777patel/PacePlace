package base.pace.paceplace.event;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import base.pace.paceplace.R;
import base.pace.paceplace.httpclient.EventDetailsHttpClient;
import base.pace.paceplace.httpclient.UserDetailsHttpClient;
import base.pace.paceplace.login.UserInfo;
import base.pace.paceplace.util.PacePlaceConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventGenerateFragment extends Fragment {

    private final String TAG = "EventGenerateFragment";
    private List<String> mLocationList = new ArrayList<>();
    AppCompatEditText mEventNameEditText, mEventDateEditText, mEventDescriptionEditText;
    Button mRegisterButton, mClearButton;
    AppCompatSpinner mGraduationTypeSpinner, mSubjectSelectSpinner, mLocationSpinner;
    //adding map for data
    Map<String, ArrayList<String>> mStaticInfo = new HashMap<>();
    Calendar myCalendar = Calendar.getInstance();
    UserInfo mUserInfo;
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_generate_layout, container, false);

        mEventNameEditText = view.findViewById(R.id.eventNameEditText);
        mEventDateEditText = view.findViewById(R.id.eventDateEditText);
        mEventDescriptionEditText = view.findViewById(R.id.eventDescEditText);
        mRegisterButton = view.findViewById(R.id.registerEventButton);
        mClearButton = view.findViewById(R.id.clearButton);
        mGraduationTypeSpinner = view.findViewById(R.id.graduationTypeEventSpinnerSelect);
        mSubjectSelectSpinner = view.findViewById(R.id.subjecTypeEventSpinnerSelect);
        mLocationSpinner = view.findViewById(R.id.locationSpinnerSelect);
        mAVLoadingIndicatorView = view.findViewById(R.id.event_generate_avi);
        Bundle args = getArguments();
        if (args != null) {
            mUserInfo = (UserInfo) args.getSerializable(PacePlaceConstants.USER_INFO);
        }

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearViews();
            }
        });

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

        mEventDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureClickListeners();
        mAVLoadingIndicatorView.bringToFront();
        getStaticInfo();
        getLocation();
    }

    private void getLocation(){
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.smoothToShow();
        EventDetailsHttpClient.getInstance().getLocationDetails(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    if (response.body() != null) {
                        JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                        if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                            JSONArray courseDetailsJsonArray = receivedJSONObject.getJSONArray(PacePlaceConstants.DATA);
                            if (courseDetailsJsonArray != null) {
                                for (int i = 0; i < courseDetailsJsonArray.length(); i++) {
                                    JSONObject obj = courseDetailsJsonArray.getJSONObject(i);
                                    mLocationList.add(obj.optString("location_name"));
                                }
                                configureLocationSpinner(mLocationSpinner,PacePlaceConstants.LOCATION);
                            } else {
                                // No data found
                                generateToastMessage(R.string.location_not_found);
                            }
                        } else {
                            generateToastMessage(R.string.location_issue_in_response_json);
                        }
                    }
                    else {
                        generateToastMessage(R.string.location_issue_in_response_json);
                    }
                } catch (JSONException e) {
                    generateToastMessage(R.string.location_issue_in_response_json);
                    e.printStackTrace();
                }
                finally {
                    mAVLoadingIndicatorView.setVisibility(View.GONE);
                    mAVLoadingIndicatorView.smoothToHide();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.event_issue_in_response_json);
            }
        });
    }

    public void configureLocationSpinner(AppCompatSpinner typeSpinner, String typeSpinnerString) {
        ArrayList<String> finalString = new ArrayList<>();
        finalString.add(typeSpinnerString);
        finalString.addAll(mLocationList);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_layout, finalString);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        typeSpinner.setAdapter(dataAdapter);
    }

    public void configureSpinner(AppCompatSpinner typeSpinner, String typeSpinnerString) {
        ArrayList<String> finalString = new ArrayList<>();
        finalString.add(typeSpinnerString);
        ArrayList<String> list = mStaticInfo.get(typeSpinnerString);
        finalString.addAll(list);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_layout, finalString);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        typeSpinner.setAdapter(dataAdapter);
    }

    private void configureClickListeners() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventName = mEventNameEditText.getText().toString();
                String eventDate = mEventDateEditText.getText().toString();
                String eventDescription = mEventDescriptionEditText.getText().toString();
                String location = mLocationSpinner.getSelectedItem().toString();

                String graduationType = mGraduationTypeSpinner.getSelectedItem().toString();
                String subjectType = mSubjectSelectSpinner.getSelectedItem().toString();

                int locationPos = mLocationSpinner.getSelectedItemPosition();

                if(eventName.isEmpty() || eventDate.isEmpty() || eventDescription.isEmpty() || locationPos==0){
                    if (eventName.isEmpty()) {
                        mEventNameEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    if (eventDate.isEmpty()) {
                        mEventDateEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    if (eventDescription.isEmpty()) {
                        mEventDescriptionEditText.setHintTextColor(getResources().getColor(R.color.colorRed));
                    }
                    generateToastMessage(R.string.all_fields_required);
                    return;
                }


                // TODO : Get user id from main fragment to get event created by
                EventDetail newEventDetails = new EventDetail(eventName, eventDate, eventDescription,
                        graduationType, subjectType, mUserInfo.getmUserId(), location);

                registerEvent(newEventDetails);

                // To hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != imm) {
                    imm.hideSoftInputFromWindow(mEventNameEditText.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(mEventDateEditText.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(mEventDescriptionEditText.getWindowToken(), 0);
                }
            }
        });
    }

    private void registerEvent(EventDetail eventDetail) {
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.smoothToShow();
        EventDetailsHttpClient.getInstance().addEvent(eventDetail.getmEventName(), eventDetail.getmEventDescription(),
                eventDetail.getmEventAddress(), String.valueOf(eventDetail.getmEventCreatedByUserId()), eventDetail.getmEventDateTime(),
                eventDetail.getmEventSubjectType(), eventDetail.getmEventGradType()
                , new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                                JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                                if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                                    generateToastMessage(R.string.event_posted_success);
                                    clearViews();
                                } else {
                                    generateToastMessage(R.string.event_posted_failed);
                                }
                            } else {
                                generateToastMessage(R.string.event_posted_failed);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            generateToastMessage(R.string.event_posted_failed);
                        }
                        finally {
                            mAVLoadingIndicatorView.setVisibility(View.GONE);
                            mAVLoadingIndicatorView.smoothToHide();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                        generateToastMessage(R.string.event_posted_failed);
                        mAVLoadingIndicatorView.setVisibility(View.GONE);
                        mAVLoadingIndicatorView.smoothToHide();
                    }
                });
    }

    private void clearViews() {
        // To clear the text views
        mEventNameEditText.setText("");
        mEventDescriptionEditText.setText("");
        mEventDateEditText.setText("");

        configureSpinner(mGraduationTypeSpinner, PacePlaceConstants.GRADUATION_TYPE);
        configureSpinner(mSubjectSelectSpinner, PacePlaceConstants.SUBJECT);
        configureLocationSpinner(mLocationSpinner, PacePlaceConstants.LOCATION);
    }

    private void getStaticInfo() {
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.smoothToShow();
        UserDetailsHttpClient.getInstance().getStaticInfo(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    if (response.body() != null) {
                        JSONObject receivedJSONObject = new JSONObject(response.body().toString());

                        if ((Boolean) receivedJSONObject.get(PacePlaceConstants.RESPONSE)) {
                            JSONObject responseDataJSON = (JSONObject) receivedJSONObject.getJSONObject(PacePlaceConstants.DATA);
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
                            configureSpinner(mGraduationTypeSpinner, PacePlaceConstants.GRADUATION_TYPE);
                            configureSpinner(mSubjectSelectSpinner, PacePlaceConstants.SUBJECT);

                        } else {
                            generateToastMessage(R.string.static_issue_in_response_json);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    generateToastMessage(R.string.static_issue_in_response_json);
                    mAVLoadingIndicatorView.setVisibility(View.GONE);
                    mAVLoadingIndicatorView.smoothToHide();
                }
                finally {
                    mAVLoadingIndicatorView.setVisibility(View.GONE);
                    mAVLoadingIndicatorView.smoothToHide();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.static_issue_in_response_json);
                mAVLoadingIndicatorView.setVisibility(View.GONE);
                mAVLoadingIndicatorView.smoothToHide();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (compareDates())
            mEventDateEditText.setText(sdf.format(myCalendar.getTime()));
        else
            generateToastMessage(R.string.event_date_validation);
    }

    private Boolean compareDates() {
        Calendar currentDate = Calendar.getInstance();
        if (myCalendar.getTime().after(currentDate.getTime()))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }
}
