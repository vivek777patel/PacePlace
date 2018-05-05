package base.pace.paceplace.course;

import java.io.Serializable;

public class CourseDetail implements Serializable {
    private String mCourseName, mCourseRatings, mCourseProfessor, mCourseProfRatings, mCourseDay,
            mCourseTime, mCourseAddress, mCourseRoom,
            mCourseStartDate, mCourseEndDate, mStudentCourseRatings, mStudentCourseProfRatings;

    private int mCourseDayInt, mCredit, mNoOfCourseRater, mSeatCapacity, mSeatAvailable,
            mNoOfProfRater, mCourseProfessorId, mCourseId, mStudentCourseId, mProfRateId, mProfUserId;
    //                                                   Graduate       CS or IS      In Class or online
    private String mAddressLine1, mCity, mCourseDesc, mProfEmail, mCourseLevel, mCourseMajor, mCourseType;

    public CourseDetail(String x){
        mCourseName = x;
    }

    public CourseDetail(String courseName, String courseRatings, String courseProfessor, String courseProfRatings, String courseDay,
                        String courseTime, String courseAddress, String courseRoom, String courseStartDate, String courseEndDate,
                        String addressLine1, String city, String courseDesc, String profEmail, String courseLevel, String courseMajor, String courseType,
                        int courseDayInt, int credit, int noOfCourseRater, int seatCapacity, int seatAvailable,int noOfProfRater,
                        String studentCourseRatings, String studentCourseProfRatings, int courseProfessorId,
                        int courseId, int studentCourseId, int profRateId
    ) {
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

        mAddressLine1 = addressLine1;
        mCity = city;
        mCourseDesc = courseDesc;
        mProfEmail = profEmail;
        mCourseLevel = courseLevel;
        mCourseMajor = courseMajor;
        mCourseType = courseType;

        mCourseDayInt = courseDayInt;
        mCredit = credit;
        mNoOfCourseRater = noOfCourseRater;
        mSeatCapacity = seatCapacity;
        mSeatAvailable = seatAvailable;
        mNoOfProfRater = noOfProfRater;
        mStudentCourseRatings = studentCourseRatings;
        mStudentCourseProfRatings = studentCourseProfRatings;

        mCourseProfessorId = courseProfessorId;
        mCourseId = courseId;
        mStudentCourseId = studentCourseId;
        mProfRateId = profRateId;
    }


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


    public void setmCourseName(String mCourseName) {
        this.mCourseName = mCourseName;
    }

    public void setmCourseRatings(String mCourseRatings) {
        this.mCourseRatings = mCourseRatings;
    }

    public void setmCourseProfessor(String mCourseProfessor) {
        this.mCourseProfessor = mCourseProfessor;
    }

    public void setmCourseProfRatings(String mCourseProfRatings) {
        this.mCourseProfRatings = mCourseProfRatings;
    }

    public void setmCourseDay(String mCourseDay) {
        this.mCourseDay = mCourseDay;
    }

    public void setmCourseTime(String mCourseTime) {
        this.mCourseTime = mCourseTime;
    }

    public void setmCourseAddress(String mCourseAddress) {
        this.mCourseAddress = mCourseAddress;
    }

    public void setmCourseRoom(String mCourseRoom) {
        this.mCourseRoom = mCourseRoom;
    }

    public void setmCourseStartDate(String mCourseStartDate) {
        this.mCourseStartDate = mCourseStartDate;
    }

    public void setmCourseEndDate(String mCourseEndDate) {
        this.mCourseEndDate = mCourseEndDate;
    }

    public int getmCourseDayInt() {
        return mCourseDayInt;
    }

    public void setmCourseDayInt(int mCourseDayInt) {
        this.mCourseDayInt = mCourseDayInt;
    }

    public int getmCredit() {
        return mCredit;
    }

    public void setmCredit(int mCredit) {
        this.mCredit = mCredit;
    }

    public int getmNoOfCourseRater() {
        return mNoOfCourseRater;
    }

    public void setmNoOfCourseRater(int mNoOfCourseRater) {
        this.mNoOfCourseRater = mNoOfCourseRater;
    }

    public int getmNoOfProfRater() {
        return mNoOfProfRater;
    }

    public void setmNoOfProfRater(int mNoOfProfRater) {
        this.mNoOfProfRater = mNoOfProfRater;
    }

    public int getmSeatCapacity() {
        return mSeatCapacity;
    }

    public void setmSeatCapacity(int mSeatCapacity) {
        this.mSeatCapacity = mSeatCapacity;
    }

    public int getmSeatAvailable() {
        return mSeatAvailable;
    }

    public void setmSeatAvailable(int mSeatAvailable) {
        this.mSeatAvailable = mSeatAvailable;
    }

    public String getmAddressLine1() {
        return mAddressLine1;
    }

    public void setmAddressLine1(String mAddressLine1) {
        this.mAddressLine1 = mAddressLine1;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmCourseDesc() {
        return mCourseDesc;
    }

    public void setmCourseDesc(String mCourseDesc) {
        this.mCourseDesc = mCourseDesc;
    }

    public String getmProfEmail() {
        return mProfEmail;
    }

    public void setmProfEmail(String mProfEmail) {
        this.mProfEmail = mProfEmail;
    }

    public String getmCourseLevel() {
        return mCourseLevel;
    }

    public void setmCourseLevel(String mCourseLevel) {
        this.mCourseLevel = mCourseLevel;
    }

    public String getmCourseMajor() {
        return mCourseMajor;
    }

    public void setmCourseMajor(String mCourseMajor) {
        this.mCourseMajor = mCourseMajor;
    }

    public String getmCourseType() {
        return mCourseType;
    }

    public void setmCourseType(String mCourseType) {
        this.mCourseType = mCourseType;
    }

    public String getmStudentCourseRatings() {return mStudentCourseRatings;}

    public void setmStudentCourseRatings(String mStudentCourseRatings) {
        this.mStudentCourseRatings = mStudentCourseRatings;
    }

    public String getmStudentCourseProfRatings() {return mStudentCourseProfRatings;}

    public void setmStudentCourseProfRatings(String mStudentCourseProfRatings) {
        this.mStudentCourseProfRatings = mStudentCourseProfRatings;
    }

    public int getmCourseProfessorId() {
        return mCourseProfessorId;
    }

    public void setmCourseProfessorId(int mCourseProfessorId) {
        this.mCourseProfessorId = mCourseProfessorId;
    }

    public int getmCourseId() {
        return mCourseId;
    }

    public void setmCourseId(int mCourseId) {
        this.mCourseId = mCourseId;
    }

    public int getmStudentCourseId() {
        return mStudentCourseId;
    }

    public void setmStudentCourseId(int mStudentCourseId) {
        this.mStudentCourseId = mStudentCourseId;
    }

    public int getmProfRateId() {
        return mProfRateId;
    }

    public void setmProfRateId(int mProfRateId) {
        this.mProfRateId = mProfRateId;
    }

    public int getmProfUserId() {
        return mProfUserId;
    }

    public void setmProfUserId(int mProfUserId) {
        this.mProfUserId = mProfUserId;
    }
}
