package eu.mobile.onko.models;

public class ScheduleModel {

    private int mScheduleId;

    private String mStartHour;

    private String mEndHour;

    private int mWeekDayId;

    private int mUserId;

    public int getmScheduleId() {
        return mScheduleId;
    }

    public void setmScheduleId(int mScheduleId) {
        this.mScheduleId = mScheduleId;
    }

    public String getmStartHour() {
        return mStartHour;
    }

    public void setmStartHour(String mStartHour) {
        this.mStartHour = mStartHour;
    }

    public String getmEndHour() {
        return mEndHour;
    }

    public void setmEndHour(String mEndHour) {
        this.mEndHour = mEndHour;
    }

    public int getWeekDayId() {
        return mWeekDayId;
    }

    public void setWeekDayId(int weekDayId) {
        this.mWeekDayId = weekDayId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }
}