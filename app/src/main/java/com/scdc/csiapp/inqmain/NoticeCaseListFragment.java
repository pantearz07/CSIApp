package com.scdc.csiapp.inqmain;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiListNoticeCase;
import com.scdc.csiapp.apimodel.ApiNoticeCase;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.invmain.CSIDataTabFragment;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.main.WelcomeActivity;
import com.scdc.csiapp.tablemodel.TbNoticeCase;

import java.util.ArrayList;
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
    // connect sqlite
    DBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    Boolean networkConnectivity = false;
    GetDateTime getDateTime;
    String officialID;
    EmergencyTabFragment emergencyTabFragment;
    CSIDataTabFragment csiDataTabFragment;
    private static final String TAG = "DEBUG-NoticeCaseListFragment";
    private static final String Bundle_Key = "noticecase";
    Snackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.casescene_fragment_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.home);
        mDbHelper = new DBHelper(getActivity());
        mManager = new PreferenceData(getActivity());
        context = view.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        rootLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);
        officialID = WelcomeActivity.profile.getTbOfficial().OfficialID;

        cd = new ConnectionDetector(context);
        networkConnectivity = cd.isNetworkAvailable();
        getDateTime = new GetDateTime();

        emergencyTabFragment = new EmergencyTabFragment();
        csiDataTabFragment = new CSIDataTabFragment();
        fabBtn = (FloatingActionButton) view.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
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
                final String saveDataTime = dateTimeCurrent[0] + dateTimeCurrent[1] + dateTimeCurrent[2] + dateTimeCurrent[3] + dateTimeCurrent[4];


                dialog.setPositiveButton("ถัดไป", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        String NoticeCaseID = "MC_" + saveDataTime;
                        TbNoticeCase tbNoticeCase = new TbNoticeCase();
                        tbNoticeCase.NoticeCaseID = NoticeCaseID;
                        tbNoticeCase.Mobile_CaseID = null;
                        tbNoticeCase.InquiryOfficialID = officialID;
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

                dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                dialog.create();
                dialog.show();
            }
        });

        caseList = new ArrayList<>();
        rvDraft = (RecyclerView) view.findViewById(R.id.rvDraft);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rvDraft.setLayoutManager(llm);
        rvDraft.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                if (networkConnectivity) {
                    Log.i("log_show draft", "Refreshing!! ");

                    swipeContainer.setRefreshing(true);
                    new ConnectlistNoticecase().execute();

                } else {
                    swipeContainer.setRefreshing(true);
                    // ดึงค่าจาก SQLite เพราะไม่มีการต่อเน็ต
                    selectApiNoticeCaseFromSQLite();

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
                    new ConnectlistNoticecase().execute();
                    //  initializeData();
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
        });
        apiNoticeCaseListAdapter = new ApiNoticeCaseListAdapter(caseList);
        rvDraft.setAdapter(apiNoticeCaseListAdapter);
        apiNoticeCaseListAdapter.setOnItemClickListener(onItemClickListener);

        // ตรวจสอบการเชื่อมต่ออินเตอร์แล้วแยกการทำงานกัน
        if (networkConnectivity) {
            new ConnectlistNoticecase().execute();
        } else {
            selectApiNoticeCaseFromSQLite();
        }

        return view;
    }

    ApiNoticeCaseListAdapter.OnItemClickListener onItemClickListener = new ApiNoticeCaseListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            final ApiNoticeCase apiNoticeCase = caseList.get(position);
            final String caserepTD = apiNoticeCase.getTbNoticeCase().getNoticeCaseID().toString();

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setMessage("ดูข้อมูลการตรวจนี้ " + caserepTD);

            builder.setPositiveButton("ดู", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Bundle i = new Bundle();
                    i.putSerializable(emergencyTabFragment.Bundle_Key, apiNoticeCase.getTbNoticeCase());
                    i.putString(emergencyTabFragment.Bundle_mode, "view");
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    emergencyTabFragment.setArguments(i);
                    fragmentTransaction.replace(R.id.containerView, emergencyTabFragment).addToBackStack(null).commit();
                }
            });

            if(apiNoticeCase.getTbNoticeCase().CaseStatus.equalsIgnoreCase("receive")) {
                builder.setNeutralButton("แก้ไข", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle i = new Bundle();
                        i.putSerializable(emergencyTabFragment.Bundle_Key, apiNoticeCase.getTbNoticeCase());
                        i.putString(emergencyTabFragment.Bundle_mode, "edit");
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        emergencyTabFragment.setArguments(i);
                        fragmentTransaction.replace(R.id.containerView, emergencyTabFragment).addToBackStack(null).commit();
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

    public void selectApiNoticeCaseFromSQLite() {
        ApiListNoticeCase apiListNoticeCase = mDbHelper.selectApiNoticeCase(WelcomeActivity.profile.getTbOfficial().OfficialID);
        caseList = apiListNoticeCase.getData().getResult();
        Log.d(TAG, "Update apiNoticeCaseListAdapter SQLite");

        if (swipeContainer != null && swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        apiNoticeCaseListAdapter = new ApiNoticeCaseListAdapter(caseList);
        rvDraft.setAdapter(apiNoticeCaseListAdapter);
        apiNoticeCaseListAdapter.notifyDataSetChanged();
        apiNoticeCaseListAdapter.setOnItemClickListener(onItemClickListener);
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

                // เพิ่มข้อมูลที่ได้มาลง SQLite ด้วย syncNoticeCase
                int size = caseList.size();
                List<TbNoticeCase> tbNoticeCases = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    tbNoticeCases.add(caseList.get(i).getTbNoticeCase());
                }
                mDbHelper.syncNoticeCase(tbNoticeCases);

                // เอาข้อมูลไปแสดงใน RV
                apiNoticeCaseListAdapter.notifyDataSetChanged();
                Log.d(TAG, "Update apiNoticeCaseListAdapter");

                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                apiNoticeCaseListAdapter = new ApiNoticeCaseListAdapter(caseList);
                rvDraft.setAdapter(apiNoticeCaseListAdapter);
                apiNoticeCaseListAdapter.setOnItemClickListener(onItemClickListener);
            }
        }
    }
}