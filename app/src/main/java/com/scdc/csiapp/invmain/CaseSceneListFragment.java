package com.scdc.csiapp.invmain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.scdc.csiapp.apimodel.ApiStatus;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbUsers;

import java.util.ArrayList;
import java.util.List;

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

    RecyclerView rvDraft;
    SwipeRefreshLayout swipeContainer;
    private ApiCaseSceneListAdapter apiCaseSceneListAdapter;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewlayout = inflater.inflate(R.layout.casescene_fragment_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.home);
        mDbHelper = new DBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        context = viewlayout.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        rootLayout = (CoordinatorLayout) viewlayout.findViewById(R.id.rootLayout);
        if (WelcomeActivity.profile.getTbOfficial() != null) {
            officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;
        } else {
            Intent gotoWelcomeActivity = new Intent(mContext, WelcomeActivity.class);
            getActivity().finish();
            startActivity(gotoWelcomeActivity);
        }
        cd = new ConnectionDetector(context);
        getDateTime = new GetDateTime();

        final CSIDataTabFragment fCSIDataTabFragment = new CSIDataTabFragment();
        fabBtn = (FloatingActionButton) viewlayout.findViewById(R.id.fabBtn);
        fabBtn.setVisibility(View.GONE);

        csiDataTabFragment = new CSIDataTabFragment();
        assignTabFragment = new AssignTabFragment();
        caseList = new ArrayList<>();
        rvDraft = (RecyclerView) viewlayout.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rvDraft.setLayoutManager(llm);
        rvDraft.setHasFixedSize(true);

        swipeContainer = (SwipeRefreshLayout) viewlayout.findViewById(R.id.swipeContainer);
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
                    selectApiCaseSceneFromSQLite();

                    snackbar = Snackbar.make(rootLayout, getString(R.string.offline_mode), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();
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

//                    new ConnectlistCasescene().execute();

                    //  initializeData();
                } else {
                    swipeContainer.setRefreshing(true);
                    // ดึงค่าจาก SQLite เพราะไม่มีการต่อเน็ต
                    selectApiCaseSceneFromSQLite();

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
        });
        apiCaseSceneListAdapter = new ApiCaseSceneListAdapter(caseList);
        rvDraft.setAdapter(apiCaseSceneListAdapter);
        apiCaseSceneListAdapter.setOnItemClickListener(onItemClickListener);

        // ตรวจสอบการเชื่อมต่ออินเตอร์แล้วแยกการทำงานกัน
        if (cd.isNetworkAvailable()) {
//            new ConnectlistCasescene().execute();
            mHandler.removeCallbacks(mHandlerTaskcheckConnect);//หยุดการตรวจการเชื่อมกับเซิร์ฟเวอร์เก่า
            mHandlerTaskcheckConnect.run();//เริ่มการทำงานส่วนตรวจสอบการเชื่อมต่อเซิร์ฟเวอร์ใหม่

        } else {
            selectApiCaseSceneFromSQLite();
        }

        return viewlayout;
    }

    ApiCaseSceneListAdapter.OnItemClickListener onItemClickListener = new ApiCaseSceneListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            final ApiCaseScene apiNoticeCase = caseList.get(position);
            final String caserepID = apiNoticeCase.getTbCaseScene().getCaseReportID().toString();
            final String mode = apiNoticeCase.getMode().toString();
//            AlertDialog.Builder builder =
//                    new AlertDialog.Builder(getActivity());
//            builder.setMessage("ดูข้อมูลการตรวจนี้ " + caserepID);
//
//            builder.setPositiveButton("ดู", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {

//                }
//            });
//
//            if (apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("accept") || apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("investigating")) {
//                builder.setNeutralButton("แก้ไข", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {

//                    }
//                });
//            }
//
//
//            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.create();
//            builder.show();

            //Snackbar.make(view, "Clicked " + csidata.caseReportID, Snackbar.LENGTH_LONG)
            //       .setAction("Action", null).show();

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
                                                             Log.d(TAG, "view");
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
                                                             Log.d(TAG, "edit");
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
                                                                         snackbar = Snackbar.make(rootLayout, getString(R.string.save_error) + " " + apiNoticeCase.getTbCaseScene().CaseReportID.toString(), Snackbar.LENGTH_INDEFINITE)
                                                                                 .setAction(getString(R.string.ok), new View.OnClickListener() {
                                                                                     @Override
                                                                                     public void onClick(View view) {


                                                                                     }
                                                                                 });
                                                                         snackbar.show();
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
        dialog.setMessage("Do you want to exit?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }

        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    public void selectApiCaseSceneFromSQLite() {
        ApiListCaseScene apiListNoticeCase = mDbHelper.selectApiCaseScene(officialID);
        caseList = apiListNoticeCase.getData().getResult();
        Log.d(TAG, "Update apiNoticeCaseListAdapter SQLite");

        if (swipeContainer != null && swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        apiCaseSceneListAdapter = new ApiCaseSceneListAdapter(caseList);
        rvDraft.setAdapter(apiCaseSceneListAdapter);
        apiCaseSceneListAdapter.notifyDataSetChanged();
        apiCaseSceneListAdapter.setOnItemClickListener(onItemClickListener);
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
                Log.d(TAG, apiListCaseScene.getStatus());
                Log.d(TAG, String.valueOf(apiListCaseScene.getData().getResult().size()));

                // ข้อมูล ApiNoticeCase ที่ได้จากเซิร์ฟเวอร์
                caseList = apiListCaseScene.getData().getResult();

                // เอาข้อมูลไปแสดงใน RV
                apiCaseSceneListAdapter.notifyDataSetChanged();
                Log.d(TAG, "Update apiNoticeCaseListAdapter");

                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                apiCaseSceneListAdapter = new ApiCaseSceneListAdapter(caseList);
                rvDraft.setAdapter(apiCaseSceneListAdapter);
                apiCaseSceneListAdapter.setOnItemClickListener(onItemClickListener);
            } else {
                if (snackbar == null || !snackbar.isShown()) {
                    snackbar = Snackbar.make(rootLayout, "ดาวน์โหลดข้อมูลผิดพลาด", Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                }
                            });
                    snackbar.show();
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
                new ConnectlistCasescene().execute();
            } else {
                selectApiCaseSceneFromSQLite();
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
}
