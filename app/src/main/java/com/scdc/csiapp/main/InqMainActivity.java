package com.scdc.csiapp.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.inqmain.NoticeCaseListFragment;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;

public class InqMainActivity extends AppCompatActivity {
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    Context mContext;
    private static String strSDCardPathName_temp = "/CSIFiles/temp/";
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    static FragmentManager mFragmentManager;
    public static final String KEY_PROFILE = "key_profile";
    ApiProfile apiProfile;

    //รายการคดี อยู่ใน package/inqmain
    NoticeCaseListFragment noticeCaseListFragment;

    //ใช้ อยู่ใน package/main
    //รายชื่อเจ้าหน้าที่ตำรวจ
    PoliceListFragment policeListFragment;
    SettingFragment settingFragment;
    //แก้ไขประวัติส่วนตัว
    ProfileFragment profileFragment;
    View headerView;
    Toolbar toolbar;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    private PreferenceData mManager;
    String officialID, username, password, accestype;
    TextView OfficialName, txtusername;
    ImageView avatar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    private static final String TAG = "DEBUG-InqMainActivity";
    ConnectionDetector cd;
    String nameOfficial = "";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inq_activity_main);
        mManager = new PreferenceData(this);
        mContext = getApplicationContext();
        mDbHelper = new SQLiteDBHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        cd = new ConnectionDetector(getApplicationContext());

        /**
         *Setup the DrawerLayout and NavigationView
         */
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, mNavigationView, false);
        OfficialName = (TextView) headerView.findViewById((R.id.officialName));
        txtusername = (TextView) headerView.findViewById((R.id.username));
        avatar = (ImageView) headerView.findViewById(R.id.profile_image);
        mNavigationView.addHeaderView(headerView);
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the CSIDataTabFragment as the first Fragment
         */

        noticeCaseListFragment = new NoticeCaseListFragment();
        policeListFragment = new PoliceListFragment();
        settingFragment = new SettingFragment();
        profileFragment = new ProfileFragment();

        mFragmentManager = getSupportFragmentManager();
//        setFragment(noticeCaseListFragment, 1);
        if (savedInstanceState == null) {
            // PreferenceData member id
            setUserProfile();
            //set Header View in Navigation
            setHeaderView();
            replaceFragment();
        } else {

        }
        BusProvider.getInstance().register(this);
        String menuFragment = getIntent().getStringExtra("menuFragment");
        if (menuFragment != null) {
            if (menuFragment.equals("noticeCaseListFragment")) {
                setFragment(noticeCaseListFragment, 0);
            }
        }

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Consume input from header view. This disables the ripple effect
                mDrawerLayout.closeDrawers();
                mFragmentManager.popBackStack();
                setFragment(profileFragment, 1);
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
                    mFragmentManager.popBackStack();
                    setFragment(noticeCaseListFragment, 1);
                }

                if (menuItem.getItemId() == R.id.nav_item_police) {
                    mFragmentManager.popBackStack();
                    setFragment(policeListFragment, 1);
                }

                if (menuItem.getItemId() == R.id.nav_item_Settings) {
                    mFragmentManager.popBackStack();
                    setFragment(settingFragment, 1);
                }
                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(InqMainActivity.this);
                    dialog.setTitle(R.string.ad_title);
                    dialog.setIcon(R.drawable.ic_noti);
                    dialog.setCancelable(true);
                    dialog.setMessage(R.string.ad_message);
                    dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Boolean status = mManager.clearLoggedInOfficial();
                            Log.d("clear logout", String.valueOf(status));
                            Toast.makeText(InqMainActivity.this, "ออกจากระบบแล้ว", Toast.LENGTH_SHORT).show();
                            finish();
                            System.exit(0);
                        }

                    });

                    dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

    public void replaceFragment() {
//        replaceFragment(CaseSceneListFragment.newInstance());
        noticeCaseListFragment.newInstance();
        setFragment(noticeCaseListFragment, 0);
    }

    private void setUserProfile() {
        try {
            if (WelcomeActivity.profile.getTbOfficial() != null) {
                BusProvider.getInstance().post(WelcomeActivity.profile);
                officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;
                username = WelcomeActivity.profile.getTbUsers().id_users;
                password = WelcomeActivity.profile.getTbUsers().pass;
                accestype = WelcomeActivity.profile.getTbOfficial().AccessType;
                nameOfficial = WelcomeActivity.profile.getTbOfficial().Rank
                        + WelcomeActivity.profile.getTbOfficial().FirstName
                        + " " + WelcomeActivity.profile.getTbOfficial().LastName;
            } else {

                Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);
                finish();
                startActivity(gotoWelcomeActivity);

            }
        } catch (NullPointerException e) {
            Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);
            finish();
            startActivity(gotoWelcomeActivity);
        }
    }

    private void setHeaderView() {

        OfficialName.setText(nameOfficial);
        txtusername.setText(username);
        try {
            if (WelcomeActivity.profile.getTbUsers() == null) {
                Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);
                finish();
                startActivity(gotoWelcomeActivity);
            } else {
                File avatarfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), strSDCardPathName_temp + WelcomeActivity.profile.getTbUsers().getPicture());
                if (avatarfile.exists()) {
                    Picasso.with(this)
                            .load(avatarfile)
                            .resize(100, 100)
                            .centerCrop()
                            .into(avatar);
                }
            }
        } catch (NullPointerException e) {
            Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);
            finish();
            startActivity(gotoWelcomeActivity);
        }
        Log.i(TAG, officialID);
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
            mFragmentManager.popBackStack();
            setFragment(settingFragment, 1);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(
                new ActivityResultEvent(requestCode, resultCode, data));
    }

    public static void setFragment(Fragment fragment, int backstackyes) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (backstackyes == 1) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.containerView, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (apiProfile == null) {
            apiProfile = new ApiProfile();
            apiProfile = WelcomeActivity.profile;
        } else {
            apiProfile = WelcomeActivity.profile;
        }
        outState.putParcelable(KEY_PROFILE, Parcels.wrap(apiProfile));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        apiProfile = Parcels.unwrap(savedInstanceState.getParcelable(KEY_PROFILE));
        if (WelcomeActivity.profile == null) {
            WelcomeActivity.profile = new ApiProfile();
            WelcomeActivity.profile = apiProfile;
        } else {
            WelcomeActivity.profile = apiProfile;
        }

        // PreferenceData member id
        setUserProfile();
        //set Header View in Navigation
        setHeaderView();
        Log.i(TAG, "from onRestoreInstanceState" + officialID);
    }
}
