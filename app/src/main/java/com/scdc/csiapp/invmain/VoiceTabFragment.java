package com.scdc.csiapp.invmain;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.apimodel.ApiMultimedia;
import com.scdc.csiapp.connecting.ConnectionDetector;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pantearz07 on 14/3/2559.
 */
public class VoiceTabFragment extends Fragment {
    private static final String TAG = "DEBUG-VoiceTabFragment";
    ViewGroup viewByIdaddVoice;
    // record
    private MediaRecorder recorder = null;
    // play
    private MediaPlayer mMedia;
    private Handler handler = new Handler();
    private SeekBar seekBar;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    SQLiteDBHelper mDbHelper;
    DBHelper dbHelper;
    SQLiteDatabase mDb;
    ListView listViewVoice;
    Uri uri;
    private PreferenceData mManager;
    String caseReportID, sVoiceRecID, timeStamp;
    TextView txtVoiceNum;
    GetDateTime getDateTime;
    List<TbMultimediaFile> tbMultimediaFileList = null;
    Context mContext;
    private static String strSDCardPathName_Voi = "/CSIFiles/";
    String defaultIP = "180.183.251.32/mcsi";
    ConnectionDetector cd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        caseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
        dbHelper = new DBHelper(getActivity());
        View viewVoiceTab = inflater.inflate(R.layout.voice_tab_layout, container, false);
        mContext = viewVoiceTab.getContext();
        cd = new ConnectionDetector(getActivity());
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);


        if (mMedia != null) {
            mMedia.release();
        }
        listViewVoice = (ListView) viewVoiceTab.findViewById(R.id.gridViewVoiceRecord);
        //gridViewVoiceRecord = (GridView) viewVoiceTab.findViewById(R.id.gridViewVoiceRecord);
        txtVoiceNum = (TextView) viewVoiceTab.findViewById(R.id.txtVoiceNum);
        rootLayout = (CoordinatorLayout) viewVoiceTab.findViewById(R.id.rootLayout);

        fabBtn = (FloatingActionButton) viewVoiceTab.findViewById(R.id.fabBtn);

        fabBtn.setOnClickListener(new VoiceOnClickListener());

        if (CSIDataTabFragment.mode == "view") {

            CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            p.setAnchorId(View.NO_ID);
            p.width = 0;
            p.height = 0;
            fabBtn.setLayoutParams(p);
            fabBtn.hide();
        }


        return viewVoiceTab;

    }

    public void showMediaDialog() {
        // TODO Auto-generated method stub
        final AlertDialog.Builder addDialog = new AlertDialog.Builder(
                getActivity());
        final LayoutInflater inflaterDialog = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View Viewlayout = inflaterDialog.inflate(R.layout.add_media_dialog, viewByIdaddVoice);
        addDialog.setIcon(R.drawable.ic_record);
        addDialog.setTitle("เพิ่มไฟล์บันทึกเสียง");
        addDialog.setView(Viewlayout);

        String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
        sVoiceRecID = "VOI_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5];
        final String timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];


        final TextView editMediaName = (TextView) Viewlayout
                .findViewById(R.id.editMediaName);
        editMediaName.setText(sVoiceRecID);

        final EditText editMediaDescription = (EditText) Viewlayout
                .findViewById(R.id.editMediaDescription);
        editMediaDescription.setHint("คำอธิบายไฟล์เสียง");

        // Button OK
        addDialog.setPositiveButton(getString(R.string.save),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        String sMediaName, sMediaDescription;
                        sMediaName = editMediaName.getText().toString();
                        if (editMediaDescription.getText().toString()
                                .equals("")) {
                            sMediaDescription = "";
                        } else {
                            sMediaDescription = editMediaDescription.getText()
                                    .toString();
                        }
                        Log.i("show", "Voice " + sMediaName + " "
                                + sMediaDescription);

                        showVoiceRecorderDialog(caseReportID, sMediaName, sMediaDescription, timeStamp);

                    }

                })

                // Button Cancel
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        addDialog.create();
        addDialog.show();

    }

    public void showVoiceRecorderDialog(String sReportID, String sVoiceID,
                                        String sVoiceDescription, String timeStamp) {
        // TODO Auto-generated method stub
        // custom dialog
        final String stimeStamp = timeStamp;
        final String sReportID1 = sReportID;
        final String sVoiceID1 = sVoiceID;
        final String sVoiceDescription1 = sVoiceDescription;
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.voicerecord_dialog);
        dialog.setTitle("บันทึกเสียง");
        final Chronometer myChronometer = (Chronometer) dialog.findViewById(R.id.chronometer);
        // set the custom dialog components - text, image and
        // button
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("ชื่อไฟล์ : " + sVoiceID1 + "\n\nคำอธิบาย : "
                + sVoiceDescription1);
//        final long[] timeWhenStopped = {0};
//        final boolean[] recordstatus = {true};
        final ImageButton btnStart = (ImageButton) dialog.findViewById(R.id.btnStart);
        final ImageButton btnStop = (ImageButton) dialog.findViewById(R.id.btnStop);
        final ImageButton btnPause = (ImageButton) dialog.findViewById(R.id.btnPause);

        final ImageButton btnDelete = (ImageButton) dialog.findViewById(R.id.btnDelete);
        btnPause.setVisibility(View.GONE);
        btnStop.setEnabled(false);

        // if button is clicked, close the custom dialog
        btnStart.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                myChronometer.setBase(SystemClock.elapsedRealtime());
                myChronometer.start();
                btnStop.setEnabled(true);
                btnStart.setEnabled(false);
                startRecording(sVoiceID1);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Start Recording", Toast.LENGTH_SHORT).show();
//                if (recordstatus[0] == true) {
//                    myChronometer.setBase(SystemClock.elapsedRealtime());
//                    myChronometer.start();
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "Start Recording", Toast.LENGTH_SHORT).show();
//                    Log.i("show", "Voice 1");
//                    btnStop.setEnabled(true);
//                    btnStart.setVisibility(View.GONE);
//                    btnPause.setVisibility(View.VISIBLE);
//                    startRecording(sVoiceID1);
//                }else{
//                    myChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped[0]);
//                    myChronometer.start();
//                    btnStart.setVisibility(View.GONE);
//                    btnPause.setVisibility(View.VISIBLE);
//                    recordstatus[0] = true;
//                }
            }
        });
//        btnPause.setOnClickListener(new ImageButton.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (recordstatus[0] == true) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        pauseRecording();
//                    }
//                    timeWhenStopped[0] = myChronometer.getBase() - SystemClock.elapsedRealtime();
//                    myChronometer.stop();
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            "Pause Recording", Toast.LENGTH_SHORT).show();
//                    btnPause.setVisibility(View.GONE);
//                    btnStart.setVisibility(View.VISIBLE);
//                    recordstatus[0] = false;
//                }
//            }
//        });
        // if button is clicked, close the custom dialog
        btnStop.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                myChronometer.stop();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Stop Recording", Toast.LENGTH_SHORT).show();
                stopRecording();
                List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
                ApiMultimedia apiMultimedia = new ApiMultimedia();
                TbMultimediaFile tbMultimediaFile = new TbMultimediaFile();
                tbMultimediaFile.CaseReportID = caseReportID;
                tbMultimediaFile.FileID = sVoiceID1;
                tbMultimediaFile.FileDescription = sVoiceDescription1;
                tbMultimediaFile.FileType = "voice";
                tbMultimediaFile.FilePath = sVoiceID1 + ".3gp";
                tbMultimediaFile.Timestamp = stimeStamp;
                apiMultimedia.setTbMultimediaFile(tbMultimediaFile);

                apiMultimediaList.add(apiMultimedia);
                CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
//                CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);
                Log.i(TAG, "apiMultimediaList " + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                if (isSuccess) {
                    Log.i(TAG, "voice saved to Gallery!" + ResultTabFragment.strSDCardPathName + "Voice/" + " : " + sVoiceID1 + ".3gp");
                    showListVoiceRecord();
                }
                dialog.dismiss();

            }
        });

        dialog.show();
    }


    private void startRecording(String sVoiceID1) {
        // TODO Auto-generated method stub
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename(sVoiceID1));
        Log.i("show Filename", getFilename(sVoiceID1));
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @TargetApi(Build.VERSION_CODES.N)
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void pauseRecording() {
//        if (null != recorder) {
//            try {
//                recorder.pause();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.N)
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void resumeRecording() {
//        if (null != recorder) {
//            recorder.resume();
//        }
//    }

    private void stopRecording() {
        if (null != recorder) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    private String getFilename(String sVoiceID1) {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), strSDCardPathName_Voi);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getPath() + "/" + sVoiceID1 + ".3gp";
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
                    .show();
        }
    };

    public void showListVoiceRecord() {
        // TODO Auto-generated method stub
        tbMultimediaFileList = new ArrayList<>();
        if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
            Log.i(TAG, "view online tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            if (cd.isNetworkAvailable()) {
                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().CaseReportID.equals(CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID)) {
                        if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileType.equals("voice")) {
                            tbMultimediaFileList.add(CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile());
                        }
                    }
                }
                Log.i(TAG, "tbMultimediaFileList " + String.valueOf(tbMultimediaFileList.size()));
            } else {
                tbMultimediaFileList = dbHelper.selectedMediafiles(caseReportID, "voice");

            }
        } else {
            tbMultimediaFileList = dbHelper.selectedMediafiles(caseReportID, "voice");

            Log.i(TAG, "tbMultimediaFileList voice offline " + String.valueOf(tbMultimediaFileList.size()));
        }
        if (tbMultimediaFileList != null) {

            txtVoiceNum.setText(String.valueOf(tbMultimediaFileList.size()));

            listViewVoice.setVisibility(View.VISIBLE);

            listViewVoice.setAdapter(new VoiceRecordAdapter(
                    getActivity()));
            // OnClick
            listViewVoice.setOnItemClickListener(new MyOnItemClickListener());
        } else {
            txtVoiceNum.setText(String.valueOf(0));

            listViewVoice.setVisibility(View.GONE);
            Log.i("Recieve", "Null!! ");

        }
    }

    public void showPlayDialog(final String filepath, final String sVoiceID) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.voiceplay_dialog);
        dialog.setTitle("ฟังเสียง");
        final TextView textView1 = (TextView) dialog
                .findViewById(R.id.textView1);
        textView1.setText("ชื่อไฟล์ : " + sVoiceID);

        Uri uri = Uri.parse(filepath);

        mMedia = new MediaPlayer();
        mMedia = MediaPlayer.create(getActivity(), uri);
        if(mMedia != null) {
            mMedia.start();

            seekBar = (SeekBar) dialog.findViewById(R.id.seekBar1);
            seekBar.setMax(mMedia.getDuration());
            seekBar.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    UpdateseekChange(v);
                    return false;
                }
            });
            startPlayProgressUpdater();
            final ImageButton btn1 = (ImageButton) dialog.findViewById(R.id.button1); // Start
            final ImageButton btn2 = (ImageButton) dialog.findViewById(R.id.button2); // Pause
            final ImageButton btn3 = (ImageButton) dialog.findViewById(R.id.button3); // Stop


            // Start
            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    textView1.setText("Playing : " + sVoiceID + "....");
                    mMedia.start();
                    startPlayProgressUpdater();
                    btn1.setEnabled(false);
                    btn2.setEnabled(true);
                    btn3.setEnabled(true);
                }
            });

            // Pause
            btn2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    textView1.setText("Pause : " + sVoiceID + "....");
                    mMedia.pause();
                    btn1.setEnabled(true);
                    btn2.setEnabled(false);
                    btn3.setEnabled(false);
                }
            });

            // Stop
            btn3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    textView1.setText("Stop Play : " + sVoiceID);
                    mMedia.stop();
                    btn1.setEnabled(true);
                    btn2.setEnabled(false);
                    btn3.setEnabled(false);
                    try {
                        mMedia.prepare();
                        mMedia.seekTo(0);
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });

            dialog.show();
        }else{
            Toast.makeText(getActivity(), R.string.no_voice, Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateseekChange(View v) {
        if (mMedia.isPlaying()) {
            SeekBar sb = (SeekBar) v;
            mMedia.seekTo(sb.getProgress());
        }
    }

    public void startPlayProgressUpdater() {
        seekBar.setProgress(mMedia.getCurrentPosition());

        if (mMedia.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }


    public class VoiceRecordAdapter extends BaseAdapter {
        private Context context;


        public VoiceRecordAdapter(Context c) {
            // TODO Auto-generated method stub
            context = c;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return tbMultimediaFileList.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater
                        .inflate(R.layout.list_voice_record, null);
            }
            final String sVoiceID = tbMultimediaFileList.get(position).FileID.toString();
            final String sVoiceName = tbMultimediaFileList.get(position).FilePath.toString();
            TextView txtVoiceName = (TextView) convertView
                    .findViewById(R.id.txtVoiceName);
            txtVoiceName.setText(sVoiceName);

            TextView txtVoiceDesc = (TextView) convertView
                    .findViewById(R.id.txtVoiceDesc);
            txtVoiceDesc.setText("คำอธิบาย: "
                    + tbMultimediaFileList.get(position).FileDescription.toString());
            final String strPath = strSDCardPathName_Voi + sVoiceName;
            final String filepath = "http://" + defaultIP + "/assets/csifiles/"
                    + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/voice/"
                    + sVoiceName;
            final File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), strPath);
            final ImageView btnExpandmenu = (ImageView) convertView.findViewById(R.id.btnExpandmenu);
            if (CSIDataTabFragment.mode.equals("view")) {
                btnExpandmenu.setVisibility(View.GONE);
            }
            btnExpandmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(getActivity(), btnExpandmenu);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.voice_menu, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            //noinspection SimplifiableIfStatement
                            if (id == R.id.downloadvoice) {
                                if (curfile.exists()) {
                                    Toast.makeText(getActivity(), R.string.got_voice, Toast.LENGTH_SHORT).show();
                                } else {
                                    if (cd.isNetworkAvailable()) {
                                        int count;
                                        File myDir;
                                        try {
                                            myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), strSDCardPathName_Voi);
                                            myDir.mkdirs();
                                            Log.i(TAG, "display file name: " + filepath);

                                            URL url = new URL(filepath);
                                            URLConnection conexion = url.openConnection();
                                            conexion.connect();

                                            int lenghtOfFile = conexion.getContentLength();
                                            Log.d(TAG, "Lenght of file: " + lenghtOfFile);

                                            InputStream input = new BufferedInputStream(url.openStream());
                                            File filevoi = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), strPath);
                                            if (filevoi.exists()) {
                                                filevoi.delete();
                                            }
                                            filevoi.createNewFile();
                                            // Get File Name from URL
                                            OutputStream output = new FileOutputStream(filevoi);

                                            byte data[] = new byte[1024];
                                            long total = 0;
                                            while ((count = input.read(data)) != -1) {
                                                total += count;
                                                output.write(data, 0, count);
                                            }
                                            Log.i(TAG, "DownloadFile voice " + filevoi.getPath());
                                            if (total > 0) {
                                                Toast.makeText(getActivity(), getString(R.string.save_voice_success), Toast.LENGTH_SHORT).show();
                                            }
                                            output.flush();
                                            output.close();
                                            input.close();
                                        } catch (Exception e) {
                                            Log.e("Error: ", e.getMessage());
                                            Toast.makeText(getActivity(), getString(R.string.save_error), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            if (id == R.id.deletevoice) {
                                if (curfile.exists()) {
                                    long saveStatus = dbHelper.DeleteSelectedData("multimediafile", "FileID", sVoiceID);
                                    if (saveStatus <= 0) {
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                getString(R.string.delete_error),
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "ลบไฟล์เสียงเรียบร้อยเเล้ว",
                                                Toast.LENGTH_LONG).show();
                                        for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                                            if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID.equals(sVoiceID)) {
                                                CSIDataTabFragment.apiCaseScene.getApiMultimedia().remove(i);
                                                Log.i(TAG, "delete file name " + sVoiceID);
                                                curfile.delete();
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            "ไม่มีไฟล์เสียง ไม่สามารถลบได้",
                                            Toast.LENGTH_LONG).show();
                                }
                                showListVoiceRecord();
                            }
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }


            });
            return convertView;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume voice", "resume");

        showListVoiceRecord();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mMedia != null) {
            mMedia.release();
        }
    }

    private class VoiceOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == fabBtn) {
                Log.i("show", "voice");
                viewByIdaddVoice = (ViewGroup) view.findViewById(R.id.layout_media_dialog);
                showMediaDialog();
            }
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


            String strPath = strSDCardPathName_Voi
                    + tbMultimediaFileList.get(position).FilePath
                    .toString();
            String sVoiceName = tbMultimediaFileList.get(position).FilePath
                    .toString();
            String sVoiceID = tbMultimediaFileList.get(position).FileID
                    .toString();

            final File curfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), strPath);
            String filepath = "http://" + defaultIP + "/assets/csifiles/"
                    + CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID + "/voice/"
                    + sVoiceName;
            if (CSIDataTabFragment.mode.equals("view") && CSIDataTabFragment.apiCaseScene.getMode().equals("online")) {
                if (cd.isNetworkAvailable()) {
                    Toast.makeText(getActivity(),
                            "เลือกไฟล์เสียง : " + strPath,
                            Toast.LENGTH_SHORT).show();
                    showPlayDialog(filepath, sVoiceID);
                } else {
                    if (curfile.exists()) {
                        Toast.makeText(getActivity(),
                                "เลือกไฟล์เสียง : " + strPath,
                                Toast.LENGTH_SHORT).show();
                        showPlayDialog(curfile.getAbsolutePath(), sVoiceID);
                    } else {
                        Toast.makeText(getActivity(),
                                "ไม่มีไฟล์เสียงนี้บนเครื่อง",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (curfile.exists()) {
                    Toast.makeText(getActivity(),
                            "เลือกไฟล์เสียง : " + strPath,
                            Toast.LENGTH_SHORT).show();
                    showPlayDialog(curfile.getAbsolutePath(), sVoiceID);
                } else {
                    if (cd.isNetworkAvailable()) {
                        Toast.makeText(getActivity(),
                                "เลือกไฟล์เสียง : " + strPath,
                                Toast.LENGTH_SHORT).show();
                        showPlayDialog(filepath, sVoiceID);
                        Log.i(TAG, "ไม่มีไฟล์เสียง เล่นจาก server");
                    } else {
                        Toast.makeText(getActivity(),
                                "ไม่มีไฟล์เสียงนี้บนเครื่อง",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
