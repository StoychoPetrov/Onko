package eu.mobile.onko.globalClasses;

/**
 * Created by Stoycho Petrov on 9.4.2018 г..
 */

public class GlobalData {

    private static GlobalData instance;

    private int     mUserId;
    private String  mUserEmail;
    private String  mPassword;
    private String  mFirstName;
    private String  mLastName;
    private String  mPhoneNumber;
    private String  mToken;

    public static GlobalData getInstance(){
        if(instance != null)
            return instance;
        else
            instance = new GlobalData();

        return instance;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserEmail() {
        return mUserEmail;
    }

    public void setmUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
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

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }
}
