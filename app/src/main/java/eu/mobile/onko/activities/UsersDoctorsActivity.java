package eu.mobile.onko.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eu.mobile.onko.OnItemClickListener;
import eu.mobile.onko.R;
import eu.mobile.onko.adapters.UsersDoctorsAdapter;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.UserModel;

public class UsersDoctorsActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, OnItemClickListener {

    private ImageView                   mBackArrowImg;
    private RecyclerView                mUsersDoctorsListView;

    private RecyclerView.LayoutManager  mLayoutManager;
    private UsersDoctorsAdapter         mAdapter;
    private ArrayList<UserModel>        mDoctorsArrayList   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_doctors);

        initUI();
        setListeners();
        getUsersDoctorsRequest(getIntent().getIntExtra(Utils.DOCTOR_ID, 0));
    }

    private void initUI(){
        mBackArrowImg            = findViewById(R.id.back_arrow_img);
        mUsersDoctorsListView    = findViewById(R.id.users_doctors_listview);

        ((TextView) findViewById(R.id.title_txt)).setText(getIntent().getStringExtra(Utils.DOCTOR_NAME));

        mUsersDoctorsListView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mUsersDoctorsListView.setLayoutManager(mLayoutManager);
        mUsersDoctorsListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new UsersDoctorsAdapter(mDoctorsArrayList, this);
        mUsersDoctorsListView.setAdapter(mAdapter);
    }

    private void setListeners(){
        mBackArrowImg.setOnClickListener(this);
    }

    private void getUsersDoctorsRequest(int doctorId) {
        PostRequest postRequest = new PostRequest(this, new JSONObject(), this);
        postRequest.setmHttpMethod("GET");
        postRequest.execute(Utils.URL + Utils.GET_USERS_DOCTORS + doctorId);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackArrowImg.getId())
            onBackPressed();
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String response) {
        try {
            mDoctorsArrayList.clear();
            JSONArray jsonArray = new JSONArray(response);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject  userDoctorJson  = jsonArray.getJSONObject(i);
                UserModel   userModel       = new UserModel();

                userModel.setmId(userDoctorJson.getInt(Utils.ID));
                userModel.setmEmail(userDoctorJson.getString(Utils.USER_EMAIL));
                userModel.setmFirstName(userDoctorJson.getString(Utils.USER_FIRST_NAME));
                userModel.setmLastName(userDoctorJson.getString(Utils.USER_LAST_NAME));
                userModel.setmPhone(userDoctorJson.getString(Utils.USER_PHONE));

                mDoctorsArrayList.add(userModel);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickItem(int position) {
        UserModel userModel = mDoctorsArrayList.get(position);
        Intent intent = new Intent(this, ReserveHourActivity.class);
        intent.putExtra(Utils.INTENT_DOCTOR , userModel);
        intent.putExtra(Utils.DOCTOR_NAME   , getIntent().getStringExtra(Utils.DOCTOR_NAME));
        startActivity(intent);
    }
}