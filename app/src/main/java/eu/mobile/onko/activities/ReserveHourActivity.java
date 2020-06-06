package eu.mobile.onko.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import eu.mobile.onko.OnItemClickListener;
import eu.mobile.onko.R;
import eu.mobile.onko.adapters.FreeHoursAdapter;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.ReservationDialog;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.FreeHoursModel;
import eu.mobile.onko.models.CreateReservationModel;
import eu.mobile.onko.models.UserModel;

import static eu.mobile.onko.globalClasses.Utils.SERVER_DATE_TIME;

public class ReserveHourActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, OnItemClickListener {

    private ImageView                   mBackArrowImg;
    private ImageView                   mLeftArrowImg;
    private ImageView                   mRightArrowImg;
    private RecyclerView mFreeHoursRecyclerView;
    private TextView                    mDateTxt;

    private RecyclerView.LayoutManager  mLayoutManager;

    private FreeHoursModel              mFreeHours          = new FreeHoursModel();
    private FreeHoursAdapter            mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_hour);

        initUI();
        setListeners();
        setAdapter();
    }

    private void initUI(){
        mBackArrowImg             = findViewById(R.id.back_arrow_img);
        mFreeHoursRecyclerView    = findViewById(R.id.freeHoursRecyclerView);
        mDateTxt                  = findViewById(R.id.dateTxt);
        mLeftArrowImg             = findViewById(R.id.left_arrow_img);
        mRightArrowImg            = findViewById(R.id.right_arrow_img);

        ((TextView) findViewById(R.id.title_txt)).setText(R.string.free_hours);

        mFreeHoursRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mFreeHoursRecyclerView.setLayoutManager(mLayoutManager);

        setDate(Calendar.getInstance().getTime());
    }

    private void setListeners(){
        mBackArrowImg.setOnClickListener(this);
        mDateTxt.setOnClickListener(this);
        mRightArrowImg.setOnClickListener(this);
        mLeftArrowImg.setOnClickListener(this);
    }

    private void setDate(Date date){
        mDateTxt.setText(Utils.formatDate(date, Utils.DATE_FORMAT));
        getDoctorsRequest(Utils.formatDate(date, Utils.SERVER_DATE_FORMAT));
    }

    private void setAdapter() {
        mFreeHoursRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mFreeHoursRecyclerView.setLayoutManager(mLayoutManager);

        mFreeHoursRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new FreeHoursAdapter(new ArrayList<>(Arrays.asList(mFreeHours.getFreeHours())), this);
        mFreeHoursRecyclerView.setAdapter(mAdapter);
    }

    private void getDoctorsRequest(String date){

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Utils.USER_TOKEN , GlobalData.getInstance().getmToken());
            jsonObject.put(Utils.DOCTOR_ID  , ((UserModel) getIntent().getParcelableExtra(Utils.INTENT_DOCTOR)).getmId());
            jsonObject.put(Utils.DATE       , date);

            PostRequest postRequest = new PostRequest(this, jsonObject, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.GET_FREE_HOURS);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void showDatePicker() {
        Date initDate       = Utils.parseDate(mDateTxt.getText().toString());
        final Calendar calendar   = Calendar.getInstance();
        calendar.setTime(initDate);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR          , year);
                calendar.set(Calendar.MONTH         , month);
                calendar.set(Calendar.DAY_OF_MONTH  , dayOfMonth);

                setDate(calendar.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        dialog.getDatePicker().setMinDate(tomorrow.getTimeInMillis());
        dialog.show();
    }

    private void shiftDate(boolean isNext) {
        Date date = Utils.parseDate(mDateTxt.getText().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_MONTH, isNext ? 1 : -1);

        setDate(calendar.getTime());
    }

    private void saveHour(Date date) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Utils.USER_TOKEN         , GlobalData.getInstance().getmToken());
            jsonObject.put(Utils.DATE               , Utils.formatDate(date, SERVER_DATE_TIME));
            jsonObject.put(Utils.SCHEDULE_ID        , mFreeHours.getScheduleId());
            jsonObject.put(Utils.CLIENT_ID          , GlobalData.getInstance().getmUserId());

            PostRequest postRequest = new PostRequest(this, jsonObject, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.RESERVATIONS);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackArrowImg.getId())
            onBackPressed();
        else if(view.getId() == mDateTxt.getId())
            showDatePicker();
        else if(view.getId() == mLeftArrowImg.getId())
            shiftDate(false);
        else if(view.getId() == mRightArrowImg.getId())
            shiftDate(true);
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String response) {
        try {
            if(url.equals(Utils.URL + Utils.GET_FREE_HOURS)) {
                if (responseCode == Utils.STATUS_SUCCESS) {
                    JSONObject data = new JSONObject(response);
                    JSONArray hours = data.getJSONArray(Utils.FREE_HOURS);

                    String[] hoursArray = new String[hours.length()];

                    for (int i = 0; i < hours.length(); i++)
                        hoursArray[i] = hours.getString(i);

                    mFreeHours = new FreeHoursModel(
                            data.getInt(Utils.SCHEDULE_ID),
                            hoursArray
                    );

                    mAdapter.setmItemsArrayList(new ArrayList<>(Arrays.asList(mFreeHours.getFreeHours())));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            else {
                if(responseCode == Utils.STATUS_SUCCESS) {
                    finish();
                }
                else
                    Toast.makeText(this, getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickItem(int position) {

        String[] hours = mFreeHours.getFreeHours()[position].split(":");
        Date date = Utils.parseDate(mDateTxt.getText().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY   , Integer.parseInt(hours[0]));
        calendar.set(Calendar.MINUTE        , Integer.parseInt(hours[1]));

        UserModel doctor = getIntent().getParcelableExtra(Utils.INTENT_DOCTOR);

        final CreateReservationModel createReservationModel = new CreateReservationModel();
        createReservationModel.setDate(calendar.getTime());
        createReservationModel.setCustomerName(GlobalData.getInstance().getmFirstName() + " " + GlobalData.getInstance().getmLastName());
        createReservationModel.setDoctorName(doctor.getmFirstName() + " " + doctor.getmLastName());
        createReservationModel.setDoctorTitle(getIntent().getStringExtra(Utils.DOCTOR_NAME));

        ReservationDialog dialog = new ReservationDialog(this, createReservationModel,
                new ReservationDialog.OnButtonClickListeners() {
            @Override
            public void onPositiveClicked() {
                saveHour(createReservationModel.getDate());
            }
        });
        dialog.show();
    }
}