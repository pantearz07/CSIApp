package com.scdc.csiapp.invmain;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiCaseScene;
import com.scdc.csiapp.apimodel.ApiListCaseScene;
import com.scdc.csiapp.apimodel.ApiListNoticeCase;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.inqmain.ApiNoticeCaseListAdapter;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;

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
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    Snackbar snackbar;

    GetDateTime getDateTime;
    String officialID;
    private static final String TAG = "DEBUG-CaseSceneListFragment";

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
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        cd = new ConnectionDetector(context);
        networkConnectivity = cd.isNetworkAvailable();
        networkConnectivity = false;
        getDateTime = new GetDateTime();

        final CSIDataTabFragment fCSIDataTabFragment = new CSIDataTabFragment();
        fabBtn = (FloatingActionButton) viewlayout.findViewById(R.id.fabBtn);
        fabBtn.setVisibility(View.GONE);

        csiDataTabFragment = new CSIDataTabFragment();

        caseList = new ArrayList<>();
        rvDraft = (RecyclerView) viewlayout.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rvDraft.setLayoutManager(llm);
        rvDraft.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) viewlayout.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                if (networkConnectivity) {
                    Log.i("log_show draft", "Refreshing!! ");

                    swipeContainer.setRefreshing(true);
                    new ConnectlistCasescene().execute();

                } else {
                    swipeContainer.setRefreshing(true);
                    // ดึงค่าจาก SQLite เพราะไม่มีการต่อเน็ต
                    //selectApiNoticeCaseFromSQLite();

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

                if (networkConnectivity) {
                    Log.i("log_show draft", "Refreshing!! ");

                    swipeContainer.setRefreshing(true);
                    new ConnectlistCasescene().execute();
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
        if (networkConnectivity) {
            new ConnectlistCasescene().execute();
        } else {
            selectApiCaseSceneFromSQLite();
        }

        return viewlayout;
    }

    ApiCaseSceneListAdapter.OnItemClickListener onItemClickListener = new ApiCaseSceneListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            final ApiCaseScene apiNoticeCase = caseList.get(position);
            final String caserepTD = apiNoticeCase.getTbNoticeCase().getNoticeCaseID().toString();

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setMessage("ดูข้อมูลการตรวจนี้ " + caserepTD);

            builder.setPositiveButton("ดู", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Bundle i = new Bundle();
                    i.putSerializable(csiDataTabFragment.Bundle_Key, apiNoticeCase.getTbNoticeCase());
                    i.putString(csiDataTabFragment.Bundle_mode, "view");
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    csiDataTabFragment.setArguments(i);
                    fragmentTransaction.replace(R.id.containerView, csiDataTabFragment).addToBackStack(null).commit();
                }
            });

            if (apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("accept") || apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("investigating")) {
                builder.setNeutralButton("แก้ไข", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle i = new Bundle();
                        i.putSerializable(csiDataTabFragment.Bundle_Key, apiNoticeCase.getTbNoticeCase());
                        i.putString(csiDataTabFragment.Bundle_mode, "edit");
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        csiDataTabFragment.setArguments(i);
                        fragmentTransaction.replace(R.id.containerView, csiDataTabFragment).addToBackStack(null).commit();
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

            //Snackbar.make(view, "Clicked " + csidata.caseReportID, Snackbar.LENGTH_LONG)
            //       .setAction("Action", null).show();


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
        ApiListCaseScene apiListNoticeCase = mDbHelper.selectApiCaseScene(WelcomeActivity.profile.getTbOfficial().OfficialID);
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

                // เพิ่มข้อมูลที่ได้มาลง SQLite ด้วย syncNoticeCase
//                int size = caseList.size();
//                List<TbCaseScene> tbNoticeCases = new ArrayList<>(size);
//                for (int i = 0; i < size; i++) {
//                    tbNoticeCases.add(caseList.get(i).getTbCaseScene());
//                }
//                mDbHelper.syncNoticeCase(tbNoticeCases);

                // เอาข้อมูลไปแสดงใน RV
                apiCaseSceneListAdapter.notifyDataSetChanged();
                Log.d(TAG, "Update apiNoticeCaseListAdapter");

                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                apiCaseSceneListAdapter = new ApiCaseSceneListAdapter(caseList);
                rvDraft.setAdapter(apiCaseSceneListAdapter);
                apiCaseSceneListAdapter.setOnItemClickListener(onItemClickListener);
            }
        }
    }
}
