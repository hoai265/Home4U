package app.hoai.bkit4u.home4u;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;

import app.hoai.bkit4u.home4u.controller.FragmentController;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.controller.PreferencesController;
import app.hoai.bkit4u.home4u.fragment.AddIrActionFragement;
import app.hoai.bkit4u.home4u.fragment.AnalyticsFragment;
import app.hoai.bkit4u.home4u.fragment.ConfigFragment;
import app.hoai.bkit4u.home4u.fragment.HomeFragment;
import app.hoai.bkit4u.home4u.fragment.NotificationFragment;
import app.hoai.bkit4u.home4u.fragment.OfflineModeFragment;
import app.hoai.bkit4u.home4u.fragment.SettingFragment;
import app.hoai.bkit4u.home4u.fragment.dialog.AddActionDialogFragment;
import app.hoai.bkit4u.home4u.fragment.dialog.AddDeviceDialogFragment;
import app.hoai.bkit4u.home4u.listener.OnFragmentChangeListener;
import app.hoai.bkit4u.home4u.listener.OnGetNotifications;
import app.hoai.bkit4u.home4u.listener.ResponseListener;
import app.hoai.bkit4u.home4u.model.BaseDeviceModel;
import app.hoai.bkit4u.home4u.model.DeviceActionModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentChangeListener, OnGetNotifications
{

    HomeFragment mHomeFragmentInstance;
    AnalyticsFragment mAnalyticsFragmentInstance;
    SettingFragment mSettingFragmentInstance;
    ConfigFragment mConfigFragmentInstance;
    NotificationFragment mNotificationFragment;
//    AddIrActionFragement mAddIrActionFragment;
//    OfflineModeFragment mOfflineModeFragmentInstance;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFragments();
        FragmentController.init(getSupportFragmentManager(), R.id.fragment_container);
        FragmentController.addFragment(getHomeFragment());

        NetworkController.createIntance(this);
        Firebase.setAndroidContext(this);
        PreferencesController.createInstance(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mNotificationFragment = (NotificationFragment) getFragmentManager().findFragmentById(R.id.fragment_notification);
        mNotificationFragment.setOnGetNotification(this);
        onHideNotification();
    }

    private void initFragments()
    {

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_speech)
        {
            Intent intent = new Intent(this, SpeechControlActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings ("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_home:
                FragmentController.changeFragment(getHomeFragment(), "Home");
                break;
            case R.id.nav_analytics:
                FragmentController.changeFragment(getOfflineModeFragmentInstance(), "Offline Mode");
                break;
            case R.id.nav_setting:
                FragmentController.changeFragment(getSettingFragment(), "Setting");
                break;
            case R.id.nav_config:
                FragmentController.changeFragment(getconfigFragment(), "Config");
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    HomeFragment getHomeFragment()
    {
        if (mHomeFragmentInstance == null)
        {
            mHomeFragmentInstance = new HomeFragment();
            mHomeFragmentInstance.setOnFragmentChangeListener(this);
        }

        return mHomeFragmentInstance;
    }

    AnalyticsFragment getAnalyticsFragment()
    {
        if (mAnalyticsFragmentInstance == null)
        {
            mAnalyticsFragmentInstance = new AnalyticsFragment();
        }

        return mAnalyticsFragmentInstance;
    }

    ConfigFragment getconfigFragment()
    {

        mConfigFragmentInstance = new ConfigFragment();
        mConfigFragmentInstance.setOnFragmentChangeListener(this);

        return mConfigFragmentInstance;
    }

    SettingFragment getSettingFragment()
    {
        if (mSettingFragmentInstance == null)
        {
            mSettingFragmentInstance = new SettingFragment();
        }

        return mSettingFragmentInstance;
    }

    AddIrActionFragement getAddIrActionFragment(String id)
    {

        AddIrActionFragement mAddIrActionFragment = new AddIrActionFragement();
        mAddIrActionFragment.setOnFragmentChangeListener(this);
        mAddIrActionFragment.setDeviceID(id);

        return mAddIrActionFragment;
    }

    OfflineModeFragment getOfflineModeFragmentInstance()
    {
        OfflineModeFragment mOfflineModeFragmentInstance = new OfflineModeFragment();
        return mOfflineModeFragmentInstance;
    }

    @Override
    public void onShowNotification()
    {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                .show(mNotificationFragment)
                .commit();
    }

    @Override
    public void onHideNotification()
    {
        getFragmentManager().beginTransaction()
                .hide(mNotificationFragment)
                .commit();
    }

    @Override
    public void onChangeToolbarTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onAddDeviceRequest(final View v, final String objectId)
    {
        AddDeviceDialogFragment dialogFragment = AddDeviceDialogFragment.getInstance();
        dialogFragment.setOnChooseDeviceCompleted(new AddDeviceDialogFragment.OnChooseDeviceCompletedListener()
        {
            @Override
            public void onCompleteted(BaseDeviceModel model)
            {
                NetworkController.getInstance().addRoomDevice(new ResponseListener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Snackbar.make(v, "Add room device successfully!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onError()
                    {

                    }
                }, model.getId(), objectId);
            }

            @Override
            public void onError()
            {

            }
        });
        dialogFragment.show(getSupportFragmentManager(), "add room device");
    }

    @Override
    public void onAddActionRequest(final View v, final String objectId)
    {
        AddActionDialogFragment dialogFragment = AddActionDialogFragment.getInstance();
        dialogFragment.setOnChooseActionCompleted(new AddActionDialogFragment.OnChooseActionCompletedListener()
        {
            @Override
            public void onCompleteted(DeviceActionModel model)
            {
                NetworkController.getInstance().addEventAction(new ResponseListener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Snackbar.make(v, "Add event action successfully!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onError()
                    {

                    }
                }, model.getId(), objectId);
            }

            @Override
            public void onError()
            {

            }
        });
        dialogFragment.show(getSupportFragmentManager(), "add event action");
    }

    @Override
    public void onAddIrActionRequest(String id)
    {
        FragmentController.changeFragment(getAddIrActionFragment(id), "AddIrAction");
    }

    @Override
    public void onHomeRequest()
    {
        FragmentController.changeFragment(getHomeFragment(), "Home");
    }
}
