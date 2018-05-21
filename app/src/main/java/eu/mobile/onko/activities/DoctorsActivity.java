package eu.mobile.onko.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class DoctorsActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

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

        mDoctorsAdapter = new DoctorsAdapter(this, mDoctorsArrayList);
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

                    mDoctorsArrayList.add(new DoctorRowModel(DoctorRowModel.SECTION_INDEX, mkbGroup.getString(Utils.MKB_GROUP)));

                    JSONArray doctors = mkbGroup.getJSONArray(Utils.DOCTORS);
                    for(int j = 0 ; j < doctors.length(); j++){
                        JSONObject doctor = doctors.getJSONObject(j);

                        mDoctorsArrayList.add(new DoctorRowModel(DoctorRowModel.ITEM_INDEX, doctor.getString(Utils.DOCTOR_NAME)));
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
}
