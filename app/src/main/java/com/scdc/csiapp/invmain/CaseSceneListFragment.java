package com.scdc.csiapp.invmain;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiListCaseScene;
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
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbUsers;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;
import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * Created by Pantearz07 on 14/9/2559.
 */
public class CaseSceneListFragment extends Fragment {
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    Context context;
    //Recycle view
    private List<ApiCaseScene> caseList;
    private static Bundle mBundleRecyclerViewState;
    RecyclerView rvDraft;
    SwipeRefreshLayout swipeContainer;
    private ApiCaseSceneListAdapter apiCaseSceneListAdapter;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    // connect sqlite
    SQLiteDatabase mDb;
    DBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    CSIDataTabFragment csiDataTabFragment;
    AssignTabFragment assignTabFragment;
    ConnectionDetector cd;
    //  Boolean networkConnectivity = false;
    Snackbar snackbar;
    GetDateTime getDateTime;
    String officialID;
    private static final String TAG = "DEBUG-CaseSceneListFragment";
    public static final String LIST_INSTANCE_STATE = "datastate";
    TbOfficial tbOfficial;
    TbUsers tbUsers;
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 10; //10 second
    View viewlayout;
    public static final String KEY_PROFILE = "key_profile";
    public static final String KEY_CONNECT = "key_connect";
    ApiProfile apiProfile;
    ApiConnect api;
    ApiCaseScene apiNoticeCase;
    String mode, caserepID;
    private static final String[] sortlists =
            {"วันเวลารับเเจ้งเหตุล่าสุด", "วันเวลารับเเจ้งเหตุเก่าสุด", "วันเวลาเเก้ไขล่าสุด", "วันเวลาเเก้ไขเก่าสุด"};
    String mSelected;
    int wSelected = 0;

    public static CaseSceneListFragment newInstance() {
        return new CaseSceneListFragment();
    }

    public CaseSceneListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewlayout = inflater.inflate(R.layout.casescene_fragment_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.home);
        mDbHelper = new DBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        context = viewlayout.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        csiDataTabFragment = new CSIDataTabFragment();
        assignTabFragment = new AssignTabFragment();

        cd = new ConnectionDetector(context);
        getDateTime = new GetDateTime();

        rootLayout = (CoordinatorLayout) viewlayout.findViewById(R.id.rootLayout);
        fabBtn = (FloatingActionButton) viewlayout.findViewById(R.id.fabBtn);
        fabBtn.setVisibility(View.GONE);

        caseList = new ArrayList<>();
        rvDraft = (RecyclerView) viewlayout.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rvDraft.setLayoutManager(llm);
        rvDraft.setHasFixedSize(true);

        swipeContainer = (SwipeRefreshLayout) viewlayout.findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeColors(Color.parseColor("#4183D7"),
                Color.parseColor("#F62459"),
                Color.parseColor("#03C9A9"),
                Color.parseColor("#F4D03F"));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAdapterData();
            }
        });
        return viewlayout;
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
            WelcomeActivity.api = new ApiConnect(context);
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
        Parcelable listState = rvDraft.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        Log.i(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume api");
        if (WelcomeActivity.api == null) {
            Intent gotoWelcomeActivity = new Intent(context, WelcomeActivity.class);
            getActivity().finish();
            startActivity(gotoWelcomeActivity);
            Log.i(TAG, "onResume api null");
        }
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            rvDraft.getLayoutManager().onRestoreInstanceState(listState);
            Log.i(TAG, "onResume mBundleRecyclerViewState");
        }
    }

    public void selectApiCaseSceneFromSQLite() {
        ApiListCaseScene apiListNoticeCase = mDbHelper.selectApiCaseScene(officialID);
        caseList = apiListNoticeCase.getData().getResult();
//        Log.d(TAG, "Update apiNoticeCaseListAdapter SQLite");

        setAdapterList_sort();
    }

    private void setAdapterList() {
        Collections.sort(caseList, new Comparator<ApiCaseScene>() {
            @Override
            public int compare(ApiCaseScene obj1, ApiCaseScene obj2) {
                SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date_source = obj1.getTbCaseScene().ReceivingCaseDate + " " + obj1.getTbCaseScene().ReceivingCaseTime;
                String date_des = obj2.getTbCaseScene().ReceivingCaseDate + " " + obj2.getTbCaseScene().ReceivingCaseTime;
                try {
//                            Log.i("Compare" , String.valueOf(dfDate.parse(date_source).compareTo(dfDate.parse(date_des))));
                    return dfDate.parse(date_des).compareTo(dfDate.parse(date_source));
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        apiCaseSceneListAdapter = new ApiCaseSceneListAdapter(caseList);
        rvDraft.setAdapter(apiCaseSceneListAdapter);
        apiCaseSceneListAdapter.notifyDataSetChanged();
        apiCaseSceneListAdapter.setOnItemClickListener(onItemClickListener);
        swipeContainer.setRefreshing(false);
    }

    private void setAdapterData() {
        if (cd.isNetworkAvailable()) {
//                    Log.i("log_show draft", "Refreshing!! ");
            swipeContainer.setRefreshing(true);
            mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
            mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่
            new ConnectlistCasescene().execute();
        } else {
            swipeContainer.setRefreshing(true);
            // ดึงค่าจาก SQLite เพราะไม่มีการต่อเน็ต
            selectApiCaseSceneFromSQLite();
//                    Log.i("log_show draft", "fail network");
            snackbar = Snackbar.make(rootLayout, getString(R.string.offline_mode), LENGTH_INDEFINITE)
                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.show();
        }
    }

    ApiCaseSceneListAdapter.OnItemClickListener onItemClickListener = new ApiCaseSceneListAdapter.OnItemClickListener() {


        @Override
        public void onItemClick(View view, int position) {
            apiNoticeCase = caseList.get(position);
            mode = apiNoticeCase.getMode().toString();
            caserepID = apiNoticeCase.getTbCaseScene().getCaseReportID().toString();

            Log.d(TAG, "onItemClick");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //Creating the instance of PopupMenu
                PopupMenu popup = null;
                popup = new PopupMenu(getActivity(), view, Gravity.RIGHT);

                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.csi_menu_1, popup.getMenu());
                Menu popupMenu = popup.getMenu();
                popupMenu.findItem(R.id.edit).setVisible(false);
                if (apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("accept")
                        || apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("investigating")
                        || apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("investigated")) {
                    popupMenu.findItem(R.id.edit).setVisible(true);
                }
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.view:
                                        viewCase();

                                        break;
                                    case R.id.edit:
                                        editCase();
                                        break;
                                }
                                return true;
                            }
                        }
                );
                popup.show();
            } else {
                Log.d(TAG, "AlertDialog");
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage("ดูข้อมูลการตรวจนี้ " + caserepID);
                builder.setPositiveButton("ดู", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewCase();
                    }
                });
                if (apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("accept")
                        || apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("investigating")
                        || apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("investigated")) {
                    builder.setNeutralButton("แก้ไข", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editCase();
                        }
                    });
                }
                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
            }
        }
    };

    private void viewCase() {
        final Bundle i = new Bundle();
        //สถานะคดี
        if (apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("assign")) {
            i.putSerializable(assignTabFragment.Bundle_Key, apiNoticeCase);
            i.putString(assignTabFragment.Bundle_mode, "view");
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            assignTabFragment.setArguments(i);
            fragmentTransaction.replace(R.id.containerView, assignTabFragment).addToBackStack(null).commit();

        } else {

            i.putSerializable(csiDataTabFragment.Bundle_Key, apiNoticeCase);
            i.putString(csiDataTabFragment.Bundle_mode, "view");
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            csiDataTabFragment.setArguments(i);
            fragmentTransaction.replace(R.id.containerView, csiDataTabFragment).addToBackStack(null).commit();
        }
    }

    private void editCase() {
        final Bundle i = new Bundle();
        if (mode.equals("online")) {
            if (apiNoticeCase.getTbCaseScene().getLastUpdateDate() == "") {
            }
            boolean isSuccess1 = mDbHelper.updateAlldataCase(apiNoticeCase);
            if (isSuccess1) {
                i.putSerializable(csiDataTabFragment.Bundle_Key, apiNoticeCase);
                i.putString(csiDataTabFragment.Bundle_mode, "edit");
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                csiDataTabFragment.setArguments(i);
                fragmentTransaction.replace(R.id.containerView, csiDataTabFragment).addToBackStack(null).commit();

            } else {
                if (snackbar == null || !snackbar.isShown()) {
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_SHORT,
                            getString(R.string.save_error)
                                    + " " + apiNoticeCase.getTbCaseScene().CaseReportID.toString());
                    snackBarAlert.createSnacbar();
                }
            }

        } else {
            i.putSerializable(csiDataTabFragment.Bundle_Key, apiNoticeCase);
            i.putString(csiDataTabFragment.Bundle_mode, "edit");
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            csiDataTabFragment.setArguments(i);
            fragmentTransaction.replace(R.id.containerView, csiDataTabFragment).addToBackStack(null).commit();
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Exit");
        dialog.setIcon(R.drawable.ic_noti);
        dialog.setCancelable(true);
        dialog.setMessage("คุณต้องการออกจากระบบใช่หรือไม่");
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }

        });

        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    class ConnectlistCasescene extends AsyncTask<Void, Void, ApiListCaseScene> {

        @Override
        protected ApiListCaseScene doInBackground(Void... voids) {
            try {
                return WelcomeActivity.api.listCasescene();
            } catch (RuntimeException e) {
                Log.e(TAG, e.getMessage());
                WelcomeActivity.api = new ApiConnect(getActivity());
                return WelcomeActivity.api.listCasescene();
            }
        }

        @Override
        protected void onPostExecute(ApiListCaseScene apiListCaseScene) {
            super.onPostExecute(apiListCaseScene);
            if (apiListCaseScene != null) {
//                Log.d(TAG, apiListCaseScene.getStatus());
//                Log.d(TAG, String.valueOf(apiListCaseScene.getData().getResult().size()));

                // ข้อมูล ApiNoticeCase ที่ได้จากเซิร์ฟเวอร์
                caseList = apiListCaseScene.getData().getResult();

                setAdapterList_sort();
            } else {
//                if (snackbar == null || !snackbar.isShown()) {
//                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_INDEFINITE,
//                            "ดาวน์โหลดข้อมูลผิดพลาด");
//                    snackBarAlert.createSnacbar();
//                }
//                selectApiCaseSceneFromSQLite();
                Toast.makeText(getActivity(), "ชื่อผู้ใช้ไม่ถูกต้อง กรุณาเข้าสู่ระบบใหม่",
                        Toast.LENGTH_SHORT).show();
                Boolean status = mManager.clearLoggedInOfficial();
                Log.d("clear logout", String.valueOf(status));
                Intent mStartActivity = new Intent(context, WelcomeActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 600, mPendingIntent);
                System.exit(0);
            }
        }
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
                snackbar = Snackbar.make(rootLayout, getString(R.string.cannot_connect_server_offline), LENGTH_INDEFINITE)
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

    private void setUserProfile() {
        try {
            if (WelcomeActivity.profile.getTbOfficial() != null) {
                officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;

            } else {

                Intent gotoWelcomeActivity = new Intent(context, WelcomeActivity.class);
                getActivity().finish();
                startActivity(gotoWelcomeActivity);

            }
        } catch (NullPointerException e) {
            Intent gotoWelcomeActivity = new Intent(context, WelcomeActivity.class);
            getActivity().finish();
            startActivity(gotoWelcomeActivity);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sorting:
                // Do Fragment menu item stuff here
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("จัดเรียงตาม");
                builder.setSingleChoiceItems(sortlists, wSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelected = sortlists[which];
                        wSelected = which;
                    }
                });
                builder.setPositiveButton("เลือก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
                        swipeContainer.post(new Runnable() {
                            @Override
                            public void run() {
                                setAdapterData(); 
                            }
                        });
                        Toast.makeText(getActivity(), "จัดเรียงตาม " +
                                mSelected, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("ยกเลิก", null);
                builder.create();

// สุดท้ายอย่าลืม show() ด้วย
                builder.show();
                return true;
            default:
                break;
        }

        return false;
    }

    private void setAdapterList_sort() {

        // จัดเรียงข้อมูลใน caseList ให้เรียงตามวันที่แจ้งเหตุ ReceivingCaseDate,ReceivingCaseTime ก่อนถึงจะเอาไปแสดง
        // caseList เก็บข้อมูลในรูป List ใช้ Collections sort ได้
        if (wSelected == 0) {
            Collections.sort(caseList, new Comparator<ApiCaseScene>() {
                @Override
                public int compare(ApiCaseScene obj1, ApiCaseScene obj2) {
                    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date_source = obj1.getTbCaseScene().ReceivingCaseDate + " " + obj1.getTbCaseScene().ReceivingCaseTime;
                    String date_des = obj2.getTbCaseScene().ReceivingCaseDate + " " + obj2.getTbCaseScene().ReceivingCaseTime;
                    try {
                        return dfDate.parse(date_des).compareTo(dfDate.parse(date_source));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
            Log.i(TAG, "จัดเรียงตาม " + String.valueOf(wSelected));
        } else if (wSelected == 1) {

            Collections.sort(caseList, new Comparator<ApiCaseScene>() {
                @Override
                public int compare(ApiCaseScene obj1, ApiCaseScene obj2) {
                    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date_source = obj1.getTbCaseScene().ReceivingCaseDate + " " + obj1.getTbCaseScene().ReceivingCaseTime;
                    String date_des = obj2.getTbCaseScene().ReceivingCaseDate + " " + obj2.getTbCaseScene().ReceivingCaseTime;
                    try {
                        return dfDate.parse(date_source).compareTo(dfDate.parse(date_des));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
            Log.i(TAG, "จัดเรียงตาม " + String.valueOf(wSelected));
        } else if (wSelected == 2) {
            Collections.sort(caseList, new Comparator<ApiCaseScene>() {
                @Override
                public int compare(ApiCaseScene obj1, ApiCaseScene obj2) {
                    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date_source = obj1.getTbCaseScene().LastUpdateDate + " " + obj1.getTbCaseScene().LastUpdateTime;
                    String date_des = obj2.getTbCaseScene().LastUpdateDate + " " + obj2.getTbCaseScene().LastUpdateTime;
                    try {
                        return dfDate.parse(date_des).compareTo(dfDate.parse(date_source));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
            Log.i(TAG, "จัดเรียงตาม " + String.valueOf(wSelected));
        } else if (wSelected == 3) {
            Collections.sort(caseList, new Comparator<ApiCaseScene>() {
                @Override
                public int compare(ApiCaseScene obj1, ApiCaseScene obj2) {
                    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date_source = obj1.getTbCaseScene().LastUpdateDate + " " + obj1.getTbCaseScene().LastUpdateTime;
                    String date_des = obj2.getTbCaseScene().LastUpdateDate + " " + obj2.getTbCaseScene().LastUpdateTime;
                    try {
                        return dfDate.parse(date_source).compareTo(dfDate.parse(date_des));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
            Log.i(TAG, "จัดเรียงตาม " + String.valueOf(wSelected));
        }

        apiCaseSceneListAdapter = new ApiCaseSceneListAdapter(caseList);
        rvDraft.setAdapter(apiCaseSceneListAdapter);
        apiCaseSceneListAdapter.notifyDataSetChanged();
        apiCaseSceneListAdapter.setOnItemClickListener(onItemClickListener);
        swipeContainer.setRefreshing(false);
    }
}
