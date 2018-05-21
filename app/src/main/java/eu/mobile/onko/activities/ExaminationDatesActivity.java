package eu.mobile.onko.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eu.mobile.onko.R;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.ExaminationDateModel;
import eu.mobile.onko.models.PeriodModel;

public class ExaminationDatesActivity extends AppCompatActivity implements ResponseListener, View.OnClickListener {

    private ImageView                         mBackArrowImg;
    private TextView                          mTitleTxt;
    private ListView                          mDatesListView;

    private ArrayList<ExaminationDateModel>   mExaminationsDatesArrayList = new ArrayList<>();
    private ArrayList<PeriodModel>            mPeriodsArrayList           = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_dates);

        initUI();
        setListeners();

        try {
            getExaminationsDate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initUI(){
        mBackArrowImg   = findViewById(R.id.back_arrow_img);
        mTitleTxt       = findViewById(R.id.title_txt);

        mTitleTxt.setText(R.string.examinations_dates);
    }

    private void setListeners(){
        mBackArrowImg.setOnClickListener(this);
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
        if(responseCode == Utils.STATUS_SUCCESS){
            try {
                JSONObject data     = new JSONObject(response);
                JSONArray dates     = data.getJSONArray(Utils.EXAMINATIONS);
                JSONArray periods   = data.getJSONArray(Utils.PERIODS);

                for(int i = 0 ; i < dates.length(); i++){
                    ExaminationDateModel examinationDateModel = new ExaminationDateModel(dates.getJSONObject(i).getInt(Utils.PERIOD_ID), dates.getJSONObject(i).getString(Utils.DATE));
                    mExaminationsDatesArrayList.add(examinationDateModel);
                }

                for(int i = 0; i < periods.length(); i++){
                    PeriodModel periodModel = new PeriodModel(periods.getJSONObject(i).getInt(Utils.PERIOD_ID), periods.getJSONObject(i).getString(Utils.PERIOD_NAME));
                    mPeriodsArrayList.add(periodModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mBackArrowImg.getId())
            onBackPressed();
    }
}
