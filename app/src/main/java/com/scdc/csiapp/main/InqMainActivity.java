package com.scdc.csiapp.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.gcmservice.GcmRegisterService;

public class InqMainActivity extends AppCompatActivity {
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;


    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;


    EmergencyListFragment emergencyListFragment;

    AcceptListFragment acceptListFragment;
    DoneListFragment doneListFragment;
    PoliceListFragment policeListFragment;

    NotiFragment notiFragment;
    SettingFragment settingFragment;
    ProfileFragment profileFragment;
    Toolbar toolbar;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    private PreferenceData mManager;
    String officialID, username;
    TextView OfficialName,txtusername;
    ImageView avatar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    private static final String TAG = "InqMainActivity";
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inq_activity_main);
        mManager = new PreferenceData(this);
        // PreferenceData member id
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        username = mManager.getPreferenceData(mManager.KEY_USERNAME);

        mDbHelper = new SQLiteDBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        cd = new ConnectionDetector(getApplicationContext());
        networkConnectivity = cd.networkConnectivity();
        /**
         *Setup the DrawerLayout and NavigationView
         */
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, mNavigationView, false);
       OfficialName = (TextView) headerView.findViewById((R.id.officialName));
         txtusername = (TextView) headerView.findViewById((R.id.username));
         avatar = (ImageView) headerView.findViewById(R.id.profile_image);
        //OfficialName.setText(officialID);
        txtusername.setText(username);
        Log.i("login", officialID);
        new OfficialDataTask().execute(officialID);

        mNavigationView.addHeaderView(headerView);
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the CSIDataTabFragment as the first Fragment
         */

        emergencyListFragment = new EmergencyListFragment();

        acceptListFragment = new AcceptListFragment();
        doneListFragment = new DoneListFragment();
        policeListFragment = new PoliceListFragment();
          notiFragment = new NotiFragment();
        settingFragment = new SettingFragment();
        profileFragment = new ProfileFragment();
        mFragmentManager = getSupportFragmentManager();
        if (networkConnectivity) {
            Log.i("networkConnect inqmain", "connect!! ");

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                registerReceiver();
                Log.i("GCM", "connect!! ");
                if (checkPlayServices()) {
                    registerGcm();
                }

            }

        } else {
            Log.i("networkConnect inqmain", "no connect!! ");
        }


        String menuFragment = getIntent().getStringExtra("menuFragment");
        if (menuFragment != null)
        {
            if (menuFragment.equals("acceptListFragment"))
            {
                FragmentTransaction ftdraft = mFragmentManager.beginTransaction();
                ftdraft.replace(R.id.containerView, acceptListFragment);
                ftdraft.addToBackStack(null);
                ftdraft.commit();
            }
        }else{
            FragmentTransaction fthome = mFragmentManager.beginTransaction();
            fthome.replace(R.id.containerView, emergencyListFragment);
            fthome.addToBackStack(null);
            fthome.commit();
        }

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Consume input from header view. This disables the ripple effect
                //Toast.makeText(getApplicationContext(),"Send Selected", Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawers();
                //mFragmentTransaction.replace(R.id.containerView, profileFragment).commit();
                FragmentTransaction ftprofile = getSupportFragmentManager().beginTransaction();
                ftprofile.replace(R.id.containerView, profileFragment);
                ftprofile.addToBackStack(null);
                ftprofile.commit();
            }
        });




        /**
         * Setup click events on the Navigation View Items.
         */


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_item_receivingcase) {

                    FragmentTransaction fthome2 = mFragmentManager.beginTransaction();
                    fthome2.replace(R.id.containerView, emergencyListFragment);
                    fthome2.addToBackStack(null);
                    fthome2.commit();


                }

                if (menuItem.getItemId() == R.id.nav_item_draft) {
                    FragmentTransaction ftdraft = mFragmentManager.beginTransaction();
                    ftdraft.replace(R.id.containerView, acceptListFragment);
                    ftdraft.addToBackStack(null);
                    ftdraft.commit();

                }
                if (menuItem.getItemId() == R.id.nav_item_full) {
                    FragmentTransaction ftfull = mFragmentManager.beginTransaction();
                    ftfull.replace(R.id.containerView, doneListFragment);
                    ftfull.addToBackStack(null);
                    ftfull.commit();
                }

                if (menuItem.getItemId() == R.id.nav_item_police) {
                    FragmentTransaction ftpolice = mFragmentManager.beginTransaction();
                    ftpolice.replace(R.id.containerView, policeListFragment);
                    ftpolice.addToBackStack(null);
                    ftpolice.commit();
                }
                /*
                if (menuItem.getItemId() == R.id.nav_item_noti) {
                    FragmentTransaction ftnoti = mFragmentManager.beginTransaction();
                    ftnoti.replace(R.id.containerView, notiFragment);
                    ftnoti.addToBackStack(null);
                    ftnoti.commit();
                }*/
                if (menuItem.getItemId() == R.id.nav_item_Settings) {
                    FragmentTransaction ftsetting = mFragmentManager.beginTransaction();
                    ftsetting.replace(R.id.containerView, settingFragment);
                    ftsetting.addToBackStack(null);
                    ftsetting.commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(InqMainActivity.this);
                    dialog.setTitle("Exit");
                    dialog.setIcon(R.drawable.ic_noti);
                    dialog.setCancelable(true);
                    dialog.setMessage("คุณต้องการออกจากระบบใช่หรือไม่");
                    dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Boolean status = mManager.clearLoggedInOfficial();
                            Log.d("clear logout", String.valueOf(status));
                            Toast.makeText(InqMainActivity.this, "ออกจากระบบแล้ว", Toast.LENGTH_SHORT).show();
                            finish();
                            System.exit(0);
                        }

                    });

                    dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

                    dialog.show();

                }
                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.inq_appname) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent gotoIPSettingActivity = new Intent(this, IPSettingActivity.class);

            startActivity(gotoIPSettingActivity);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    class OfficialDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        protected String[] doInBackground(String... params) {
            //
            // Show Data
            String[] arrData = {""};
            arrData = mDbHelper.SelectDataOfficial(params[0]);
            if(arrData != null) {
                Log.i("Recieve login", arrData[6]+" "+arrData[7]);
            }else{
                Log.i("Recieve login", "not have");
            }
            return arrData;
        }

        protected void onPostExecute(String[] arrData) {
            // Dismiss ProgressBar
            //Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            /*** Default Value ***/
            OfficialName.setText(arrData[4] +" "+ arrData[6]+" "+arrData[7]);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(
                new ActivityResultEvent(requestCode, resultCode, data));
    }

    private void registerGcm() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            // register GCM registration complete receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GcmRegisterService.REGISTRATION_COMPLETE));
            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                   new IntentFilter(GcmRegisterService.PUSH_NOTIFICATION));
            isReceiverRegistered = true;

        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        isReceiverRegistered = false;

    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences.getBoolean(GcmRegisterService.SENT_TOKEN_TO_SERVER, false);
            // TODO Do something here
        }
    };

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume inqmain", "");

        if (networkConnectivity) {
            Log.i("networkConnec inqmain", "connect!! ");

            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
                registerReceiver(); Log.i("GCM", "connect!! ");
            }

        } else {
            Log.i("networkConnec inqmain", "no connect!! ");
        }
    }
    @Override
    protected void onPause() {

        if (networkConnectivity) {
            Log.i("networkConnec inqmain", "connect!! ");
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
                unregisterReceiver();
            }
        }else {
            Log.i("networkConnect inqmain", "no connect!! ");
        } super.onPause();
    }
}
