package base.pace.paceplace.course;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import base.pace.paceplace.R;

public class CourseRegistrationListViewAdapter extends BaseAdapter {

    Context mContext;
    List<CourseDetail> mCourseInfoList;
    View.OnClickListener mClickListener;
    private static final String TAG = "CourseRegistrationListViewAdapter";

    public CourseRegistrationListViewAdapter(Context context, List<CourseDetail> courseInfoList, View.OnClickListener itemClickListener){
        mContext = context;
        mCourseInfoList = courseInfoList;
        mClickListener = itemClickListener;
    }

    @Override
    public int getCount() { return mCourseInfoList.size(); }

    @Override
    public Object getItem(int position) { return mCourseInfoList.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.course_registration_list_view, null);
            viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.list_item_course_name),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_professor),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_day),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_time),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_address),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_room),
                    (ImageView) convertView.findViewById(R.id.list_item_course_add_remove)); // creating new item/ViewHolder

            convertView.setTag(viewHolder);

        }
        else
            viewHolder = (ViewHolder)convertView.getTag();  //reusing item/ViewHolder

        CourseDetail courseInfo = (CourseDetail) getItem(position);
        //Log.i(TAG, "SSSSSS"+  mContext.getResources().getString(R.string.course_rating,courseInfo.getmCourseRatings()));
        viewHolder.mCourseNameTextView.setText(courseInfo.getmCourseName());
        viewHolder.mCourseProfessorTextView.setText(mContext.getResources().getString(R.string.course_professor,courseInfo.getmCourseProfessor()));
        viewHolder.mCourseDayTextView.setText(mContext.getResources().getString(R.string.course_day,courseInfo.getmCourseDay()));
        viewHolder.mCourseTimeTextView.setText(mContext.getResources().getString(R.string.course_time,courseInfo.getmCourseTime()));
        viewHolder.mCourseAddressTextView.setText(courseInfo.getmCourseAddress());
        viewHolder.mCourseRoomTextView.setText(courseInfo.getmCourseRoom());
        viewHolder.mCourseNameTextView.setTag(position);

        if (courseInfo.getmCourseSelectedIndicator()){
            viewHolder.mCourseAddRemoveImgView.setBackgroundResource(R.drawable.ic_remove);
        }
        else {
            viewHolder.mCourseAddRemoveImgView.setBackgroundResource(R.drawable.ic_add);
        }
        viewHolder.mCourseAddRemoveImgView.setTag(position);
        viewHolder.mCourseAddRemoveImgView.setOnClickListener(mClickListener);
        return convertView;
    }


    private class ViewHolder {

        TextView mCourseNameTextView, mCourseProfessorTextView, mCourseDayTextView,
                mCourseTimeTextView,mCourseAddressTextView,mCourseRoomTextView;
        ImageView mCourseAddRemoveImgView;

        ViewHolder(TextView courseNameTextView, TextView courseProfessorTextView,TextView courseDayTextView,
                   TextView courseTimeTextView, TextView courseAddressTextView, TextView courseRoomTextView,
                   ImageView courseAddRemoveImgView) {
            mCourseNameTextView = courseNameTextView;
            mCourseProfessorTextView = courseProfessorTextView;
            mCourseDayTextView = courseDayTextView;
            mCourseTimeTextView = courseTimeTextView;
            mCourseAddressTextView = courseAddressTextView;
            mCourseRoomTextView = courseRoomTextView;
            mCourseAddRemoveImgView = courseAddRemoveImgView;
        }
    }
}
