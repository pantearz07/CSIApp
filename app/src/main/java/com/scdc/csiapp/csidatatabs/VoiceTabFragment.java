package com.scdc.csiapp.csidatatabs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;

import java.io.File;
import java.io.IOException;

/**
 * Created by Pantearz07 on 14/3/2559.
 */
public class VoiceTabFragment extends Fragment {
      String arrDataVoiceRecord[][];
    ViewGroup viewByIdaddVoice;
    String[] Cmd = {"Play", "Delete"};
    // record
    private MediaRecorder recorder = null;
    // play
    private MediaPlayer mMedia;
    private Handler handler = new Handler();
    private SeekBar seekBar;
    Uri audioFileUri;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    //ImageView imageView;
    //GridView gridViewVoiceRecord;
    ListView listViewVoice;
    Uri uri;
    private PreferenceData mManager;
    String officialID, reportID;
    TextView txtVoiceNum;
    GetDateTime getDateTime;
    String[] updateDT,datetime;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        getDateTime = new GetDateTime();

        updateDT = getDateTime.getDateTimeNow();
        datetime = getDateTime.getDateTimeCurrent();
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        View viewVoiceTab = inflater.inflate(R.layout.voice_tab_layout, container, false);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (mMedia != null) {
            mMedia.release();
        }
        listViewVoice = (ListView) viewVoiceTab.findViewById(R.id.gridViewVoiceRecord);
        //gridViewVoiceRecord = (GridView) viewVoiceTab.findViewById(R.id.gridViewVoiceRecord);
        txtVoiceNum = (TextView) viewVoiceTab.findViewById(R.id.txtVoiceNum);
        rootLayout = (CoordinatorLayout) viewVoiceTab.findViewById(R.id.rootLayout);
        showListVoiceRecord();

        fabBtn = (FloatingActionButton) viewVoiceTab.findViewById(R.id.fabBtn);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.i("show", "voice");
                viewByIdaddVoice = (ViewGroup) view.findViewById(R.id.layout_media_dialog);
                showMediaDialog();
            }
        });
      return viewVoiceTab;

    }
    public void showMediaDialog() {
        // TODO Auto-generated method stub
        final AlertDialog.Builder addDialog = new AlertDialog.Builder(
                getActivity());
        final LayoutInflater inflaterDialog = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View Viewlayout = inflaterDialog.inflate(R.layout.add_media_dialog, viewByIdaddVoice);
        addDialog.setIcon(android.R.drawable.btn_star_big_on);
        addDialog.setTitle("เพิ่มไฟล์บันทึกเสียง");
        addDialog.setView(Viewlayout);

        final String timeStamp =updateDT[0]+" "+updateDT[1];


        String sVoiceRecID = "VOI_" + datetime[2]+""+datetime[1]+""+datetime[0]+"_"+ datetime[3]+""+datetime[4]+""+datetime[5];
        final TextView editMediaName = (TextView) Viewlayout
                .findViewById(R.id.editMediaName);
        editMediaName.setText(sVoiceRecID);

        final EditText editMediaDescription = (EditText) Viewlayout
                .findViewById(R.id.editMediaDescription);
        editMediaDescription.setHint("คำอธิบายไฟล์เสียง");

        // Button OK
        addDialog.setPositiveButton("Save",
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

                        showVoiceRecorderDialog(reportID, sMediaName, sMediaDescription, timeStamp);

                    }

                })

                // Button Cancel
                .setNegativeButton("Cancel",
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

        // set the custom dialog components - text, image and
        // button
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("ชื่อไฟล์ : " + sVoiceID1 + "\n\nคำอธิบาย : "
                + sVoiceDescription1);
        final TextView txtTime = (TextView) dialog.findViewById(R.id.txtTime);
        final Button btnStart = (Button) dialog.findViewById(R.id.btnStart);
        final Button btnStop = (Button) dialog.findViewById(R.id.btnStop);
        btnStop.setEnabled(false);

        // if button is clicked, close the custom dialog
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Start Recording", Toast.LENGTH_SHORT).show();

                Log.i("show", "Voice 1");
                btnStop.setEnabled(true);
                btnStart.setEnabled(false);
                startRecording(sVoiceID1);
            }
        });

        // if button is clicked, close the custom dialog
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Stop Recording", Toast.LENGTH_SHORT).show();
                stopRecording();

                new saveDataMedia().execute(reportID, sVoiceID1, sVoiceID1 + ".3gp", sVoiceDescription1, stimeStamp, "voice");
/*
                long saveStatus = mDbHelper.saveVoiceRecorder(sReportID1,
                        sVoiceID1, sVoiceID1 + ".3gp", sVoiceDescription1);
                if (saveStatus <= 0) {
                    Log.i("Recieve", "Error!! ");
                } else {
                    Log.i("Recieve", "ok");
                }*/
                dialog.dismiss();

            }
        });

        dialog.show();
    }
    class saveDataMedia extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        @Override
        protected String doInBackground(String... params) {
            String arrData = "";


            long saveStatus = mDbHelper.saveDataMultimediaifile(params[0], params[1], params[5], params[2],
                    params[3], params[4]);
            if (saveStatus <= 0) {
                arrData = "error";
                Log.i("saveData " + params[5], "Error!! ");
            } else {
                arrData = "save";
                Log.i("saveData " + params[5], params[2]);


            }

            return arrData;
        }

        protected void onPostExecute(String arrData) {
            if (arrData == "save") {
                Log.i("saveData", "save");
                //showAllPhoto();
                //showAllVideo();
                showListVoiceRecord();
            } else {
                Log.i("saveData", "error");

            }

        }
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
        String filepath = Environment.getExternalStorageDirectory().toString();
        File file = new File(filepath + "/CSIFiles/VoiceRecorder/");

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + sVoiceID1 + ".3gp");
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderIcon(android.R.drawable.btn_star_big_on);
        menu.setHeaderTitle("ไฟล์เสียง :"
                + arrDataVoiceRecord[info.position][2].toString());
        String[] menuItems = Cmd;
        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    public void showListVoiceRecord() {
        // TODO Auto-generated method stub
        arrDataVoiceRecord = mDbHelper.SelectDataMultimediaFile(reportID, "voice");
        if (arrDataVoiceRecord != null) {
            txtVoiceNum.setText(String.valueOf(arrDataVoiceRecord.length));

            listViewVoice.setVisibility(View.VISIBLE);

            listViewVoice.setAdapter(new VoiceRecordAdapter(
                    getActivity(), arrDataVoiceRecord));
            //registerForContextMenu(listViewVoiceRecord);
            // OnClick
            listViewVoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    String root = Environment
                            .getExternalStorageDirectory().toString();
                    String strPath = root
                            + "/CSIFiles/VoiceRecorder/"
                            + arrDataVoiceRecord[position][3]
                            .toString();
                    String sVoiceName = arrDataVoiceRecord[position][3]
                            .toString();
                    Toast.makeText(getActivity(),
                            "Your selected : " + strPath,
                            Toast.LENGTH_SHORT).show();

                    showPlayDialog(sVoiceName);

                }
            });
        } else {
            txtVoiceNum.setText(String.valueOf(0));

            listViewVoice.setVisibility(View.INVISIBLE);
            Log.i("Recieve", "Null!! ");

        }
    }

    public void showPlayDialog(String sVoiceName) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.voiceplay_dialog);
        dialog.setTitle("ฟังเสียง");
        final String sVoiceName1 = sVoiceName;
        final TextView textView1 = (TextView) dialog
                .findViewById(R.id.textView1);
        textView1.setText("ชื่อไฟล์ : " + sVoiceName1);

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/CSIFiles/VoiceRecorder/" + sVoiceName1);
        mMedia = new MediaPlayer();
        mMedia = MediaPlayer.create(getActivity(), uri);
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
        final Button btn1 = (Button) dialog.findViewById(R.id.button1); // Start
        final Button btn2 = (Button) dialog.findViewById(R.id.button2); // Pause
        final Button btn3 = (Button) dialog.findViewById(R.id.button3); // Stop
        // Start
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView1.setText("Playing : " + sVoiceName1 + "....");
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
                textView1.setText("Pause : " + sVoiceName1 + "....");
                mMedia.pause();
                btn1.setEnabled(true);
                btn2.setEnabled(false);
                btn3.setEnabled(false);
            }
        });

        // Stop
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView1.setText("Stop Play : " + sVoiceName1);
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = Cmd;
        String CmdName = menuItems[menuItemIndex];
        String sVoiceRecordID = arrDataVoiceRecord[info.position][0].toString();
        String sReportID = arrDataVoiceRecord[info.position][1].toString();
        String sVoiceRecordPath = arrDataVoiceRecord[info.position][3]
                .toString();

        // Check Event Command
        if ("Play".equals(CmdName)) {
            String root = Environment.getExternalStorageDirectory().toString();

            String sVoiceName = arrDataVoiceRecord[info.position][3].toString();
            String strPath = root + "/CSIFiles/VoiceRecorder/"
                    + sVoiceName;
            Toast.makeText(getActivity(), "Your selected : " + strPath,
                    Toast.LENGTH_SHORT).show();

            showPlayDialog(sVoiceName);
        } else if ("Delete".equals(CmdName)) {


            long saveStatus = mDbHelper.DeleteMediaFile(sReportID, sVoiceRecordID);
            if (saveStatus <= 0) {


                Log.i("Recieve", "Null!! ");
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "ลบไฟล์เสียง " + sVoiceRecordPath + " เรียบร้อยเเล้ว",
                        Toast.LENGTH_LONG).show();
            }

            showListVoiceRecord();

        }
        return true;
    }
    public class VoiceRecordAdapter extends BaseAdapter {
        private Context context;
        private String[][] lis;

        public VoiceRecordAdapter(Context c, String[][] li) {
            // TODO Auto-generated method stub
            context = c;
            lis = li;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return lis.length;
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

            TextView txtVoiceName = (TextView) convertView
                    .findViewById(R.id.txtVoiceName);
            txtVoiceName.setText(lis[position][3].toString());

            TextView txtVoiceDesc = (TextView) convertView
                    .findViewById(R.id.txtVoiceDesc);
            txtVoiceDesc.setText("คำอธิบาย: "
                    + lis[position][4].toString());

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
}
