package eu.mobile.onko.activities.doctorActivities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import eu.mobile.onko.R;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.Utils;

public class DoctorsMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private Toolbar         mToolbar;
    private DrawerLayout    mDrawerLayout;
    private NavigationView  mNavigationView;
    private ViewPager2      mViewPager;

    private int             mClickedMenuItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_main);

        initUI();
        setListeners();
        setAdapter();
    }

    private void initUI() {

        mToolbar                = findViewById(R.id.toolbar);
        mDrawerLayout           = findViewById(R.id.drawer_layout);
        mNavigationView         = findViewById(R.id.nav_list_view);
        mViewPager              = findViewById(R.id.viewPager);

        setSupportActionBar(mToolbar);

        View headerView                 = mNavigationView.getHeaderView(0);

        String names = GlobalData.getInstance().getmFirstName() + " " + GlobalData.getInstance().getmLastName();

        TextView navUsername            = headerView.findViewById(R.id.name_txt);
        TextView navEmail               = headerView.findViewById(R.id.email_txt);
        ImageView profileImg            = headerView.findViewById(R.id.profile_image);

        navUsername.setText(names);
        navEmail.setText(GlobalData.getInstance().getmUserEmail());
        profileImg.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_account_circle));

        Utils.setTypeFace(this, navUsername,Utils.ROBOTO_MEDIUM);
        Utils.setTypeFace(this, navEmail,Utils.ROBOTO_REGULAR);
    }

    private void setListeners() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mDrawerLayout.addDrawerListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void startDoctorsWorkingTimeActivity() {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    private void setAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator((TabLayout) findViewById(R.id.tabLayout), mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(position == 0 ? R.string.next : R.string.previous);
            }
        }
        );
        tabLayoutMediator.attach();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mClickedMenuItemId  = item.getItemId();
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        if (mClickedMenuItemId == R.id.workingHours) {
            startDoctorsWorkingTimeActivity();
        }

        mClickedMenuItemId  = 0;
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    class ViewPagerAdapter extends FragmentStateAdapter {

        ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            Bundle bundle   = new Bundle();
            DoctorReservationsFragment fragment = new DoctorReservationsFragment();

            bundle.putInt("position", position);
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}