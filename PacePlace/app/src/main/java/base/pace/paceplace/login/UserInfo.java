package base.pace.paceplace.login;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {

    private int mUserId;
    private String mFirstName, mLastName, mEmail, mPassword, mContact, mDob;
    private String mGender,mGraduationType,mStudentType,mSubject,mStatus,mAccountType;
    private int mGenderInt,mGraduationTypeInt,mStudentTypeInt,mSubjectInt,mStatusInt,mAccountTypeInt;
    private Date mStartDate,mEndDate,mGraduationDate,mLastLoginTime;

    public UserInfo(){

    }

    public UserInfo(String email,String password,String firstName,String lastName,String contact,String dob){
        mEmail = email;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;
        mContact = contact;
        mDob = dob;
    }

    public UserInfo(String email,String password,String firstName,String lastName,String contact,String dob,
                    String gender,String graduationType,String studentType,String subject, String accountType){
        mEmail = email;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;
        mContact = contact;
        mDob = dob;
        mGender = gender;
        mGraduationType = graduationType;
        mStudentType = studentType;
        mSubject = subject;
        mAccountType = accountType;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmContact() {
        return mContact;
    }

    public void setmContact(String mContact) {
        this.mContact = mContact;
    }

    public String getmDob() {
        return mDob;
    }

    public void setmDob(String mDob) {
        this.mDob = mDob;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmGraduationType() {
        return mGraduationType;
    }

    public void setmGraduationType(String mGraduationType) {
        this.mGraduationType = mGraduationType;
    }

    public String getmStudentType() {
        return mStudentType;
    }

    public void setmStudentType(String mStudentType) {
        this.mStudentType = mStudentType;
    }

    public String getmSubject() {
        return mSubject;
    }

    public void setmSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public int getmGenderInt() {
        return mGenderInt;
    }

    public void setmGenderInt(int mGenderInt) {
        this.mGenderInt = mGenderInt;
    }

    public int getmGraduationTypeInt() {
        return mGraduationTypeInt;
    }

    public void setmGraduationTypeInt(int mGraduationTypeInt) {
        this.mGraduationTypeInt = mGraduationTypeInt;
    }

    public int getmStudentTypeInt() {
        return mStudentTypeInt;
    }

    public void setmStudentTypeInt(int mStudentTypeInt) {
        this.mStudentTypeInt = mStudentTypeInt;
    }

    public int getmSubjectInt() {
        return mSubjectInt;
    }

    public void setmSubjectInt(int mSubjectInt) {
        this.mSubjectInt = mSubjectInt;
    }

    public int getmStatusInt() {
        return mStatusInt;
    }

    public void setmStatusInt(int mStatusInt) {
        this.mStatusInt = mStatusInt;
    }

    public Date getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Date getmEndDate() {
        return mEndDate;
    }

    public void setmEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }

    public Date getmGraduationDate() {
        return mGraduationDate;
    }

    public void setmGraduationDate(Date mGraduationDate) {
        this.mGraduationDate = mGraduationDate;
    }

    public Date getmLastLoginTime() {
        return mLastLoginTime;
    }

    public void setmLastLoginTime(Date mLastLoginTime) {
        this.mLastLoginTime = mLastLoginTime;
    }

    public String getmAccountType() {
        return mAccountType;
    }

    public void setmAccountType(String mAccountType) {
        this.mAccountType = mAccountType;
    }

    public int getmAccountTypeInt() {
        return mAccountTypeInt;
    }

    public void setmAccountTypeInt(int mAccountTypeInt) {
        this.mAccountTypeInt = mAccountTypeInt;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }
}
