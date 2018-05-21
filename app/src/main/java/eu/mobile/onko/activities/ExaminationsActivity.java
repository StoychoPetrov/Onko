package eu.mobile.onko.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.ExaminationModel;

public class ExaminationsActivity extends AppCompatActivity implements ResponseListener, View.OnClickListener,
        AdapterView.OnItemClickListener{

    private ListView    mExaminationsListView;
    private ImageView   mBackArrowImg;

    private ArrayList<ExaminationModel> mExaminationsArrayList      = new ArrayList<>();
    private ArrayList<String>           mExaminationsNamesArrayList = new ArrayList<>();
    private ArrayAdapter<String>        mExaminationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinations);

        initUI();
        setListeners();

        try {
            getExaminations();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initUI(){
        mBackArrowImg           = findViewById(R.id.back_arrow_img);
        mExaminationsListView   = findViewById(R.id.examinations_listview);

        ((TextView) findViewById(R.id.title_txt)).setText(R.string.examinations);

        mExaminationsAdapter    = new ArrayAdapter<>(this, R.layout.item_mkb_group, R.id.mkb_group_name, mExaminationsNamesArrayList);
        mExaminationsListView.setAdapter(mExaminationsAdapter);
    }

    private void setListeners(){
        mBackArrowImg.setOnClickListener(this);
        mExaminationsListView.setOnItemClickListener(this);
    }

    private void getExaminations() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Utils.USER_TOKEN     , GlobalData.getInstance().getmToken());
        jsonObject.put(Utils.INTENT_MKB_ID  , getIntent().getIntExtra(Utils.INTENT_MKB_ID, 0));

        PostRequest postRequest = new PostRequest(this, jsonObject, this);
        postRequest.setmHttpMethod("POST");
        postRequest.execute(Utils.URL + Utils.GET_EXAMINATIONS);
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String response) {
        if(responseCode == Utils.STATUS_SUCCESS){
            try {
                mExaminationsArrayList.clear();
                mExaminationsNamesArrayList.clear();
                JSONArray jsonArray = new JSONArray(response);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject examination = jsonArray.getJSONObject(i);

                    mExaminationsArrayList.add(new ExaminationModel(examination.getInt(Utils.EXAMINATION_ID)
                            , examination.getString(Utils.EXAMINATION_NAME)));
                    mExaminationsNamesArrayList.add(examination.getString(Utils.EXAMINATION_NAME));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mExaminationsAdapter.notifyDataSetChanged();
                    }
                });

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ExaminationDatesActivity.class);
        intent.putExtra(Utils.EXAMINATION_ID            , mExaminationsArrayList.get(position).getmExaminationId());
        intent.putExtra(Utils.USER_MKB_ID_PARAM         , getIntent().getIntExtra(Utils.USER_MKB_ID_PARAM, 0));
        startActivity(intent);
    }
}
