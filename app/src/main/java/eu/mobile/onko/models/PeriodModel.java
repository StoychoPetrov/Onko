package eu.mobile.onko.models;

/**
 * Created by Stoycho Petrov on 21.5.2018 г..
 */

public class PeriodModel {
    private int     mPeriodId;
    private String  mPeriodName;

    public PeriodModel(int mPeriodId, String mPeriodName) {
        this.mPeriodId = mPeriodId;
        this.mPeriodName = mPeriodName;
    }

    public int getmPeriodId() {
        return mPeriodId;
    }

    public void setmPeriodId(int mPeriodId) {
        this.mPeriodId = mPeriodId;
    }

    public String getmPeriodName() {
        return mPeriodName;
    }

    public void setmPeriodName(String mPeriodName) {
        this.mPeriodName = mPeriodName;
    }
}
