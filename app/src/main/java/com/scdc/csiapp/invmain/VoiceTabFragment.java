package com.scdc.csiapp.invmain;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
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
    public static final int REQUEST_GALLERY = 88;
    String imageEncoded;
    List<String> imagesEncodedList;
    GetPathUri getPathUri;
    ConnectionDetector cd;
    protected static final int DIALOG_AddDescPhoto = 0;
    ViewGroup viewDescPhoto;
    View viewVoiceTab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        getDateTime = new GetDateTime();
        caseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
        dbHelper = new DBHelper(getActivity());
        viewVoiceTab = inflater.inflate(R.layout.voice_tab_layout, container, false);
        mContext = viewVoiceTab.getContext();
        cd = new ConnectionDetector(getActivity());
        SharedPreferences sp = getActivity().getSharedPreferences(PreferenceData.PREF_IP, mContext.MODE_PRIVATE);
        defaultIP = sp.getString(PreferenceData.KEY_IP, defaultIP);
        getPathUri = new GetPathUri();

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
            }
        });
        btnDelete.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                myChronometer.stop();
                stopRecording();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), strSDCardPathName_Voi + sVoiceID1 + ".3gp");

                if (file.exists()) {
                    file.delete();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Delete Recording", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "ไม่มีไฟล์", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        btnStop.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                myChronometer.stop();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Stop Recording", Toast.LENGTH_SHORT).show();
                stopRecording();

                saveToListDB(sVoiceID1, stimeStamp, ".3gp", sVoiceDescription1);
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
        if (mMedia != null) {
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
        } else {
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
//            setListViewHeightBasedOnItems(listViewVoice);
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
            final String descvoice = tbMultimediaFileList.get(position).FileDescription.toString();
            if(tbMultimediaFileList.get(position).FileDescription == null ||
                    tbMultimediaFileList.get(position).FileDescription.length() == 0){
            }else {
                txtVoiceDesc.setText("คำอธิบาย: " + tbMultimediaFileList.get(position).FileDescription.toString());
            }
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
                            if (id == R.id.descphoto) {
                                Log.i(TAG, "  descphoto ");
                                final AlertDialog.Builder addDialog = new AlertDialog.Builder(getActivity());
                                final LayoutInflater inflaterDialog = (LayoutInflater) getActivity().getSystemService(
                                        Context.LAYOUT_INFLATER_SERVICE);

                                View Viewlayout = inflaterDialog.inflate(R.layout.add_media_dialog,
                                        (ViewGroup) viewVoiceTab.findViewById(R.id.layout_media_dialog));
                                addDialog.setIcon(R.drawable.ic_drawing);
                                addDialog.setTitle("เพิ่มคำอธิบาย");
                                addDialog.setView(Viewlayout);
                                TextView editMediaName = (TextView) Viewlayout
                                        .findViewById(R.id.editMediaName);
                                editMediaName.setText(sVoiceID);
                                final EditText editMediaDescription = (EditText) Viewlayout
                                        .findViewById(R.id.editMediaDescription);
                                if (descvoice == null || descvoice.equals("")) {
                                    editMediaDescription.setHint("คำอธิบายภาพ");
                                } else {
                                    editMediaDescription.setText(descvoice);
                                }
                                addDialog.setPositiveButton(getString(R.string.save),
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {
                                                for (int i = 0; i < CSIDataTabFragment.apiCaseScene.getApiMultimedia().size(); i++) {
                                                    if (CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileID.equals(sVoiceID)) {
                                                        CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i)
                                                                .getTbMultimediaFile().FileDescription = editMediaDescription.getText().toString();
                                                        Toast.makeText(mContext, getString(R.string.save_complete), Toast.LENGTH_SHORT).show();
                                                        Log.v(TAG, "Click FileDescription " + i + " " + sVoiceID + " " +
                                                                CSIDataTabFragment.apiCaseScene.getApiMultimedia().get(i).getTbMultimediaFile().FileDescription.toString());
                                                    }
                                                }
                                                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                                                if (isSuccess) {
                                                    showListVoiceRecord();
                                                }
                                                dialog.dismiss();

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
                String title = getString(R.string.importvoice);
                CharSequence[] itemlist = {getString(R.string.voice),
                        getString(R.string.folder)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_record);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterfaceOnClickListener());
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();

            }
        }
    }

    private class DialogInterfaceOnClickListener implements DialogInterface.OnClickListener {


        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case 0:// Take Photo
                    // Do Take Photo task here
                    viewByIdaddVoice = (ViewGroup) viewVoiceTab.findViewById(R.id.layout_media_dialog);
                    showMediaDialog();
                    break;
                case 1:// Choose Existing Photo
                    // Do Pick Photo task here
                    pickVoice();
//                    pickFromGallery();

                    break;
                default:
                    break;
            }
        }
    }

    private void pickVoice() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "เลือกไฟล์เสียง"), REQUEST_GALLERY);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // รับข้อมูลจากอัลบั้มภาพ และบันทึกภาพลงแอพ
        if (requestCode == REQUEST_GALLERY) {
            if (resultCode == getActivity().RESULT_OK && null != data) {
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imagesEncodedList = new ArrayList<String>();
                    if (data.getData() != null) {
                        Uri mImageUri = data.getData();
                        // Get the cursor getFilepath
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            imageEncoded = getPathUri.getPath(getActivity(), mImageUri);
                        } else {
                            imageEncoded = getFilepath(filePathColumn, mImageUri);
                        }
                        Log.i(TAG, "REQUEST_GALLERY " + imageEncoded);
                        saveToMyAlbum(imageEncoded);
                    } else { //ถ้าเลือกหลายรูป
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    //Multiple images
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    // Get the cursor getFilepath
                                    imageEncoded = getPathUri.getPath(getActivity(), uri);
//                                    Log.v(TAG, "REQUEST_GALLERY [" + i + "] " + imageEncoded);

                                    if (imageEncoded != null) {
                                        imagesEncodedList.add(imageEncoded);
                                        saveToMyAlbum(imageEncoded);
                                    }
                                }
                                Log.v(TAG, "REQUEST_GALLERY Selected Images   :" + " imagesEncodedList :" + imagesEncodedList.size());
                            }
                        } else {
                            Toast.makeText(getActivity(), "ไม่สามารถเลือกหลายรูปได้", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    showListVoiceRecord();
                    Log.i(TAG, "REQUEST_GALLERY");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // After Cancel code.
                Log.i(TAG, "Cancel REQUEST_GALLERY");
            } else {
                Log.i(TAG, "Failed to REQUEST_GALLERY");
            }

        }
    }

    private void saveToMyAlbum(String imageEncoded) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), strSDCardPathName_Voi);
            if (!file.exists()) {
                file.mkdirs();
            }
            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File datadest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            String sVoiceID = "", sFileType = "";
            String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
            sVoiceID = "VOI_" + CurrentDate_ID[2] + CurrentDate_ID[1] + CurrentDate_ID[0] + "_" + CurrentDate_ID[3] + CurrentDate_ID[4] + CurrentDate_ID[5] + CurrentDate_ID[6];
            timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];
            sFileType = imageEncoded.substring(imageEncoded.lastIndexOf("."));
            Log.i(TAG, "sVoiceID " + sVoiceID + " sImageType " + sFileType);
            if (sd.canWrite()) {
//                        String sourceImagePath = "/path/to/source/file.jpg";
                String destinationImagePath = strSDCardPathName_Voi + sVoiceID + sFileType;
                File source = new File(imageEncoded);

                File destination = new File(datadest, destinationImagePath);
                if (source.exists()) {
                    Log.i(TAG, "source ");
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();

                    try {
                        dst.transferFrom(src, 0, src.size());
                        Log.i(TAG, "transferFrom ");
                    } catch (IOException e) {
                        Log.i(TAG, "transferFrom " + e.getMessage());
                    }
                    if (destination.exists()) {
//                                source.delete();
                        Log.i(TAG, "source.delete ");
                        saveToListDB(sVoiceID, timeStamp, sFileType, "");
                    }
                    src.close();
                    dst.close();
                    Log.i(TAG, "save new Voice from gallery " + destinationImagePath);
                } else {
                    Log.i(TAG, "Voice from gallery error ");
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Voice from gallery error " + e.getMessage());
        }
    }

    public String getFilepath(String[] filePathColumn, Uri uri) {
        // Get the cursor
        Cursor cursor = getActivity().getContentResolver().query(uri,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imageEncoded = cursor.getString(columnIndex);

        cursor.close();
        return imageEncoded;
    }

    // save ข้อมูลรูปภาพไว้ใน list table และ sqlite
    private void saveToListDB(String sVoiceID, String stimeStamp, String sFileType, String sVoiceDescription) {
        try {
            List<ApiMultimedia> apiMultimediaList = new ArrayList<>();
            ApiMultimedia apiMultimedia = new ApiMultimedia();
            TbMultimediaFile tbMultimediaFile = new TbMultimediaFile();
            tbMultimediaFile.CaseReportID = caseReportID;
            tbMultimediaFile.FileID = sVoiceID;
            tbMultimediaFile.FileDescription = sVoiceDescription;
            tbMultimediaFile.FileType = "voice";
            tbMultimediaFile.FilePath = sVoiceID + sFileType;
            tbMultimediaFile.Timestamp = stimeStamp;
            apiMultimedia.setTbMultimediaFile(tbMultimediaFile);

            apiMultimediaList.add(apiMultimedia);
            CSIDataTabFragment.apiCaseScene.getApiMultimedia().add(apiMultimedia);
//                CSIDataTabFragment.apiCaseScene.setApiMultimedia(apiMultimediaList);
            Log.i(TAG, "apiMultimediaList " + String.valueOf(CSIDataTabFragment.apiCaseScene.getApiMultimedia().size()));
            boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
            if (isSuccess) {
                Log.i(TAG, "voice saved to Gallery!" + ResultTabFragment.strSDCardPathName + "Voice/" + " : " + sVoiceID + ".3gp");
                showListVoiceRecord();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
