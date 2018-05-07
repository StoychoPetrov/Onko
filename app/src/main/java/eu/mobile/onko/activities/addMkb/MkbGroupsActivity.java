package eu.mobile.onko.activities.addMkb;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

import eu.mobile.onko.R;
import eu.mobile.onko.adapters.MkbAdapter;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.MkbModel;

public class MkbGroupsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ResponseListener {

    private static final int        REQUEST_CODE_MKB    = 0;

    private ImageView               mBackArrowImg;
    private ListView                mMkbGroupsListView;

    private ArrayList<String>       mGroupNames         = new ArrayList<>();
    private ArrayList<MkbModel>     mMkbGroupsArrayList = new ArrayList<>();
    private ArrayAdapter<String>    mMkbGroupsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mkb_groups);

        initUI();
        setListeners();
        getMkbGroups();
    }

    private void initUI(){
        mBackArrowImg           = findViewById(R.id.back_arrow_img);
        mMkbGroupsListView      = findViewById(R.id.mkb_groups_list_view);

        ((TextView) findViewById(R.id.title_txt)).setText(R.string.mkb_groups);

        setAdapter();
    }

    private void setListeners(){
        mMkbGroupsListView.setOnItemClickListener(this);
        mBackArrowImg.setOnClickListener(this);
    }

    private void setAdapter(){
        mMkbGroupsAdapter   = new ArrayAdapter<>(this, R.layout.item_mkb_group, R.id.mkb_group_name, mGroupNames);
        mMkbGroupsListView.setAdapter(mMkbGroupsAdapter);
    }

    private void getMkbGroups() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Utils.USER_TOKEN, GlobalData.getInstance().getmToken());
            PostRequest postRequest = new PostRequest(this, jsonObject, this);
            postRequest.setmHttpMethod("GET");
            postRequest.execute(Utils.URL + Utils.MKB_GROUPS);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mBackArrowImg.getId()){
            onBackPressed();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MkbActivity.class);
        intent.putExtra(Utils.INTENT_GROUP_ID       , mMkbGroupsArrayList.get(position).getmMkbId());
        intent.putExtra(Utils.INTENT_GROUP_NAME     , mMkbGroupsArrayList.get(position).getmMkbName());
        startActivityForResult(intent, REQUEST_CODE_MKB);
    }

    @Override
    public void onResponseReceived(int responseCode, String response) {
        if(responseCode == Utils.STATUS_SUCCESS){
            try {
                JSONArray mkbGroups = new JSONArray(response);
                for(int i = 0; i < mkbGroups.length(); i++){
                    JSONObject mkbGroup = mkbGroups.getJSONObject(i);
                    MkbModel group = new MkbModel(mkbGroup.getInt(Utils.ID), mkbGroup.getString(Utils.MKB_GROUP_NAME));
                    mMkbGroupsArrayList.add(group);
                    mGroupNames.add(group.getmMkbName());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMkbGroupsAdapter.notifyDataSetChanged();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}