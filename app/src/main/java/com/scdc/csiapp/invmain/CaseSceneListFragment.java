package com.scdc.csiapp.invmain;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;

        import com.scdc.csiapp.R;
        import com.scdc.csiapp.connecting.ConnectionDetector;
        import com.scdc.csiapp.connecting.PreferenceData;
        import com.scdc.csiapp.connecting.SQLiteDBHelper;
        import com.scdc.csiapp.main.CSIDataTabFragment;
        import com.scdc.csiapp.main.GetDateTime;

/**
 * Created by Pantearz07 on 14/9/2559.
 */
public class CaseSceneListFragment extends Fragment {
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    FragmentManager mFragmentManager;
    Context context;
    // connect sqlite
    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    long isConnectingToInternet = 0;
    GetDateTime getDateTime;
    String officialID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x = inflater.inflate(R.layout.home_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.home);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        context = x.getContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        rootLayout = (CoordinatorLayout) x.findViewById(R.id.rootLayoutHome);
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);

        //isConnectingToInternet = cd.isConnectingToInternet();
        getDateTime = new GetDateTime();

        final CSIDataTabFragment fCSIDataTabFragment = new CSIDataTabFragment();

        fabBtn = (FloatingActionButton) x.findViewById(R.id.fabBtnHome);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View view1 = inflater.inflate(R.layout.dialog_add_case, null);
                dialog.setView(view1);

                final EditText edtReportNo = (EditText) view1.findViewById(R.id.edtReportNo);
                final Spinner spnCaseType = (Spinner) view1.findViewById(R.id.spnCaseType);
                final String[] CaseType = getResources().getStringArray(R.array.casetype);
                ArrayAdapter<String> adapterCaseType = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, CaseType);
                spnCaseType.setAdapter(adapterCaseType);
                final String[] selectedCaseType = new String[1];
                spnCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedCaseType[0] = CaseType[position];

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedCaseType[0] = CaseType[0];
                    }
                });
                /*final String mCaseTypeArray[][] = mDbHelper.SelectCaseType();
                if (mCaseTypeArray != null) {
                    String[] mCaseTypeArray2 = new String[mCaseTypeArray.length];
                    for (int i = 0; i < mCaseTypeArray.length; i++) {
                        mCaseTypeArray2[i] = mCaseTypeArray[i][1];
                        Log.i("show mCaseTypeArray", mCaseTypeArray2[i].toString());
                    }
                    ArrayAdapter<String> adapterTypeCase = new ArrayAdapter<String>(
                            getActivity(), android.R.layout.simple_dropdown_item_1line,
                            mCaseTypeArray2);
                    spnCaseType.setAdapter(adapterTypeCase);
                }else{
                    Log.i("show mCaseTypeArray", "null");
                }*/
                final Spinner spnSubCaseType = (Spinner) view1.findViewById(R.id.spnSubCaseType);
                //final String mSubCaseTypeArray[][] = mDbHelper.SelectSubCaseType();
                final String[] SubCaseType = getResources().getStringArray(R.array.subcasetypeproperties);
                ArrayAdapter<String> adapterSubCaseType = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, SubCaseType);
                spnSubCaseType.setAdapter(adapterSubCaseType);
                final String[] selectedSubCaseType = new String[1];
                spnSubCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSubCaseType[0] = SubCaseType[position];

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedSubCaseType[0] =SubCaseType[0];
                    }
                });

                final TextView edtYear = (TextView) view1.findViewById(R.id.edtYear);
                dialog.setTitle("เพิ่มข้อมูลการตรวจสถานที่เกิดเหตุ");
                dialog.setIcon(R.drawable.ic_noti);
                dialog.setCancelable(true);
// Current Date
               /* Calendar calendar = Calendar.getInstance();
                int mHour = calendar.get(Calendar.HOUR);
                int mMinute = calendar.get(Calendar.MINUTE);
                int mYear = calendar.get(Calendar.YEAR);*/
                final String dateTimeCurrent[] = getDateTime.getDateTimeCurrent();
                final String reportYear = dateTimeCurrent[0];
                final String saveDataTime = dateTimeCurrent[2] + dateTimeCurrent[1] + dateTimeCurrent[0]+"_"+dateTimeCurrent[3] + dateTimeCurrent[4] + dateTimeCurrent[5];

                edtYear.setText(" / "+reportYear);
                dialog.setPositiveButton("ถัดไป", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String reportNo = null;
                        if (edtReportNo.getText().toString().equals("")) {
                            reportNo = "";
                        } else {
                            reportNo = edtReportNo.getText().toString();
                        }

                        String reportID = "RC_" + saveDataTime ;

                        // save new Report
                        long saveStatus1 = mDbHelper.saveReportID(
                                reportID, reportNo, officialID, reportYear,selectedCaseType[0],selectedSubCaseType[0], "investigating");

                        if (saveStatus1 <= 0) {
                            Log.i("save report", "Error!! ");
                        } else {
                            Log.i("save report", reportID + " " + reportNo
                                    + " " + officialID + " " + reportYear);
                        }
                        //save preference reportID
                        mManager.setPreferenceData(mManager.PREF_REPORTID, reportID);


                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, fCSIDataTabFragment).addToBackStack(null).commit();


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

        return x;
    }

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
}
