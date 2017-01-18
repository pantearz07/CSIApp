package com.scdc.csiapp.policetabs;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiListOfficial;
import com.scdc.csiapp.apimodel.ApiOfficial;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.SnackBarAlert;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.tablemodel.TbOfficial;

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
    private Context mContext;
    private PreferenceData mManager;
    SQLiteDatabase mDb;
    DBHelper mDbHelper;
    ConnectionDetector cd;
    Snackbar snackbar;

    GetDateTime getDateTime;

    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 20; //20 second

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
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) viewinvestigator.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                if (cd.isNetworkAvailable()) {
                    Log.i("log_show draft", "Refreshing!! ");
                    swipeContainer.setRefreshing(true);
                    mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
                    mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่

                } else {
                    swipeContainer.setRefreshing(true);
                    // ดึงค่าจาก SQLite เพราะไม่มีการต่อเน็ต
                    selectApiOfficialFromSQLite();
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayoutInv, LENGTH_INDEFINITE,
                            getString(R.string.offline_mode));
                    snackBarAlert.createSnacbar();

                    Log.i("log_show draft", "fail network");
                }

            }

        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                Log.i("log_show draft", "Runnable");

                if (cd.isNetworkAvailable()) {
                    Log.i("log_show draft", "Refreshing!! ");

                    swipeContainer.setRefreshing(true);
                    mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
                    mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่

                } else {
                    swipeContainer.setRefreshing(true);
                    // ดึงค่าจาก SQLite เพราะไม่มีการต่อเน็ต
                    selectApiOfficialFromSQLite();

                    Log.i("log_show draft", "fail network");
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayoutInv, LENGTH_INDEFINITE,
                            getString(R.string.offline_mode));
                    snackBarAlert.createSnacbar();

                }
            }
        });
        officialListAdapter = new OfficialListAdapter(apiOfficialList);
        rv.setAdapter(officialListAdapter);

        officialListAdapter.setOnItemClickListener(onItemClickListener);
        if (cd.isNetworkAvailable()) {
            new ConnectlistOfficial().execute();
            mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
            mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่

        } else {
            selectApiOfficialFromSQLite();
        }

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
                new ConnectlistOfficial().execute();
            } else {
                selectApiOfficialFromSQLite();
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
            }
        }
    }

    public void selectApiOfficialFromSQLite() {
        ApiListOfficial apiListOfficial = mDbHelper.selectApiOfficial("inquiryofficial");
        apiOfficialList = apiListOfficial.getData().getResult();

        Log.d(TAG, "Update OfficialListAdapter SQLite");

        if (swipeContainer != null && swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        officialListAdapter = new OfficialListAdapter(apiOfficialList);
        rv.setAdapter(officialListAdapter);
        officialListAdapter.notifyDataSetChanged();
        officialListAdapter.setOnItemClickListener(onItemClickListener);
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
                // เพิ่มข้อมูลที่ได้มาลง SQLite ด้วย syncOfficial ปิดไว้ก่อน เพราะไม่ต้องดึงมาทั้งหมดไม่งั้นจะหนักเครื่องเฉยๆ
                int size = apiOfficialList.size();
                List<TbOfficial> tbOfficials = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    tbOfficials.add(apiOfficialList.get(i).getTbOfficial());
                }
                mDbHelper.syncOfficial(tbOfficials);
                // เอาข้อมูลไปแสดงใน RV
                officialListAdapter.notifyDataSetChanged();
                Log.d(TAG, "Update officialListAdapter");

                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                officialListAdapter = new OfficialListAdapter(apiOfficialList);
                rv.setAdapter(officialListAdapter);
                officialListAdapter.setOnItemClickListener(onItemClickListener);
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

}

