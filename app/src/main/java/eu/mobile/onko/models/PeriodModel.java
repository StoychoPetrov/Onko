package eu.mobile.onko.models;

/**
 * Created by Stoycho Petrov on 21.5.2018 г..
 */

public class PeriodModel {
    private int     mPeriodId;
    private int     mPeriodName;

    public PeriodModel(int mPeriodId, int mPeriodName) {
        this.mPeriodId = mPeriodId;
        this.mPeriodName = mPeriodName;
    }

    public int getmPeriodId() {
        return mPeriodId;
    }

    public void setmPeriodId(int mPeriodId) {
        this.mPeriodId = mPeriodId;
    }

    public int getmPeriodName() {
        return mPeriodName;
    }

    public void setmPeriodName(int mPeriodName) {
        this.mPeriodName = mPeriodName;
    }
}
