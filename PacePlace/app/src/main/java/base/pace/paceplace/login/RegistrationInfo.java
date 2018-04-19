package base.pace.paceplace.login;

import java.io.Serializable;

public class RegistrationInfo implements Serializable {

    private String mFirstName, mLastName, mEmail, mPassword, mContact, mDob;

    public RegistrationInfo(){

    }

    public RegistrationInfo(String email,String password,String firstName,String lastName,String contact,String dob){
        mEmail = email;
        mPassword = password;
        mFirstName = firstName;
        mLastName = lastName;
        mContact = contact;
        mDob = dob;
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
}
