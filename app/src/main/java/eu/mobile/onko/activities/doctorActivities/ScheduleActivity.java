package eu.mobile.onko.activities.doctorActivities;

import android.app.TimePickerDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import eu.mobile.onko.R;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.ScheduleModel;
import eu.mobile.onko.models.WeekDayEnum;

public class ScheduleActivity extends AppCompatActivity implements ResponseListener, View.OnClickListener {

    private HashMap<Integer, ScheduleModel> mWorkingHours  = new HashMap<>();

    private ConstraintLayout mMondayLayout;
    private ConstraintLayout mTuesdayLayout;
    private ConstraintLayout mWednesdayLayout;
    private ConstraintLayout mThursdayLayout;
    private ConstraintLayout mFridayLayout;
    private ConstraintLayout mSaturdayLayout;
    private ConstraintLayout mSundayLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initUI();
        setListeners();
        getSchedule();
    }

    private void initUI() {
        mMondayLayout   = findViewById(R.id.mondayLayout);
        mTuesdayLayout  = findViewById(R.id.tuesdayLayout);
        mWednesdayLayout= findViewById(R.id.wednesdayLayout);
        mThursdayLayout = findViewById(R.id.thursdayLayout);
        mFridayLayout   = findViewById(R.id.fridayLayout);
        mSaturdayLayout = findViewById(R.id.saturdayLayout);
        mSundayLayout   = findViewById(R.id.sundayLayout);

        ((TextView) mMondayLayout.findViewById(R.id.dayTxt)).setText(getString(R.string.monday));
        ((TextView) mTuesdayLayout.findViewById(R.id.dayTxt)).setText(getString(R.string.tuesday));
        ((TextView) mWednesdayLayout.findViewById(R.id.dayTxt)).setText(getString(R.string.wednesday));
        ((TextView) mThursdayLayout.findViewById(R.id.dayTxt)).setText(getString(R.string.thursday));
        ((TextView) mFridayLayout.findViewById(R.id.dayTxt)).setText(getString(R.string.friday));
        ((TextView) mSaturdayLayout.findViewById(R.id.dayTxt)).setText(getString(R.string.saturday));
        ((TextView) mSundayLayout.findViewById(R.id.dayTxt)).setText(getString(R.string.sunday));
    }

    private void setListeners() {
        mMondayLayout.findViewById(R.id.startHourTxt).setOnClickListener(this);
        mMondayLayout.findViewById(R.id.endHourTxt).setOnClickListener(this);

        mTuesdayLayout.findViewById(R.id.startHourTxt).setOnClickListener(this);
        mTuesdayLayout.findViewById(R.id.endHourTxt).setOnClickListener(this);

        mWednesdayLayout.findViewById(R.id.startHourTxt).setOnClickListener(this);
        mWednesdayLayout.findViewById(R.id.endHourTxt).setOnClickListener(this);

        mThursdayLayout.findViewById(R.id.startHourTxt).setOnClickListener(this);
        mThursdayLayout.findViewById(R.id.endHourTxt).setOnClickListener(this);

        mFridayLayout.findViewById(R.id.startHourTxt).setOnClickListener(this);
        mFridayLayout.findViewById(R.id.endHourTxt).setOnClickListener(this);

        mSaturdayLayout.findViewById(R.id.startHourTxt).setOnClickListener(this);
        mSaturdayLayout.findViewById(R.id.endHourTxt).setOnClickListener(this);

        mSundayLayout.findViewById(R.id.startHourTxt).setOnClickListener(this);
        mSundayLayout.findViewById(R.id.endHourTxt).setOnClickListener(this);

        findViewById(R.id.back_arrow_img).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_txt)).setText(R.string.working_time);
    }

    private void getSchedule(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Utils.USER_TOKEN , GlobalData.getInstance().getmToken());
            PostRequest postRequest = new PostRequest(this, jsonObject, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.GET_SCHEDULERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void putHour(ScheduleModel scheduleModel) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Utils.ID             , scheduleModel.getmScheduleId());
            jsonObject.put(Utils.START_HOUR     , !scheduleModel.getmStartHour().equalsIgnoreCase("N/A") ? scheduleModel.getmStartHour() : null);
            jsonObject.put(Utils.END_HOUR       , !scheduleModel.getmEndHour().equalsIgnoreCase("N/A") ? scheduleModel.getmEndHour() : null);
            jsonObject.put(Utils.WEEK_DAY_ID    , scheduleModel.getWeekDayId());
            jsonObject.put(Utils.USER_ID        , scheduleModel.getmUserId());


            PostRequest postRequest = new PostRequest(this, jsonObject, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.SCHEDULES + scheduleModel.getmScheduleId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {

        mMondayLayout.findViewById(R.id.startHourTxt).setTag(mWorkingHours.get(WeekDayEnum.MONDAY.getId()));
        mMondayLayout.findViewById(R.id.endHourTxt).setTag(mWorkingHours.get(WeekDayEnum.MONDAY.getId()));

        mTuesdayLayout.findViewById(R.id.startHourTxt).setTag(mWorkingHours.get(WeekDayEnum.TUESDAY.getId()));
        mTuesdayLayout.findViewById(R.id.endHourTxt).setTag(mWorkingHours.get(WeekDayEnum.TUESDAY.getId()));

        mWednesdayLayout.findViewById(R.id.startHourTxt).setTag(mWorkingHours.get(WeekDayEnum.WEDNESDAY.getId()));
        mWednesdayLayout.findViewById(R.id.endHourTxt).setTag(mWorkingHours.get(WeekDayEnum.WEDNESDAY.getId()));

        mThursdayLayout.findViewById(R.id.startHourTxt).setTag(mWorkingHours.get(WeekDayEnum.THURSDAY.getId()));
        mThursdayLayout.findViewById(R.id.endHourTxt).setTag(mWorkingHours.get(WeekDayEnum.THURSDAY.getId()));

        mFridayLayout.findViewById(R.id.startHourTxt).setTag(mWorkingHours.get(WeekDayEnum.FRIDAY.getId()));
        mFridayLayout.findViewById(R.id.endHourTxt).setTag(mWorkingHours.get(WeekDayEnum.FRIDAY.getId()));

        mSaturdayLayout.findViewById(R.id.startHourTxt).setTag(mWorkingHours.get(WeekDayEnum.SATURDAY.getId()));
        mSaturdayLayout.findViewById(R.id.endHourTxt).setTag(mWorkingHours.get(WeekDayEnum.SATURDAY.getId()));

        mSundayLayout.findViewById(R.id.startHourTxt).setTag(mWorkingHours.get(WeekDayEnum.SUNDAY.getId()));
        mSundayLayout.findViewById(R.id.endHourTxt).setTag(mWorkingHours.get(WeekDayEnum.SUNDAY.getId()));

        ((TextView) mMondayLayout.findViewById(R.id.startHourTxt)).setText(mWorkingHours.get(WeekDayEnum.MONDAY.getId()).getmStartHour());
        ((TextView) mMondayLayout.findViewById(R.id.endHourTxt)).setText(mWorkingHours.get(WeekDayEnum.MONDAY.getId()).getmEndHour());

        ((TextView) mTuesdayLayout.findViewById(R.id.startHourTxt)).setText(mWorkingHours.get(WeekDayEnum.TUESDAY.getId()).getmStartHour());
        ((TextView) mTuesdayLayout.findViewById(R.id.endHourTxt)).setText(mWorkingHours.get(WeekDayEnum.TUESDAY.getId()).getmEndHour());

        ((TextView) mWednesdayLayout.findViewById(R.id.startHourTxt)).setText(mWorkingHours.get(WeekDayEnum.WEDNESDAY.getId()).getmStartHour());
        ((TextView) mWednesdayLayout.findViewById(R.id.endHourTxt)).setText(mWorkingHours.get(WeekDayEnum.WEDNESDAY.getId()).getmEndHour());

        ((TextView) mThursdayLayout.findViewById(R.id.startHourTxt)).setText(mWorkingHours.get(WeekDayEnum.THURSDAY.getId()).getmStartHour());
        ((TextView) mThursdayLayout.findViewById(R.id.endHourTxt)).setText(mWorkingHours.get(WeekDayEnum.THURSDAY.getId()).getmEndHour());

        ((TextView) mFridayLayout.findViewById(R.id.startHourTxt)).setText(mWorkingHours.get(WeekDayEnum.FRIDAY.getId()).getmStartHour());
        ((TextView) mFridayLayout.findViewById(R.id.endHourTxt)).setText(mWorkingHours.get(WeekDayEnum.FRIDAY.getId()).getmEndHour());

        ((TextView) mSaturdayLayout.findViewById(R.id.startHourTxt)).setText(mWorkingHours.get(WeekDayEnum.SATURDAY.getId()).getmStartHour());
        ((TextView) mSaturdayLayout.findViewById(R.id.endHourTxt)).setText(mWorkingHours.get(WeekDayEnum.SATURDAY.getId()).getmEndHour());

        ((TextView) mSundayLayout.findViewById(R.id.startHourTxt)).setText(mWorkingHours.get(WeekDayEnum.SUNDAY.getId()).getmStartHour());
        ((TextView) mSundayLayout.findViewById(R.id.endHourTxt)).setText(mWorkingHours.get(WeekDayEnum.SUNDAY.getId()).getmEndHour());
    }

    private void showTimePicker(final TextView textView, final ScheduleModel scheduleModel, final boolean isStart) {

        String time         = textView.getText().toString();
        String[]  timeArray = time.split(":");

        int hour    = !time.equalsIgnoreCase("N/A") ? Integer.parseInt(timeArray[0]) : 9;
        int minutes = !time.equalsIgnoreCase("N/A") ? Integer.parseInt(timeArray[1]) : 0;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int pHour, int pMinute) {
                        String time = (pHour < 10 ? "0" + pHour : pHour) + ":" + (pMinute < 10 ? "0" + pMinute : pMinute);
                        textView.setText(time);
                        if(isStart)
                            scheduleModel.setmStartHour(time);
                        else
                            scheduleModel.setmEndHour(time);

                        putHour(scheduleModel);
                    }
                }, hour, minutes, true);

        timePickerDialog.show();
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String response) {
        if(url.equalsIgnoreCase(Utils.URL + Utils.GET_SCHEDULERS)) {
            if (responseCode == Utils.STATUS_SUCCESS) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    mWorkingHours.clear();
                    for (int i = 0 ; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        ScheduleModel scheduleModel = new ScheduleModel();
                        scheduleModel.setmScheduleId(jsonObject.getInt(Utils.SCHEDULE_ID));
                        scheduleModel.setmStartHour(jsonObject.getString(Utils.START_HOUR));
                        scheduleModel.setmEndHour(jsonObject.getString(Utils.END_HOUR));
                        scheduleModel.setWeekDayId(jsonObject.getInt(Utils.WEEK_DAY_ID));
                        scheduleModel.setmUserId(jsonObject.getInt(Utils.USER_ID));

                        mWorkingHours.put(scheduleModel.getWeekDayId(), scheduleModel);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData();
                        }
                    });
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            else
                Toast.makeText(this, getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        if(view instanceof TextView)
            showTimePicker((TextView) view, (ScheduleModel) view.getTag(), view.getId() == R.id.startHourTxt);
        else if(view.getId() == R.id.back_arrow_img)
            onBackPressed();
    }
}