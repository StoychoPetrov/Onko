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
import eu.mobile.onko.adapters.DoctorsAdapter;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.DoctorRowModel;

public class DoctorsActivity extends AppCompatActivity
        implements View.OnClickListener, ResponseListener, DoctorsAdapter.OnDoctorsListener {

    private ImageView                   mBackArrowImg;
    private RecyclerView                mDoctorsListView;

    private DoctorsAdapter              mDoctorsAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;
    private ArrayList<DoctorRowModel>   mDoctorsArrayList   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

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

        mDoctorsAdapter = new DoctorsAdapter(this, mDoctorsArrayList, this);
        mDoctorsListView.setAdapter(mDoctorsAdapter);
    }

    private void setListeners(){
        mBackArrowImg.setOnClickListener(this);
    }

    private void getDoctorsRequest(){
        PostRequest postRequest = new PostRequest(this, new JSONObject(), this);
        postRequest.setmHttpMethod("GET");
        postRequest.execute(Utils.URL + Utils.GET_DOCTORS);
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

                for(int i = 0; i < data.length(); i++){

                    JSONObject mkbGroup = data.getJSONObject(i);

                    mDoctorsArrayList.add(new DoctorRowModel(DoctorRowModel.SECTION_INDEX, mkbGroup.getString(Utils.MKB_GROUP), 0));

                    JSONArray doctors = mkbGroup.getJSONArray(Utils.DOCTORS);
                    for(int j = 0 ; j < doctors.length(); j++){
                        JSONObject doctor = doctors.getJSONObject(j);

                        mDoctorsArrayList.add(new DoctorRowModel(DoctorRowModel.ITEM_INDEX, doctor.getString(Utils.DOCTOR_NAME), doctor.getInt(Utils.DOCTOR_ID)));
                    }
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
    public void onDoctorClicked(int position) {
        Intent intent = new Intent(this, UsersDoctorsActivity.class);
        intent.putExtra(Utils.DOCTOR_ID, mDoctorsArrayList.get(position).getmDoctorId());
        intent.putExtra(Utils.DOCTOR_NAME, mDoctorsArrayList.get(position).getmLabel());
        startActivity(intent);
    }
}