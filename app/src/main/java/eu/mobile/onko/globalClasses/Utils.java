package eu.mobile.onko.globalClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by stoycho.petrov on 05/09/2017.
 */

public class Utils {

    // REQUEST LINKS
    public static final String URL                      = "http://90.154.211.179:50110/api/";
    public static final String LOGIN                    = "users/login";
    public static final String REGISTER_PUSH_TOKEN      = "users/registerPushToken";
    public static final String REGISTER                 = "users/register";
    public static final String USER_MKBS                = "userMkb/getAll";
    public static final String MKB_GROUPS               = "MkbGroups";
    public static final String MKBS                     = "Mkbs";
    public static final String ADD_ACTION               = "/add";
    public static final String DELETE_ACTION            = "/delete";
    public static final String USER_MKB_CONTROLLER      = "userMkb";
    public static final String GET_EXAMINATIONS         = "examinations/getByMkb";
    public static final String GET_DOCTORS              = "doctors/getDoctors";
    public static final String GET_USER_EXAMINATION     = "userExaminations/getUserExaminations";
    public static final String EXAMINATIONS_CONTROLLER  = "userExaminations";

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
    public static final String ID               = "id";
    public static final String USER_EMAIL       = "user_email";
    public static final String USER_PASSWORD    = "user_password";
    public static final String USER_FIRST_NAME  = "user_first_name";
    public static final String USER_LAST_NAME   = "user_last_name";
    public static final String USER_PHONE       = "user_phone";
    public static final String USER_MKB         = "UserMKB";
    public static final String USER_TOKEN       = "auth_token";
    public static final String MKB_ID           = "MkbId";
    public static final String MKB_NAME         = "MkbName";
    public static final String MKB_GROUP_NAME   = "mkb_group_name";
    public static final String MKB_TITLE        = "mkb_name";
    public static final String MKB_ID_PARAM     = "mkb_id";
    public static final String USER_MKB_ID      = "UserMkbId";
    public static final String EXAMINATION_ID   = "examination_id";
    public static final String EXAMINATION_NAME = "examination_name";
    public static final String MKB_GROUP        = "mkb_group";
    public static final String DOCTORS          = "doctors";
    public static final String DOCTOR_NAME      = "doctor_name";
    public static final String USER_MKB_ID_PARAM= "user_mkb_id";
    public static final String EXAMINATIONS     = "examinations";
    public static final String PERIODS          = "periods";
    public static final String PERIOD_ID        = "period_id";
    public static final String DATE             = "date";
    public static final String PERIOD_NAME      = "period_name";
    public static final String ID_USER_MKB      = "id_user_mkb";
    public static final String ID_PERIOD        = "id_period";
    public static final String ID_EXAMINATION   = "id_examination";
    public static final String EXAMINATION_DATE = "examination_date";
    public static final String USER_EXAMINATION_ID  = "user_examination_id";
    public static final String PUSH_TOKEN           = "push_token";


    // INTENT EXTRAS
    public static final String INTENT_GROUP_ID      = "group_id";
    public static final String INTENT_GROUP_NAME    = "group_name";
    public static final String INTENT_MKB_ID        = "mkb_id";

    // PREFERENCES
    public static final String PREFERENCES_PUSH_TOKEN   = "push_token";

    public static final String SUPPORT_EMAIL            = "stoychopetrov95@gmail.com";

    public static void setTypeFace(Context context, TextView textView, String typeFacePath){
        Typeface customFont = Typeface.createFromAsset(context.getAssets(),  typeFacePath);
        textView.setTypeface(customFont);
    }
}
