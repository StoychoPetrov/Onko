package eu.mobile.onko.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stoycho Petrov on 15.5.2018 г..
 */

public class DoctorModel implements Parcelable {

    private int         mDoctorId;
    private String      mDoctorName;

    public DoctorModel(int mDoctorId, String mDoctorName) {
        this.mDoctorId      = mDoctorId;
        this.mDoctorName    = mDoctorName;
    }

    protected DoctorModel(Parcel in) {
        mDoctorId = in.readInt();
        mDoctorName = in.readString();
    }

    public static final Creator<DoctorModel> CREATOR = new Creator<DoctorModel>() {
        @Override
        public DoctorModel createFromParcel(Parcel in) {
            return new DoctorModel(in);
        }

        @Override
        public DoctorModel[] newArray(int size) {
            return new DoctorModel[size];
        }
    };

    public int getmDoctorId() {
        return mDoctorId;
    }

    public void setmDoctorId(int mDoctorId) {
        this.mDoctorId = mDoctorId;
    }

    public String getmDoctorName() {
        return mDoctorName;
    }

    public void setmDoctorName(String mDoctorName) {
        this.mDoctorName = mDoctorName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDoctorId);
        dest.writeString(mDoctorName);
    }
}
