package eu.mobile.onko.models;

/**
 * Created by Stoycho Petrov on 13.5.2018 г..
 */

public class ExaminationModel {

    private int     mExaminationId;
    private String  mExaminationName;

    public ExaminationModel(int mExaminationId, String mExaminationName) {
        this.mExaminationId     = mExaminationId;
        this.mExaminationName   = mExaminationName;
    }

    public int getmExaminationId() {
        return mExaminationId;
    }

    public void setmExaminationId(int mExaminationId) {
        this.mExaminationId = mExaminationId;
    }

    public String getmExaminationName() {
        return mExaminationName;
    }

    public void setmExaminationName(String mExaminationName) {
        this.mExaminationName = mExaminationName;
    }
}
