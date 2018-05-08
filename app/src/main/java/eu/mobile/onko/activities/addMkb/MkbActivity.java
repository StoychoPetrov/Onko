package eu.mobile.onko.activities.addMkb;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eu.mobile.onko.R;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.MkbModel;

public class MkbActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ResponseListener {

    private ListView                mMkbListView;
    private ImageView               mBackArrowImg;

    private ArrayAdapter<String>    mMkbAdapter;
    private ArrayList<String>       mMkbNamesArrayList  = new ArrayList<>();
    private ArrayList<MkbModel>     mMkbArrayList       = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mkb);

        initUI();
        setListeners();
        setAdapter();

        if(getIntent() != null && getIntent().hasExtra(Utils.INTENT_GROUP_ID))
            getAllMkbByGroup(getIntent().getIntExtra(Utils.INTENT_GROUP_ID, 0));
    }

    private void initUI(){
        mMkbListView    = findViewById(R.id.mkb_listview);
        mBackArrowImg   = findViewById(R.id.back_arrow_img);

        ((TextView) findViewById(R.id.title_txt)).setText(getIntent().getStringExtra(Utils.INTENT_GROUP_NAME));
    }

    private void setListeners(){
        mBackArrowImg.setOnClickListener(this);
        mMkbListView.setOnItemClickListener(this);
    }

    private void setAdapter(){
        mMkbAdapter = new ArrayAdapter<String>(this, R.layout.item_mkb_name, R.id.mkb_name_txt, mMkbNamesArrayList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater inflater       = MkbActivity.this.getLayoutInflater();

                if(convertView == null)
                    convertView = inflater.inflate(R.layout.item_mkb_name, null);

                String mkbName = mMkbNamesArrayList.get(position);
                mkbName = mkbName.substring(0,1).toUpperCase() + mkbName.substring(1);

                ((TextView) convertView.findViewById(R.id.mkb_name_txt)).setText(mkbName);

                return convertView;
            }
        };
        mMkbListView.setAdapter(mMkbAdapter);
    }

    private void getAllMkbByGroup(int groupId){
        String queryString = Utils.URL + Utils.MKBS + Utils.GROUP_ID + groupId;
        PostRequest getMkbsByGroup = new PostRequest(this, null, this);
        getMkbsByGroup.setmHttpMethod("GET");
        getMkbsByGroup.execute(queryString);
    }

    private void addUserMkb(int mkbId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Utils.USER_TOKEN     , GlobalData.getInstance().getmToken());
            jsonObject.put(Utils.MKB_ID_PARAM   , mkbId);

            PostRequest postRequest = new PostRequest(this, jsonObject, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.USER_MKB_CONTROLLER + Utils.ADD_ACTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mBackArrowImg.getId())
            onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addUserMkb(mMkbArrayList.get(position).getmMkbId());
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String response) {
        if(url.equalsIgnoreCase(Utils.URL + Utils.USER_MKB_CONTROLLER + Utils.ADD_ACTION)){
            if(responseCode == Utils.STATUS_SUCCESS){
                Toast.makeText(this, getString(R.string.you_have_added_mkb_successful), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
            else {
                Toast.makeText(this, getString(R.string.you_couldnt_add_mkb), Toast.LENGTH_SHORT).show();
            }
        }
        else if(responseCode == Utils.STATUS_SUCCESS){
            try {
                JSONArray mkbs = new JSONArray(response);

                for(int i = 0 ; i < mkbs.length(); i++){
                    JSONObject mkb = mkbs.getJSONObject(i);

                    MkbModel mkbModel = new MkbModel(0, mkb.getInt(Utils.ID), mkb.getString(Utils.MKB_TITLE));
                    mMkbArrayList.add(mkbModel);
                    mMkbNamesArrayList.add(mkbModel.getmMkbName());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMkbAdapter.notifyDataSetChanged();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
