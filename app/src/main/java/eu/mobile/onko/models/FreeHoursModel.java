package eu.mobile.onko.models;

public class FreeHoursModel {

    private int         mScheduleId;

    private String[]    mFreeHours;

    public FreeHoursModel() {
        mFreeHours  = new String[0];
    }

    public FreeHoursModel(int mScheduleId, String[] mFreeHours) {
        this.mScheduleId    = mScheduleId;
        this.mFreeHours     = mFreeHours;
    }

    public int getScheduleId() {
        return mScheduleId;
    }

    public void setScheduleId(int mScheduleId) {
        this.mScheduleId = mScheduleId;
    }

    public String[] getFreeHours() {
        return mFreeHours;
    }

    public void setFreeHours(String[] mFreeHours) {
        this.mFreeHours = mFreeHours;
    }
}