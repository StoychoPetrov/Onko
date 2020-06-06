package eu.mobile.onko.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private int     mId;
    private String  mEmail;
    private String  mFirstName;
    private String  mLastName;
    private String  mPhone;
    private int     mDoctorId;

    public UserModel() {}

    protected UserModel(Parcel in) {
        mId = in.readInt();
        mEmail = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mPhone = in.readString();
        mDoctorId = in.readInt();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
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

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public int getmDoctorId() {
        return mDoctorId;
    }

    public void setmDoctorId(int mDoctorId) {
        this.mDoctorId = mDoctorId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mEmail);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mPhone);
        dest.writeInt(mDoctorId);
    }
}
