package com.scdc.csiapp.gcmservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.scdc.csiapp.connecting.DBHelper;
import com.scdc.csiapp.connecting.PreferenceData;
import com.scdc.csiapp.R;
import com.google.android.gms.gcm.GcmListenerService;
import com.scdc.csiapp.main.GetDateTime;
import com.scdc.csiapp.tablemodel.TbOfficial;

import java.util.Date;

/**
 * Created by Pantearz07 on 22/5/2559.
 */
public class GcmDownstreamService extends GcmListenerService {
    private static final String TAG = "DcmDownstreamService";
    private PreferenceData mManager;
    String officialID, accesstype = "";
    private static NotificationManager mNotificationManager;
    Context context;
    TbOfficial tbOfficial = new TbOfficial();
    DBHelper dbHelper;
    GetDateTime getDateTime;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        dbHelper = new DBHelper(context);
        getDateTime = new GetDateTime();
        mManager = new PreferenceData(context);
        officialID = mManager.getPreferenceData(dbHelper.COL_OfficialID);
        accesstype = mManager.getPreferenceData(dbHelper.COL_AccessType);
        Log.i("GcmDownstreamService", officialID + " " + accesstype);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        // TODO Do something here
        Log.e(TAG, "Message Incoming");
        String Title = data.getString("gcm.notification.Title");
        if (accesstype.equals("investigator")) {

            Log.e(TAG, "Title : " + Title);
            String InvestigatorOfficialID = data.getString("gcm.notification.InvestigatorOfficialID");
            Log.e(TAG, "InvestigatorOfficialID : " + InvestigatorOfficialID);
            if (officialID.equals(InvestigatorOfficialID)) {
                if (Title.equals("newreceivingcase")) {
                    String nameintent = "MainActivity";
                    String CaseReportID = data.getString("gcm.notification.CaseReportID");
                    String SubCaseTypeName = data.getString("gcm.notification.SubCaseTypeName");
                    String PoliceStation = data.getString("gcm.notification.PoliceStation");
                    String ReceivingCaseDate = getDateTime.changeDateFormatToCalendar(data.getString("gcm.notification.ReceivingCaseDate"));
                    String ReceivingCaseTime = getDateTime.changeTimeFormatToDB(data.getString("gcm.notification.ReceivingCaseTime"));
                    String Address = data.getString("gcm.notification.Address");
                    String InquiryOfficial = data.getString("gcm.notification.InquiryOfficial");

                    Log.e(TAG, "CaseReportID : " + CaseReportID);
                    Log.e(TAG, "SubCaseTypeName : " + SubCaseTypeName);
                    Log.e(TAG, "PoliceStation : " + PoliceStation);
                    Log.e(TAG, "InquiryOfficial : " + InquiryOfficial);
                    Log.e(TAG, "ReceivingCaseDate : " + ReceivingCaseDate);
                    Log.e(TAG, "ReceivingCaseTime : " + ReceivingCaseTime);
                    String message = "คดี " + SubCaseTypeName
                            + "\n" + "รับเเจ้ง: " + ReceivingCaseDate + " " + ReceivingCaseTime + " น.";
                    String bigmessage = "คดี " + SubCaseTypeName
                            + " " + "สถานีตำรวจ " + PoliceStation
                            + "\n" + "ที่อยู่ " + Address
                            + "\n" + "พงส. " + InquiryOfficial
                            + "\n" + "รับเเจ้งเมื่อ " + ReceivingCaseDate + " " + ReceivingCaseTime + " น.";

                    sendNotification("มีงานเข้าใหม่", message, bigmessage, nameintent, "casescenelistfragment");
                }
                if (Title.equals("newschedule")) {

                    String nameintent = "MainActivity";

                    String scheduleDate = getDateTime.changeDateFormatToCalendar(data.getString("gcm.notification.scheduleDate"));


                    Log.e(TAG, "scheduleDate : " + scheduleDate);
                    String message = "ตารางเวรวันที่ " + scheduleDate;
                    String bigmessage = "ตารางเวรตรวนสถานที่เกิดเหตุใหม่" + "\n" + "วันที่ " + scheduleDate;
                    sendNotification("อัพเดทตารางเวรใหม่", message, bigmessage, nameintent, "calendarfragment");

                }

            }
        } else if (accesstype.equals("inquiryofficial")) {
            String InquiryOfficialID = data.getString("gcm.notification.InquiryOfficialID");
            if (officialID.equals(InquiryOfficialID)) {
                if (Title.equals("acceptreceivingcase")) {
                    String nameintent = "InqMainActivity";
                    String CaseReportID = data.getString("gcm.notification.CaseReportID");
                    String Investigator = data.getString("gcm.notification.Investigator");

                    String ReceivingCaseDate = getDateTime.changeDateFormatToCalendar(data.getString("gcm.notification.ReceivingCaseDate"));
                    String ReceivingCaseTime = getDateTime.changeTimeFormatToDB(data.getString("gcm.notification.ReceivingCaseTime"));
                    String Address = data.getString("gcm.notification.Address");


                    Log.e(TAG, "Investigator : " + Investigator);
                    String message = "ที่อยู่ " + Address;
                    String bigmessage = "ที่อยู่ " + Address
                            + "\n" + Investigator
                            + "\n" + "เมื่อ " + ReceivingCaseDate + " " + ReceivingCaseTime + " น.";

                    sendNotification("รับคดีไปตรวจ", message, bigmessage, nameintent, "noticecaselistfragment");
                }
                if (Title.equals("newreceivingcase")) {
                    String nameintent = "InqMainActivity";
                    String CaseReportID = data.getString("gcm.notification.CaseReportID");
                    String SubCaseTypeName = data.getString("gcm.notification.SubCaseTypeName");
                    String PoliceStation = data.getString("gcm.notification.PoliceStation");
                    String ReceivingCaseDate = getDateTime.changeDateFormatToCalendar(data.getString("gcm.notification.ReceivingCaseDate"));
                    String ReceivingCaseTime = getDateTime.changeTimeFormatToDB(data.getString("gcm.notification.ReceivingCaseTime"));
                    String Address = data.getString("gcm.notification.Address");

                    Log.e(TAG, "CaseReportID : " + CaseReportID);
                    Log.e(TAG, "SubCaseTypeName : " + SubCaseTypeName);
                    Log.e(TAG, "PoliceStation : " + PoliceStation);
                    Log.e(TAG, "ReceivingCaseDate : " + ReceivingCaseDate);
                    Log.e(TAG, "ReceivingCaseTime : " + ReceivingCaseTime);
                    String message = "คดี " + SubCaseTypeName
                            + "\n" + "จ่ายงานเมื่อ: " + ReceivingCaseDate + " " + ReceivingCaseTime + " น.";
                    String bigmessage = "คดี " + SubCaseTypeName
                            + " " + "สถานีตำรวจ " + PoliceStation
                            + "\n" + "ที่อยู่ " + Address
                            + "\n" + "จ่ายงานเมื่อ " + ReceivingCaseDate + " " + ReceivingCaseTime + " น.";

                    sendNotification("จ่ายงานแล้ว", message, bigmessage, nameintent, "noticecaselistfragment");
                }
                if (Title.equals("reportedcasescene")) {
                    String nameintent = "InqMainActivity";
                    String CaseReportID = data.getString("gcm.notification.CaseReportID");
                    String Investigator = data.getString("gcm.notification.Investigator");

                    String ReceivingCaseDate = getDateTime.changeDateFormatToCalendar(data.getString("gcm.notification.CompleteSceneDate"));
                    String ReceivingCaseTime = getDateTime.changeTimeFormatToDB(data.getString("gcm.notification.CompleteSceneTime"));
                    String Address = data.getString("gcm.notification.Address");
                    Log.e(TAG, "Investigator : " + Investigator);
                    String message = "ที่อยู่ " + Address;
                    String bigmessage = "ที่อยู่ " + Address
                            + "\n" + Investigator
                            + "\n" + "เสร็จเมื่อ " + ReceivingCaseDate + " " + ReceivingCaseTime + " น.";

                    sendNotification("มีคดีจัดทำรายงานเสร็จแล้ว", message, bigmessage, nameintent, "noticecaselistfragment");
                }
            }
        }
        if (Title.equals("newofficial")) {
            String nameintent = null;
            if (accesstype.equals("investigator")) {
                nameintent = "MainActivity";
            }else{
                nameintent = "InqMainActivity";
            }
            String info = data.getString("gcm.notification.info");
            Log.e(TAG, "info : " + info);
            String message = "มีการอัพเดทรายชื่อเจ้าหน้าที่ตำรวจ";
            String bigmessage = "มีการอัพเดทรายชื่อเจ้าหน้าที่ตำรวจ" + "\n" + "ชื่อ " + info;
            sendNotification("กรุณาอัพเดทข้อมูลรายชื่อ", message, bigmessage, nameintent, "settingfragment");
        }
        if (Title.equals("newdata")) {
            String nameintent = null;
            if (accesstype.equals("investigator")) {
                nameintent = "MainActivity";
            }else{
                nameintent = "InqMainActivity";
            }
            String message = "มีการอัพเดทข้อมูลเกี่ยวกับตำรวจ";
            String bigmessage = "ไปที่การตั้งค่าในระบบ เพื่ออัพเดทข้อมูลเกี่ยวกับตำรวจ";
            sendNotification("กรุณาอัพเดทข้อมูลเกี่ยวกับตำรวจ", message, bigmessage, nameintent, "settingfragment");
        }
        if (Title.equals("sendnoti")) {
            String nameintent, namefragment = null;
            if (accesstype.equals("investigator")) {
                nameintent = "MainActivity";
                namefragment = "CaseSceneListFragment";
            }else{
                nameintent = "InqMainActivity";
                namefragment = "NoticeCaseListFragment";
            }
            String subtitle = data.getString("gcm.notification.subtitle");
            String message = data.getString("gcm.notification.message");
            String bigmessage = data.getString("gcm.notification.bigmessage");
            sendNotification(subtitle, message, bigmessage, nameintent, namefragment);
        }
    }

    private void sendNotification(String Title, String message, String bigmessage, String nameintent, String namefragment) {
        Intent intent = null;
        try {
            intent = new Intent(this, Class.forName("com.scdc.csiapp.main." + nameintent));
//            if(namefragment.equals("noticecaselistfragment")) {
//                intent = new Intent(this, Class.forName("com.scdc.csiapp.inqmain." + nameintent));
//            }else if(namefragment.equals("casescenelistfragment")) {
//                intent = new Intent(this, Class.forName("com.scdc.csiapp.invmain." + nameintent));
//            }else if(namefragment.equals("calendarfragment")){
//                intent = new Intent(this, Class.forName("com.scdc.csiapp.schdule." + nameintent));
//            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Intent intent = new Intent(this, WelcomeActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("menuFragment", namefragment);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                0);  //FLAG_UPDATE_CURRENT FLAG_ONE_SHOT PendingIntent.FLAG_UPDATE_CURRENT
// Sets up the Snooze and Dismiss action buttons that will appear in the
// big view of the notification.
      /*  Intent dismissIntent = new Intent(this, WelcomeActivity.class);
        dismissIntent.setAction(CommonConstants.ACTION_DISMISS);
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(this, WelcomeActivity.class);
        snoozeIntent.setAction(CommonConstants.ACTION_SNOOZE);
        PendingIntent piSnooze = PendingIntent.getActivity(this, 0, snoozeIntent,  PendingIntent.FLAG_CANCEL_CURRENT);*/
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            // API 21 up
            Notification.Builder builder = new Notification.Builder(context);
            builder.setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.app_logo)
                    .setContentTitle(Title)
                    .setContentText(message)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(bigmessage))
                    .setAutoCancel(true)
                    .setWhen((new Date()).getTime())
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL);
//            Notification notification = builder.build();
//            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;
//            mNotificationManager =
//                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            mNotificationManager.notify(1001, notification);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1001, builder.build());
        } else {
            //api < 19
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setPriority(NotificationCompat.PRIORITY_MAX) //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
                            .setSmallIcon(R.mipmap.app_logo)
                            .setContentTitle(Title)
                            .setContentText(message)

                            .setWhen((new Date()).getTime())
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission

                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(bigmessage))

          /*      .addAction (R.drawable.ic_delete,
                        "ปิด", piDismiss)
                .addAction (R.drawable.ic_draft,
                        "เปิดดู", piSnooze)*/
                            .setAutoCancel(true);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1001, notificationBuilder.build());

        }
    }


}