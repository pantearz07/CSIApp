package com.scdc.csiapp.policetabs;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiListOfficial;
import com.scdc.csiapp.apimodel.ApiOfficial;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.BusProvider;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.SnackBarAlert;
import com.scdc.csiapp.main.WelcomeActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;

/**
 * Created by Pantearz07 on 14/10/2559.
 */

public class InquiryOfficialListFragment extends Fragment {
    private static final String TAG = "DEBUG-InquiryOfficialListFragment";
    CoordinatorLayout rootLayoutInv;
    RecyclerView rv;
    SwipeRefreshLayout swipeContainer;
    OfficialListAdapter officialListAdapter;
    private List<ApiOfficial> apiOfficialList;
    private List<ApiOfficial> apiOfficialListTemp;
    private Context mContext;
    private PreferenceData mManager;
    SQLiteDatabase mDb;
    DBHelper mDbHelper;
    ConnectionDetector cd;
    Snackbar snackbar;
    SearchView searchView;
    GetDateTime getDateTime;
    ApiProfile apiProfile;
    ApiConnect api;
    String officialID;
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    public static final String KEY_PROFILE = "key_profile";
    public static final String KEY_CONNECT = "key_connect";
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 20; //20 second

    public static InquiryOfficialListFragment newInstance() {
        return new InquiryOfficialListFragment();
    }

    public InquiryOfficialListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewinvestigator = inflater.inflate(R.layout.investigator_list_tab_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.policelist);
        mDbHelper = new DBHelper(getActivity());
        mManager = new PreferenceData(getActivity());
        mContext = viewinvestigator.getContext();
        rootLayoutInv = (CoordinatorLayout) viewinvestigator.findViewById(R.id.rootLayoutInv);
        rv = (RecyclerView) viewinvestigator.findViewById(R.id.rv);
        cd = new ConnectionDetector(mContext);
        getDateTime = new GetDateTime();

        apiOfficialList = new ArrayList<>();
        apiOfficialListTemp = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) viewinvestigator.findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAdapterData();
            }
        });
        searchView = (SearchView) viewinvestigator.findViewById(R.id.search);
        searchView.setQueryHint("ค้นหารายชื่อ");
        searchView.setIconifiedByDefault(false);

// perform set on query text listener event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
// do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
// do something when text changes
                apiOfficialList = apiOfficialListTemp;
                Log.d(TAG, "onQueryTextChange " + query);
                if (query.length() == 0) {
                    setAdapterList();
                }
                List<ApiOfficial> src_list = new ArrayList<>();
                for (int i = 0; i < apiOfficialList.size(); i++) {
                    try {
                        String Rank = "", Position = "", FirstName = "", LastName = "", PoliceStationName = "", Alias = "", PhoneNumber = "";
                        if (apiOfficialList.get(i).getTbOfficial() != null) {
                            if (apiOfficialList.get(i).getTbOfficial().Rank != null) {
                                Rank = apiOfficialList.get(i).getTbOfficial().Rank;
                            }
                            if (apiOfficialList.get(i).getTbOfficial().FirstName != null) {
                                FirstName = apiOfficialList.get(i).getTbOfficial().FirstName;
                            }
                            if (apiOfficialList.get(i).getTbOfficial().LastName != null) {
                                LastName = apiOfficialList.get(i).getTbOfficial().LastName;
                            }
                            if (apiOfficialList.get(i).getTbOfficial().Alias != null) {
                                Alias = apiOfficialList.get(i).getTbOfficial().Alias;
                            }
                            if (apiOfficialList.get(i).getTbOfficial().Position != null) {
                                Position = apiOfficialList.get(i).getTbOfficial().Position;
                            }
                            if (apiOfficialList.get(i).getTbOfficial().PhoneNumber != null) {
                                PhoneNumber = apiOfficialList.get(i).getTbOfficial().PhoneNumber;
                            }
                            if (apiOfficialList.get(i).getTbPoliceStation().PoliceStationName != null) {
                                PoliceStationName = apiOfficialList.get(i).getTbPoliceStation().PoliceStationName;
                            }
                        }
                        String offInfo = "";
                        offInfo = Rank + " " + FirstName + " " + LastName;
                        if (offInfo.contains(query)) {
                            src_list.add(apiOfficialList.get(i));
                        } else if (Alias.contains(query)) {
                            src_list.add(apiOfficialList.get(i));
                        } else if (Position.contains(query)) {
                            src_list.add(apiOfficialList.get(i));
                        } else if (PhoneNumber.contains(query)) {
                            src_list.add(apiOfficialList.get(i));
                        } else if (PoliceStationName.contains(query)) {
                            src_list.add(apiOfficialList.get(i));
                        }

                    } catch (Exception e) {
                    }
                }
                apiOfficialList = src_list;
                setAdapterList();
                return false;
            }
        });
        return viewinvestigator;
    }

    Runnable mHandlerTaskcheckConnect = new Runnable() {
        @Override
        public void run() {
            ConnectApiCheckConnect connectApi = new ConnectApiCheckConnect();
            connectApi.execute();
            mHandler.postDelayed(mHandlerTaskcheckConnect, INTERVAL);
        }
    };

    class ConnectApiCheckConnect extends AsyncTask<ApiStatus, Boolean, ApiStatus> {

        @Override
        protected ApiStatus doInBackground(ApiStatus... apiStatuses) {
            return WelcomeActivity.api.checkConnect();
        }

        @Override
        protected void onPostExecute(ApiStatus apiStatus) {
            super.onPostExecute(apiStatus);
            if (apiStatus != null && apiStatus.getStatus().equalsIgnoreCase("success")) {
                mHandler.removeCallbacks(mHandlerTaskcheckConnect);
            } else {
                try {
                    snackbar = Snackbar.make(rootLayoutInv, getString(R.string.cannot_connect_server_offline), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (snackbar != null) {
                                        snackbar.dismiss();//ปิดการแจ้งเตือนเก่าออกให้หมดก่อนตรวจใหม่
                                    }
                                    mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(getActivity());
                                    LayoutInflater inflater = getActivity().getLayoutInflater();

                                    view = inflater.inflate(R.layout.ipsetting_dialog, null);
                                    builder.setView(view);
                                    final AutoCompleteTextView ipvalueEdt = (AutoCompleteTextView) view.findViewById(R.id.ipvalueEdt);
                                    final String[] ip_list = getResources().getStringArray(
                                            R.array.ip_list);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,
                                            ip_list);
                                    ipvalueEdt.setThreshold(1);
                                    ipvalueEdt.setAdapter(adapter);
                                    ipvalueEdt.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View view, MotionEvent motionEvent) {

                                            ipvalueEdt.showDropDown();

                                            return false;
                                        }
                                    });

                                    builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (ipvalueEdt.getText().equals("")) {
                                                Toast.makeText(getActivity(),
                                                        getString(R.string.please_input_data),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                String ipvalue = ipvalueEdt.getText().toString();
                                                WelcomeActivity.api.updateIP(ipvalue);

                                                if (snackbar != null) {
                                                    snackbar.dismiss();//ปิดการแจ้งเตือนเก่าออกให้หมดก่อนตรวจใหม่
                                                }
                                                mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
                                                mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่

                                                Toast.makeText(getActivity(),
                                                        getString(R.string.save_complete),
                                                        Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });

                                    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    builder.show();
                                }
                            });
                    snackbar.show();
                } catch (Exception e) {

                }
            }
        }
    }

    public void selectApiOfficialFromSQLite() {
        ApiListOfficial apiListOfficial = mDbHelper.selectApiOfficial("inquiryofficial");
        apiOfficialList = apiListOfficial.getData().getResult();
        apiOfficialListTemp = apiListOfficial.getData().getResult();
        Log.d(TAG, "Update OfficialListAdapter SQLite");
        setAdapterList();

    }

    class ConnectlistOfficial extends AsyncTask<Void, Void, ApiListOfficial> {

        @Override
        protected ApiListOfficial doInBackground(Void... voids) {
            try {
                return WelcomeActivity.api.listOfficial("inquiryofficial");
            } catch (RuntimeException e) {
                Log.e(TAG, e.getMessage());
                WelcomeActivity.api = new ApiConnect(getActivity());
                return WelcomeActivity.api.listOfficial("inquiryofficial");
            }
        }

        @Override
        protected void onPostExecute(ApiListOfficial apiListOfficial) {
            super.onPostExecute(apiListOfficial);
            if (apiListOfficial != null) {
                Log.d(TAG, apiListOfficial.getStatus());
                Log.d(TAG, String.valueOf(apiListOfficial.getData().getResult().size()));

                // ข้อมูล ApiNoticeCase ที่ได้จากเซิร์ฟเวอร์
                apiOfficialList = apiListOfficial.getData().getResult();
                apiOfficialListTemp = apiListOfficial.getData().getResult();

                // เอาข้อมูลไปแสดงใน RV
                setAdapterList();
            } else {
                selectApiOfficialFromSQLite();
            }
        }
    }

    OfficialListAdapter.OnItemClickListener onItemClickListener = new OfficialListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            final ApiOfficial apiOfficial = apiOfficialList.get(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), view, Gravity.RIGHT);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.official_menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                     public boolean onMenuItemClick(MenuItem item) {
                                                         switch (item.getItemId()) {
                                                             case R.id.call:
                                                                 calling(apiOfficial.getTbOfficial().getPhoneNumber());

                                                                 break;

                                                         }
                                                         return true;
                                                     }
                                                 }
                );
                popup.show();
            } else {
                calling(apiOfficial.getTbOfficial().getPhoneNumber());
            }
        }
    };

    // ฟังชัน โทรออก
    private void calling(String sPhonenumber) {
        if (sPhonenumber == null || sPhonenumber.equals("")) {

        } else {
            try {
                Log.i(TAG, "Calling a Phone Number " + sPhonenumber);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:=" + sPhonenumber.replace(" ", "").trim()));
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            } catch (ActivityNotFoundException activityException) {
                Log.e("Calling a Phone Number", "Call failed", activityException);
            }
        }
    }

    private void setAdapterData() {
        if (cd.isNetworkAvailable()) {
//                    Log.i("log_show draft", "Refreshing!! ");
            swipeContainer.setRefreshing(true);
            mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
            mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่
            selectApiOfficialFromSQLite();
            new ConnectlistOfficial().execute();
        } else {
            swipeContainer.setRefreshing(true);
            // ดึงค่าจาก SQLite เพราะไม่มีการต่อเน็ต
            selectApiOfficialFromSQLite();
//                    Log.i("log_show draft", "fail network");
            SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayoutInv, LENGTH_INDEFINITE,
                    getString(R.string.offline_mode));
            snackBarAlert.createSnacbar();
        }
    }

    private void setAdapterList() {
        if (swipeContainer != null && swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        officialListAdapter = new OfficialListAdapter(apiOfficialList);
        rv.setAdapter(officialListAdapter);
        officialListAdapter.notifyDataSetChanged();
        officialListAdapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                setAdapterData();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = rv.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        Log.i(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            // Fragment ถูกสร้างขึ้นมาครั้งแรก
            //check user profile
            setUserProfile();

        } else {
            // Fragment ถูก Restore ขึ้นมา
            restoreInstanceState(savedInstanceState);
            setUserProfile();
            Log.i(TAG, "from onActivityCreated" + officialID);

        }
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        apiProfile = Parcels.unwrap(savedInstanceState.getParcelable(KEY_PROFILE));
        if (WelcomeActivity.profile == null) {
            WelcomeActivity.profile = new ApiProfile();
            WelcomeActivity.profile = apiProfile;
        } else {
            WelcomeActivity.profile = apiProfile;
        }
        api = Parcels.unwrap(savedInstanceState.getParcelable(KEY_CONNECT));
        if (WelcomeActivity.api == null) {
            WelcomeActivity.api = new ApiConnect(mContext);
            WelcomeActivity.api = api;
        } else {
            WelcomeActivity.api = api;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (apiProfile == null) {
            apiProfile = new ApiProfile();
            apiProfile = WelcomeActivity.profile;
        } else {
            apiProfile = WelcomeActivity.profile;
        }
        if (api == null) {
            api = new ApiConnect(getActivity());
            api = WelcomeActivity.api;
        } else {
            api = WelcomeActivity.api;
        }
        outState.putParcelable(KEY_PROFILE, Parcels.wrap(apiProfile));
        outState.putParcelable(KEY_CONNECT, Parcels.wrap(api));
        super.onSaveInstanceState(outState);
    }

    private void setUserProfile() {
        try {
            if (WelcomeActivity.profile.getTbOfficial() != null) {
                officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;

            } else {

                Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);
                getActivity().finish();
                startActivity(gotoWelcomeActivity);

            }
        } catch (NullPointerException e) {
            Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);
            getActivity().finish();
            startActivity(gotoWelcomeActivity);
        }
    }
}

