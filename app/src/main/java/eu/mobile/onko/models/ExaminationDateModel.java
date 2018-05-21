package eu.mobile.onko.models;

/**
 * Created by Stoycho Petrov on 21.5.2018 г..
 */

public class ExaminationDateModel {

    private int         mPeriodId;
    private String      mDate;

    public ExaminationDateModel(int mPeriodId, String mDate) {
        this.mPeriodId = mPeriodId;
        this.mDate = mDate;
    }

    public int getmPeriodId() {
        return mPeriodId;
    }

    public void setmPeriodId(int mPeriodId) {
        this.mPeriodId = mPeriodId;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
