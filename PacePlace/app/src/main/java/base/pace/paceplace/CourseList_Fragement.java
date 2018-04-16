package base.pace.paceplace;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CourseList_Fragement extends Fragment{

    public static final String TAG = "CourseListTabFragment";
    TextView mCourseNameTextView,mCourseProfessorTextView,mCourseDayTextView,
            mCourseTimeTextView,mCourseAddressTextView,mCourseRoomTextView,
            mCourseStartDateTextView,mCourseEndDateTextView;

    CourseListViewAdapter mAdapter;
    ArrayList<CourseList> mCourseLists = new ArrayList<>();
    ListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragement , vg, false);

        mListView = view.findViewById(R.id.coursesListView);
        /*
        mCourseNameTextView = view.findViewById(R.id.list_item_course_name);
        mCourseProfessorTextView = view.findViewById(R.id.list_item_lec_course_professor);
        mCourseDayTextView = view.findViewById(R.id.list_item_lec_course_day);
        mCourseTimeTextView = view.findViewById(R.id.list_item_lec_course_time);
        mCourseAddressTextView = view.findViewById(R.id.list_item_lec_course_address);
        mCourseRoomTextView = view.findViewById(R.id.list_item_lec_course_room);
        mCourseStartDateTextView= view.findViewById(R.id.list_item_lec_course_start_date);
        mCourseEndDateTextView= view.findViewById(R.id.list_item_lec_course_end_date);*/

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureCourseList();
    }

    private void configureCourseList() {
        //CourseList courseList;

        mCourseLists.add(new CourseList("Algo", "Ratings:-4/4","Suzzana","Ratings:-4/4","Wenesday","6pm - 9pm",
                "163 William Street", "Room:1420","Sept,6 2017","Dec,20 2017"));
        mCourseLists.add(new CourseList("Mobile Web Content","Ratings:-4/4","Haik","Ratings:-4/4","Wenesday","6pm - 9pm",
                "163 William Street", "Room:1420","Sept,6 2017","Dec,20 2017"));
        mCourseLists.add(new CourseList("Project 1","Ratings:-3/4","Yuri","Ratings:-3/4","Wenesday","6pm - 9pm",
                "163 William Street", "Room:1420","Sept,6 2017","Dec,20 2017"));

        mAdapter = new CourseListViewAdapter(getActivity(), mCourseLists);
        mListView.setAdapter(mAdapter);
    }

}
