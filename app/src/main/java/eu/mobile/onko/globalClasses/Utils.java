package eu.mobile.onko.globalClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by stoycho.petrov on 05/09/2017.
 */

public class Utils {

    // REQUEST LINKS
    public static final String URL                      = "http://192.168.0.106:50110/api/";
    public static final String LOGIN                    = "users/login";
    public static final String REGISTER                 = "users/register";
    public static final String USER_MKBS                = "userMkb/getAll";
    public static final String MKB_GROUPS               = "MkbGroups";
    public static final String MKBS                     = "Mkbs";
    public static final String ADD_ACTION               = "/add";
    public static final String DELETE_ACTION            = "/delete";
    public static final String USER_MKB_CONTROLLER      = "userMkb";

    // QUERY PARAMS
    public static final String GROUP_ID     = "?groupId=";

    // COMMUNICATION PARAMS
    public static final String EMAIL    = "email";
    public static final String PASSWORD = "password";

    //HTTP STATUS
    public static final int    STATUS_SUCCESS   = 200;
    public static final int    STATUS_NOT_FOUND = 404;

    // TYPEFACES PATH
    public static final String ROBOTO_MEDIUM    = "fonts/Roboto-Medium.ttf";
    public static final String ROBOTO_REGULAR   = "fonts/Roboto-Regular.ttf";

    // JSON PARAMS
    public static final String ID              = "id";
    public static final String USER_EMAIL      = "user_email";
    public static final String USER_PASSWORD   = "user_password";
    public static final String USER_FIRST_NAME = "user_first_name";
    public static final String USER_LAST_NAME  = "user_last_name";
    public static final String USER_PHONE      = "user_phone";
    public static final String USER_MKB        = "UserMKB";
    public static final String USER_TOKEN      = "auth_token";
    public static final String MKB_ID          = "MkbId";
    public static final String MKB_NAME        = "MkbName";
    public static final String MKB_GROUP_NAME  = "mkb_group_name";
    public static final String MKB_TITLE       = "mkb_name";
    public static final String MKB_ID_PARAM    = "mkb_id";
    public static final String USER_MKB_ID     = "UserMkbId";

    // INTENT EXTRAS
    public static final String INTENT_GROUP_ID      = "group_id";
    public static final String INTENT_GROUP_NAME    = "group_name";
    public static final String INTENT_MKB_ID        = "mkb_id";

    public static void setTypeFace(Context context, TextView textView, String typeFacePath){
        Typeface customFont = Typeface.createFromAsset(context.getAssets(),  typeFacePath);
        textView.setTypeface(customFont);
    }
}
