package com.scdc.csiapp.invmain;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scdc.csiapp.R;
import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.SQLiteDBHelper;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbMultimediaFile;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Pantearz07 on 25/12/2558.
 */
public class DrawingDiagramFragment extends Fragment implements OnClickListener {
    private static final String TAG = "DEBUG-DrawingDiagramFragment";
    SQLiteDBHelper mDbHelper;
    DBHelper dbHelper;
    SQLiteDatabase mDb;
    private static final String ARG_REPORT_ID = "report_id";
    private static final String ARG_MEDIA_NAME = "MediaName";
    private static final String ARG_MEDIA_DESC = "MediaDescription";
    TextView txtFilesName, txtDescFile;
    String sDiagramID, FileType, FilePath, FileDescription;
    // custom drawing view
    private DrawingView drawView;
    // buttons
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
    // sizes
    private float smallBrush, mediumBrush, largeBrush;
    GetDateTime getDateTime;
    String[] updateDT;
    TbMultimediaFile tbMultimediaFile;

    public DrawingDiagramFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of
        // actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.drawing_diagram_scene,
                container, false);

        // Permission StrictMode
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
        // database
        Bundle args = getArguments();
        sDiagramID = args.getString(DiagramTabFragment.Bundle_ID);
        FileDescription = args.getString(DiagramTabFragment.Bundle_MediaDescription, "");
        Log.i(TAG, "  Drawing  " + sDiagramID + " " + FileDescription);
        tbMultimediaFile = new TbMultimediaFile();
        dbHelper = new DBHelper(getActivity());
        mDbHelper = new SQLiteDBHelper(getActivity());
        txtFilesName = (TextView) rootView.findViewById(R.id.txtFilesName);
        txtDescFile = (TextView) rootView.findViewById(R.id.txtDescFile);
        txtFilesName.setText(sDiagramID);
        txtDescFile.setText(FileDescription);
        getDateTime = new GetDateTime();
        updateDT = getDateTime.getDateTimeNow();

        // get drawing view
        drawView = (DrawingView) rootView.findViewById(R.id.drawing);

        // get the palette and first color button
        LinearLayout paintLayout = (LinearLayout) rootView
                .findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        //currPaint.setImageDrawable(getResources().getDrawable(
        //        R.drawable.paint_pressed));
        currPaint.setImageResource(getResources().getIdentifier(
                "paint_pressed", "drawable", "com.scdc.csiapp"));

        // sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        // draw button
        drawBtn = (ImageButton) rootView.findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        // set initial size
        drawView.setBrushSize(mediumBrush);

        // erase button
        eraseBtn = (ImageButton) rootView.findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        // new button
        newBtn = (ImageButton) rootView.findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        // save button
        saveBtn = (ImageButton) rootView.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        return rootView;
    }

    // user clicked paint
    public void paintClicked(View view) {
        // use chosen color

        // set erase false
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if (view != currPaint) {
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            // update ui

            imgView.setImageResource(getResources().getIdentifier(
                    "paint_pressed", "drawable", "com.scdc.csiapp"));

            currPaint.setImageResource(getResources().getIdentifier(
                    "paint", "drawable", "com.scdc.csiapp"));

            // imgView.setImageDrawable(getResources().getDrawable(
            //        R.drawable.paint_pressed));
            // currPaint.setImageDrawable(getResources().getDrawable(
            //         R.drawable.paint));
            currPaint.setImageResource(getResources().getIdentifier(
                    "paint", "drawable", "com.scdc.csiapp"));

            currPaint = (ImageButton) view;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.draw_btn) {
            // draw button clicked
            final Dialog brushDialog = new Dialog(getActivity());
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            // listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton) brushDialog
                    .findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog
                    .findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog
                    .findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            // show and wait for user interaction
            brushDialog.show();
        } else if (view.getId() == R.id.erase_btn) {
            // switch to erase - choose size
            final Dialog brushDialog = new Dialog(getActivity());
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            // size buttons
            ImageButton smallBtn = (ImageButton) brushDialog
                    .findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog
                    .findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog
                    .findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        } else if (view.getId() == R.id.new_btn) {
            // new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(
                    getActivity());
            newDialog.setTitle("New drawing");
            newDialog
                    .setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            drawView.startNew();
                            dialog.dismiss();
                        }
                    });
            newDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            newDialog.show();
        } else if (view.getId() == R.id.save_btn) {
            // save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(
                    getActivity());
            saveDialog.setTitle("บันทึกภาพวาด");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // save drawing
                            drawView.setDrawingCacheEnabled(true);
                            Bitmap finalBitmap = drawView.getDrawingCache();
                            String root = Environment
                                    .getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/CSIFiles/Pictures/");
                            myDir.mkdirs();
                            String[] CurrentDate_ID = getDateTime.getDateTimeCurrent();
                            String timeStamp = CurrentDate_ID[0] + "-" + CurrentDate_ID[1] + "-" + CurrentDate_ID[2] + " " + CurrentDate_ID[3] + ":" + CurrentDate_ID[4] + ":" + CurrentDate_ID[5];
                            tbMultimediaFile.CaseReportID = CSIDataTabFragment.apiCaseScene.getTbCaseScene().CaseReportID;
                            tbMultimediaFile.FileID = sDiagramID;
                            tbMultimediaFile.FileDescription = FileDescription;
                            tbMultimediaFile.FileType = "diagram";
                            tbMultimediaFile.FilePath = sDiagramID + ".jpg";
                            tbMultimediaFile.Timestamp = timeStamp;
                             DiagramTabFragment.tbMultimediaFileList.add(tbMultimediaFile);
//                            CSIDataTabFragment.apiCaseScene.setTbMultimediaFiles(DiagramTabFragment.tbMultimediaFileList);
//                            Log.i(TAG, "tbMultimediaFileList num:" + String.valueOf(CSIDataTabFragment.apiCaseScene.getTbMultimediaFiles().size()));
                            String fname = sDiagramID + ".jpg";
                            File file = new File(myDir, fname);
                            if (file.exists())
                                file.delete();
                            try {
                                FileOutputStream out = new FileOutputStream(
                                        file);
                                finalBitmap.compress(
                                        Bitmap.CompressFormat.JPEG, 100, out);
                                out.flush();
                                out.close();

                                boolean isSuccess = dbHelper.updateAlldataCase(CSIDataTabFragment.apiCaseScene);
                                if (isSuccess) {
                                    Toast savedToast = Toast.makeText(getActivity()
                                                    .getApplicationContext(),
                                            "Drawing saved to Gallery!" + myDir
                                                    + " : " + fname,
                                            Toast.LENGTH_SHORT);
                                    savedToast.show();
                                    getActivity().onBackPressed();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
            saveDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            getActivity().onBackPressed();
                        }
                    });
            saveDialog.show();
        }
    }
}
