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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectServer;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.gcmservice.GcmRegisterService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    private static final String TAG = "MainActivity";
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;

    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    long isConnectingToInternet = 0;

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;

    //อันเก่า
    HomeFragment homeFragment;
    ReceivingCaseListFragment receivingCaseListFragment;
    ReceivingCaseListFragment2 receivingCaseListFragment2;
    DraftListFragment draftListFragment;
    FullListFragment fullListFragment;
    //--อันเก่าา--//

    //รายการคดี
    // CaseSceneListFragment caseSceneListFragment;

    //เมนูอื่นๆ
    PoliceListFragment policeListFragment;
    ScheduleInvestigatorsFragment scheduleInvestigatorsFragment;
    SettingFragment settingFragment;
    ProfileFragment profileFragment;

    //ไม่ได้ใช้
    NotiFragment notiFragment;
    ScheduleInvestigationFragment scheduleInvestigationFragment;

    Toolbar toolbar;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    private PreferenceData mManager;
    String officialID, username;
    TextView OfficialName, txtusername;
    ImageView avatar;
    GetDateTime getDateTime;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mManager = new PreferenceData(this);
        // PreferenceData member id
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        username = mManager.getPreferenceData(mManager.KEY_USERNAME);
        cd = new ConnectionDetector(getApplicationContext());
        networkConnectivity = cd.networkConnectivity();
        mDbHelper = new SQLiteDBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        getDateTime = new GetDateTime();
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
        //new OfficialDataTask().execute(officialID);

        mNavigationView.addHeaderView(headerView);
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the CSIDataTabFragment as the first Fragment
         */
        //อันเก่า//
        homeFragment = new HomeFragment();
        receivingCaseListFragment = new ReceivingCaseListFragment();
        receivingCaseListFragment2 = new ReceivingCaseListFragment2();
        draftListFragment = new DraftListFragment();
        fullListFragment = new FullListFragment();

        // caseSceneListFragment  = new CaseSceneListFragment();

        policeListFragment = new PoliceListFragment();
        scheduleInvestigatorsFragment = new ScheduleInvestigatorsFragment();
        settingFragment = new SettingFragment();
        profileFragment = new ProfileFragment();

        scheduleInvestigationFragment = new ScheduleInvestigationFragment();
        notiFragment = new NotiFragment();
        mFragmentManager = getSupportFragmentManager();
        if (networkConnectivity) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                registerReceiver();

                if (checkPlayServices()) {
                    registerGcm();
                }

            }
            Log.i("networkConnect main", "connect!! ");
        } else {
            Log.i("networkConnect main", "no connect!! ");
        }

        FragmentTransaction fthome = mFragmentManager.beginTransaction();
        fthome.replace(R.id.containerView, receivingCaseListFragment2);
        fthome.addToBackStack(null);
        fthome.commit();

        String menuFragment = getIntent().getStringExtra("menuFragment");
        if (menuFragment != null) {
            if (menuFragment.equals("receivingCaseListFragment2")) {
                FragmentTransaction fthome2 = mFragmentManager.beginTransaction();
                fthome2.replace(R.id.containerView, receivingCaseListFragment2);
                fthome2.addToBackStack(null);
                fthome2.commit();

            }
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

                if (menuItem.getItemId() == R.id.nav_item_casescene) {
                    FragmentTransaction fthome2 = mFragmentManager.beginTransaction();
                    fthome2.replace(R.id.containerView, receivingCaseListFragment2);
                    fthome2.addToBackStack(null);
                    fthome2.commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_receivingcase) {
                    FragmentTransaction fthome2 = mFragmentManager.beginTransaction();
                    fthome2.replace(R.id.containerView, receivingCaseListFragment2);
                    fthome2.addToBackStack(null);
                    fthome2.commit();
                }

                if (menuItem.getItemId() == R.id.nav_item_draft) {
                    FragmentTransaction ftdraft = mFragmentManager.beginTransaction();
                    ftdraft.replace(R.id.containerView, draftListFragment);
                    ftdraft.addToBackStack(null);
                    ftdraft.commit();

                }
                if (menuItem.getItemId() == R.id.nav_item_full) {
                    FragmentTransaction ftfull = mFragmentManager.beginTransaction();
                    ftfull.replace(R.id.containerView, fullListFragment);
                    ftfull.addToBackStack(null);
                    ftfull.commit();
                }
                if (menuItem.getItemId() == R.id.nav_item_schedule) {
                    FragmentTransaction ftschedule = mFragmentManager.beginTransaction();
                    ftschedule.replace(R.id.containerView, scheduleInvestigatorsFragment);
                    ftschedule.addToBackStack(null);
                    ftschedule.commit();
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Exit");
                    dialog.setIcon(R.drawable.ic_noti);
                    dialog.setCancelable(true);
                    dialog.setMessage("คุณต้องการออกจากระบบใช่หรือไม่");
                    dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Boolean status = mManager.clearLoggedInOfficial();
                            Log.d("clear logout", String.valueOf(status));

                            Toast.makeText(MainActivity.this, "ออกจากระบบแล้ว", Toast.LENGTH_SHORT).show();
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
                R.string.th_appname) {
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


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume mainactivity", "");

        if (networkConnectivity) {
            Log.i("networkConnect main", "connect!! ");
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
                //  registerReceiver(); Log.i("GCM", "connect!! ");
            }
        } else {
            Log.i("networkConnect main", "no connect!! ");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause mainactivity", "");
        if (networkConnectivity) {
            Log.i("networkConnect main", "connect!! ");
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
                //  unregisterReceiver();
                Log.i("GCM", "connect!! ");
            }
        } else {
            Log.i("networkConnect main", "no connect!! ");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        Log.d("onStart mainactivity", "");
        if (networkConnectivity) {
            Log.i("networkConnect main", "connect!! vvv");

        } else {
            Log.i("networkConnect main", "no connect!! ");
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//     /   Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.scdc.csiapp.main/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
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
            /*FragmentTransaction ftsetting = mFragmentManager.beginTransaction();
            ftsetting.replace(R.id.containerView, settingFragment);
            ftsetting.addToBackStack(null);
            ftsetting.commit();*/
//            Intent gotoIPSettingActivity = new Intent(this, IPSettingActivity.class);
//
//            startActivity(gotoIPSettingActivity);
//            this.finish();
//            return true;
            if (networkConnectivity) {
                Log.d("internet status", "connected to wifi");

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();

                View view = inflater.inflate(R.layout.ipsetting_dialog, null);
                builder.setView(view);

                final EditText ipvalueEdt = (EditText) view.findViewById(R.id.ipvalueEdt);


                builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (ipvalueEdt.getText().equals("")) {
                            Toast.makeText(getApplicationContext(), "กรุณาป้อนข้อมูล",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String ipvalue = ipvalueEdt.getText().toString();
                            SharedPreferences sp = getSharedPreferences(PreferenceData.PREF_IP, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(PreferenceData.KEY_IP, ipvalue);
                            editor.commit();
                            Log.d("ipvalue connect", ipvalue);
                            ApiConnect.updateIP();
                            Toast.makeText(getApplicationContext(), "บันทึกเรียบร้อย " + ipvalue,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            } else {
                Log.d("internet status", "no Internet Access");

                Toast.makeText(getBaseContext(),
                        "กรุณาเชื่อมต่ออินเตอร์เน็ต",
                        Toast.LENGTH_SHORT).show();


            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.scdc.csiapp.main/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
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
            if (arrData != null) {
                Log.i("Recieve login", arrData[6] + " " + arrData[7]);
            } else {
                Log.i("Recieve login", "not have");
            }
            return arrData;
        }

        protected void onPostExecute(String[] arrData) {
            // Dismiss ProgressBar
            //Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            /*** Default Value ***/
            OfficialName.setText(arrData[4] + " " + arrData[6] + " " + arrData[7]);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(
                new ActivityResultEvent(requestCode, resultCode, data));
    }


    public boolean logout(String sOfficialID) {
       /* long status = mDbHelper.updatelogoutofficial(officialID,"investigated");
        if (status > 0) {
            Log.i("updateReportStatus", "OK");
        }else{

            Log.i("updateReportStatus", "error");
        }*/
        final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();

        final String Mo_LogoutTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("OfficialID", sOfficialID));
        // params.add(new BasicNameValuePair("Mo_LoginTime", Mo_LoginTime));
        params.add(new BasicNameValuePair("Mo_LogoutTime", Mo_LogoutTime));
        String resultServer = ConnectServer.getJsonPostGet(params,
                "logtemp");
        /*** Default Value ***/
        String strStatusID = "0";
        String strError = "Unknow Status!";
        JSONObject c;

        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // Prepare Save Data
        if (strStatusID.equals("0")) {
            Log.i("logout", strError);

        } else {
            Log.i("logout", strError + " " + sOfficialID);

        }

        return true;
    }

    private void registerGcm() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            Log.i("registerReceiver", "registerReceiver");
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
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
