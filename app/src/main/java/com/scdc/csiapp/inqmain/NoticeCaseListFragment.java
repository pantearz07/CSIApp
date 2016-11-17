package com.scdc.csiapp.inqmain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiListNoticeCase;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
import com.scdc.csiapp.apimodel.ApiProfile;
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ApiConnect;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.invmain.CSIDataTabFragment;
import com.scdc.csiapp.main.BusProvider;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.tablemodel.TbNoticeCase;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Pantearz07 on 14/9/2559.
 */
public class NoticeCaseListFragment extends Fragment {
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    Context context;
    //Recycle view
    private List<ApiNoticeCase> caseList;
    RecyclerView rvDraft;
    SwipeRefreshLayout swipeContainer;
    private ApiNoticeCaseListAdapter apiNoticeCaseListAdapter;
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    // connect sqlite
    DBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    //Boolean networkConnectivity = false;
    GetDateTime getDateTime;
    String officialID;
    EmergencyTabFragment emergencyTabFragment;
    CSIDataTabFragment csiDataTabFragment;
    private static final String TAG = "DEBUG-NoticeCaseListFragment";
    private static final String Bundle_Key = "noticecase";
    Snackbar snackbar;
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 20; //20 second
    public static final String KEY_PROFILE = "key_profile";
    public static final String KEY_CONNECT = "key_connect";
    ApiProfile apiProfile;
    ApiConnect api;

    public static NoticeCaseListFragment newInstance() {
        return new NoticeCaseListFragment();
    }

    public NoticeCaseListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.casescene_fragment_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.home);
        mDbHelper = new DBHelper(getActivity());
        mManager = new PreferenceData(getActivity());
        context = view.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        cd = new ConnectionDetector(context);
        //networkConnectivity = cd.isNetworkAvailable();
        getDateTime = new GetDateTime();

        emergencyTabFragment = new EmergencyTabFragment();
        csiDataTabFragment = new CSIDataTabFragment();
        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        fabBtn = (FloatingActionButton) view.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new NoticeOnClickListener());

        caseList = new ArrayList<>();
        rvDraft = (RecyclerView) view.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rvDraft.setLayoutManager(llm);
        rvDraft.setHasFixedSize(true);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    private void setAdapterList() {
        apiNoticeCaseListAdapter = new ApiNoticeCaseListAdapter(caseList);
        rvDraft.setAdapter(apiNoticeCaseListAdapter);
        apiNoticeCaseListAdapter.notifyDataSetChanged();
        apiNoticeCaseListAdapter.setOnItemClickListener(onItemClickListener);
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAdapterData();
            }
        });
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
    public void onResume() {
        super.onResume();
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            rvDraft.getLayoutManager().onRestoreInstanceState(listState);
            Log.i(TAG, "onResume mBundleRecyclerViewState");
        }
    }

    private void setAdapterData() {
        if (cd.isNetworkAvailable()) {
            Log.i("log_show draft", "Refreshing!! ");

            swipeContainer.setRefreshing(true);
            mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
            mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่

        } else {
            swipeContainer.setRefreshing(true);
            // ดึงค่าจาก SQLite เพราะไม่มีการต่อเน็ต
            selectApiNoticeCaseFromSQLite();

            Log.i("log_show draft", "fail network");
            snackbar = Snackbar.make(rootLayout, getString(R.string.offline_mode), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.show();
        }
    }

    ApiNoticeCaseListAdapter.OnItemClickListener onItemClickListener = new ApiNoticeCaseListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            final ApiNoticeCase apiNoticeCase = caseList.get(position);
            final String caserepTD = apiNoticeCase.getTbNoticeCase().getNoticeCaseID().toString();
            final String mode = apiNoticeCase.getMode().toString();

            PopupMenu popup = new PopupMenu(getActivity(), view, Gravity.RIGHT);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.csi_menu_1, popup.getMenu());
            Menu popupMenu = popup.getMenu();
            popupMenu.findItem(R.id.edit).setVisible(false);
            if (apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("receive")) {
                popupMenu.findItem(R.id.edit).setVisible(true);
            }
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                 public boolean onMenuItemClick(MenuItem item) {
                                                     final Bundle i = new Bundle();
                                                     FragmentTransaction fragmentTransaction;
                                                     switch (item.getItemId()) {
                                                         case R.id.view:
                                                             Log.d(TAG, "view");
                                                             i.putSerializable(emergencyTabFragment.Bundle_Key, apiNoticeCase.getTbNoticeCase());
                                                             i.putString(emergencyTabFragment.Bundle_mode, "view");
                                                             fragmentTransaction = mFragmentManager.beginTransaction();
                                                             emergencyTabFragment.setArguments(i);
                                                             fragmentTransaction.replace(R.id.containerView, emergencyTabFragment).addToBackStack(null).commit();
                                                             break;
                                                         case R.id.edit:
                                                             Log.d(TAG, "edit");
                                                             if (mode.equals("online")) {
                                                                 boolean isSuccess = mDbHelper.saveNoticeCase(apiNoticeCase.getTbNoticeCase());
                                                                 if (isSuccess) {
                                                                     i.putSerializable(emergencyTabFragment.Bundle_Key, apiNoticeCase.getTbNoticeCase());
                                                                     i.putString(emergencyTabFragment.Bundle_mode, "edit");
                                                                     fragmentTransaction = mFragmentManager.beginTransaction();
                                                                     emergencyTabFragment.setArguments(i);
                                                                     fragmentTransaction.replace(R.id.containerView, emergencyTabFragment).addToBackStack(null).commit();
                                                                 } else {
                                                                     if (snackbar == null || !snackbar.isShown()) {
                                                                         snackbar = Snackbar.make(rootLayout, getString(R.string.save_error), Snackbar.LENGTH_INDEFINITE)
                                                                                 .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                                                     @Override
                                                                                     public void onClick(View view) {


                                                                                     }
                                                                                 });
                                                                         snackbar.show();
                                                                     }
                                                                 }
                                                             } else {
                                                                 i.putSerializable(emergencyTabFragment.Bundle_Key, apiNoticeCase.getTbNoticeCase());
                                                                 i.putString(emergencyTabFragment.Bundle_mode, "edit");
                                                                 fragmentTransaction = mFragmentManager.beginTransaction();
                                                                 emergencyTabFragment.setArguments(i);
                                                                 fragmentTransaction.replace(R.id.containerView, emergencyTabFragment).addToBackStack(null).commit();
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
        dialog.setTitle(R.string.ad_title);
        dialog.setIcon(R.drawable.ic_noti);
        dialog.setCancelable(true);
        dialog.setMessage(R.string.ad_message);
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

    public void selectApiNoticeCaseFromSQLite() {
        ApiListNoticeCase apiListNoticeCase = mDbHelper.selectApiNoticeCase(WelcomeActivity.profile.getTbOfficial().OfficialID);
        caseList = apiListNoticeCase.getData().getResult();
        Log.d(TAG, "Update apiNoticeCaseListAdapter SQLite");

        setAdapterList();
    }

    class ConnectlistNoticecase extends AsyncTask<Void, Void, ApiListNoticeCase> {

        @Override
        protected ApiListNoticeCase doInBackground(Void... voids) {
            return WelcomeActivity.api.listNoticecase();
        }

        @Override
        protected void onPostExecute(ApiListNoticeCase apiListNoticeCase) {
            super.onPostExecute(apiListNoticeCase);
            if (apiListNoticeCase != null) {
                Log.d(TAG, apiListNoticeCase.getStatus());
                Log.d(TAG, String.valueOf(apiListNoticeCase.getData().getResult().size()));

                // ข้อมูล ApiNoticeCase ที่ได้จากเซิร์ฟเวอร์
                caseList = apiListNoticeCase.getData().getResult();
                //caseList.get(2).setMode("online");

                Collections.sort(caseList, new Comparator<ApiNoticeCase>() {
                    @Override
                    public int compare(ApiNoticeCase obj1, ApiNoticeCase obj2) {
                        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date_source = obj1.getTbNoticeCase().ReceivingCaseDate + " " + obj1.getTbNoticeCase().ReceivingCaseTime;
                        String date_des = obj2.getTbNoticeCase().ReceivingCaseDate + " " + obj2.getTbNoticeCase().ReceivingCaseTime;
                        try {
                            return dfDate.parse(date_des).compareTo(dfDate.parse(date_source));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                setAdapterList();
            } else {
                selectApiNoticeCaseFromSQLite();
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
                new ConnectlistNoticecase().execute();
            } else {
                selectApiNoticeCaseFromSQLite();
//                if (snackbar == null || !snackbar.isShown()) {
                snackbar = Snackbar.make(rootLayout, getString(R.string.cannot_connect_server_offline), Snackbar.LENGTH_INDEFINITE)
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

    private class NoticeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view1 = inflater.inflate(R.layout.dialog_add_case, null);
            dialog.setView(view1);

            final Spinner spnCaseType = (Spinner) view1.findViewById(R.id.spnCaseType);
            final Spinner spnSubCaseType = (Spinner) view1.findViewById(R.id.spnSubCaseType);
            //ดึงค่าจาก TbCaseSceneType
            final String mCaseTypeArray[][] = mDbHelper.SelectCaseType();
            if (mCaseTypeArray != null) {
                String[] mCaseTypeArray2 = new String[mCaseTypeArray.length];
                for (int i = 0; i < mCaseTypeArray.length; i++) {
                    mCaseTypeArray2[i] = mCaseTypeArray[i][1];
                    Log.i(TAG + " show mCaseTypeArray", mCaseTypeArray2[i].toString());
                }
                ArrayAdapter<String> adapterTypeCase = new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_dropdown_item_1line,
                        mCaseTypeArray2);
                spnCaseType.setAdapter(adapterTypeCase);
            } else {
                Log.i(TAG + " show mCaseTypeArray", "null");
            }
            final String[] selectedCaseType = new String[1];
            final String[] selectedSubCaseType = new String[1];
            spnCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCaseType[0] = mCaseTypeArray[position][0];
                    Log.i(TAG + " show mCaseTypeArray", selectedCaseType[0]);
                    //String mSubCaseTypeArray[][]= mDbHelper.SelectSubCaseTypeByCaseType(selectedCaseType[0]);
                    //ดึงค่าจาก TbSubCaseSceneType
                    final String mSubCaseTypeArray[][] = mDbHelper.SelectSubCaseTypeByCaseType(selectedCaseType[0]);
                    if (mSubCaseTypeArray != null) {
                        String[] mSubCaseTypeArray2 = new String[mSubCaseTypeArray.length];
                        for (int i = 0; i < mSubCaseTypeArray.length; i++) {
                            mSubCaseTypeArray2[i] = mSubCaseTypeArray[i][2];
                            Log.i(TAG + " show mSubCaseTypeArray2", mSubCaseTypeArray2[i].toString());
                        }
                        ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_dropdown_item_1line, mSubCaseTypeArray2);
                        spnSubCaseType.setAdapter(adapterSubCaseType);
                    } else {
                        spnSubCaseType.setAdapter(null);
                        selectedSubCaseType[0] = null;
                        Log.i(TAG + " show mSubCaseTypeArray", String.valueOf(selectedSubCaseType[0]));
                    }

                    spnSubCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedSubCaseType[0] = mSubCaseTypeArray[position][0];
                            Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType[0]);
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                            selectedSubCaseType[0] = mSubCaseTypeArray[0][0];
                            Log.i(TAG + " show mSubCaseTypeArray", selectedSubCaseType[0]);
                        }
                    });
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    selectedCaseType[0] = mCaseTypeArray[0][0];
                    Log.i(TAG + " show mCaseTypeArray", selectedCaseType[0]);
                    //String mSubCaseTypeArray[][]= mDbHelper.SelectSubCaseTypeByCaseType(selectedCaseType[0]);
                }
            });

            dialog.setTitle("เพิ่มข้อมูลการตรวจสถานที่เกิดเหตุ");
            dialog.setIcon(R.drawable.ic_noti);
            dialog.setCancelable(true);
            // Current Date
            final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
            final String saveDataTime = dateTimeCurrent[0] + dateTimeCurrent[1] + dateTimeCurrent[2] + dateTimeCurrent[3] + dateTimeCurrent[4] + dateTimeCurrent[5];


            dialog.setPositiveButton("ถัดไป", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    String NoticeCaseID = "MC_" + saveDataTime;
                    TbNoticeCase tbNoticeCase = new TbNoticeCase();
                    tbNoticeCase.NoticeCaseID = NoticeCaseID;
                    tbNoticeCase.Mobile_CaseID = NoticeCaseID;
                    tbNoticeCase.InquiryOfficialID = officialID;
                    tbNoticeCase.PoliceStationID = WelcomeActivity.profile.getTbOfficial().PoliceStationID;
                    tbNoticeCase.InvestigatorOfficialID = null;
                    tbNoticeCase.SCDCAgencyCode = null;
                    tbNoticeCase.CaseTypeID = selectedCaseType[0];
                    tbNoticeCase.SubCaseTypeID = selectedSubCaseType[0];
                    tbNoticeCase.CaseStatus = "receive";
                    tbNoticeCase.ReceivingCaseDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                    tbNoticeCase.ReceivingCaseTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];
                    tbNoticeCase.DISTRICT_ID = null;
                    tbNoticeCase.AMPHUR_ID = null;
                    tbNoticeCase.PROVINCE_ID = null;
                    tbNoticeCase.LastUpdateDate = dateTimeCurrent[0] + "-" + dateTimeCurrent[1] + "-" + dateTimeCurrent[2];
                    tbNoticeCase.LastUpdateTime = dateTimeCurrent[3] + ":" + dateTimeCurrent[4] + ":" + dateTimeCurrent[5];

                    if (tbNoticeCase != null) {
//                            boolean isSuccess = mDbHelper.saveNoticeCase(tbNoticeCase);
//                            if (isSuccess) {
                        Bundle i = new Bundle();
                        i.putSerializable(Bundle_Key, tbNoticeCase);

                        i.putString(emergencyTabFragment.Bundle_mode, "new");
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        emergencyTabFragment.setArguments(i);
                        fragmentTransaction.replace(R.id.containerView, emergencyTabFragment).addToBackStack(null).commit();
                        //  fragmentTransaction.replace(R.id.containerView, csiDataTabFragment).addToBackStack(null).commit();
//                            } else {
//                                Toast.makeText(getActivity(), R.string.save_complete, Toast.LENGTH_LONG).show();
//                            }
                    }


                }

            });

            dialog.setCancelable(true);
            dialog.setNeutralButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.create();
            dialog.show();
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
            WelcomeActivity.api = new ApiConnect(getActivity());
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
}