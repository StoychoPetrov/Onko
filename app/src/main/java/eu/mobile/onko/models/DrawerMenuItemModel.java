package eu.mobile.onko.models;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by stoycho.petrov on 04/09/2017.
 */

public class DrawerMenuItemModel {

    private String      mTitle;
    private Drawable    mImage;

    public DrawerMenuItemModel(String mTitle, Drawable mImage) {
        this.mTitle = mTitle;
        this.mImage = mImage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Drawable getmImage() {
        return mImage;
    }

    public void setmImage(Drawable mImage) {
        this.mImage = mImage;
    }
}
