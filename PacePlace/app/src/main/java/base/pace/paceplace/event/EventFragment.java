package base.pace.paceplace.event;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.pace.paceplace.R;
import base.pace.paceplace.httpclient.EventDetailsHttpClient;
import base.pace.paceplace.util.PacePlaceConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventFragment extends Fragment {

    private final String TAG = "EventFragment";
    private RecyclerView mRecyclerView;
    private EventListViewAdapter mAdapter;
    private List<EventDetail> mEventList = new ArrayList<>();
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    private SwipeRefreshLayout mSwipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragement, container, false);
        mAVLoadingIndicatorView = view.findViewById(R.id.avi_event_details);
        mSwipeContainer = view.findViewById(R.id.swipeContainer_event_details);
        mRecyclerView = view.findViewById(R.id.event_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        configureEventListView();
    }

    private void configureEventListView() {

        getEventList();

        mAdapter = new EventListViewAdapter(getActivity(), mEventList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        mEventList.clear();
                        getEventList();
                    }
                }
        );
    }

    private void getEventList() {
        mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
        mAVLoadingIndicatorView.smoothToShow();
        EventDetailsHttpClient.getInstance().getEventDetails(new Callback<JsonObject>() {
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
                                    mEventList.add(setEventDetailsFromResponse(obj));
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                // No data found
                                generateToastMessage(R.string.events_not_found);
                            }
                        } else {
                            generateToastMessage(R.string.event_issue_in_response_json);
                        }
                    }
                    else {
                        generateToastMessage(R.string.event_issue_in_response_json);
                    }
                } catch (JSONException e) {
                    generateToastMessage(R.string.event_issue_in_response_json);
                    e.printStackTrace();
                }
                mAVLoadingIndicatorView.setVisibility(View.GONE);
                mAVLoadingIndicatorView.smoothToHide();
                mSwipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                generateToastMessage(R.string.event_issue_in_response_json);
                mAVLoadingIndicatorView.setVisibility(View.GONE);
                mAVLoadingIndicatorView.smoothToHide();
                mSwipeContainer.setRefreshing(false);
            }
        });
    }

    private EventDetail setEventDetailsFromResponse(JSONObject jsonObject) {
        return new EventDetail(
                jsonObject.optString("event_name"),
                jsonObject.optString("event_day"),
                jsonObject.optString("event_date"),
                jsonObject.optString("location_name"),
                jsonObject.optString("address_line1"),
                jsonObject.optString("grad_type"),
                jsonObject.optString("subject"),
                jsonObject.optString("firstname")
                );
    }

                // To generate Toast message
    private void generateToastMessage(int id) {
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }
}
