package base.pace.paceplace.course;

public class CourseProfRater {

    private int mNoOfCourseRater, mNoOfProfRater, mCourseId, mStudentCourseId, mProfRateId ,
            mCourseProfessorId  ; // Probably its not necessary
    private float mCourseRatings, mProfessorRatings, mUserCourseRatings, mUserProfessorRatings;

    public CourseProfRater(int noOfCourseRater,int noOfProfRater,int courseId,int studentCourseId,int profRateId ,int courseProfessorId,
                            float courseRatings,float professorRatings,float userCourseRatings,float userProfessorRatings){
        mNoOfCourseRater = noOfCourseRater;
        mNoOfProfRater = noOfProfRater;
        mCourseId = courseId;
        mStudentCourseId = studentCourseId;
        mProfRateId = profRateId;
        mCourseProfessorId = courseProfessorId;
        mCourseRatings = courseRatings;
        mProfessorRatings = professorRatings;
        mUserCourseRatings = userCourseRatings;
        mUserProfessorRatings = userProfessorRatings;
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

    public int getmCourseProfessorId() {
        return mCourseProfessorId;
    }

    public void setmCourseProfessorId(int mCourseProfessorId) {
        this.mCourseProfessorId = mCourseProfessorId;
    }

    public float getmCourseRatings() {
        return mCourseRatings;
    }

    public void setmCourseRatings(float mCourseRatings) {
        this.mCourseRatings = mCourseRatings;
    }

    public float getmProfessorRatings() {
        return mProfessorRatings;
    }

    public void setmProfessorRatings(float mProfessorRatings) {
        this.mProfessorRatings = mProfessorRatings;
    }

    public float getmUserCourseRatings() {
        return mUserCourseRatings;
    }

    public void setmUserCourseRatings(float mUserCourseRatings) {
        this.mUserCourseRatings = mUserCourseRatings;
    }

    public float getmUserProfessorRatings() {
        return mUserProfessorRatings;
    }

    public void setmUserProfessorRatings(float mUserProfessorRatings) {
        this.mUserProfessorRatings = mUserProfessorRatings;
    }
}
