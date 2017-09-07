package eu.mobile.onko.models;

import android.graphics.drawable.Drawable;

/**
 * Created by stoycho.petrov on 05/09/2017.
 */

public class ProfileModel {

    private String      mName;
    private String      mEmail;
    private Drawable    mProfileImage;

    public ProfileModel(String mName, String mEmail, Drawable mProfileImage) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mProfileImage = mProfileImage;
    }

    public String getmName() {

        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public Drawable getmProfileImage() {
        return mProfileImage;
    }

    public void setmProfileImage(Drawable mProfileImage) {
        this.mProfileImage = mProfileImage;
    }
}
