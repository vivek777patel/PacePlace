package base.pace.paceplace.course;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import base.pace.paceplace.R;

public class CourseListViewAdapter extends BaseAdapter {
    Context mContext;
    List<CourseDetail> mCourseInfoList;
    View.OnClickListener mClickListener;
    private static final String TAG = "CourseListViewAdapter";

    public CourseListViewAdapter(Context context, List<CourseDetail> courseInfoList){
        mContext = context;
        mCourseInfoList = courseInfoList;
    }

    @Override
    public int getCount() {
        return mCourseInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourseInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.courses_list_view, null);
            viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.list_item_course_name),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_ratings),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_professor),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_prof_ratings),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_day),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_time),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_address),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_room),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_start_date),
                    (TextView) convertView.findViewById(R.id.list_item_lec_course_end_date)); // creating new item/ViewHolder
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();  //reusing item/ViewHolder

        CourseDetail courseInfo = (CourseDetail) getItem(position);
        viewHolder.mCourseNameTextView.setText(courseInfo.getmCourseName());
        viewHolder.mCourseRatingsTextView.setText(courseInfo.mCourseRatings);
        viewHolder.mCourseProfessorTextView.setText(courseInfo.getmCourseProfessor());
        viewHolder.mCourseProfRatingsTextView.setText(courseInfo.mCourseProfRatings);
        viewHolder.mCourseDayTextView.setText(courseInfo.getmCourseDay());
        viewHolder.mCourseTimeTextView.setText(courseInfo.getmCourseTime());
        viewHolder.mCourseAddressTextView.setText(courseInfo.getmCourseAddress());
        viewHolder.mCourseRoomTextView.setText(courseInfo.getmCourseRoom());
        viewHolder.mCourseStartDateTextView.setText(courseInfo.getmCourseStartDate());
        viewHolder.mCourseEndDateTextView.setText(courseInfo.getmCourseEndDate());
        viewHolder.mCourseNameTextView.setTag(courseInfo);

        return convertView;
    }

    private class ViewHolder {

        TextView mCourseNameTextView, mCourseRatingsTextView, mCourseProfessorTextView, mCourseProfRatingsTextView,mCourseDayTextView,
                mCourseTimeTextView,mCourseAddressTextView,mCourseRoomTextView,
                mCourseStartDateTextView,mCourseEndDateTextView;

        ViewHolder(TextView courseNameTextView,TextView courseRatingsTextView, TextView courseProfessorTextView, TextView courseProfRatingsTextView,TextView courseDayTextView,
                   TextView courseTimeTextView, TextView courseAddressTextView, TextView courseRoomTextView,
                   TextView courseStartDateTextView, TextView courseEndDateTextView) {
            mCourseNameTextView = courseNameTextView;
            mCourseRatingsTextView = courseRatingsTextView;
            mCourseProfessorTextView = courseProfessorTextView;
            mCourseProfRatingsTextView = courseProfRatingsTextView;
            mCourseDayTextView = courseDayTextView;
            mCourseTimeTextView = courseTimeTextView;
            mCourseAddressTextView = courseAddressTextView;
            mCourseRoomTextView = courseRoomTextView;
            mCourseStartDateTextView = courseStartDateTextView;
            mCourseEndDateTextView = courseEndDateTextView;
        }
    }
}
