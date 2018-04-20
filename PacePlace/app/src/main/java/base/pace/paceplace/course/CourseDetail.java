package base.pace.paceplace.course;

import java.io.Serializable;

public class CourseDetail implements Serializable {
    String mCourseName, mCourseRatings, mCourseProfessor, mCourseProfRatings, mCourseDay,
            mCourseTime, mCourseAddress, mCourseRoom,
            mCourseStartDate, mCourseEndDate;


    public CourseDetail(String courseName, String courseRatings, String courseProfessor, String courseProfRatings, String courseDay,
               String courseTime, String courseAddress, String courseRoom,
               String courseStartDate, String courseEndDate) {
        mCourseName = courseName;
        mCourseRatings = courseRatings;
        mCourseProfessor = courseProfessor;
        mCourseProfRatings = courseProfRatings;
        mCourseProfRatings = courseProfRatings;
        mCourseDay = courseDay;
        mCourseTime = courseTime;
        mCourseAddress = courseAddress;
        mCourseRoom = courseRoom;
        mCourseStartDate = courseStartDate;
        mCourseEndDate = courseEndDate;
    }

    public String getmCourseName() {
        return mCourseName;
    }

    public String getmCourseProfessor() {
        return mCourseProfessor;
    }

    public String getmCourseDay() {
        return mCourseDay;
    }

    public String getmCourseTime() {
        return mCourseTime;
    }

    public String getmCourseAddress() {
        return mCourseAddress;
    }

    public String getmCourseRoom() {
        return mCourseRoom;
    }

    public String getmCourseStartDate() {
        return mCourseStartDate;
    }

    public String getmCourseEndDate() {
        return mCourseEndDate;
    }

    public String getmCourseRatings() {
        return mCourseRatings;
    }

    public String getmCourseProfRatings() {
        return mCourseProfRatings;
    }


}
