package eu.mobile.onko.models;

/**
 * Created by Stoycho Petrov on 5.5.2018 г..
 */

public class MkbModel {

    private int     mMkbId;
    private String  mMkbName;

    public MkbModel(int mMkbId, String mMkbName) {
        this.mMkbId = mMkbId;
        this.mMkbName = mMkbName;
    }

    public int getmMkbId() {
        return mMkbId;
    }

    public void setmMkbId(int mMkbId) {
        this.mMkbId = mMkbId;
    }

    public String getmMkbName() {
        return mMkbName;
    }

    public void setmMkbName(String mMkbName) {
        this.mMkbName = mMkbName;
    }
}
