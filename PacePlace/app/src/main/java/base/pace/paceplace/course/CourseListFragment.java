package base.pace.paceplace.course;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import base.pace.paceplace.R;
import base.pace.paceplace.util.PacePlaceConstants;

public class CourseListFragment extends Fragment{

    public static final String TAG = "CourseListFragment";
    /*TextView mCourseNameTextView,mCourseProfessorTextView,mCourseDayTextView,
            mCourseTimeTextView,mCourseAddressTextView,mCourseRoomTextView,
            mCourseStartDateTextView,mCourseEndDateTextView;*/

    CourseListViewAdapter mAdapter;
    ArrayList<CourseDetail> mCourseLists = new ArrayList<>();
    ListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_fragement , vg, false);

        mListView = view.findViewById(R.id.coursesListView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureCourseList();
    }

    private void configureCourseList() {
        Bundle args = getArguments();
        mCourseLists = (ArrayList<CourseDetail>) args.get(PacePlaceConstants.COURSE_LIST);
        mAdapter = new CourseListViewAdapter(getActivity(), mCourseLists);
        mListView.setAdapter(mAdapter);
    }

}
