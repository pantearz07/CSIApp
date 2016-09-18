package com.scdc.csiapp.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.connecting.SyncData;
import com.scdc.csiapp.connecting.SyncDataAddress;
import com.scdc.csiapp.connecting.SyncDataPolice;

/**
 * Created by Pantearz07 on 22/9/2558.
 */
public class SettingFragment extends Fragment {

    CoordinatorLayout rootLayoutSetting;
    private ListView listMenuSetting;

    SQLiteDatabase mDb;
    SQLiteDBHelper mDbHelper;
    private Context mContext;
    private PreferenceData mManager;
    ConnectionDetector cd;
    private static final String TAG = "DEBUG-SettingFragment";
    //ProgressDialog progressBar;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private static final String[] MenuSetting = new String[]{
            "ตั้งค่า IP ใหม่", "อัพเดตข้อมูลทั้งหมด", "อัพเดตข้อมูลพื้นฐานสำหรับตำรวจ", "อัพเดตข้อมูลพื้นฐานสำหรับที่อยู่"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setting_layout, null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings);

        rootLayoutSetting = (CoordinatorLayout) view.findViewById(R.id.rootLayoutSetting);
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        mManager = new PreferenceData(getActivity());
        cd = new ConnectionDetector(getActivity());

        listMenuSetting = (ListView) view.findViewById(R.id.listSetting);
        listMenuSetting.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, MenuSetting));
        listMenuSetting.setTextFilterEnabled(true);
        listMenuSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, final int position,
                                    long id) {
                if (cd.isNetworkAvailable()) {
                    if (position == 0) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();

                        View view = inflater.inflate(R.layout.ipsetting_dialog, null);
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
                    } else if (position == 1) {
                        Log.i(TAG, "saveAllData");
                        // ดึงข้อมูลอัพเดทจากเซิร์ฟเวอร์
                        SyncData syncData = new SyncData();
                        syncData.execute();
                    } else if (position == 2) {

                        Log.i(TAG, "savePoliceData");
                        SyncDataPolice syncDataPolice = new SyncDataPolice();
                        syncDataPolice.execute();
                    } else if (position == 3) {
                        Log.i(TAG, "saveAddressData");
                        SyncDataAddress syncDataAddress = new SyncDataAddress();
                        syncDataAddress.execute();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.network_unavailable,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }


}
