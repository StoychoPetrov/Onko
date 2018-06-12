package eu.mobile.onko.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eu.mobile.onko.R;
import eu.mobile.onko.activities.addMkb.MkbGroupsActivity;
import eu.mobile.onko.adapters.MkbAdapter;
import eu.mobile.onko.adapters.NavDrawerAdapter;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.DrawerMenuItemModel;
import eu.mobile.onko.models.MkbModel;
import eu.mobile.onko.models.ProfileModel;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, DrawerLayout.DrawerListener, ResponseListener, View.OnClickListener, MkbAdapter.OnButtonsClicked {

    private static final int REQUEST_CODE_MKB_GROUP = 0;
    private static final int INDEX_DOCTORS          = 1;
    private static final int INDEX_FEEDBACK         = 2;
    private static final int INDEX_LOG_OUT          = 3;

    private Toolbar                 mToolbar;
    private ListView                mNavDrawerListView;
    private ListView                mMkbListView;
    private DrawerLayout            mDrawerLayout;
    private FloatingActionButton    mAddFab;

    private ArrayList<MkbModel> mUserMkbsArrayList = new ArrayList<>();
    private MkbAdapter          mMkbAdapter;

    private boolean         mFromDrawerItemClicked;
    private int             mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        setListeners();
        setData();

        getUserMkbs();

        registerPushToken();
    }

    private void initUI(){
        mToolbar                = findViewById(R.id.toolbar);
        mNavDrawerListView      = findViewById(R.id.nav_list_view);
        mDrawerLayout           = findViewById(R.id.drawer_layout);
        mMkbListView            = findViewById(R.id.mkb_list_view);
        mAddFab                 = findViewById(R.id.diseases_fab);

        setSupportActionBar(mToolbar);

        loadDrawerMenu();
    }

    private void setListeners(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mNavDrawerListView.setOnItemClickListener(this);
        mMkbListView.setOnItemClickListener(this);
        mDrawerLayout.addDrawerListener(this);
        mAddFab.setOnClickListener(this);
    }

    private void setData(){
        mMkbAdapter = new MkbAdapter(this, mUserMkbsArrayList, this);
        mMkbListView.setAdapter(mMkbAdapter);
    }

        private void loadDrawerMenu(){

        ArrayList<DrawerMenuItemModel> menuItems = new ArrayList<>();
        menuItems.add(new DrawerMenuItemModel(getString(R.string.doctors), ContextCompat.getDrawable(this, R.drawable.doctors_icon)));
        menuItems.add(new DrawerMenuItemModel(getString(R.string.feedback),ContextCompat.getDrawable(this,R.drawable.ic_email)));
        menuItems.add(new DrawerMenuItemModel(getString(R.string.log_out),ContextCompat.getDrawable(this,R.drawable.ic_logout)));

        NavDrawerAdapter adapter = new NavDrawerAdapter(this, menuItems);
        mNavDrawerListView.setAdapter(adapter);
    }

    private void onLogOut(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void onDrawerItemSelected(int position){
        switch (position){
            case INDEX_DOCTORS:
                Intent intent = new Intent(this, DoctorsActivity.class);
                startActivity(intent);
                break;
            case INDEX_FEEDBACK:
                sendEmail();
                break;
            case INDEX_LOG_OUT:
                onLogOut();
                break;
        }
    }

    private void sendEmail(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"onko@support.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Onco Application");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerPushToken(){
        try {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            JSONObject data = new JSONObject();
            data.put(Utils.USER_TOKEN, GlobalData.getInstance().getmToken());
            data.put(Utils.PUSH_TOKEN, preferences.getString(Utils.PREFERENCES_PUSH_TOKEN, ""));

            PostRequest postRequest = new PostRequest(this, data, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.REGISTER_PUSH_TOKEN);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getUserMkbs() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Utils.USER_TOKEN, GlobalData.getInstance().getmToken());
            PostRequest getMkbs = new PostRequest(this, jsonObject, this);
            getMkbs.execute(Utils.URL + Utils.USER_MKBS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteUserMkb(int id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Utils.USER_TOKEN , GlobalData.getInstance().getmToken());
            jsonObject.put(Utils.ID         , id);
            PostRequest postRequest = new PostRequest(this, jsonObject, this);
            postRequest.setmHttpMethod("POST");
            postRequest.execute(Utils.URL + Utils.USER_MKB_CONTROLLER + Utils.DELETE_ACTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(adapterView.getId() == mNavDrawerListView.getId()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mFromDrawerItemClicked = true;
            mPosition = position;
        }
        else if(adapterView.getId() == mMkbListView.getId()){
            Intent intent = new Intent(this, ExaminationsActivity.class);
            intent.putExtra(Utils.INTENT_MKB_ID     , mUserMkbsArrayList.get(position).getmMkbId());
            intent.putExtra(Utils.USER_MKB_ID_PARAM , mUserMkbsArrayList.get(position).getmUserMkbId());
            startActivity(intent);
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if(mFromDrawerItemClicked && mPosition >= 0){
            onDrawerItemSelected(mPosition);
            mFromDrawerItemClicked = false;
            mPosition = -1;
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onResponseReceived(String url, int responseCode, String result) {

        if(url.equalsIgnoreCase(Utils.URL + Utils.USER_MKB_CONTROLLER + Utils.DELETE_ACTION)){
            if(responseCode == Utils.STATUS_SUCCESS) {
                Toast.makeText(this, getString(R.string.you_have_deleted_mkb_successful), Toast.LENGTH_SHORT).show();
                getUserMkbs();
            }
            else
                Toast.makeText(this, getString(R.string.you_havent_deleted_mkb_seccessful), Toast.LENGTH_SHORT).show();
        }
        else {
            if(!result.isEmpty()) {
                try {
                    mUserMkbsArrayList.clear();
                    JSONArray mkbs = new JSONArray(result);

                    for (int i = 0; i < mkbs.length(); i++) {
                        MkbModel mkbModel = new MkbModel(mkbs.getJSONObject(i).getInt(Utils.USER_MKB_ID), mkbs.getJSONObject(i).getInt(Utils.MKB_ID), mkbs.getJSONObject(i).getString(Utils.MKB_NAME));
                        mUserMkbsArrayList.add(mkbModel);
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

    @Override
    public void onClick(View v) {
        if(v.getId() == mAddFab.getId()){
            Intent intent = new Intent(this, MkbGroupsActivity.class);
            startActivityForResult(intent, REQUEST_CODE_MKB_GROUP);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_MKB_GROUP && resultCode == RESULT_OK){
            getUserMkbs();
        }
    }

    @Override
    public void onDeleteClicked(final int position) {

        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_mkb))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_mkb))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteUserMkb(mUserMkbsArrayList.get(position).getmUserMkbId());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}