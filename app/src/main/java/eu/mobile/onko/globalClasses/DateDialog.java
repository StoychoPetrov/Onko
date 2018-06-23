package eu.mobile.onko.globalClasses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Stoycho Petrov on 27.5.2018 г..
 */

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private String          mDefaultDate         = "";
    private long            mMinDateInMillis     = 0;
    private OnDateSelected  mListener;

    public interface OnDateSelected {
        void onDateSelect(Calendar calendar);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;

        if(!mDefaultDate.equalsIgnoreCase("")){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendar.setTime(sdf.parse(mDefaultDate));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        year    = calendar.get(Calendar.YEAR);
        month   = calendar.get(Calendar.MONTH);
        day     = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(),this,year,month,day);

        if(mMinDateInMillis != 0)
            dpd.getDatePicker().setMinDate(mMinDateInMillis);

        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR          , year);
        calendar.set(Calendar.MONTH         , month);
        calendar.set(Calendar.DAY_OF_MONTH  , dayOfMonth);

        if(mListener != null)
            mListener.onDateSelect(calendar);
    }

    public void setmDefaultDate(String mDefaultDate) {
        this.mDefaultDate = mDefaultDate;
    }

    public void setmListener(OnDateSelected mListener) {
        this.mListener = mListener;
    }

    public long getmMinDateInMillis() {
        return mMinDateInMillis;
    }

    public void setmMinDateInMillis(long mMinDateInMillis) {
        this.mMinDateInMillis = mMinDateInMillis;
    }
}
