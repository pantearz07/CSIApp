package com.scdc.csiapp.csidatatabs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.ActivityResultBus;
import com.scdc.csiapp.main.ActivityResultEvent;
import com.scdc.csiapp.main.GetDateTime;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;


public class PhotosTabFragment extends Fragment {
    public static final int REQUEST_CAMERA = 111;
    public static final int REQUEST_VIDEO = 222;
    String mCurrentPhotoPath,mCurrentVideoPath;
    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/CSIFiles" + "/";
     String sPhotoID,sVideoID;

    private static final String[] menu = {"ถ่ายภาพ", "อัดวิดีโอ", "อัดเสียง"};
    // public static final int RESULT_OK = 2;
    SQLiteDBHelper mDbHelper;
    SQLiteDatabase mDb;
    //ImageView imageView;
    GridView gView1, gView2, gView3;
    Uri uri;
    private PreferenceData mManager;
    String officialID, reportID;

    String arrDataPhoto[][];
    String arrDataVideo[][];
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
    GetDateTime getDateTime;
    String[] updateDT;
    FloatingActionButton fabBtn;
    CoordinatorLayout rootLayout;
    protected static final int MENU_VIDEO = 1;
    protected static final int MENU_PHOTO = 0;
    protected static final int MENU_VOICE = 2;

    public static int RECORD_REQUEST = 1;

    //Button buttonIntent,buttonIntentVideo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = new PreferenceData(getActivity());
        officialID = mManager.getPreferenceData(mManager.KEY_OFFICIALID);
        reportID = mManager.getPreferenceData(mManager.PREF_REPORTID);
        getDateTime = new GetDateTime();
        updateDT = getDateTime.getDateTimeNow();
        mDbHelper = new SQLiteDBHelper(getActivity());
        mDb = mDbHelper.getWritableDatabase();
        View viewPhotosTab = inflater.inflate(R.layout.photos_tab_layout, container, false);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (mMedia != null) {
            mMedia.release();
        }
        gView1 = (GridView) viewPhotosTab.findViewById(R.id.gridViewShowMedia);
        gView2 = (GridView) viewPhotosTab.findViewById(R.id.gridViewShowVideo);
        gView3 = (GridView) viewPhotosTab.findViewById(R.id.gridViewVoiceRecord);
        rootLayout = (CoordinatorLayout) viewPhotosTab.findViewById(R.id.rootLayout);


        showAllPhoto();
        showAllVideo();
        showListVoiceRecord();
        //imageView = (ImageView)viewPhotosTab.findViewById(R.id.imageView);
        //buttonIntent = (Button)viewPhotosTab.findViewById(R.id.buttonIntent);

        //buttonIntentVideo = (Button)viewPhotosTab.findViewById(R.id.buttonIntentVideo);


        fabBtn = (FloatingActionButton) viewPhotosTab.findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose device");
                builder.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String timeStamp = "";
                        File newfile;
                        switch (which) {
                            case MENU_PHOTO:
                                createFolder("Pictures");

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                timeStamp = updateDT[0]+" "+updateDT[1];

                                sPhotoID = "IMG_" + updateDT[0]+"_"+updateDT[1];
                                String sPhotoPath = sPhotoID + ".jpg";
                                newfile = new File(strSDCardPathName, "Pictures/" + sPhotoPath);
                                if (newfile.exists())
                                    newfile.delete();
                                try {
                                    newfile.createNewFile();
                                    mCurrentPhotoPath = newfile.getAbsolutePath();
                                } catch (IOException e) {
                                }
                                if (newfile != null) {
                                    uri = Uri.fromFile(newfile);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    getActivity().startActivityForResult(Intent.createChooser(cameraIntent
                                            , "Take a picture with"), REQUEST_CAMERA);

                                    String sPhotoDescription = "";
                                    new saveDataMedia().execute(reportID, sPhotoID, sPhotoPath, sPhotoDescription, timeStamp, "photo");

                                    Log.i("show", "PHOTO saved to Gallery!" + strSDCardPathName + "Pictures/" + " : " + sPhotoPath);
                                }

                                break;
                            case MENU_VIDEO:

                                createFolder("Videoes");
                                Intent intentvideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                                timeStamp = updateDT[0]+" "+updateDT[1];

                                sVideoID = "VID_" + updateDT[0]+"_"+updateDT[1];
                                String sVideoPath = sVideoID + ".mp4";
                                newfile = new File(strSDCardPathName, "Videoes/" + sVideoPath);
                                if (newfile.exists())
                                    newfile.delete();
                                try {
                                    newfile.createNewFile();
                                    mCurrentVideoPath = newfile.getAbsolutePath();
                                } catch (IOException e) {
                                }

                                if (newfile != null) {
                                    Uri videoUri = Uri.fromFile(newfile);

                                    intentvideo.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                                    getActivity().startActivityForResult(intentvideo, REQUEST_VIDEO);
                                    String sVideoDescription = "";
                                    new saveDataMedia().execute(reportID, sVideoID, sVideoPath, sVideoDescription, timeStamp, "video");
                                    Log.i("show", "Video saved to Gallery!" + strSDCardPathName + "Videoes/" + " : " + sVideoPath);

                                }

                                break;
                            case MENU_VOICE:

                                Log.i("show", "voice");
                                viewByIdaddVoice = (ViewGroup) view.findViewById(R.id.layout_media_dialog);
                                showMediaDialog();
                               /* Intent intentVoice = new Intent(
                                        MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                startActivityForResult(intentVoice, RECORD_REQUEST);
*/
                                break;
                        }
                    }
                });

                builder.create();
                builder.show();
            }
        });
        return viewPhotosTab;

    }


    public static void createFolder(String pathType) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/CSIFiles/" + pathType + "/");
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }

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

        final String timeStamp = updateDT[0]+" "+updateDT[1];

        String sVoiceRecID = "VOI_" + updateDT[0]+"_"+updateDT[1];
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
                showListVoiceRecord();
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
            gView3.setVisibility(View.VISIBLE);

            gView3.setAdapter(new VoiceRecordAdapter(
                    getActivity(), arrDataVoiceRecord));
            //registerForContextMenu(listViewVoiceRecord);
            // OnClick
            gView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            gView3.setVisibility(View.GONE);
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

    public void showAllVideo() {
        // TODO Auto-generated method stub
        arrDataVideo = mDbHelper.SelectDataMultimediaFile(reportID, "video");
        if (arrDataVideo != null) {
            gView2.setVisibility(View.VISIBLE);
            gView2.setAdapter(new VideoAdapter(getActivity(), arrDataVideo));
            registerForContextMenu(gView2);
            // OnClick
            gView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent VideoPlayActivity = new Intent(getActivity(),
                            VideoPlayerActivity.class);
                    VideoPlayActivity.putExtra("VideoPath",
                            arrDataVideo[position][3].toString());
                    startActivity(VideoPlayActivity);

                }
            });
        } else {
            gView2.setVisibility(View.GONE);
            Log.i("Recieve", "Null!! ");

        }
    }


    public void showAllPhoto() {
        // TODO Auto-generated method stub
        arrDataPhoto = mDbHelper.SelectDataMultimediaFile(reportID, "photo");
        if (arrDataPhoto != null) {
            Log.i("arrDataPhoto", String.valueOf(arrDataPhoto.length));
            gView1.setVisibility(View.VISIBLE);
            gView1.setAdapter(new PhotoAdapter(getActivity(), arrDataPhoto));
            registerForContextMenu(gView1);
            // OnClick
            gView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    showViewPic(arrDataPhoto[position][3].toString());
                }
            });
        } else {
            gView1.setVisibility(View.GONE);
            Log.i("Recieve", "Null!! ");

        }
    }

    public void showViewPic(String sPicPath) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getActivity(),
                R.style.FullHeightDialog);
        dialog.setContentView(R.layout.view_pic_dialog);
        String root = Environment.getExternalStorageDirectory().toString();
        String strPath = root + "/CSIFiles/Pictures/" + sPicPath;

        // Image Resource
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imgPhoto);

        Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);
        if(bmpSelectedImage !=null){
        int width = bmpSelectedImage.getWidth();
        int height = bmpSelectedImage.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap resizedBitmap = Bitmap.createBitmap(bmpSelectedImage, 0, 0,
                width, height, matrix, true);
        imageView.setImageBitmap(resizedBitmap);}
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Result media", String.valueOf(requestCode) + " " + String.valueOf(resultCode));


        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == getActivity().RESULT_OK) {
                try {

                    Log.i("REQUEST_Photo", "Photo save");
                    showAllPhoto();
                    //showAllVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                Log.i("REQUEST_Photo", "media recording cancelled."+sPhotoID);
                File photosfile = new File(mCurrentPhotoPath);
                if (photosfile.exists()) {
                    photosfile.delete();
                    long saveStatus = mDbHelper.DeleteMediaFile(reportID, sPhotoID);
                    if (saveStatus <= 0) {
                        Log.i("deletephoto", "Cannot delete!! ");

                    } else {
                        Log.i("deletephoto", "ok");
                        showAllPhoto();
                    }
                }
            } else {
                Log.i("REQUEST_Photo", "Failed to record media");
            }
        }
        if (requestCode == REQUEST_VIDEO) {
            if (resultCode == getActivity().RESULT_OK) {
                try {

                    Log.i("REQUEST_VIDEO", "VIDEO save");
                      showAllVideo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                //data.getData();
                Log.i("REQUEST_VIDEO", "media recording cancelled."+sVideoID);
                File videosfile = new File(mCurrentVideoPath);
                if (videosfile.exists()) {
                    videosfile.delete();
                    long saveStatus = mDbHelper.DeleteMediaFile(reportID, sVideoID);
                    if (saveStatus <= 0) {
                        Log.i("delete video", "Cannot delete!! ");

                    } else {
                        Log.i("delete video", "ok");
                        showAllVideo();
                    }
                }
            }else {
            Log.i("REQUEST_VIDEO", "Failed to record media");
        }
        }

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

            } else {
                Log.i("saveData", "error");

            }
        }
    }

    public class PhotoAdapter extends BaseAdapter {
        private Context context;
        private String[][] lis;

        public PhotoAdapter(Context c, String[][] li) {
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
                convertView = inflater.inflate(R.layout.show_photos, null);
            }

            TextView textView = (TextView) convertView
                    .findViewById(R.id.txtDescPhoto);
            String root = Environment.getExternalStorageDirectory().toString();

            String strPath = root + "/CSIFiles/Pictures/"
                    + lis[position][3].toString();

            Log.i("list photo", "/CSIFiles/Pictures/" + lis[position][3].toString());

            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);

                textView.setText(lis[position][3].toString() + "\n"
                        + lis[position][4].toString());
                Bitmap bmpSelectedImage = BitmapFactory.decodeFile(strPath);

            int width1 = bmpSelectedImage.getWidth();
            int height1 = bmpSelectedImage.getHeight();
            Log.i("size", width1 + " " + height1);
            int width = width1 / 13;
            int height = height1 / 13;
            Log.i("resize", width + " " + height);
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bmpSelectedImage,
                    width, height, true);
            imageView.setImageBitmap(resizedbitmap);


               // imageView.setImageBitmap(bmpSelectedImage);

            return convertView;

        }
    }

    public class VideoAdapter extends BaseAdapter {
        private Context context;
        private String[][] lis;

        public VideoAdapter(Context c, String[][] li) {
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
                convertView = inflater.inflate(R.layout.show_photos, null);
            }

            TextView textView = (TextView) convertView
                    .findViewById(R.id.txtDescPhoto);
            String root = Environment.getExternalStorageDirectory().toString();
            Log.i("list Videoes", "/CSIFiles/Videoes/" + lis[position][3].toString());

            String strPath = root + "/CSIFiles/Videoes/"
                    + lis[position][3].toString();
            textView.setText(lis[position][3].toString() + "\n"
                    + lis[position][4].toString());
            // Image Resource
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.imgPhoto);

            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(strPath,
                    MediaStore.Video.Thumbnails.MINI_KIND);
            imageView.setImageBitmap(bmThumbnail);
            return convertView;

        }
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
            txtVoiceName.setText("ชื่อไฟล์: " + lis[position][3].toString());
            Log.i("list photo", "/CSIFiles/Voice/" + lis[position][3].toString());

            TextView txtVoiceDesc = (TextView) convertView
                    .findViewById(R.id.txtVoiceDesc);
            txtVoiceDesc.setText("คำอธิบายไฟล์เสียง: "
                    + lis[position][4].toString());

            return convertView;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "resume");
        showAllPhoto();
        showAllVideo();
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