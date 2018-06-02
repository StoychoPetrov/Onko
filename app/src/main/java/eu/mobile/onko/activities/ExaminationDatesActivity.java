package eu.mobile.onko.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eu.mobile.onko.R;
import eu.mobile.onko.adapters.ExaminationsDatesAdapter;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.DateDialog;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.ExaminationDateModel;
import eu.mobile.onko.models.PeriodModel;

public class ExaminationDatesActivity extends AppCompatActivity implements ResponseListener, View.OnClickListener, ExaminationsDatesAdapter.OnItemClick {

    private ImageView                               mBackArrowImg;
    private TextView                                mTitleTxt;
    private RecyclerView                            mDatesListView;
    private Button                                  mAddDateBtn;

    private RecyclerView.LayoutManager              mLayoutManager;
    private ExaminationsDatesAdapter                mAdapter;
    private ArrayList<ExaminationDateModel>         mExaminationsDatesArrayList = new ArrayList<>();
    private ArrayList<PeriodModel>                  mPeriodsArrayList           = new ArrayList<>();
    private HashMap<Integer, ExaminationDateModel>  mDates                      = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_dates);

        initUI();
        setListeners();
        setAdapter();

        try {
            getExaminationsDate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initUI(){
        mBackArrowImg   = findViewById(R.id.back_arrow_img);
        mTitleTxt       = findViewById(R.id.title_txt);
        mDatesListView  = findViewById(R.id.dates_listview);
        mAddDateBtn     = findViewById(R.id.add_date_btn);

        mTitleTxt.setText(R.string.examinations_dates);
    }

    private void setAdapter(){
        mLayoutManager = new LinearLayoutManager(this);
        mDatesListView.setLayoutManager(mLayoutManager);

        mAdapter = new ExaminationsDatesAdapter(this, mExaminationsDatesArrayList);
        mDatesListView.setAdapter(mAdapter);
        mAdapter.setmListener(this);
    }

    private void setListeners(){
        mBackArrowImg.setOnClickListener(this);
        mAddDateBtn.setOnClickListener(this);
    }

    private void setData(){
        if(mDates.size() > 0) {
            mAddDateBtn.setVisibility(View.GONE);
            findViewById(R.id.no_data).setVisibility(View.GONE);

            for (int i = 0; i < mPeriodsArrayList.size(); i++) {
                PeriodModel periodModel = mPeriodsArrayList.get(i);

                if (!mDates.containsKey(periodModel.getmPeriodId())) {
                    String previousDate = mDates.get(mPeriodsArrayList.get(i - 1).getmPeriodId()).getmDate();
                    int difference = periodModel.getmPeriodName() - mDates.get(mPeriodsArrayList.get(i - 1).getmPeriodId()).getmPeriodName();

                    try {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.setTime(sdf.parse(previousDate));

                        calendar.add(Calendar.DAY_OF_YEAR, difference * 7);

                        ExaminationDateModel examinationDateModel = new ExaminationDateModel(0, periodModel.getmPeriodId(), sdf.format(calendar.getTime()));
                        examinationDateModel.setmPeriodName(periodModel.getmPeriodName());
                        examinationDateModel.setmType(ExaminationDateModel.PREDICTED_EXAMINATION);

                        mDates.put(periodModel.getmPeriodId(), examinationDateModel);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            Iterator it = mDates.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                mExaminationsDatesArrayList.add((ExaminationDateModel) pair.getValue());
                it.remove();
            }

            Collections.sort(mExaminationsDatesArrayList, new Comparator<ExaminationDateModel>() {
                @Override
                public int compare(ExaminationDateModel o1, ExaminationDateModel o2) {
                    return Integer.valueOf(o1.getmPeriodId()).compareTo(o2.getmPeriodId());
                }
            });

            findViewById(R.id.no_data).setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
        else {
            mAddDateBtn.setVisibility(View.VISIBLE);
            findViewById(R.id.no_data).setVisibility(View.VISIBLE);
        }
    }

    private void chooseDate(String date, final int position, final boolean isNewDate){
        DateDialog dateDialog = new DateDialog();
        dateDialog.setmDefaultDate(date);
        dateDialog.setmListener(new DateDialog.OnDateSelected() {
            @Override
            public void onDateSelect(Calendar calendar) {
                int examinationId   = getIntent().getIntExtra(Utils.EXAMINATION_ID, 0);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                if(isNewDate)
                    addExaminationDate(mExaminationsDatesArrayList.isEmpty() ? mPeriodsArrayList.get(position).getmPeriodId() : mExaminationsDatesArrayList.get(position).getmPeriodId(), examinationId, sdf.format(calendar.getTime()));
                else
                    updateExaminationDate(mExaminationsDatesArrayList.get(position).getmId(), mExaminationsDatesArrayList.get(position).getmPeriodId(), examinationId, sdf.format(calendar.getTime()));
            }
        });
        dateDialog.show(getSupportFragmentManager(), "datePicker");
    }

    private void updateExaminationDate(int id, int periodId, int examinationId, String date){
        try {
            JSONObject data = new JSONObject();
            data.put(Utils.ID_USER_MKB      , getIntent().getIntExtra(Utils.USER_MKB_ID_PARAM, 0));
            data.put(Utils.ID_PERIOD        , periodId);
            data.put(Utils.ID_EXAMINATION   , examinationId);
            data.put(Utils.EXAMINATION_DATE , date);

            PostRequest postRequest = new PostRequest(this, data, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.EXAMINATIONS_CONTROLLER + "?id=" + id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addExaminationDate(int periodId, int examinationId, String date){
        try {
            JSONObject data = new JSONObject();
            data.put(Utils.ID_USER_MKB      , getIntent().getIntExtra(Utils.USER_MKB_ID_PARAM, 0));
            data.put(Utils.ID_PERIOD        , periodId);
            data.put(Utils.ID_EXAMINATION   , examinationId);
            data.put(Utils.EXAMINATION_DATE , date);

            PostRequest postRequest = new PostRequest(this, data, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.EXAMINATIONS_CONTROLLER);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getExaminationsDate() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Utils.EXAMINATION_ID     , getIntent().getIntExtra(Utils.EXAMINATION_ID, 0));
        jsonObject.put(Utils.USER_MKB_ID_PARAM  , getIntent().getIntExtra(Utils.USER_MKB_ID_PARAM,    0));

        PostRequest postRequest = new PostRequest(this, jsonObject, this);
        postRequest.setmHttpMethod("POST");
        postRequest.execute(Utils.URL + Utils.GET_USER_EXAMINATION);
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String response) {
        if(url.equals(Utils.URL + Utils.GET_USER_EXAMINATION)) {
            if (responseCode == Utils.STATUS_SUCCESS) {
                mExaminationsDatesArrayList.clear();
                mPeriodsArrayList.clear();
                mDates.clear();

                try {
                    JSONObject data = new JSONObject(response);
                    JSONArray dates = data.getJSONArray(Utils.EXAMINATIONS);
                    JSONArray periods = data.getJSONArray(Utils.PERIODS);

                    for (int i = 0; i < dates.length(); i++) {
                        ExaminationDateModel examinationDateModel = new ExaminationDateModel(dates.getJSONObject(i).getInt(Utils.USER_EXAMINATION_ID), dates.getJSONObject(i).getInt(Utils.PERIOD_ID), dates.getJSONObject(i).getString(Utils.DATE));
                        examinationDateModel.setmPeriodName(dates.getJSONObject(i).getInt(Utils.PERIOD_NAME));
                        examinationDateModel.setmType(ExaminationDateModel.PAST_EXAMINATION);
                        mDates.put(dates.getJSONObject(i).getInt(Utils.PERIOD_ID), examinationDateModel);
                    }

                    for (int i = 0; i < periods.length(); i++) {
                        PeriodModel periodModel = new PeriodModel(periods.getJSONObject(i).getInt(Utils.PERIOD_ID), periods.getJSONObject(i).getInt(Utils.PERIOD_NAME));
                        mPeriodsArrayList.add(periodModel);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(url.equals(Utils.URL + Utils.EXAMINATIONS_CONTROLLER) && responseCode == Utils.STATUS_SUCCESS){
            try {
                getExaminationsDate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(url.contains(Utils.URL + Utils.EXAMINATIONS_CONTROLLER + "?id=") && responseCode == Utils.STATUS_SUCCESS){
            try {
                getExaminationsDate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mBackArrowImg.getId())
            onBackPressed();
        else if(v.getId() == mAddDateBtn.getId()) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            chooseDate(format.format(calendar.getTime()), 0, true);
        }
    }

    @Override
    public void onItemClickListener(int position) {
        if(mExaminationsDatesArrayList.get(position).getmType() == ExaminationDateModel.PREDICTED_EXAMINATION){
            chooseDate(mExaminationsDatesArrayList.get(position).getmDate(), position, true);
        }
    }

    @Override
    public void onEditImgClicked(int position) {
        chooseDate(mExaminationsDatesArrayList.get(position).getmDate(), position, false);
    }
}
