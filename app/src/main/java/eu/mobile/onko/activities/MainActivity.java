package eu.mobile.onko.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import eu.mobile.onko.R;
import eu.mobile.onko.adapters.NavDrawerAdapter;
import eu.mobile.onko.models.DrawerMenuItemModel;
import eu.mobile.onko.models.ProfileModel;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DrawerLayout.DrawerListener{

    private static final int INDEX_FEEDBACK         = 1;
    private static final int INDEX_LOG_OUT          = 2;

    private Toolbar         mToolbar;
    private ListView        mNavDrawerListView;
    private DrawerLayout    mDrawerLayout;

    private boolean         mFromDrawerItemClicked;
    private int             mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        setListeners();
    }

    private void initUI(){
        mToolbar                = (Toolbar)         findViewById(R.id.toolbar);
        mNavDrawerListView      = (ListView)        findViewById(R.id.nav_list_view);
        mDrawerLayout           = (DrawerLayout)    findViewById(R.id.drawer_layout);

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
        mDrawerLayout.addDrawerListener(this);
    }

    private void loadDrawerMenu(){
        ProfileModel profileModel = new ProfileModel("Ivan Ivanov Ivanov","ivan@gmail.com",null);

        ArrayList<DrawerMenuItemModel> menuItems = new ArrayList<>();
        menuItems.add(new DrawerMenuItemModel(getString(R.string.feedback),ContextCompat.getDrawable(this,R.drawable.ic_email)));
        menuItems.add(new DrawerMenuItemModel(getString(R.string.log_out),ContextCompat.getDrawable(this,R.drawable.ic_logout)));

        NavDrawerAdapter adapter = new NavDrawerAdapter(this,menuItems,profileModel);
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
            case INDEX_FEEDBACK:
                break;
            case INDEX_LOG_OUT:
                onLogOut();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mFromDrawerItemClicked = true;
        mPosition = position;
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
}