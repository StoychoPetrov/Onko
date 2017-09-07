package eu.mobile.onko.globalClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by stoycho.petrov on 05/09/2017.
 */

public class Utils {

    // TYPEFACES PATH
    public static final String ROBOTO_MEDIUM    = "fonts/Roboto-Medium.ttf";
    public static final String ROBOTO_REGULAR   = "fonts/Roboto-Regular.ttf";

    public static void setTypeFace(Context context, TextView textView, String typeFacePath){
        Typeface customFont = Typeface.createFromAsset(context.getAssets(),  typeFacePath);
        textView.setTypeface(customFont);
    }
}
