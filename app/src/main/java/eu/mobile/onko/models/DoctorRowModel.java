package eu.mobile.onko.models;

/**
 * Created by Stoycho Petrov on 15.5.2018 г..
 */

public class DoctorRowModel {

    public static final int SECTION_INDEX   = 0;
    public static final int ITEM_INDEX      = 1;

    private int     mRowType;
    private int     mDoctorId;
    private String  mLabel;

    public DoctorRowModel(int mRowType, String mLabel, int doctorId) {
        this.mRowType   = mRowType;
        this.mLabel     = mLabel;
        this.mDoctorId  = doctorId;
    }

    public int getmRowType() {
        return mRowType;
    }

    public void setmRowType(int mRowType) {
        this.mRowType = mRowType;
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public int getmDoctorId() {
        return mDoctorId;
    }

    public void setmDoctorId(int mDoctorId) {
        this.mDoctorId = mDoctorId;
    }
}
