package com.scdc.csiapp.invmain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

    public static CaseSceneListFragment newInstance() {
        return new CaseSceneListFragment();
    }

    public CaseSceneListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
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
        setAdapterList();
    }

    private void setAdapterList() {
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
            final ApiCaseScene apiNoticeCase = caseList.get(position);
            final String caserepID = apiNoticeCase.getTbCaseScene().getCaseReportID().toString();
            final String mode = apiNoticeCase.getMode().toString();

            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(getActivity(), view, Gravity.RIGHT);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.csi_menu_1, popup.getMenu());
            Menu popupMenu = popup.getMenu();
            popupMenu.findItem(R.id.edit).setVisible(false);
            if (apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("accept") || apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("investigating")) {
                popupMenu.findItem(R.id.edit).setVisible(true);
            }
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                 public boolean onMenuItemClick(MenuItem item) {
                                                     final Bundle i = new Bundle();

                                                     switch (item.getItemId()) {
                                                         case R.id.view:
//                                                             Log.d(TAG, "view");
//
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
                                                             break;
                                                         case R.id.edit:
//                                                             Log.d(TAG, "edit");
                                                             if (mode.equals("online")) {

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
                                                             break;
                                                     }
                                                     return true;
                                                 }
                                             }
            );
            popup.show();
        }
    };

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
            return WelcomeActivity.api.listCasescene();
        }

        @Override
        protected void onPostExecute(ApiListCaseScene apiListCaseScene) {
            super.onPostExecute(apiListCaseScene);
            if (apiListCaseScene != null) {
//                Log.d(TAG, apiListCaseScene.getStatus());
//                Log.d(TAG, String.valueOf(apiListCaseScene.getData().getResult().size()));

                // ข้อมูล ApiNoticeCase ที่ได้จากเซิร์ฟเวอร์
                caseList = apiListCaseScene.getData().getResult();
                //sort caselist

                Collections.sort(caseList, new Comparator<ApiCaseScene>() {
                    @Override
                    public int compare(ApiCaseScene obj1, ApiCaseScene obj2) {
                        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date_source = obj1.getTbCaseScene().ReceivingCaseDate + " " + getDateTime.formatTime(obj1.getTbCaseScene().ReceivingCaseTime);
                        String date_des = obj2.getTbCaseScene().ReceivingCaseDate + " " + getDateTime.formatTime(obj2.getTbCaseScene().ReceivingCaseTime);
                        try {
//                            Log.i("Compare" , String.valueOf(dfDate.parse(date_source).compareTo(dfDate.parse(date_des))));
                            return dfDate.parse(date_des).compareTo(dfDate.parse(date_source));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                setAdapterList();
            } else {
                if (snackbar == null || !snackbar.isShown()) {
                    SnackBarAlert snackBarAlert = new SnackBarAlert(snackbar, rootLayout, LENGTH_INDEFINITE,
                            "ดาวน์โหลดข้อมูลผิดพลาด");
                    snackBarAlert.createSnacbar();
                }
                selectApiCaseSceneFromSQLite();

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
                try {
                    new ConnectlistCasescene().execute();

                } catch (RuntimeException e) {
                    Intent gotoWelcomeActivity = new Intent(context, WelcomeActivity.class);
                    getActivity().finish();
                    startActivity(gotoWelcomeActivity);
                }
            } else {
                selectApiCaseSceneFromSQLite();
//                if (snackbar == null || !snackbar.isShown()) {
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
                                final EditText ipvalueEdt = (EditText) view.findViewById(R.id.ipvalueEdt);

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
//                }
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

}
