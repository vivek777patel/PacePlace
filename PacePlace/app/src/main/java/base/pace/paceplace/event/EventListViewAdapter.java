package base.pace.paceplace.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import base.pace.paceplace.R;

public class EventListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<EventDetail> mEventInfoList;

    private static final String TAG = "CourseListViewAdapter";

    EventListViewAdapter(Context context, List<EventDetail> eventInfoList) {
        mContext = context;
        mEventInfoList = eventInfoList;
        Log.i("alskd",""+mEventInfoList);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.event_recycler_list_view, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EventDetail eventDetail = mEventInfoList.get(position);
        Log.i("assd",""+eventDetail);
        RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        Log.i(eventDetail.getmEventName(),"room");
        viewHolder.mEventNameTextView.setText(eventDetail.getmEventName());
        viewHolder.mEventLocationTextView.setText(mContext.getResources().getString(R.string.event_location, eventDetail.getmEventRoom(), eventDetail.getmEventAddress()));
        viewHolder.mEventDateTimeTextView.setText(eventDetail.getmEventDateTime());
        viewHolder.mEventCreatedByTextView.setText(eventDetail.getmEventCreatedBy());
        viewHolder.mEventForStudentTypeTextView.setText(mContext.getResources().getString(R.string.event_conducted_for, eventDetail.getmEventGradType(), eventDetail.getmEventSubjectType()));
    }

    @Override
    public int getItemCount() {
        return mEventInfoList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mEventNameTextView, mEventDateTimeTextView, mEventLocationTextView, mEventCreatedByTextView, mEventForStudentTypeTextView;

        RecyclerViewHolder(View view) {
            super(view);
            mEventNameTextView = view.findViewById(R.id.list_item_event_name);
            mEventDateTimeTextView = view.findViewById(R.id.list_item_event_date_time);
            mEventLocationTextView = view.findViewById(R.id.list_item_event_location);
            mEventCreatedByTextView = view.findViewById(R.id.list_item__event_created_by);
            mEventForStudentTypeTextView = view.findViewById(R.id.list_item_event_for);
        }
    }
}