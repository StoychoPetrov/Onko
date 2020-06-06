package eu.mobile.onko.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eu.mobile.onko.R;
import eu.mobile.onko.adapters.ChooseDoctorAdapter;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.DoctorModel;

public class ChooseDoctorActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, ChooseDoctorAdapter.OnItemClick {

    private ImageView mBackArrowImg;
    private RecyclerView mDoctorsListView;

    private ChooseDoctorAdapter         mDoctorsAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;
    private ArrayList<DoctorModel>      mDoctorsArrayList   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_doctor);

        initUI();
        setListeners();
        getDoctorsRequest();
    }

    private void initUI(){
        mBackArrowImg       = findViewById(R.id.back_arrow_img);
        mDoctorsListView    = findViewById(R.id.doctors_listview);

        ((TextView) findViewById(R.id.title_txt)).setText(R.string.doctors);

        mDoctorsListView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mDoctorsListView.setLayoutManager(mLayoutManager);

        mDoctorsAdapter = new ChooseDoctorAdapter(this, mDoctorsArrayList);
        mDoctorsListView.setAdapter(mDoctorsAdapter);
    }

    private void setListeners(){
        mBackArrowImg.setOnClickListener(this);
        mDoctorsAdapter.setListener(this);
    }

    private void getDoctorsRequest(){
        PostRequest postRequest = new PostRequest(this, null, this);
        postRequest.setmHttpMethod("GET");
        postRequest.execute(Utils.URL + Utils.DOCTORS_LIST);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mBackArrowImg.getId())
            onBackPressed();
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String response) {
        try {
            if(responseCode == Utils.STATUS_SUCCESS) {
                JSONArray data = new JSONArray(response);
                mDoctorsArrayList.clear();
                for(int i = 0; i < data.length(); i++){

                    JSONObject doctor = data.getJSONObject(i);
                    mDoctorsArrayList.add(new DoctorModel(doctor.getInt(Utils.DOCTOR_ID), doctor.getString(Utils.DOCTOR_NAME)));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDoctorsAdapter.notifyDataSetChanged();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent();
        intent.putExtra(Utils.INTENT_DOCTOR, mDoctorsArrayList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}