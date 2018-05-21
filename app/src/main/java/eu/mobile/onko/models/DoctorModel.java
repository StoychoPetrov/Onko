package eu.mobile.onko.models;

/**
 * Created by Stoycho Petrov on 15.5.2018 г..
 */

public class DoctorModel {

    private int         mDoctorId;
    private String      mDoctorName;

    public DoctorModel(int mDoctorId, String mDoctorName) {
        this.mDoctorId      = mDoctorId;
        this.mDoctorName    = mDoctorName;
    }

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
}
