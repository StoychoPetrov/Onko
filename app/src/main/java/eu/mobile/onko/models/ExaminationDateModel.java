package eu.mobile.onko.models;

/**
 * Created by Stoycho Petrov on 21.5.2018 г..
 */

public class ExaminationDateModel {

    public static final int PAST_EXAMINATION        = 0;
    public static final int PREDICTED_EXAMINATION   = 1;

    private int         mId;
    private int         mType;
    private int         mPeriodId;
    private int         mPeriodName;
    private String      mDate;

    public ExaminationDateModel(int id, int mPeriodId, String mDate) {
        this.mId        = id;
        this.mPeriodId  = mPeriodId;
        this.mDate      = mDate;
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

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public int getmPeriodName() {
        return mPeriodName;
    }

    public void setmPeriodName(int mPeriodName) {
        this.mPeriodName = mPeriodName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
