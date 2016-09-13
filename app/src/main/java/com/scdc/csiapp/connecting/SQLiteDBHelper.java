package com.scdc.csiapp.connecting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pantearz07 on 17/10/2558.
 */
public class SQLiteDBHelper extends SQLiteAssetHelper {

    SQLiteDatabase mDb;
    SQLiteQueryBuilder mQb;
    private static final String DB_NAME = "db_csi";
    private static final int DB_VERSION = 1;
    // all 32 table
    // official /
    public static final String TABLE_official = "official";
    public static final String COL_OfficialID = "OfficialID";
    public static final String COL_Alias = "Alias";
    public static final String COL_FirstName = "FirstName";
    public static final String COL_LastName = "LastName";
    public static final String COL_Rank = "Rank";
    public static final String COL_Position = "Position";
    public static final String COL_SubPossition = "SubPossition";

    public static final String COL_StationName = "StationName";
    public static final String COL_AgencyName = "AgencyName";
    public static final String COL_CenterName = "CenterName";

    public static final String COL_AreaCodeTel = "AreaCodeTel";
    public static final String COL_PhoneNumber = "PhoneNumber";
    public static final String COL_OfficialEmail = "OfficialEmail";
    public static final String COL_OfficialDisplayPic = "OfficialDisplayPic";
    public static final String COL_Username = "Username";
    public static final String COL_Password = "Password";
    public static final String COL_IMEI = "IMEI";
    public static final String COL_LastLogin = "LastLogin";
    public static final String COL_AccessType = "AccessType";

    // otherofficialinscene
    public static final String TABLE_otherofficialinscene = "otherofficialinscene";
    // ScheduleID,COL_CaseReportID
    // scheduleinvestigate
    public static final String TABLE_scheduleinvestigate = "scheduleinvestigate";
    public static final String COL_ScheduleID = "ScheduleID";
    public static final String COL_Schedule_Date = "Schedule_Date";
    public static final String COL_Schedule_Month = "Schedule_Month";
    //COL_SCDCCenterID
    public static final String TABLE_scheduleofofficial = "scheduleofofficial";
    // ScheduleID,OfficialID

    // TABLE casescenetype
    public static final String TABLE_casescenetype = "casescenetype";
    public static final String COL_CaseTypeID = "CaseTypeID";
    public static final String COL_CaseTypeName = "CaseTypeName";
    // subcasescenetype
    public static final String TABLE_subcasescenetype = "subcasescenetype";
    public static final String COL_SubCaseTypeID = "SubCaseTypeID";
    // CaseTypeID
    public static final String COL_SubCaseTypeName = "SubCaseTypeName";

    // TABLE_casescene /
    public static final String TABLE_CASESCENE = "casescene";
    // COL_SubCaseTypeID
    public static final String COL_CaseReportID = "CaseReportID";
    public static final String COL_InvestigatorOfficialID = "InvestigatorOfficialID";
    public static final String COL_ReportNo = "ReportNo";
    public static final String COL_Year = "Year";
    public static final String COL_ReportStatus = "ReportStatus";
    public static final String COL_NotifiedBy = "NotifiedBy";
    public static final String COL_PoliceProvince = "PoliceProvince";
    public static final String COL_PoliceStation = "PoliceStation";
    public static final String COL_CaseTel = "CaseTel";
    public static final String COL_PersonInvolvedDetail = "PersonInvolvedDetail";
    public static final String COL_FullEvidencePerformed = "FullEvidencePerformed";
    public static final String COL_CircumstanceOfCaseDetail = "CircumstanceOfCaseDetail";
    public static final String COL_Annotation = "Annotation";
    public static final String COL_MaleCriminalNum = "MaleCriminalNum";
    public static final String COL_FemaleCriminalNum = "FemaleCriminalNum";
    public static final String COL_CriminalUsedWeapon = "CriminalUsedWeapon";
    public static final String COL_ToSCDCcenter = "ToSCDCcenter";
    public static final String COL_ToSCDCagency = "ToSCDCagency";
    public static final String COL_LastDateUpdateData = "LastDateUpdateData";
    public static final String COL_LastTimeUpdateData = "LastTimeUpdateData";
    public static final String COL_ConfineSufferer = "ConfineSufferer";
    public static final String COL_VehicleCode = "VehicleCode";

    // datetime /
    public static final String COL_ReceivingCaseDate = "ReceivingCaseDate";
    public static final String COL_ReceivingCaseTime = "ReceivingCaseTime";
    public static final String COL_HappenCaseDate = "HappenCaseDate";
    public static final String COL_HappenCaseTime = "HappenCaseTime";
    public static final String COL_KnowCaseDate = "KnowCaseDate";
    public static final String COL_KnowCaseTime = "KnowCaseTime";
    public static final String COL_CompleteSceneDate = "CompleteSceneDate";
    public static final String COL_CompleteSceneTime = "CompleteSceneTime";
    // sceneinvestigation /
    // public static final String TABLE_sceneinvestigation =
    // "sceneinvestigation";
    public static final String COL_SceneInvestDate = "SceneInvestDate";
    public static final String COL_SceneInvestTime = "SceneInvestTime";

    // address /
    public static final String COL_LocaleName = "LocaleName";
    public static final String COL_HouseNo = "HouseNo";
    public static final String COL_VillageNo = "VillageNo";
    public static final String COL_VillageName = "VillageName";
    public static final String COL_LaneName = "LaneName";
    public static final String COL_RoadName = "RoadName";
    public static final String COL_District = "District";
    public static final String COL_Amphur = "Amphur";
    public static final String COL_Province = "Province";
    public static final String COL_PostalCode = "PostalCode";

    // locale
    public static final String COL_Latitude = "Latitude";
    public static final String COL_Longitude = "Longitude";
    public static final String COL_FeatureInsideDetail = "FeatureInsideDetail";
    // receivingcase
    public static final String TABLE_receivingcase = "receivingcase";
    // public static final String COL_OfficialID = "OfficialID";
    public static final String COL_InquiryOfficialID = "InquiryOfficialID";
    public static final String COL_ReceivingStatus = "ReceivingStatus";
    public static final String COL_EmergencyDate = "EmergencyDate";
    public static final String COL_EmergencyTime = "EmergencyTime";

    // sufferer /
    public static final String TABLE_SUFFERER = "sufferer";
    public static final String COL_SuffererID = "SuffererID";
    public static final String COL_SSN = "SSN";
    public static final String COL_SuffererPrename = "SuffererPrename";
    public static final String COL_SuffererFirstName = "SuffererFirstName";
    public static final String COL_SuffererLastName = "SuffererLastName";
    public static final String COL_SuffererAge = "SuffererAge";
    public static final String COL_SuffererTelephone = "SuffererTelephone";
    public static final String COL_SuffererTelMobile = "SuffererTelMobile";
    public static final String COL_SuffererStatus = "SuffererStatus";
    public static final String COL_DetainDetail = "DetainDetail";
    public static final String COL_BindDetail = "BindDetail";
    public static final String COL_FindInjuredDetail = "FindInjuredDetail";

    // suffererincase /
    public static final String TABLE_SUFFERERINCASE = "suffererincase";

    // resultscene
    public static final String TABLE_resultscene = "resultscene";
    public static final String COL_RSID = "RSID";
    // CaseReportID, RSTypeID
    // public static final String COL_RSSubType = "RSSubType";
    public static final String COL_RSDetail = "RSDetail";

    // resultscenetype
    public static final String TABLE_resultscenetype = "resultscenetype";
    public static final String COL_RSTypeID = "RSTypeID";
    public static final String COL_RSTypeName = "RSTypeName";

    // property loss/
    public static final String TABLE_PROPERTYLOSS = "propertyloss";
    public static final String COL_PropertyLossID = "PropertyLossID";
    // RSID
    public static final String COL_PropertyLossName = "PropertyLossName";
    public static final String COL_PropertyLossNumber = "PropertyLossNumber";
    public static final String COL_PropertyLossUnit = "PropertyLossUnit";
    public static final String COL_PropertyLossPosition = "PropertyLossPosition";
    public static final String COL_PropInsurance = "PropInsurance";

    // find evidence
    public static final String TABLE_FindEevidence = "findevidence";
    public static final String COL_FindEvidenceID = "FindEvidenceID";
    // CaseReportID
    public static final String COL_EvidenceTypeName = "EvidenceTypeName";
    public static final String COL_EvidenceNumber = "EvidenceNumber";
    public static final String COL_FindEvidenceZone = "FindEvidenceZone";
    public static final String COL_FindEvidencecol = "FindEvidencecol";
    public static final String COL_Marking = "Marking";
    public static final String COL_Parceling = "Parceling";
    public static final String COL_EvidencePerformed = "EvidencePerformed";

    // scene feature inside/
    public static final String TABLE_FEATUREIN = "scenefeatureinside";
    // CaseReportID
    public static final String COL_FeatureInsideID = "FeatureInsideID";
    public static final String COL_FloorNo = "FloorNo";
    public static final String COL_CaveNo = "CaveNo";
    public static final String COL_FrontInside = "FrontInside";
    public static final String COL_LeftInside = "LeftInside";
    public static final String COL_RightInside = "RightInside";
    public static final String COL_BackInside = "BackInside";
    public static final String COL_CenterInside = "CenterInside";

    // scenefeature out side/
    public static final String TABLE_FEATUREOUT = "scenefeatureoutside";
    // CaseReportID
    public static final String COL_OutsideTypeName = "OutsideTypeName";
    public static final String COL_OutsideTypeDetail = "OutsideTypeDetail";
    public static final String COL_FloorNum = "FloorNum";
    public static final String COL_CaveNum = "CaveNum";
    public static final String COL_HaveFence = "HaveFence";
    public static final String COL_HaveMezzanine = "HaveMezzanine";
    public static final String COL_HaveRoofTop = "HaveRoofTop";
    public static final String COL_FrontSide = "FrontSide";
    public static final String COL_LeftSide = "LeftSide";
    public static final String COL_RightSide = "RightSide";
    public static final String COL_BackSide = "BackSide";
    public static final String COL_SceneZone = "SceneZone";
    // multimediafile
    public static final String TABLE_multimediafile = "multimediafile";
    public static final String COL_FileID = "FileID";
    // CaseReportID
    public static final String COL_FileType = "FileType";
    public static final String COL_FilePath = "FilePath";
    public static final String COL_FileDescription = "FileDescription";
    public static final String COL_Timestamp = "Timestamp";

    // notification
    public static final String TABLE_notification = "notification";
    public static final String COL_NotificationID = "NotificationID";
    public static final String COL_Receiver_OfficialID = "Receiver_OfficialID";
    public static final String COL_NotificationMessage = "NotificationMessage";
    public static final String COL_NotificationDate = "NotificationDate";
    public static final String COL_NotificationTime = "NotificationTime";
    public static final String COL_NotificationStatus = "NotificationStatus";

    // notificationtype
    public static final String TABLE_notificationtype = "notificationtype";
    public static final String COL_NotificationTypeID = "NotificationTypeID";
    public static final String COL_NotificationTypeName = "NotificationTypeName";

    // photoofevidence
    public static final String TABLE_photoofevidence = "photoofevidence";
    // FindEvidenceID, FileID

    // photoofresultscene
    public static final String TABLE_photoofresultscene = "photoofresultscene";
    // RSID, FileID
    public static final String TABLE_photoofpropertyloss = "photoofproperyloss";
    // PropertyLossID, FileID
    //photoofoutside
    public static final String TABLE_photoofoutside = "photoofoutside";
    //CaseReportID,FileID
    //photoofinside
    public static final String TABLE_photoofinside = "photoofinside";
    //FeatureInsideID,FileID
    // policestation
    public static final String TABLE_policestation = "policestation";
    public static final String COL_PoliceStationID = "PoliceStationID";
    public static final String COL_PoliceStationCode = "PoliceStationCode";
    public static final String COL_PoliceStationName = "PoliceStationName";

    // policeagency
    public static final String TABLE_policeagency = "policeagency";
    public static final String COL_PoliceAgencyID = "PoliceAgencyID";
    public static final String COL_PoliceAgencyCode = "PoliceAgencyCode";
    public static final String COL_PoliceAgencyName = "PoliceAgencyName";

    // policecenter
    public static final String TABLE_policecenter = "policecenter";
    public static final String COL_PoliceCenterID = "PoliceCenterID";
    public static final String COL_PoliceName = "PoliceName";

    // inqposition
    public static final String TABLE_inqposition = "inqposition";
    public static final String COL_InqPosID = "InqPosID";
    public static final String COL_InqPosName = "InqPosName";
    public static final String COL_InqPosAbbr = "InqPosAbbr";
    // invposition
    public static final String TABLE_invposition = "invposition";
    public static final String COL_InvPosID = "InvPosID";
    public static final String COL_InvPosName = "InvPosName";
    public static final String COL_InvPosAbbr = "InvPosAbbr";

    // invposition
    public static final String TABLE_composition = "composition";
    public static final String COL_ComPosID = "ComPosID";
    public static final String COL_ComPosName = "ComPosName";
    public static final String COL_ComPosAbbr = "ComPosAbbr";

    // policerank
    public static final String TABLE_policerank = "policerank";
    public static final String COL_RankID = "RankID";
    public static final String COL_RankName = "RankName";
    public static final String COL_RankAbbr = "RankAbbr";

    // scdcgroup
    public static final String TABLE_scdcgroup = "scdcgroup";
    public static final String COL_SCDCGroupID = "SCDCGroupID";
    public static final String COL_SCDCGroupName = "SCDCGroupName";

    // scdccenter
    public static final String TABLE_scdccenter = "scdccenter";
    public static final String COL_SCDCCenterID = "SCDCCenterID";
    public static final String COL_SCDCCenterName = "SCDCCenterName";
    public static final String COL_SCDCCenterProvince = "SCDCCenterProvince";

    // scdcagency
    public static final String TABLE_scdcagency = "scdcagency";
    public static final String COL_SCDCAgencyID = "SCDCAgencyID";
    public static final String COL_SCDCAgencyCode = "SCDCAgencyCode";
    public static final String COL_SCDCAgencyName = "SCDCAgencyName";

    // district
    public static final String TABLE_DISTRICT = "district";
    public static final String COL_DISTRICT_ID = "DISTRICT_ID";
    public static final String COL_DISTRICT_CODE = "DISTRICT_CODE";
    public static final String COL_DISTRICT_NAME = "DISTRICT_NAME";
    // district
    public static final String TABLE_amphur = "amphur";
    public static final String COL_AMPHUR_ID = "AMPHUR_ID";
    public static final String COL_AMPHUR_CODE = "AMPHUR_CODE";
    public static final String COL_AMPHUR_NAME = "AMPHUR_NAME";
    public static final String COL_POSTCODE = "POSTCODE";
    // province
    public static final String TABLE_PROVINCE = "province";
    public static final String COL_PROVINCE_ID = "PROVINCE_ID";
    public static final String COL_PROVINCE_CODE = "PROVINCE_CODE";
    public static final String COL_PROVINCE_NAME = "PROVINCE_NAME";
    // geography
    public static final String TABLE_geography = "geography";
    public static final String COL_GEO_ID = "GEO_ID";
    public static final String COL_GEO_NAME = "GEO_NAME";

    //
    public static final String TABLE_investigatorsinscene = "investigatorsinscene";
    public static final String COL_InvOfficialID = "InvOfficialID";
//CaseReportID
    public static final String TABLE_schedulegroup = "schedulegroup";
    public static final String COL_ScheduleGroupID = "ScheduleGroupID";
//ScheduleInvestigateID
    public static final String TABLE_scheduleinvestigates = "scheduleinvestigates";
    public static final String COL_ScheduleInvestigateID = "ScheduleInvestigateID";
    public static final String COL_ScheduleDate = "ScheduleDate";
    public static final String COL_ScheduleMonth = "ScheduleMonth";
//SCDCAgencyCode
    //scheduleinvingroup
public static final String TABLE_scheduleinvingroup = "scheduleinvingroup";
    // ScheduleGroupID, InvOfficialID
    public SQLiteDBHelper(Context context) {
        //super(context, DB_NAME, null, DB_VERSION);
        super(context, DB_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DB_VERSION);

    }

    public Cursor getOfficial(String sOfficialID) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"OfficialID"};
        String sqlTables = TABLE_official;

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }

    public long SaveOfficial(String sOfficialID, String sUsername,
                             String sPassword, String sRank, String sFirstName,
                             String sLastName, String sPosition, String sSubPossition,
                             String sStationName, String sAgencyName, String sCenterName,
                             String sAreaCodeTel, String sPhoneNumber, String sOfficialEmail,
                             String sOfficialDisplayPic, String sIMEI, String sLastLogin,
                             String sAccessType, String sAlias) {

        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_official
                    + " WHERE "
                    + "OfficialID = '" + sOfficialID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data


            ContentValues Val = new ContentValues();
            Val.put(SQLiteDBHelper.COL_OfficialID, sOfficialID);
            Val.put(SQLiteDBHelper.COL_Username, sUsername);
            Val.put(SQLiteDBHelper.COL_Password, sPassword);
            Val.put(SQLiteDBHelper.COL_Rank, sRank);
            Val.put(SQLiteDBHelper.COL_FirstName, sFirstName);
            Val.put(SQLiteDBHelper.COL_LastName, sLastName);
            Val.put(SQLiteDBHelper.COL_Position, sPosition);
            Val.put(SQLiteDBHelper.COL_SubPossition, sSubPossition);
            Val.put(SQLiteDBHelper.COL_StationName, sStationName);
            Val.put(SQLiteDBHelper.COL_AgencyName, sAgencyName);
            Val.put(SQLiteDBHelper.COL_CenterName, sCenterName);
            Val.put(SQLiteDBHelper.COL_AreaCodeTel, sAreaCodeTel);
            Val.put(SQLiteDBHelper.COL_PhoneNumber, sPhoneNumber);
            Val.put(SQLiteDBHelper.COL_OfficialEmail, sOfficialEmail);
            Val.put(SQLiteDBHelper.COL_OfficialDisplayPic, sOfficialDisplayPic);
            Val.put(SQLiteDBHelper.COL_IMEI, sIMEI);
            Val.put(SQLiteDBHelper.COL_LastLogin, sLastLogin);
            Val.put(SQLiteDBHelper.COL_AccessType, sAccessType);
            Val.put(SQLiteDBHelper.COL_Alias, sAlias);
            if (cursor.getCount() != 0) {
                Log.i("had official",
                        String.valueOf(cursor.getCount()));
                db.update(TABLE_official, Val, " OfficialID = ?",
                        new String[]{String.valueOf(sOfficialID)});
                db.close();
                rows = 1;
            } else {

                db.insert(TABLE_official, null, Val);
                Log.i("SaveOfficial", "Success");
                db.close();
                rows = 2;
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }

    }

    public String[] checkUser(String sUsername, String sPassword, String sAccessType) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT OfficialID FROM official WHERE  Username='" + sUsername + "'" +
                    " AND Password='" + sPassword + "'" +
                    " AND AccessType='" + sAccessType + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     * 0 = MemberID 1 = Username 2 = Password 3 = MemberName 4 =
                     * Email 5 =Tel
                     */
                    arrData[0] = cursor.getString(0);// OfficialID

                }
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[] SelectDataOfficial(String strOfficialID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            Cursor cursor = mDb.query(TABLE_official, new String[]{"*"},
                    "OfficialID=?",
                    new String[]{String.valueOf(strOfficialID)}, null, null,
                    null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     * 0 = MemberID 1 = Username 2 = Password 3 = MemberName 4 =
                     * Email 5 =Tel
                     */
                    arrData[0] = cursor.getString(0);// alias
                    arrData[1] = cursor.getString(1); // phonnumber
                    arrData[2] = cursor.getString(2); // areacodetel
                    arrData[3] = cursor.getString(3); // subposition
                    arrData[4] = cursor.getString(4); // rank
                    arrData[5] = cursor.getString(5); // officeid
                    arrData[6] = cursor.getString(6); // firstname
                    arrData[7] = cursor.getString(7); // lastname
                    arrData[8] = cursor.getString(8); // position
                    arrData[9] = cursor.getString(9); // stationname
                    arrData[10] = cursor.getString(10); // agencyname
                    arrData[11] = cursor.getString(11); // centername
                    arrData[12] = cursor.getString(12);// officialemail
                    arrData[13] = cursor.getString(13); // officialdisplaypic
                    arrData[14] = cursor.getString(14); // username
                    arrData[15] = cursor.getString(15); // password
                    arrData[16] = cursor.getString(16);// imei
                    arrData[17] = cursor.getString(17);// lastlogin
                    arrData[18] = cursor.getString(18); // accesstype
                }
            }
            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long updateMember(String sMemberID, String sUsername,
                             String sPassword, String sRank, String sFirstName,
                             String sLastName, String sPosition, String sAreaTel, String sTel,
                             String sEmail, String sAvatar, String sStatus) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(SQLiteDBHelper.COL_OfficialID, sMemberID);
            Val.put(SQLiteDBHelper.COL_Username, sUsername);
            Val.put(SQLiteDBHelper.COL_Password, sPassword);
            Val.put(SQLiteDBHelper.COL_Rank, sRank);
            Val.put(SQLiteDBHelper.COL_FirstName, sFirstName);
            Val.put(SQLiteDBHelper.COL_LastName, sLastName);
            Val.put(SQLiteDBHelper.COL_Position, sPosition);
            Val.put(SQLiteDBHelper.COL_AreaCodeTel, sAreaTel);
            Val.put(SQLiteDBHelper.COL_PhoneNumber, sTel);
            Val.put(SQLiteDBHelper.COL_OfficialEmail, sEmail);
            Val.put(SQLiteDBHelper.COL_OfficialDisplayPic, sAvatar);
            Val.put(SQLiteDBHelper.COL_AccessType, sStatus);
            long rows = db.update(TABLE_official, Val, " OfficialID = ?",
                    new String[]{String.valueOf(sMemberID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public Cursor SelectDataPolice(String accesstype) {
        // TODO Auto-generated method stub

        try {

            mDb = this.getReadableDatabase(); // Read Data

           /* Cursor cursor = mDb.query(TABLE_official, new String[]{"*"},
                    "AccessType=?",
                    new String[]{String.valueOf(accesstype)}, null, null,
                    null, null);
*/
            String strSQL = "SELECT * FROM " + TABLE_official + " WHERE AccessType='" + accesstype + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            return cursor;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectDataInspector(String accesstype) {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " + COL_OfficialID + "," + COL_Rank + "," + COL_FirstName + "," + COL_LastName + " FROM " + TABLE_official + " WHERE AccessType='" + accesstype + "'";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        Log.i("spn DataInspector", arrData[i][0] + " " + arrData[i][1] + " " + arrData[i][2] + " " + arrData[i][3]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public ArrayList<HashMap<String, String>> SelectInvestigatorsInCase(
            String sCaseReportID) { // TODO Auto-generated
        // method stub

        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT official.OfficialID, official.Rank, official.FirstName, official.LastName, official.Position" + " FROM " + TABLE_investigatorsinscene
                    + "," + TABLE_official
                    + " WHERE " + TABLE_investigatorsinscene + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TABLE_investigatorsinscene + "." + COL_InvOfficialID + " = " + TABLE_official + "." + COL_OfficialID
                    +" ORDER BY official.OfficialID DESC, official.AccessType ASC";

            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("show", String.valueOf(cursor.getCount()) + " " + strSQL);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put(COL_OfficialID, cursor.getString(0));
                        map.put(COL_Rank, cursor.getString(1));
                        map.put(COL_FirstName, cursor.getString(2));
                        map.put(COL_LastName, cursor.getString(3));
                        map.put(COL_Position, cursor.getString(4));
                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectOtherOfficialInCase(String sReportID) {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_otherofficialinscene + " WHERE " + COL_CaseReportID + " = "
                    + "'" + sReportID + "'";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        Log.i("OtherOfficial", arrData[i][0] + " " + arrData[i][1]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }
    public String[][] Selectinvestigatorsinscene(String sReportID) {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_investigatorsinscene + " WHERE " + COL_CaseReportID + " = "
                    + "'" + sReportID + "'";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        Log.i("investigatorsinscene", arrData[i][0] + " " + arrData[i][1]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }
   public String[][] SelectDataInvestigator(String accesstype) {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " + COL_OfficialID + "," + COL_Rank + "," + COL_FirstName + "," + COL_LastName + " FROM " + TABLE_official + " WHERE AccessType='" + accesstype + "'";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        Log.i("SelectDataInvestigator", arrData[i][0] + " " + arrData[i][1] + " " + arrData[i][2] + " " + arrData[i][3]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }
    public long CheckInvestigatorInCase(String sCaseReportID, String sOfficialID) {
        // TODO Auto-generated method stub

        try {

            mDb = this.getReadableDatabase(); // Read Data
            long rows = 0;
            String strSQL = "SELECT *" + " FROM " + TABLE_otherofficialinscene
                    + " WHERE " + COL_CaseReportID + "= '" + sCaseReportID
                    + "' AND " + COL_OfficialID + " = '" + sOfficialID + "'";
            Log.i("show CheckInvestigator", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            Log.i("CheckInvestigator", String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                rows = 1;
                Log.i("have CheckInvestigator",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = 0;
                Log.i("no haveInvestigator",
                        String.valueOf(cursor.getCount()));
            }
            cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public long CheckInvestigatorsInCase(String sCaseReportID, String sOfficialID) {
        // TODO Auto-generated method stub

        try {

            mDb = this.getReadableDatabase(); // Read Data
            long rows = 0;
            String strSQL = "SELECT *" + " FROM " + TABLE_investigatorsinscene
                    + " WHERE " + COL_CaseReportID + "= '" + sCaseReportID
                    + "' AND " + COL_InvOfficialID + " = '" + sOfficialID + "'";
            Log.i("show CheckInvestigator", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            Log.i("CheckInvestigator", String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                rows = 1;
                Log.i("have CheckInvestigator",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = 0;
                Log.i("no haveInvestigator",
                        String.valueOf(cursor.getCount()));
            }
            cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public ArrayList<HashMap<String, String>> SelectInvestigatorInCase(
            String sScheduleInvestDate, String sCaseReportID) { // TODO Auto-generated
        // method stub

        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            String strSQL;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            if (sCaseReportID == null) {
                strSQL = "SELECT official.OfficialID,official.Rank,official.FirstName,official.LastName,official.Position " +
                        "FROM official,scheduleinvestigate,scheduleofofficial " +
                        "WHERE scheduleofofficial.OfficialID = official.OfficialID AND " +
                        "scheduleofofficial.ScheduleID = scheduleinvestigate.ScheduleID AND scheduleinvestigate.Schedule_Date='" + sScheduleInvestDate + "' " +
                        "ORDER BY official.OfficialID DESC ";
            } else {
                strSQL = "SELECT " +
                        "official.OfficialID, official.Rank, official.FirstName, official.LastName,official.Position " +
                        "FROM otherofficialinscene,scheduleofofficial,scheduleinvestigate,official WHERE otherofficialinscene.CaseReportID='" +
                        sCaseReportID + "' AND scheduleinvestigate.Schedule_Date='" + sScheduleInvestDate + "' AND official.OfficialID = scheduleofofficial.OfficialID AND " +
                        "scheduleinvestigate.ScheduleID= otherofficialinscene.ScheduleID AND scheduleofofficial.ScheduleID=otherofficialinscene.ScheduleID"
                +" ORDER BY official.OfficialID DESC";
            }
            Log.i("show", strSQL);
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put(COL_OfficialID, cursor.getString(0));
                        map.put(COL_Rank, cursor.getString(1));
                        map.put(COL_FirstName, cursor.getString(2));
                        map.put(COL_LastName, cursor.getString(3));
                        map.put(COL_Position, cursor.getString(4));
                        Log.i("show", cursor.getString(0));
                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }

    }

    public long SaveScheduleInvestigate(String sScheduleID, String sSchedule_Date, String sSchedule_Month, String sSCDCCenterID) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scheduleinvestigate
                    + " WHERE ScheduleID = '" + sScheduleID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data


            ContentValues Val = new ContentValues();
            Val.put(COL_ScheduleID, sScheduleID);
            Val.put(COL_Schedule_Date, sSchedule_Date);
            Val.put(COL_Schedule_Month, sSchedule_Month);
            Val.put(COL_SCDCCenterID, sSCDCCenterID);
            Log.i("cursorScheInvestigate",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had ScheduleInvestigate",
                        String.valueOf(cursor.getCount()));

            } else {


                rows = db.insert(TABLE_scheduleinvestigate, null, Val);
                Log.i("scheduleinvestigate", sScheduleID + " " + sSchedule_Date
                        + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public long Savescheduleinvestigates(String ScheduleInvestigateID, String sScheduleDate, String sScheduleMonth, String SCDCAgencyCode) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scheduleinvestigates
                    + " WHERE ScheduleInvestigateID = '" + ScheduleInvestigateID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data


            ContentValues Val = new ContentValues();
            Val.put(COL_ScheduleInvestigateID, ScheduleInvestigateID);
            Val.put(COL_ScheduleDate, sScheduleDate);
            Val.put(COL_ScheduleMonth, sScheduleMonth);
            Val.put(COL_SCDCAgencyCode, SCDCAgencyCode);
            Log.i("cursorScheInvestigates",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had ScheduleInvest",
                        String.valueOf(cursor.getCount()));

            } else {


                rows = db.insert(TABLE_scheduleinvestigates, null, Val);
                Log.i("scheduleinvestigates", ScheduleInvestigateID + " " + sScheduleDate
                        + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public String[] SelectScheduleInvestDate(String sCaseReportID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            Log.i("ScheduleInvestDate", sCaseReportID);

            String strSQL = "SELECT " + TABLE_scheduleinvestigate + "." + COL_ScheduleID
                    + " , " + TABLE_scheduleinvestigate + "." + COL_Schedule_Date
                    + " FROM " + TABLE_otherofficialinscene
                    + " , " + TABLE_scheduleinvestigate
                    + " WHERE " + TABLE_otherofficialinscene + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TABLE_otherofficialinscene + "." + COL_ScheduleID + " =" + TABLE_scheduleinvestigate + "." + COL_ScheduleID;
            Log.i("ScheduleInvestDate", strSQL);

            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);// ScheduleID
                    arrData[1] = cursor.getString(1); // Schedule_Date

                }
            }


            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long SaveScheduleofofficial(String sScheduleID, String sOfficialID) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scheduleofofficial
                    + " WHERE ScheduleID = '" + sScheduleID + "' AND "
                    + "OfficialID = '" + sOfficialID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_ScheduleID, sScheduleID);
            Val.put(COL_OfficialID, sOfficialID);

            Log.i("cursorScheduleofficial",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had Scheduleofofficial",
                        String.valueOf(cursor.getCount()));

            } else {

                rows = db.insert(TABLE_scheduleofofficial, null, Val);
                Log.i("scheduleofofficial", sScheduleID + " " + sOfficialID
                        + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public long Saveschedulegroup(String sScheduleGroupID, String sScheduleInvestigateID) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_schedulegroup
                    + " WHERE ScheduleGroupID = '" + sScheduleGroupID + "' AND "
                    + "ScheduleInvestigateID = '" + sScheduleInvestigateID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_ScheduleGroupID, sScheduleGroupID);
            Val.put(COL_ScheduleInvestigateID, sScheduleInvestigateID);

            Log.i("cursorschedulegroup",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had Saveschedulegroup",
                        String.valueOf(cursor.getCount()));

            } else {

                rows = db.insert(TABLE_schedulegroup, null, Val);
                Log.i("Saveschedulegroup", sScheduleGroupID + " " + sScheduleInvestigateID
                        + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long Savescheduleinvingroup(String sScheduleGroupID, String sOfficialID) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scheduleinvingroup
                    + " WHERE ScheduleGroupID = '" + sScheduleGroupID + "' AND "
                    + "InvOfficialID = '" + sOfficialID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_ScheduleGroupID, sScheduleGroupID);
            Val.put(COL_InvOfficialID, sOfficialID);

            Log.i("cursorScheduleofficial",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had scheduleinvingroup",
                        String.valueOf(cursor.getCount()));

            } else {

                rows = db.insert(TABLE_scheduleinvingroup, null, Val);
                Log.i("Savescheduleinvingroup", sScheduleGroupID + " " + sOfficialID
                        + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long saveScheduleCase(String sCaseReportID,
                                 String sScheduleInvestDate) {
        // TODO Auto-generated method stub

        try {
            mDb = this.getReadableDatabase(); // Read Data
            String ScheduleIDSQL = "SELECT ScheduleID FROM " + TABLE_scheduleinvestigate
                    + " WHERE " + TABLE_scheduleinvestigate + ".Schedule_Date = '" + sScheduleInvestDate + "'";
            Cursor cursorScheduleIDSQL = mDb.rawQuery(ScheduleIDSQL, null);
            String sScheduleID[] = null;
            if (cursorScheduleIDSQL != null) {
                if (cursorScheduleIDSQL.moveToFirst()) {
                    sScheduleID = new String[cursorScheduleIDSQL.getCount()];

                    int i = 0;
                    do {
                        sScheduleID[i] = cursorScheduleIDSQL.getString(0);
                        i++;
                    } while (cursorScheduleIDSQL.moveToNext());

                }
            }
            cursorScheduleIDSQL.close();
            Log.i("sScheduleID", sScheduleID[0]);
            String strSQL = "SELECT * FROM " + TABLE_otherofficialinscene
                    + " WHERE CaseReportID = '" + sCaseReportID + "' AND "
                    + "ScheduleID = '" + sScheduleID[0] + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_ScheduleID, sScheduleID[0]);

            Log.i("cursor saveScheduleCase",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had saveScheduleCase",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_otherofficialinscene, null, Val);
                Log.i("save saveScheduleCase", String.valueOf(rows)
                        + " " + sScheduleID[0] + " " + sCaseReportID);
                db.close();
            }

            cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public long saveInvestigatorsInscene(String sCaseReportID,
                                   String sInvOfficialID) {
        // TODO Auto-generated method stub

        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_investigatorsinscene
                    + " WHERE CaseReportID = '" + sCaseReportID + "' AND "
                    + "InvOfficialID = '" + sInvOfficialID  + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_InvOfficialID, sInvOfficialID);

            Log.i("cursor saveScheduleCase",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had saveScheduleCase",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_investigatorsinscene, null, Val);
                Log.i("save saveScheduleCase", String.valueOf(rows)
                        + " " + sInvOfficialID  + " " + sCaseReportID);
                db.close();
            }

            cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public long saveScheduleINCase(String sCaseReportID,
                                 String sScheduleID) {
        // TODO Auto-generated method stub

        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_otherofficialinscene
                    + " WHERE CaseReportID = '" + sCaseReportID + "' AND "
                    + "ScheduleID = '" + sScheduleID  + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_ScheduleID, sScheduleID);

            Log.i("cursor saveScheduleCase",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had saveScheduleCase",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_otherofficialinscene, null, Val);
                Log.i("save saveScheduleCase", String.valueOf(rows)
                        + " " + sScheduleID  + " " + sCaseReportID);
                db.close();
            }

            cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public long saveScheduleIDCase(String sCaseReportID,
                                   String sScheduleID) {
        // TODO Auto-generated method stub

        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_otherofficialinscene
                    + " WHERE CaseReportID = '" + sCaseReportID + "' AND "
                    + "ScheduleID = '" + sScheduleID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_ScheduleID, sScheduleID);

            Log.i("cursor saveScheduleCase",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had saveScheduleCase",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_otherofficialinscene, null, Val);
                Log.i("save saveScheduleCase", String.valueOf(rows)
                        + " " + sScheduleID + " " + sCaseReportID);
                db.close();
            }

            cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public long editInvestigatorsInscene(String sCaseReportID,
                                         String sOfficialID) {
        // TODO Auto-generated method stub

        try {
            mDb = this.getReadableDatabase(); // Read Data
           /* String strSQL = "SELECT * FROM " + TABLE_investigatorsinscene
                    + " WHERE CaseReportID = '" + sCaseReportID + "' AND "
                    + "InvOfficialID = '" + sOfficialID  + "'";

            Cursor cursor = mDb.rawQuery(strSQL, null);*/
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_InvOfficialID, sOfficialID);

            //Log.i("cursor officialinscene",String.valueOf(cursor.getCount()));

            /*if (cursor.getCount() != 0) {
                Log.i("had officialinscene",
                        String.valueOf(cursor.getCount()));

            } else {*/
                rows = db.insert(TABLE_investigatorsinscene, null, Val);
                Log.i("save officialinscene", String.valueOf(rows)
                        + " " + sOfficialID + " " + sCaseReportID);
                db.close();
            //}

           // cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    public long saveOtherofficialInscene(String sCaseReportID,
                                         String sOfficialID) {
        // TODO Auto-generated method stub

        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_otherofficialinscene
                    + " WHERE CaseReportID = '" + sCaseReportID + "' AND "
                    + "OfficialID = '" + sOfficialID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_OfficialID, sOfficialID);

            Log.i("cursor officialinscene",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had officialinscene",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_otherofficialinscene, null, Val);
                Log.i("save officialinscene", String.valueOf(rows)
                        + " " + sOfficialID + " " + sCaseReportID);
                db.close();
            }

            cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeleteOtherofficialInscene(String sCaseReportID,
                                           String sOfficialID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

           /* long rows = db.delete(TABLE_otherofficialinscene, COL_CaseReportID
                    + " =? AND " + COL_OfficialID + " =?", new String[]{
                    sCaseReportID, sOfficialID});*/
            long rows = db.delete(TABLE_investigatorsinscene, COL_CaseReportID
                    + " =? AND " + COL_InvOfficialID+ " IN(SELECT OfficialID FROM official WHERE AccessType = 'investigator2')", new String[]{
                    sCaseReportID});
            db.close();
            Log.i("DeleteInvestigators", String.valueOf(rows)
                    + sCaseReportID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }
    public void DeleteInvestigatorsInscene(String sCaseReportID) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            //SQLiteStatement insertCmd;
           // String strSQL ="DELETE investigatorsinscene FROM investigatorsinscene,official " +
            //        "WHERE official.OfficialID = investigatorsinscene.InvOfficialID AND official.AccessType = 'investigator2' and investigatorsinscene.CaseReportID = '"+sCaseReportID+"'";
            String strSQL ="DELETE FROM investigatorsinscene WHERE CaseReportID = '"+sCaseReportID+"' AND InvOfficialID IN(SELECT OfficialID FROM official WHERE AccessType = 'investigator2')";
            Log.i("DeleteInvInscene", strSQL);
            //insertCmd = db.compileStatement(strSQL);
          //Log.i("DeleteInvInscene",
              //      String.valueOf(insertCmd.executeUpdateDelete()));
            db.rawQuery(strSQL,null);
            db.close();
            //return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            //return -1;
            Log.i("DeleteInvInscene", "error");
        }
    }
    public long SaveAllInvestigator(String sOfficialID, String sRank,
                                    String sFirstName, String sLastName, String sPosition,
                                    String sSubPossition, String sStationName, String sAgencyName,
                                    String sCenterName, String sAreaCodeTel, String sPhoneNumber,
                                    String sOfficialEmail, String sOfficialDisplayPic,
                                    String sLastLogin, String sAccessType) {

        try {
            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT * FROM " + TABLE_official + " WHERE OfficialID='" + sOfficialID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            Log.i("Investigator", "had " + cursor.getCount());
            if (cursor.getCount() == 0) {
                SQLiteDatabase db;
                db = this.getWritableDatabase(); // Write Data

                ContentValues Val = new ContentValues();
                Val.put(SQLiteDBHelper.COL_OfficialID, sOfficialID);

                Val.put(SQLiteDBHelper.COL_Rank, sRank);
                Val.put(SQLiteDBHelper.COL_FirstName, sFirstName);
                Val.put(SQLiteDBHelper.COL_LastName, sLastName);
                Val.put(SQLiteDBHelper.COL_Position, sPosition);
                Val.put(SQLiteDBHelper.COL_SubPossition, sSubPossition);
                Val.put(SQLiteDBHelper.COL_StationName, sStationName);
                Val.put(SQLiteDBHelper.COL_AgencyName, sAgencyName);
                Val.put(SQLiteDBHelper.COL_CenterName, sCenterName);
                Val.put(SQLiteDBHelper.COL_AreaCodeTel, sAreaCodeTel);
                Val.put(SQLiteDBHelper.COL_PhoneNumber, sPhoneNumber);
                Val.put(SQLiteDBHelper.COL_OfficialEmail, sOfficialEmail);
                Val.put(SQLiteDBHelper.COL_OfficialDisplayPic, sOfficialDisplayPic);

                Val.put(SQLiteDBHelper.COL_LastLogin, sLastLogin);
                Val.put(SQLiteDBHelper.COL_AccessType, sAccessType);
                // Val.put(SQLiteDBHelper.COL_Alias, sAlias);
                long rows = db.insert(TABLE_official, null, Val);

                db.close();

                return rows; // return rows inserted.
            } else {
                Log.i("Recieve", "had " + sOfficialID);
                return -2;
            }

        } catch (Exception e) {
            return -1;
        }

    }

    public String[] SelectReceivingCase(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {

            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data
//, official.Rank, official.FirstName,official.LastName
        /*  String strSQL = "SELECT official.OfficialID, official.Rank, official.FirstName,official.LastName" + " FROM "
                    + TABLE_receivingcase + "," + TABLE_official + " WHERE "
                    + TABLE_receivingcase + "." + COL_CaseReportID + " = " + "'" + sCaseReportID
                    + "' AND "
                    + TABLE_receivingcase + "." + COL_InquiryOfficialID + " = " + TABLE_official + "." + COL_OfficialID;
           */
            String strSQL = "SELECT InquiryOfficialID" + " FROM " + TABLE_receivingcase + " WHERE " + COL_CaseReportID + " = " + "'" + sCaseReportID + "'";

            Log.i("show ReceivingCase", strSQL);

            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    //   arrData[1] = cursor.getString(1);
                    //  arrData[2] = cursor.getString(2);
                    //  arrData[3] = cursor.getString(3);
                }
                Log.i("SelectReceivingCase", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long SaveReceivingCase(String sCaseReportID,
                                  String sInquiryOfficialID, String sReceivingStatus, String sEmergencyDate, String sEmergencyTime) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data
/*
 String strSQL1 = "SELECT OfficialID FROM official WHERE FirstName = '"+sInquiryOfficialFName+"' AND LastName = '"+sInquiryOfficialLName+"'";
            Cursor cursor = mDb.rawQuery(strSQL1, null);
            String arrData[] = null;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    arrData[0] = cursor.getString(0);
                }
                Log.i("show searchInquiry ",
                        String.valueOf(arrData[0]));
            }
 */
            String strSQL = "SELECT * FROM " + TABLE_receivingcase
                    + " WHERE 1 AND CaseReportID = '" + sCaseReportID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            // หาidซ้ำก่อนเเล้วค่อยเซฟ
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_InquiryOfficialID, sInquiryOfficialID);
            Val.put(COL_ReceivingStatus, sReceivingStatus);
            Val.put(COL_EmergencyDate, sEmergencyDate);
            Val.put(COL_EmergencyTime, sEmergencyTime);


            Log.i("cursor receivingcase", String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {

                rows = db.update(TABLE_receivingcase, Val, COL_CaseReportID
                                + " = ?",
                        new String[]{String.valueOf(sCaseReportID)});
                Log.i("update receivingcase",
                        sInquiryOfficialID + " "
                                + String.valueOf(cursor.getCount()) + " "
                                + String.valueOf(sInquiryOfficialID));
                db.close();

            } else {
                rows = db.insert(TABLE_receivingcase, null, Val);
                Log.i("show receivingcase", sCaseReportID + " "
                        + sInquiryOfficialID + " " + String.valueOf(rows));
                db.close();
            }

            cursor.close();

            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] searchInquiryOfficialID(String sReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM "
                    + TABLE_receivingcase + " WHERE " + COL_CaseReportID + " = "
                    + "'" + sReportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                }
                Log.i("show searchInquiry ",
                        String.valueOf(arrData[0]));
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectAllCaseType() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_casescenetype;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableCaseType() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_casescenetype;

            Log.i("show DropTableCaseType", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableCaseType",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectSubCaseType() {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_subcasescenetype;
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);

                        Log.i("show SelectSubCaseType", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long DropTableSubCaseType() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_subcasescenetype;

            Log.i("show Drop SubCaseType", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableSubCaseType",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectPoliceStation() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_policestation;
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("selectPoliceStation", String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        Log.i("selectPoliceStation", arrData[i][3]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTablePoliceStation() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_policestation;

            Log.i("dropTablePoliceStation", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTablePoliceStation",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SavePoliceAgency(String sPoliceAgencyID,
                                 String sPoliceAgencyCode, String sPoliceCenterID,
                                 String sPoliceAgencyName) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_policeagency
                    + " WHERE PoliceAgencyID = '" + sPoliceAgencyID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_PoliceAgencyID, sPoliceAgencyID);
            Val.put(COL_PoliceAgencyCode, sPoliceAgencyCode);
            Val.put(COL_PoliceCenterID, sPoliceCenterID);
            Val.put(COL_PoliceAgencyName, sPoliceAgencyName);
            if (cursor.getCount() != 0) {
                Log.i("had Police Agency",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_policeagency, null, Val);
                Log.i("show Police Agency", sPoliceAgencyID + " "
                        + sPoliceAgencyName + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectPoliceAgency() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_policeagency;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTablePoliceAgency() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_policeagency;

            Log.i("DropTablePoliceAgency", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTablePoliceAgency",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SavePoliceCenter(String sPoliceCenterID, String sPoliceName) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_policecenter
                    + " WHERE PoliceCenterID = '" + sPoliceCenterID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_PoliceCenterID, sPoliceCenterID);
            Val.put(COL_PoliceName, sPoliceName);
            if (cursor.getCount() != 0) {
                Log.i("had PoliceCenter",
                        String.valueOf(cursor.getCount()));

            } else {

                rows = db.insert(TABLE_policecenter, null, Val);
                Log.i("show PoliceCenter", sPoliceCenterID + " " + sPoliceName
                        + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectPoliceCenter() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_policecenter;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTablePoliceCenter() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_policecenter;

            Log.i("DropTablePoliceCenter", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTablePoliceCenter",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SavePoliceRank(String sRankID, String sRankName,
                               String sRankAbbr) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_policerank
                    + " WHERE RankID = '" + sRankID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_RankID, sRankID);
            Val.put(COL_RankName, sRankName);
            Val.put(COL_RankAbbr, sRankAbbr);
            if (cursor.getCount() != 0) {
                Log.i("had Police rank",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_policerank, null, Val);
                Log.i("show Police rank",
                        sRankID + " " + sRankName + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectPoliceRank() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_policerank;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTablePoliceRank() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_policerank;

            Log.i("DropTablePoliceRank", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTablePoliceRank",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveInvPosition(String sInvPosID, String sInvPosName,
                                String sInvPosAbbr) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_invposition
                    + " WHERE InvPosID = '" + sInvPosID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_InvPosID, sInvPosID);
            Val.put(COL_InvPosName, sInvPosName);
            Val.put(COL_InvPosAbbr, sInvPosAbbr);
            if (cursor.getCount() != 0) {
                Log.i("had  invposition",
                        String.valueOf(cursor.getCount()));

            } else {
              rows = db.insert(TABLE_invposition, null, Val);
            Log.i("investigator position", sInvPosID + " " + sInvPosName
                    + " " + String.valueOf(rows));
            db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectInvPosition() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_invposition;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableInvPosition() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_invposition;

            Log.i("DropTableInvposition", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableInvposition",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveInqPosition(String sInqPosID, String sInqPosName,
                                String sInqPosAbbr) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_inqposition
                    + " WHERE InqPosID = '" + sInqPosID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_InqPosID, sInqPosID);
            Val.put(COL_InqPosName, sInqPosName);
            Val.put(COL_InqPosAbbr, sInqPosAbbr);
            if (cursor.getCount() != 0) {
                Log.i("had  inq position",
                        String.valueOf(cursor.getCount()));

            } else {
              rows = db.insert(TABLE_inqposition, null, Val);
            Log.i("show inq position", sInqPosID + " " + sInqPosName + " "
                    + String.valueOf(rows));
            db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectInqPosition() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_inqposition;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableInqPosition() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_inqposition;

            Log.i("DropTableInqposition", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableInqposition",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveComPosition(String sComPosID, String sComPosName,
                                String sComPosAbbr) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_composition
                    + " WHERE  ComPosID = '" + sComPosID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_ComPosID, sComPosID);
            Val.put(COL_ComPosName, sComPosName);
            Val.put(COL_ComPosAbbr, sComPosAbbr);
            if (cursor.getCount() != 0) {
                Log.i("had com position",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_composition, null, Val);
            Log.i("show com position", sComPosID + " " + sComPosName + " "
                    + String.valueOf(rows));
            db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectComPosition() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_composition;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableComPosition() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_composition;

            Log.i(" Drop Composition", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableComposition",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveSCDCAgency(String sSCDCAgencyID, String sSCDCAgencyCode,
                               String sSCDCCenterID, String sSCDCAgencyName) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scdcagency
                    + " WHERE SCDCAgencyID = '" + sSCDCAgencyID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_SCDCAgencyID, sSCDCAgencyID);
            Val.put(COL_SCDCAgencyCode, sSCDCAgencyCode);
            Val.put(COL_SCDCCenterID, sSCDCCenterID);
            Val.put(COL_SCDCAgencyName, sSCDCAgencyName);
            if (cursor.getCount() != 0) {
                Log.i("had SCDCAgency",
                        String.valueOf(cursor.getCount()));

            } else {
            rows = db.insert(TABLE_scdcagency, null, Val);
            Log.i("show SCDCAgency", sSCDCAgencyID + " " + sSCDCAgencyName
                    + " " + String.valueOf(rows));
            db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectSCDCcenterName(String sToSCDCcenterID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scdccenter + " WHERE " + COL_SCDCCenterID + "= '" + sToSCDCcenterID + "'";
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     * 0 = MemberID 1 = Name 2 = Tel
                     */
                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[] SelectSCDCagencyName(String sToSCDCagencyID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scdcagency + " WHERE " + COL_SCDCAgencyCode + "= '" + sToSCDCagencyID + "'";
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("SelectSCDCagencyName", String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     * 0 = MemberID 1 = Name 2 = Tel
                     */
                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[] SelectSCDCAgency() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scdcagency;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(i);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectAllSCDCAgency() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scdcagency;
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("SelectAllSCDCAgency", String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        Log.i("SelectAllSCDCAgency", arrData[i][3]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectAllSCDCCenter() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scdccenter;
            Cursor cursor = db.rawQuery(strSQL, null);
            Log.i("SelectAllSCDCCenter", String.valueOf(cursor.getCount()));
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);

                        Log.i("SelectAllSCDCCenter", arrData[i][2]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableSCDCAgency() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_scdcagency;

            Log.i("DropTableSCDCAgency", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableSCDCAgency",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveSCDCCenter(String sSCDCCenterID, String sSCDCCenterName,
                               String sSCDCCenterProvince) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scdccenter
                    + " WHERE SCDCCenterID = '" + sSCDCCenterID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_SCDCCenterID, sSCDCCenterID);
            Val.put(COL_SCDCCenterName, sSCDCCenterName);
            Val.put(COL_SCDCCenterProvince, sSCDCCenterProvince);
            if (cursor.getCount() != 0) {
                Log.i("had SCDC center",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_scdccenter, null, Val);

                Log.i("show SCDC center", sSCDCCenterID + " " + sSCDCCenterName
                        + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectSCDCCenter() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_scdccenter;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableSCDCCenter() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_scdccenter;

            Log.i("DropTableSCDCcenter", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableSCDCcenter",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectSubTypeCaseID(String SubCaseTypeName) {
        // TODO Auto-generated method stub
        try {

            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT SubCaseTypeID" + " FROM "
                    + TABLE_subcasescenetype + " WHERE 1 AND "
                    + COL_SubCaseTypeName + " LIKE " + "'" + SubCaseTypeName
                    + "'";
            Log.i("show", strSQL);

            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                }
                Log.i("show SubCaseTypeID", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectSubTypeCaseName(String SubCaseTypeID) {
        // TODO Auto-generated method stub
        try {

            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT SubCaseTypeName" + " FROM "
                    + TABLE_subcasescenetype + " WHERE 1 AND "
                    + COL_SubCaseTypeID + " LIKE " + "'" + SubCaseTypeID
                    + "'";
            Log.i("show", strSQL);

            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                }
                Log.i("show SubCaseTypeName", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectResultSceneType() {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_resultscenetype;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableResultSceneType() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_resultscenetype;

            Log.i("Dropresultscenetype", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("resultscenetype",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveAmphur(String sAmphur_ID, String sAmphur_Code,
                           String sAmphur_Name, String sPostCode, String sGEO_ID,
                           String sProvince_ID) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_amphur
                    + " WHERE  Amphur_ID = '" + sAmphur_ID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_AMPHUR_ID, sAmphur_ID);
            Val.put(COL_AMPHUR_CODE, sAmphur_Code);
            Val.put(COL_AMPHUR_NAME, sAmphur_Name);
            Val.put(COL_POSTCODE, sPostCode);
            Val.put(COL_GEO_ID, sGEO_ID);
            Val.put(COL_PROVINCE_ID, sProvince_ID);
            if (cursor.getCount() != 0) {
                Log.i("had Amphur",
                        String.valueOf(cursor.getCount()));

            } else {

                rows = db.insert(TABLE_amphur, null, Val);
                Log.i("show Amphur",
                        sAmphur_ID + " " + sAmphur_Name + " "
                                + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectAllAmphur() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_amphur;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show SelectAllAmphur", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableAmphur() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_amphur;

            Log.i("show DropTableAmphur", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableAmphur",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectAllProvince() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_PROVINCE;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        Log.i("show SelectAllProvince", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableProvince() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_PROVINCE;

            Log.i("show DropTableProvince", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableProvince",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveDistrict(String sDistrict_ID, String sDistrict_Code,
                             String sDistrict_Name, String sAmphur_ID, String sProvince_ID,
                             String sGEO_ID) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_DISTRICT
                    + " WHERE  District_ID = '" + sDistrict_ID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_DISTRICT_ID, sDistrict_ID);
            Val.put(COL_DISTRICT_CODE, sDistrict_Code);
            Val.put(COL_DISTRICT_NAME, sDistrict_Name);
            Val.put(COL_AMPHUR_ID, sAmphur_ID);
            Val.put(COL_PROVINCE_ID, sProvince_ID);
            Val.put(COL_GEO_ID, sGEO_ID);
            if (cursor.getCount() != 0) {
                Log.i("had District",
                        String.valueOf(cursor.getCount()));

            } else {
              rows = db.insert(TABLE_DISTRICT, null, Val);
            Log.i("show District", sDistrict_ID + " " + sDistrict_Name + " "
                    + String.valueOf(rows));
            db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectAllDistrict() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_DISTRICT;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show SelectAllDistrict", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableDistrict() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_DISTRICT;

            Log.i("show DropTableDistrict", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableDistrict",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectAllGeography() {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_geography;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);

                        Log.i("show SelectAllGeography", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long DropTableGeography() {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            SQLiteStatement insertCmd;
            String strSQL = "DELETE FROM " + TABLE_geography;

            Log.i("show DropTableGeography", strSQL);
            insertCmd = db.compileStatement(strSQL);
            Log.i("DropTableGeography",
                    String.valueOf(insertCmd.executeUpdateDelete()));

            db.close();
            return insertCmd.executeUpdateDelete(); // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectProvinceID(String sProvinceName) {
        // TODO Auto-generated method stub
        try {

            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT PROVINCE_ID" + " FROM " + TABLE_PROVINCE
                    + " WHERE 1 AND " + COL_PROVINCE_NAME + " LIKE " + "'"
                    + sProvinceName + "'";
            Log.i("show", strSQL);

            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                }
                Log.i("show PROVINCE_ID", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectAmphurID(String sAmphurName) {
        // TODO Auto-generated method stub
        try {

            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT AMPHUR_ID" + " FROM " + TABLE_amphur
                    + " WHERE 1 AND " + COL_AMPHUR_NAME + " LIKE " + "'"
                    + sAmphurName + "'";
            Log.i("show", strSQL);

            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                }
                Log.i("show sAmphurID2", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectAllDistrictByAmphurID(String sAmphurID) {
        // TODO Auto-generated method stub

        try {
            Log.i("show sAmphurID3", sAmphurID);
            int intAmphurID = 0;
            intAmphurID = Integer.parseInt(sAmphurID);
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_DISTRICT
                    + " WHERE AMPHUR_ID = " + intAmphurID;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show Select DISTRICT ", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectAllAmphurByProvinceID(String sProvinceID) {
        // TODO Auto-generated method stub

        try {
            Log.i("show sProvinceID2", sProvinceID);
            int intProvinceID = 0;
            intProvinceID = Integer.parseInt(sProvinceID);
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_amphur
                    + " WHERE PROVINCE_ID = " + intProvinceID;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show Select Amphur", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long saveDataCaseType(String sCaseTypeID, String sCaseTypeName) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_casescenetype
                    + " WHERE CaseTypeID = '" + sCaseTypeID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            // Val.put(COL_AddressID, sAddressID);
            Val.put(COL_CaseTypeID, sCaseTypeID);
            Val.put(COL_CaseTypeName, sCaseTypeName);

            if (cursor.getCount() != 0) {
                Log.i("had saveDataCaseType",
                        String.valueOf(cursor.getCount()));

            } else {
              rows = db.insert(TABLE_casescenetype, null, Val);
            Log.i("show saveDataCaseType", String.valueOf(rows) + " "
                    + sCaseTypeID);
            db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectCaseType() {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_casescenetype;
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);


                        Log.i("show SelectCaseType", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long saveDataSubCaseType(String sSubCaseTypeID, String sCaseTypeID,
                                    String sSubCaseTypeName) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_subcasescenetype
                    + " WHERE SubCaseTypeID = '" + sSubCaseTypeID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_SubCaseTypeID, sSubCaseTypeID);
            Val.put(COL_CaseTypeID, sCaseTypeID);
            Val.put(COL_SubCaseTypeName, sSubCaseTypeName);
            if (cursor.getCount() != 0) {
                Log.i("had SubCaseType",
                        String.valueOf(cursor.getCount()));

            } else {

              rows = db.insert(TABLE_subcasescenetype, null, Val);
            Log.i("SubCaseType", String.valueOf(rows) + " " + sSubCaseTypeID);
            db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SavePoliceStation(String sPoliceStationID,
                                  String sPoliceStationCode, String sPoliceAgencyCode,
                                  String sPoliceStationName) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_policestation
                    + " WHERE PoliceStationID = '" + sPoliceStationID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_PoliceStationID, sPoliceStationID);
            Val.put(COL_PoliceStationCode, sPoliceStationCode);
            Val.put(COL_PoliceAgencyCode, sPoliceAgencyCode);
            Val.put(COL_PoliceStationName, sPoliceStationName);
            if (cursor.getCount() != 0) {
                Log.i("had Police Station",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_policestation, null, Val);
            Log.i("show Police Station", sPoliceStationID + " "
                    + sPoliceStationName + " " + String.valueOf(rows));
            db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveProvince(String sProvince_ID, String sProvince_Code,
                             String sProvince_Name, String sGEO_ID) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_PROVINCE
                    + " WHERE PROVINCE_ID = '" + sProvince_ID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_PROVINCE_ID, sProvince_ID);
            Val.put(COL_PROVINCE_CODE, sProvince_Code);
            Val.put(COL_PROVINCE_NAME, sProvince_Name);
            Val.put(COL_GEO_ID, sGEO_ID);
            if (cursor.getCount() != 0) {
                Log.i("had Province",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_PROVINCE, null, Val);
                Log.i("show Province", sProvince_ID + " " + sProvince_Name + " "
                        + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveGeography(String sGeo_ID, String sGeo_Name) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_geography
                    + " WHERE GEO_ID = '" + sGeo_ID + "' AND "
                    + "GEO_NAME = '" + sGeo_Name + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_GEO_ID, sGeo_ID);
            Val.put(COL_GEO_NAME, sGeo_Name);
            Log.i("cursorgeography",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had geography",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_geography, null, Val);
                Log.i("show geography",
                        sGeo_ID + " " + sGeo_Name + " " + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveResultSceneType(String sRSTypeID, String sRSTypeName) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT * FROM " + TABLE_resultscenetype
                    + " WHERE RSTypeID = '" + sRSTypeID + "' AND "
                    + "RSTypeName = '" + sRSTypeName + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_RSTypeID, sRSTypeID);
            Val.put(COL_RSTypeName, sRSTypeName);
            if (cursor.getCount() != 0) {
                Log.i("had resultscenetype",
                        String.valueOf(cursor.getCount()));

            } else {
                rows = db.insert(TABLE_resultscenetype, null, Val);
                Log.i("show resultscenetype", sRSTypeID + " " + sRSTypeName + " "
                        + String.valueOf(rows));
                db.close();
            }
            cursor.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }
    // save csireport

    public long saveReportID(String CaseReportID, String reportNo,
                             String officialID, String sYear, String caseType, String subCaseType, String sStatus) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, CaseReportID);
            Val.put(COL_ReportNo, reportNo);
            Val.put(COL_Year, sYear);
            Val.put(COL_CaseTypeName, caseType);
            Val.put(COL_SubCaseTypeName, subCaseType);
            Val.put(COL_InvestigatorOfficialID, officialID);
            Val.put(COL_ReportStatus, sStatus);


            long rows = db.insert(TABLE_CASESCENE, null, Val);
            Log.i("saveReportID", reportNo + " " + sYear + " " + sStatus);

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }

    }

    public long updateEmergency(String sCaseReportID, String sSCDCcenterType,
                                String sSCDCagencyType) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_ToSCDCcenter, sSCDCcenterType);
            Val.put(COL_ToSCDCagency, sSCDCagencyType);
            Val.put(COL_ReportStatus, "receiving");
            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});
            Log.i("updateEmergency", "save ok "+sSCDCcenterType);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateCaseSceneSum(String sCaseReportID, String ReportNo,
                                   String sCaseType, String sSubCaseType, String updateDate, String updateTie) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseTypeName, sCaseType);
            Val.put(COL_SubCaseTypeName, sSubCaseType);
            Val.put(COL_ReportNo, ReportNo);
            Val.put(COL_LastDateUpdateData, updateDate);
            Val.put(COL_LastTimeUpdateData, updateTie);
            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateReportStatus(String sCaseReportID, String reportStatus) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_ReportStatus, reportStatus);
            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateCaseSceneResults(String sCaseReportID, String sCompleteSceneDate,
                                       String sCompleteSceneTime, String sMaleCriminalNum, String sFemaleCriminalNum,
                                       String sCriminalUsedWeapon, String sPersonInvolvedDetail, String sConfineSufferer,
                                       String sFullEvidencePerformed, String sAnnotation, String updateDate, String updateTime) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CompleteSceneDate, sCompleteSceneDate);
            Val.put(COL_CompleteSceneTime, sCompleteSceneTime);

            Val.put(COL_PersonInvolvedDetail, sPersonInvolvedDetail);
            Val.put(COL_FullEvidencePerformed, sFullEvidencePerformed);
            Val.put(COL_ConfineSufferer, sConfineSufferer);
            Val.put(COL_MaleCriminalNum, sMaleCriminalNum);
            Val.put(COL_FemaleCriminalNum, sFemaleCriminalNum);
            Val.put(COL_CriminalUsedWeapon, sCriminalUsedWeapon);
            Val.put(COL_Annotation, sAnnotation);
            Val.put(COL_LastDateUpdateData, updateDate);
            Val.put(COL_LastTimeUpdateData, updateTime);
            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateCaseSceneReceive(String sCaseReportID, String sNotifiedBy, String sCaseTel,
                                       String sStationName, String sLocaleName,
                                       String sHouseNo, String sVillageNo, String sVillageName,
                                       String sLaneName, String sRoadName, String sDistrict,
                                       String sAmphur, String sProvince, String sPostalCode,
                                       String sReceivingCaseDate, String sReceivingCaseTime,
                                       String sHappenCaseDate, String sHappenCaseTime,
                                       String sKnowCaseDate, String sKnowCaseTime,
                                       String sSceneInvestDate, String sSceneInvestTime,
                                       String sCircumstanceOfCaseDetail,
                                       String updateDate, String updateTime) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseTel, sCaseTel);
            Val.put(COL_NotifiedBy, sNotifiedBy);
           // Val.put(COL_PoliceStation, sStationName);
            // Val.put(COL_PoliceProvince, sProvinceName);

            Val.put(COL_LocaleName, sLocaleName);
            Val.put(COL_HouseNo, sHouseNo);
            Val.put(COL_VillageNo, sVillageNo);
            Val.put(COL_VillageName, sVillageName);
            Val.put(COL_LaneName, sLaneName);
            Val.put(COL_RoadName, sRoadName);
            Val.put(COL_District, sDistrict);
            Val.put(COL_Amphur, sAmphur);
            Val.put(COL_Province, sProvince);
            Val.put(COL_PostalCode, sPostalCode);

            Val.put(COL_ReceivingCaseDate, sReceivingCaseDate);
            Val.put(COL_ReceivingCaseTime, sReceivingCaseTime);
            Val.put(COL_HappenCaseDate, sHappenCaseDate);
            Val.put(COL_HappenCaseTime, sHappenCaseTime);
            Val.put(COL_KnowCaseDate, sKnowCaseDate);
            Val.put(COL_KnowCaseTime, sKnowCaseTime);
            Val.put(COL_SceneInvestDate, sSceneInvestDate);
            Val.put(COL_SceneInvestTime, sSceneInvestTime);
            Val.put(COL_CircumstanceOfCaseDetail, sCircumstanceOfCaseDetail);

            Val.put(COL_LastDateUpdateData, updateDate);
            Val.put(COL_LastTimeUpdateData, updateTime);
            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveLocatePolice(String sCaseReportID,
                                 String sLocatePolice) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();

            Val.put(COL_PoliceStation, sLocatePolice);

            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveTypeReceive(String sCaseReportID,
                                String sTypeReceive) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();

            Val.put(COL_NotifiedBy, sTypeReceive);

            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectCaseSceneReport(String sOfficialID) {
        // TODO Auto-generated method stub

        try {
            String arrData[][] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT * FROM " + TABLE_CASESCENE;

           /* String strSQL = "SELECT "+ COL_CaseReportID +", "
                    + COL_ReportNo +", "+ COL_Year +", "+ COL_SubCaseTypeName +", "
                    +", "+ COL_LastDateUpdateData +", "+", "+ COL_LastTimeUpdateData +", "
                    +", "+ COL_PoliceStation
                    +" FROM " + TABLE_CASESCENE + " WHERE 1 AND"
                    + COL_InvestigatorOfficialID + " = '" + sOfficialID + "'"
                    + " AND " +COL_ReportStatus + " = 'investigating'"
                    + " ORDER BY " + COL_LastDateUpdateData
                    + " , " + COL_LastTimeUpdateData + " DESC";*/
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][24] = cursor.getString(24);
                        arrData[i][25] = cursor.getString(25);
                        arrData[i][28] = cursor.getString(28);
                        arrData[i][29] = cursor.getString(29);
                        arrData[i][30] = cursor.getString(30);
                        arrData[i][20] = cursor.getString(20);
                        arrData[i][21] = cursor.getString(21);
                        arrData[i][27] = cursor.getString(27);
                        Log.i("SelectCaseSceneReport", arrData[i][25] + " " + arrData[i][27]);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long updateCaseScene(String sCaseReportID, String ReportNo,
                                String sOfficeID, String sYear, String sReportStatus,
                                String sSubCaseType, String sNotifiedBy, String sCaseTel,
                                String sStationName, String sProvinceName, String sLocaleName,
                                String sHouseNo, String sVillageNo, String sVillageName,
                                String sLaneName, String sRoadName, String sDistrict,
                                String sAmphur, String sProvince, String sPostalCode,
                                String sReceivingCaseDate, String sReceivingCaseTime,
                                String sHappenCaseDate, String sHappenCaseTime,
                                String sKnowCaseDate, String sKnowCaseTime,
                                String sSceneInvestDate, String sSceneInvestTime,
                                String sCompleteSceneDate, String sCompleteSceneTime,
                                String sCircumstanceOfCaseDetail, int sMaleCriminalNum,
                                int sFemaleCriminalNum, String sCriminalUsedWeapon,
                                String sPersonInvolvedDetail, String sFullEvidencePerformed,
                                String sAnnotation, String sToSCDCagency, String sToSCDCcenter,
                                String sLastDateUpdateData, String sLastTimeUpdateData,
                                String sConfineSufferer, String sFeatureInsideDetail,
                                String sVehicleCode, String sCaseType) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseTypeName, sCaseType);
            Val.put(COL_SubCaseTypeName, sSubCaseType);
            Val.put(COL_ReportNo, ReportNo);
            Val.put(COL_Year, sYear);
            Val.put(COL_ReportStatus, sReportStatus);
            Val.put(COL_NotifiedBy, sNotifiedBy);
            Val.put(COL_CaseTel, sCaseTel);

            Val.put(COL_PoliceStation, sStationName);
            Val.put(COL_PoliceProvince, sProvinceName);

            Val.put(COL_LocaleName, sLocaleName);
            Val.put(COL_HouseNo, sHouseNo);
            Val.put(COL_VillageNo, sVillageNo);
            Val.put(COL_VillageName, sVillageName);
            Val.put(COL_LaneName, sLaneName);
            Val.put(COL_RoadName, sRoadName);
            Val.put(COL_District, sDistrict);
            Val.put(COL_Amphur, sAmphur);
            Val.put(COL_Province, sProvince);
            Val.put(COL_PostalCode, sPostalCode);

            Val.put(COL_ReceivingCaseDate, sReceivingCaseDate);
            Val.put(COL_ReceivingCaseTime, sReceivingCaseTime);
            Val.put(COL_HappenCaseDate, sHappenCaseDate);
            Val.put(COL_HappenCaseTime, sHappenCaseTime);
            Val.put(COL_KnowCaseDate, sKnowCaseDate);
            Val.put(COL_KnowCaseTime, sKnowCaseTime);
            Val.put(COL_SceneInvestDate, sSceneInvestDate);
            Val.put(COL_SceneInvestTime, sSceneInvestTime);
            Val.put(COL_CompleteSceneDate, sCompleteSceneDate);
            Val.put(COL_CompleteSceneTime, sCompleteSceneTime);

            Val.put(COL_CircumstanceOfCaseDetail, sCircumstanceOfCaseDetail);
            Val.put(COL_PersonInvolvedDetail, sPersonInvolvedDetail);
            Val.put(COL_FullEvidencePerformed, sFullEvidencePerformed);

            Val.put(COL_MaleCriminalNum, sMaleCriminalNum);
            Val.put(COL_FemaleCriminalNum, sFemaleCriminalNum);
            Val.put(COL_CriminalUsedWeapon, sCriminalUsedWeapon);

            Val.put(COL_Annotation, sAnnotation);
            Val.put(COL_ToSCDCagency, sToSCDCagency);
            Val.put(COL_ToSCDCcenter, sToSCDCcenter);

            Val.put(COL_LastDateUpdateData, sLastDateUpdateData);
            Val.put(COL_LastTimeUpdateData, sLastTimeUpdateData);
            Val.put(COL_ConfineSufferer, sConfineSufferer);
            Val.put(COL_FeatureInsideDetail, sFeatureInsideDetail);
            Val.put(COL_VehicleCode, sVehicleCode);
            Log.i("sStationName", sStationName);
            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateDataLocale(String sCaseReportID, String sLocaleName,
                                 double sLatitude, double sLongitude) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_LocaleName, sLocaleName);
            Val.put(COL_Latitude, sLatitude);
            Val.put(COL_Longitude, sLongitude);
            long rows = db.update(TABLE_CASESCENE, Val, COL_CaseReportID
                    + " = ?", new String[]{String.valueOf(sCaseReportID)});
            Log.i("updateDataLocale", String.valueOf(rows) + " "
                    + sCaseReportID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveCaseReport(String sCaseReportID, String sInvestigatorOfficialID,
                               String sYear, String sReportStatus,
                               String sCaseType,
                               String sSubCaseType, String sNotifiedBy, String sCaseTel,
                               String sStationName, String sProvinceName, String sLocaleName,
                               String sHouseNo, String sVillageNo, String sVillageName,
                               String sLaneName, String sRoadName, String sDistrict,
                               String sAmphur, String sProvince, String sPostalCode,
                               String sLatitude, String sLongitude,
                               String sReceivingCaseDate, String sReceivingCaseTime,
                               String sHappenCaseDate, String sHappenCaseTime,
                               String sKnowCaseDate, String sKnowCaseTime,

                               String sCircumstanceOfCaseDetail, String sToSCDCagency, String sToSCDCcenter,
                               String sLastDateUpdateData, String sLastTimeUpdateData) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_CASESCENE
                    + " WHERE CaseReportID = '" + sCaseReportID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_InvestigatorOfficialID, sInvestigatorOfficialID);
            Val.put(COL_CaseTypeName, sCaseType);
            Val.put(COL_SubCaseTypeName, sSubCaseType);
            Val.put(COL_ReportNo, "");
            Val.put(COL_Year, sYear);
            Val.put(COL_ReportStatus, sReportStatus);
            Val.put(COL_NotifiedBy, sNotifiedBy);
            Val.put(COL_CaseTel, sCaseTel);

            Val.put(COL_PoliceStation, sStationName);
            Val.put(COL_PoliceProvince, sProvinceName);

            Val.put(COL_LocaleName, sLocaleName);
            Val.put(COL_HouseNo, sHouseNo);
            Val.put(COL_VillageNo, sVillageNo);
            Val.put(COL_VillageName, sVillageName);
            Val.put(COL_LaneName, sLaneName);
            Val.put(COL_RoadName, sRoadName);
            Val.put(COL_District, sDistrict);
            Val.put(COL_Amphur, sAmphur);
            Val.put(COL_Province, sProvince);
            Val.put(COL_PostalCode, sPostalCode);
            Val.put(COL_Latitude, sLatitude);
            Val.put(COL_Longitude, sLongitude);

            Val.put(COL_ReceivingCaseDate, sReceivingCaseDate);
            Val.put(COL_ReceivingCaseTime, sReceivingCaseTime);
            Val.put(COL_HappenCaseDate, sHappenCaseDate);
            Val.put(COL_HappenCaseTime, sHappenCaseTime);
            Val.put(COL_KnowCaseDate, sKnowCaseDate);
            Val.put(COL_KnowCaseTime, sKnowCaseTime);
            Val.put(COL_SceneInvestDate, "");
            Val.put(COL_SceneInvestTime, "");
            Val.put(COL_CompleteSceneDate, "");
            Val.put(COL_CompleteSceneTime, "");

            Val.put(COL_CircumstanceOfCaseDetail, sCircumstanceOfCaseDetail);
            Val.put(COL_PersonInvolvedDetail, "");
            Val.put(COL_FullEvidencePerformed, "");

            Val.put(COL_MaleCriminalNum, 0);
            Val.put(COL_FemaleCriminalNum, 0);
            Val.put(COL_CriminalUsedWeapon, "");

            Val.put(COL_Annotation, "");
            Val.put(COL_ToSCDCagency, sToSCDCagency);
            Val.put(COL_ToSCDCcenter, sToSCDCcenter);

            Val.put(COL_LastDateUpdateData, sLastDateUpdateData);
            Val.put(COL_LastTimeUpdateData, sLastTimeUpdateData);
            Val.put(COL_ConfineSufferer, "");
            Val.put(COL_FeatureInsideDetail, "");
            Val.put(COL_VehicleCode, "");
            Log.i("cursor CaseReport",
                    String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {
                Log.i("had CaseReport",
                        String.valueOf(cursor.getCount()));
                ContentValues Val2 = new ContentValues();
                Val2.put(COL_ReportStatus, sReportStatus);
                rows = db.update(TABLE_CASESCENE, Val2, COL_CaseReportID + " = ?",
                        new String[]{String.valueOf(sCaseReportID)});
                Log.i("updateCaseReport", sCaseReportID
                        + " " + String.valueOf(rows));

            } else {


                rows = db.insert(TABLE_CASESCENE, null, Val);
                Log.i("SaveCaseReport", sCaseReportID
                        + " " + String.valueOf(rows));

            }
            db.close();
            cursor.close();
            mDb.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectDataCaseScene(String reportID) {
        // TODO Auto-generated method stub
        Log.i("SelectDataCaseScene", reportID);
        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_CASESCENE
                    + " WHERE " + COL_CaseReportID + " = '" + reportID + "'";
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     * 0 = MemberID 1 = Name 2 = Tel
                     */
                    arrData[0] = cursor.getString(0);  // CaseType
                    arrData[1] = cursor.getString(1); //sVehicleCode
                    arrData[2] = cursor.getString(2); //sFeatureInsideDetail
                    arrData[3] = cursor.getString(3); //sConfineSufferer
                    arrData[4] = cursor.getString(4); //sSceneInvestTime
                    arrData[5] = cursor.getString(5); //sSceneInvestDate
                    arrData[6] = cursor.getString(6); //sLocaleName
                    arrData[7] = cursor.getString(7); //sLastDateUpdateData
                    arrData[8] = cursor.getString(8);//sLastTimeUpdateData
                    arrData[9] = cursor.getString(9);//sCriminalUsedWeapon
                    arrData[10] = cursor.getString(10);//sToSCDCagency
                    arrData[11] = cursor.getString(11);//sToSCDCcenter
                    arrData[12] = cursor.getString(12);//sPostalCode
                    arrData[13] = cursor.getString(13);//sAmphur
                    arrData[14] = cursor.getString(14);//sCompleteSceneTime
                    arrData[15] = cursor.getString(15);//sCompleteSceneDate
                    arrData[16] = cursor.getString(16);//sKnowCaseTime
                    arrData[17] = cursor.getString(17);//sKnowCaseDate
                    arrData[18] = cursor.getString(18);//sHappenCaseTime
                    arrData[19] = cursor.getString(19);//sHappenCaseDate
                    arrData[20] = cursor.getString(20);//sReceivingCaseTime20
                    arrData[21] = cursor.getString(21);//sReceivingCaseDate  21
                    arrData[22] = cursor.getString(22);//COL_PoliceStation
                    arrData[23] = cursor.getString(23); //COL_PoliceProvince
                    arrData[24] = cursor.getString(24);//sNotifiedBy
                    arrData[25] = cursor.getString(25);//sCaseReportID
                    arrData[26] = cursor.getString(26);//sSubCaseType SubCaseTypeName
                    arrData[27] = cursor.getString(27);//InvestigatorOfficialID
                    arrData[28] = cursor.getString(28);//ReportNo
                    arrData[29] = cursor.getString(29);//sYear
                    arrData[30] = cursor.getString(30);//sReportStatus
                    arrData[31] = cursor.getString(31);//sCaseTel
                    arrData[32] = cursor.getString(32);//sHouseNo
                    arrData[33] = cursor.getString(33);//sVillageNo
                    arrData[34] = cursor.getString(34);//sVillageName
                    arrData[35] = cursor.getString(35);//sLaneName
                    arrData[36] = cursor.getString(36); //sRoadName
                    arrData[37] = cursor.getString(37);//sDistrict
                    arrData[38] = cursor.getString(38);//sProvince
                    arrData[39] = cursor.getString(39);//sLatitude
                    arrData[40] = cursor.getString(40);//sLongitude
                    arrData[41] = cursor.getString(41);//sCircumstanceOfCaseDetail
                    arrData[42] = cursor.getString(42);//sPersonInvolvedDetail
                    arrData[43] = cursor.getString(43);//sFullEvidencePerformed
                    arrData[44] = cursor.getString(44);//sAnnotation
                    arrData[45] = cursor.getString(45);//sMaleCriminalNum
                    arrData[46] = cursor.getString(46);//sFemaleCriminalNum

                    Log.i("SelectDataCaseScene", arrData[8] + arrData[25] + "/" + arrData[26] + "/" + arrData[24] + "/ " + arrData[28] + "/ " + arrData[29] + "/ " + arrData[27] + "/ " + arrData[31] + "/ " + arrData[30]);

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[] SelectLocale(String sCaseReportID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " + COL_LocaleName + ", " + COL_Latitude
                    + ", " + COL_Longitude + " FROM " + TABLE_CASESCENE
                    + " WHERE " + COL_CaseReportID + " = " + sCaseReportID;
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long saveDataSufferer(String sCaseReportID, String sSuffererID, String sSSN,
                                 String sSuffererPrename, String sSuffererFirstName,
                                 String sSuffererLastName, String sSuffererAge,
                                 String sSuffererTelephone, String sSuffererTelMobile,
                                 String sSuffererStatus) {
        // TODO Auto-generated method stub
        //Log.i("Sufferer", sSuffererID);
        try {
            //Log.i("Sufferer", sSuffererFirstName);
            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT * FROM " + TABLE_SUFFERER
                    + " WHERE CaseReportID = '" + sCaseReportID + "' AND SuffererID = '"+sSuffererID+"'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;
            //Log.i("Sufferer", sSuffererID);


                SQLiteDatabase db;
                db = this.getWritableDatabase(); // Write Data

                ContentValues Val = new ContentValues();
                Val.put(COL_CaseReportID, sCaseReportID);
                Val.put(COL_SuffererID, sSuffererID);
                Val.put(COL_SSN, sSSN);
                Val.put(COL_SuffererPrename, sSuffererPrename);
                Val.put(COL_SuffererFirstName, sSuffererFirstName);
                Val.put(COL_SuffererLastName, sSuffererLastName);
                Val.put(COL_SuffererAge, sSuffererAge);
                Val.put(COL_SuffererTelephone, sSuffererTelephone);
                Val.put(COL_SuffererTelMobile, sSuffererTelMobile);
                Val.put(COL_SuffererStatus, sSuffererStatus);


                if (cursor.getCount() != 0) {
                    Log.i("had Sufferer",
                            String.valueOf(cursor.getCount()));

                    rows = db.update(TABLE_SUFFERER, Val, COL_SuffererID + " = ?",
                            new String[]{String.valueOf(sSuffererID)});
                    Log.i("updateSufferer", sCaseReportID
                            + " " + String.valueOf(rows) + " " + sSuffererID);
                    db.close();
                } else {

                    rows = db.insert(TABLE_SUFFERER, null, Val);
                    Log.i("show saveSufferer", String.valueOf(rows) + " " + sSuffererID);
                    db.close();
                }


            cursor.close();
            mDb.close();
            return rows; // return rows inserted.



        } catch (Exception e) {
            return -1;
        }
    }


    public String[][] SelectDataSufferer(String sReportID) {
        // TODO Auto - generated method stub

        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT * FROM " + TABLE_SUFFERER + " WHERE " + COL_CaseReportID + " = '" + sReportID + "'";

            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);
//********* มันเพิ่มข้อมูลเปล่ามาอีก1เเถว ฃ
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];
                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0); //COL_CaseReportID
                        arrData[i][1] = cursor.getString(1); //COL_SuffererTelMobile
                        arrData[i][2] = cursor.getString(2); //COL_SuffererPrename
                        arrData[i][3] = cursor.getString(3); //COL_SuffererID
                        arrData[i][4] = cursor.getString(4); //COL_SSN
                        arrData[i][5] = cursor.getString(5); //COL_SuffererFirstName
                        arrData[i][6] = cursor.getString(6); //COL_SuffererLastName
                        arrData[i][7] = cursor.getString(7); //COL_SuffererAge
                        arrData[i][8] = cursor.getString(8); //COL_SuffererTelephone
                        arrData[i][9] = cursor.getString(9); //COL_SuffererStatus
                        Log.i("show", arrData[i][0] + arrData[i][1] + arrData[i][2] + arrData[i][3] +
                                arrData[i][4] + arrData[i][5]);
                        i++;
                    } while (cursor.moveToNext());
                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }


    public ArrayList<HashMap<String, String>> SelectDataSuffererinCase(
            String reportID) {
        // TODO Auto-generated method stub
        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_SUFFERER + " WHERE "
                    + COL_CaseReportID + " = '" + reportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put(COL_CaseReportID, cursor.getString(0));
                        map.put(COL_SuffererTelMobile, cursor.getString(1));
                        map.put(COL_SuffererPrename, cursor.getString(2));
                        map.put(COL_SuffererID, cursor.getString(3));
                        map.put(COL_SSN, cursor.getString(4));
                        map.put(COL_SuffererFirstName, cursor.getString(5));
                        map.put(COL_SuffererLastName, cursor.getString(6));
                        map.put(COL_SuffererAge, cursor.getString(7));
                        map.put(COL_SuffererTelephone, cursor.getString(8));
                        map.put(COL_SuffererStatus, cursor.getString(9));
                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }
    }

    public long DeleteSelectedSufferer(String sSuffererID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_SUFFERER, COL_SuffererID + " = ?",
                    new String[]{String.valueOf(sSuffererID)});

            db.close();
            Log.i("DeleteSelectedSufferer", String.valueOf(rows)
                    + sSuffererID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateDataSufferer(String sSuffererID, String sSuffererPrename,
                                   String sSuffererFirstName, String sSuffererLastName,
                                   String sSuffererAge, String sSuffererTelephone,
                                   String sSuffererTelMobile, String sSuffererStatus) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_SuffererID, sSuffererID);
            Val.put(COL_SuffererPrename, sSuffererPrename);
            Val.put(COL_SuffererFirstName, sSuffererFirstName);
            Val.put(COL_SuffererLastName, sSuffererLastName);
            Val.put(COL_SuffererAge, sSuffererAge);
            Val.put(COL_SuffererTelephone, sSuffererTelephone);
            Val.put(COL_SuffererTelMobile, sSuffererTelMobile);
            Val.put(COL_SuffererStatus, sSuffererStatus);

            long rows = db.update(TABLE_SUFFERER, Val, COL_SuffererID + " = ?",
                    new String[]{String.valueOf(sSuffererID)});
            Log.i("updateData Sufferer", String.valueOf(rows) + " "
                    + sSuffererID + " " + sSuffererPrename + " "
                    + sSuffererFirstName + " " + sSuffererLastName + " "
                    + sSuffererTelephone + " " + sSuffererStatus);

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long saveDataFeatureInside(String sCaseReportID,
                                      String sFeatureInsideID, int floorNo, int caveNo,
                                      String sFrontInside, String sLeftInside, String sRightInside,
                                      String sBackInside, String sCenterInside) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_FeatureInsideID, sFeatureInsideID);
            Val.put(COL_FloorNo, floorNo);
            Val.put(COL_CaveNo, caveNo);
            Val.put(COL_FrontInside, sFrontInside);
            Val.put(COL_LeftInside, sLeftInside);
            Val.put(COL_RightInside, sRightInside);
            Val.put(COL_BackInside, sBackInside);
            Val.put(COL_CenterInside, sCenterInside);
            long rows = db.insert(TABLE_FEATUREIN, null, Val);
            Log.i("saveData FeatureInside", String.valueOf(rows) + " " + " "
                    + floorNo + " " + caveNo + " " + sFrontInside + " " + sLeftInside + " " +
                    sRightInside + " " + sBackInside + " " + sCenterInside
                    + " " + sCaseReportID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateDataSelectedFeatureInside(String sFeatureInsideID,
                                                int floorNo, int caveNo, String sFeatureInsideClassBack,
                                                String sFeatureInsideClassLeft, String sFeatureInsideClassCenter,
                                                String sFeatureInsideClassRight, String sFeatureInsideClassFront) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FloorNo, floorNo);
            Val.put(COL_CaveNo, caveNo);
            Val.put(COL_FrontInside, sFeatureInsideClassFront);
            Val.put(COL_LeftInside, sFeatureInsideClassLeft);
            Val.put(COL_RightInside, sFeatureInsideClassRight);
            Val.put(COL_BackInside, sFeatureInsideClassBack);
            Val.put(COL_CenterInside, sFeatureInsideClassCenter);
            long rows = db
                    .update(TABLE_FEATUREIN, Val, COL_FeatureInsideID + " = ?",
                            new String[]{String.valueOf(sFeatureInsideID)});
            Log.i("saveData FeatureInside", String.valueOf(rows) + " "
                    + sFeatureInsideID + " " + sFeatureInsideClassFront + " "
                    + sFeatureInsideClassLeft + " " + sFeatureInsideClassRight
                    + " " + sFeatureInsideClassBack + " "
                    + sFeatureInsideClassCenter);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public ArrayList<HashMap<String, String>> SelectAllFeatureInside(
            String reportID) {
        // TODO Auto-generated method stub
        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_FEATUREIN + " WHERE "
                    + COL_CaseReportID + " = '" + reportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put(COL_FeatureInsideID, cursor.getString(0));
                        map.put(COL_CaseReportID, cursor.getString(1));
                        map.put(COL_FloorNo, cursor.getString(2));
                        map.put(COL_CaveNo, cursor.getString(3));
                        map.put(COL_FrontInside, cursor.getString(4));
                        map.put(COL_LeftInside, cursor.getString(5));
                        map.put(COL_RightInside, cursor.getString(6));
                        map.put(COL_BackInside, cursor.getString(7));
                        map.put(COL_CenterInside, cursor.getString(8));
                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }
    }

    public long updateDataFeatureInside(String sFeatureInsideID, int floorNo, int caveNo,
                                        String sFrontInside, String sLeftInside, String sRightInside,
                                        String sBackInside, String sCenterInside) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FeatureInsideID, sFeatureInsideID);
            Val.put(COL_FloorNo, floorNo);
            Val.put(COL_CaveNo, caveNo);
            Val.put(COL_FrontInside, sFrontInside);
            Val.put(COL_LeftInside, sLeftInside);
            Val.put(COL_RightInside, sRightInside);
            Val.put(COL_BackInside, sBackInside);
            Val.put(COL_CenterInside, sCenterInside);

            long rows = db.update(TABLE_FEATUREIN, Val, COL_FeatureInsideID + " = ?",
                    new String[]{String.valueOf(sFeatureInsideID)});
            Log.i("update FeatureInside", String.valueOf(rows) + " " + " "
                    + floorNo + " " + caveNo + " " + sFrontInside + " " + sLeftInside + " " +
                    sRightInside + " " + sBackInside + " " + sCenterInside
                    + " " + sFeatureInsideID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectDataFeatureInside1(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_FEATUREIN + " WHERE "
                    + COL_CaseReportID + " = '" + sCaseReportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);
                        arrData[i][6] = cursor.getString(6);
                        arrData[i][7] = cursor.getString(7);
                        arrData[i][8] = cursor.getString(8);

                        Log.i("show FeatureInside", arrData[i][0] + "/" + arrData[i][1]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long DeleteSelectedFeatureInside(String sFeatureInsideID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_FEATUREIN,
                    COL_FeatureInsideID + " = ?",
                    new String[]{String.valueOf(sFeatureInsideID)});

            db.close();
            Log.i("show", String.valueOf(rows) + sFeatureInsideID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long saveDataFeatureOutside(String sCaseReportID,
                                       String sOutsideTypeName, String sOutsideTypeDetail, String FloorNum,
                                       String CaveNum, String sHaveFence, String sHaveMezzanine, String sHaveRoofTop,
                                       String sFrontSide, String sLeftSide, String sRightSide,
                                       String sBackSide, String sSceneZone) {
        // TODO Auto-generated method stub
        try {
            mDb = this.getReadableDatabase(); // Read Data
            Log.i("data saveDetailsData", sCaseReportID + " " +
                    sOutsideTypeName + " " + sOutsideTypeDetail + " " + FloorNum + " " + CaveNum + " " +
                    sHaveFence + " " + sHaveMezzanine + " " + sHaveRoofTop + " " + sFrontSide + " " +
                    sLeftSide + " " + sRightSide + " " + sBackSide + " " + sSceneZone);
            String strSQL = "SELECT * FROM " + TABLE_FEATUREOUT
                    + " WHERE 1 AND CaseReportID = '" + sCaseReportID + "'";
            Cursor cursor = mDb.rawQuery(strSQL, null);
            long rows = 0;

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            // Val.put(COL_FeatureOutsideID, sFeatureOutsideID);
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_OutsideTypeName, sOutsideTypeName);
            Val.put(COL_OutsideTypeDetail, sOutsideTypeDetail);
            Val.put(COL_FloorNum, Integer.parseInt(FloorNum));
            Val.put(COL_CaveNum, Integer.parseInt(CaveNum));
            Val.put(COL_HaveFence, Integer.parseInt(sHaveFence));
            Val.put(COL_HaveMezzanine, Integer.parseInt(sHaveMezzanine));
            Val.put(COL_HaveRoofTop, Integer.parseInt(sHaveRoofTop));
            Val.put(COL_FrontSide, sFrontSide);
            Val.put(COL_LeftSide, sLeftSide);
            Val.put(COL_RightSide, sRightSide);
            Val.put(COL_BackSide, sBackSide);
            Val.put(COL_SceneZone, sSceneZone);

            Log.i("cursor FeatureOutside", String.valueOf(cursor.getCount()));

            if (cursor.getCount() != 0) {

                rows = db.update(TABLE_FEATUREOUT, Val, COL_CaseReportID
                                + " = ?",
                        new String[]{String.valueOf(sCaseReportID)});
                Log.i("updateFeatureOutside", String.valueOf(rows) + " "
                        + sOutsideTypeName + " " + sCaseReportID);
                db.close();
            } else {
                rows = db.insert(TABLE_FEATUREOUT, null, Val);
                Log.i("saveData FeatureOutside", String.valueOf(rows) + " "
                        + sOutsideTypeName + " " + sCaseReportID);
                db.close();
            }

            cursor.close();
            mDb.close();
            return rows; // return rows inserted.

        } catch (Exception e) {

            return -1;
        }
    }

    public String[] SelectDataEachResultscene(String sRSID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT RSDetail FROM " + TABLE_resultscene
                    + " WHERE " + COL_RSID + " = '" + sRSID
                    + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);

                }
                Log.i("show  Resultscene ", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[] SelectDataFeatureOutside(String sCaseReportID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT *" + " FROM " + TABLE_FEATUREOUT
                    + " WHERE " + COL_CaseReportID + " = '" + sCaseReportID
                    + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                    arrData[4] = cursor.getString(4);
                    arrData[5] = cursor.getString(5);
                    arrData[6] = cursor.getString(6);
                    arrData[7] = cursor.getString(7);
                    arrData[8] = cursor.getString(8);
                    arrData[9] = cursor.getString(9);
                    arrData[10] = cursor.getString(10);
                    arrData[11] = cursor.getString(11);
                    arrData[12] = cursor.getString(12);
                }
                Log.i("show strSQL", arrData[0] + "/" + arrData[1] + "/"
                        + arrData[2] + arrData[3] + "/" + arrData[4] + "/"
                        + arrData[5] + "/" + arrData[6] + "/" + arrData[7]
                        + "/" + arrData[8] + "/" + arrData[9] + "/"
                        + arrData[10] + "/" + arrData[11] + "/" + arrData[12]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long updateCaseSceneDetails(String sCaseReportID, String sFeatureInsideDetail,
                                       String updateDate, String updateTime) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FeatureInsideDetail, sFeatureInsideDetail);

            Val.put(COL_LastDateUpdateData, updateDate);
            Val.put(COL_LastTimeUpdateData, updateTime);
            long rows = db.update(TABLE_CASESCENE, Val, " CaseReportID = ?",
                    new String[]{String.valueOf(sCaseReportID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long SaveResultScene(String sCaseReportID, String sRSID,
                                String sRSTypeID, String sRSDetail) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_RSID, sRSID);
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_RSTypeID, sRSTypeID);
            // Val.put(COL_RSSubType, sRSSubType);
            Val.put(COL_RSDetail, sRSDetail);

            long rows = db.insert(TABLE_resultscene, null, Val);
            Log.i("show resultscene", sRSID + " " + sRSTypeID + " " + sRSDetail);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateDataSelectedResultScene(String sRSID,
                                              String sCaseReportID, String sRSTypeID, String sRSDetail) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_RSID, sRSID);
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_RSTypeID, sRSTypeID);
            Val.put(COL_RSDetail, sRSDetail);

            long rows = db.update(TABLE_resultscene, Val, COL_RSID + " = ?",
                    new String[]{String.valueOf(sRSID)});
            Log.i("updateresultscene", String.valueOf(rows) + " "
                    + sRSID + " " + sCaseReportID + " " + sRSTypeID + " "
                    + sRSDetail);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectDataResultScene(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_resultscene
                    + " WHERE 1 AND " + COL_CaseReportID + " = '"
                    + sCaseReportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        Log.i("show ResultScene", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<HashMap<String, String>> SelectAllResultScene(
            String sCaseReportID, String sRSTypeID) {
        // TODO Auto-generated method stub
        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_resultscene
                    + " WHERE 1 AND " + COL_CaseReportID + " = '"
                    + sCaseReportID + "' AND " + COL_RSTypeID + " = '"
                    + sRSTypeID + "'";
            Log.i("show sRSTypeID strSQL", strSQL);
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put(COL_RSID, cursor.getString(0));
                        map.put(COL_CaseReportID, cursor.getString(1));
                        map.put(COL_RSTypeID, cursor.getString(2));
                        map.put(COL_RSDetail, cursor.getString(3));
                        // Log.i("show RSDetail", cursor.getString(3));

                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }
    }

    public long DeleteSelectedResultScene(String sRSID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_resultscene, COL_RSID + " = ?",
                    new String[]{sRSID});

            db.close();
            Log.i("Delete ResultScene", String.valueOf(rows)
                    + sRSID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[] SelectDataEachPropertyLoss(String sPropertyLossID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_PROPERTYLOSS
                    + " WHERE " + COL_PropertyLossID + " = '" + sPropertyLossID
                    + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                    arrData[4] = cursor.getString(4);
                    arrData[5] = cursor.getString(5);
                    arrData[6] = cursor.getString(6);

                }
                Log.i("show PropertyLoss ", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public long saveDataPropertyloss(String sPropertyLossID, String sCaseReportID,
                                     String sPropertyLossName,
                                     String sPropertyLossNumber, String sPropertyLossUnit,
                                     String sPropertyLossPosition, String sPropInsurance) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            Log.i("saveDataPropertyloss", sPropertyLossID);
            ContentValues Val = new ContentValues();
            Val.put(COL_PropertyLossID, sPropertyLossID);
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_PropertyLossName, sPropertyLossName);
            Val.put(COL_PropertyLossNumber, sPropertyLossNumber);
            Val.put(COL_PropertyLossUnit, sPropertyLossUnit);
            Val.put(COL_PropertyLossPosition, sPropertyLossPosition);
            Val.put(COL_PropInsurance, sPropInsurance);

            long rows = db.insert(TABLE_PROPERTYLOSS, null, Val);
            Log.i("saveData Propertyloss", String.valueOf(rows) + " "
                    + sPropertyLossID + " " + sPropertyLossName + " "
                    + sPropertyLossNumber + " " + sPropertyLossUnit + " "
                    + sPropertyLossPosition + " " + sCaseReportID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public ArrayList<HashMap<String, String>> SelectAllPropertyloss(
            String sCaseReportID) {
        // TODO Auto-generated method stub
        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_PROPERTYLOSS + " WHERE "
                    + COL_CaseReportID + " = '" + sCaseReportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put(COL_PropertyLossID, cursor.getString(0));
                        map.put(COL_CaseReportID, cursor.getString(1));
                        map.put(COL_PropertyLossName, cursor.getString(2));
                        map.put(COL_PropertyLossNumber, cursor.getString(3));
                        map.put(COL_PropertyLossUnit, cursor.getString(4));
                        map.put(COL_PropertyLossPosition, cursor.getString(5));
                        map.put(COL_PropInsurance, cursor.getString(6));
                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectDataPropertyLoss(String sReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_PROPERTYLOSS + " WHERE "
                    + COL_CaseReportID + " = '" + sReportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);
                        arrData[i][6] = cursor.getString(6);
                        Log.i("show PropertyLoss", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long updateDataSelectedPropertyLoss(String sPropertyLossID,
                                               String sPropertyLossName, String sPropertyLossNumber,
                                               String sPropertyLossUnit, String sPropertyLossPosition,
                                               String sPropInsurance) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_PropertyLossName, sPropertyLossName);
            Val.put(COL_PropertyLossNumber, sPropertyLossNumber);
            Val.put(COL_PropertyLossUnit, sPropertyLossUnit);
            Val.put(COL_PropertyLossPosition, sPropertyLossPosition);
            Val.put(COL_PropInsurance, sPropInsurance);

            long rows = db.update(TABLE_PROPERTYLOSS, Val, COL_PropertyLossID
                    + " = ?", new String[]{String.valueOf(sPropertyLossID)});
            Log.i("update PropertyLoss", String.valueOf(rows) + " "
                    + sPropertyLossID + " " + sPropertyLossName + " "
                    + COL_PropertyLossNumber);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeleteSelectedPropertyLoss(String sPropertyLossID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_PROPERTYLOSS, COL_PropertyLossID
                    + " = ?", new String[]{String.valueOf(sPropertyLossID)});

            db.close();
            Log.i("show", String.valueOf(rows) + sPropertyLossID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long saveDataFindEvidence(String sCaseReportID,
                                     String sFindEvidenceID, String sEvidenceTypeName,
                                     String sEvidenceNumber, String sFindEvidenceZone, String sMarking,
                                     String sParceling, String sEvidencePerformed) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FindEvidenceID, sFindEvidenceID);
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_EvidenceTypeName, sEvidenceTypeName);
            Val.put(COL_EvidenceNumber, sEvidenceNumber);
            Val.put(COL_FindEvidenceZone, sFindEvidenceZone);
            // Val.put(COL_FindEvidencecol, sFindEvidenceCol);
            Val.put(COL_Marking, sMarking);
            Val.put(COL_Parceling, sParceling);
            Val.put(COL_EvidencePerformed, sEvidencePerformed);

            long rows = db.insert(TABLE_FindEevidence, null, Val);
            Log.i("saveData FindEvidence", String.valueOf(rows) + " "
                    + sFindEvidenceID + " " + sEvidenceTypeName + " "
                    + sEvidenceNumber + " " + sFindEvidenceZone + " "
                    + sCaseReportID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateDataSelectedFindEvidence(String sFindEvidenceID,
                                               String sEvidenceTypeName, String sEvidenceNumber,
                                               String sFindEvidenceZone, String sMarking, String sParceling,
                                               String sEvidencePerformed) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_EvidenceTypeName, sEvidenceTypeName);
            Val.put(COL_EvidenceNumber, sEvidenceNumber);
            Val.put(COL_FindEvidenceZone, sFindEvidenceZone);
            // Val.put(COL_FindEvidencecol, sFindEvidenceCol);
            Val.put(COL_Marking, sMarking);
            Val.put(COL_Parceling, sParceling);
            Val.put(COL_EvidencePerformed, sEvidencePerformed);

            long rows = db.update(TABLE_FindEevidence, Val, COL_FindEvidenceID
                    + " = ?", new String[]{String.valueOf(sFindEvidenceID)});
            Log.i("update FindEvidence", String.valueOf(rows) + " "
                    + sFindEvidenceID + " " + sEvidenceTypeName + " "
                    + sEvidenceNumber + " " + sFindEvidenceZone);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectDataFindEvidence(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_FindEevidence + " WHERE "
                    + COL_CaseReportID + " = '" + sCaseReportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);
                        arrData[i][6] = cursor.getString(6);
                        arrData[i][7] = cursor.getString(7);
                        Log.i("show FindEvidence", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectDataEachEvidence(String sEvidencesID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT*FROM " + TABLE_FindEevidence
                    + " WHERE " + COL_FindEvidenceID + " = '" + sEvidencesID
                    + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                    arrData[4] = cursor.getString(4);
                    arrData[5] = cursor.getString(5);
                    arrData[6] = cursor.getString(6);

                }
                Log.i("show  Evidence ", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public ArrayList<HashMap<String, String>> SelectAllEvidences(
            String sCaseReportID) {
        // TODO Auto-generated method stub
        try {

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_FindEevidence + " WHERE "
                    + COL_CaseReportID + " = '" + sCaseReportID + "'";
            Log.i("show", strSQL);
            Cursor cursor = db.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        map = new HashMap<String, String>();
                        map.put(COL_FindEvidenceID, cursor.getString(0));
                        map.put(COL_CaseReportID, cursor.getString(1));
                        map.put(COL_EvidenceTypeName, cursor.getString(2));
                        map.put(COL_EvidenceNumber, cursor.getString(3));
                        map.put(COL_FindEvidenceZone, cursor.getString(4));
                        // map.put(COL_FindEvidencecol, cursor.getString(5));
                        map.put(COL_Marking, cursor.getString(5));
                        map.put(COL_Parceling, cursor.getString(6));
                        map.put(COL_EvidencePerformed, cursor.getString(7));

                        MyArrList.add(map);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;
        }
    }

    public long DeleteSelectedEvidences(String sFindEvidenceID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_FindEevidence, COL_FindEvidenceID
                    + " = ?", new String[]{String.valueOf(sFindEvidenceID)});

            db.close();
            Log.i("DeleteEvidences", String.valueOf(rows)
                    + sFindEvidenceID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public void DeleteReport1(String reportID, String sTable) {
        // TODO Auto-generated method stub
        SQLiteDatabase db;
        db = this.getWritableDatabase(); // Write Data
/*
        long rows = db.delete(sTable, COL_CaseReportID
                + " =?", new String[] {reportID});
        Log.i("show DeleteReport1 "+sTable, String.valueOf(rows));
*/
        SQLiteStatement insertCmd;
        String strSQL = "DELETE FROM " + sTable +
                " WHERE CaseReportID =?";

        insertCmd = db.compileStatement(strSQL);
        insertCmd.bindString(1, reportID);
        Log.i("show",
                strSQL + String.valueOf(insertCmd.executeUpdateDelete()));
        db.close();
    }

    public void DeleteReport2(String sCol, String sID, String sTable) { //
        // TODO Auto-generated method stub

        SQLiteDatabase db;
        db = this.getWritableDatabase(); // Write Data
        long rows = db.delete(sTable, sCol
                + " =?", new String[]{sID});
        Log.i("show DeleteReport2 " + sTable, String.valueOf(rows));
/*
        SQLiteStatement insertCmd;
        String strSQL = "DELETE FROM " + sTable +
                " WHERE " + sCol + " = ? ";

        Log.i("show", strSQL);
        insertCmd = db.compileStatement(strSQL);
        insertCmd.bindString(1, sID);
        Log.i("show",
                String.valueOf(insertCmd.executeUpdateDelete()));*/
        db.close();

    }

    public long saveDataMultimediaifile(String sCaseReportID, String sFileID,
                                        String sFileType, String sFilePath, String sFileDescription,
                                        String sTimeStamp) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FileID, sFileID);
            Val.put(COL_CaseReportID, sCaseReportID);
            Val.put(COL_FileType, sFileType);
            Val.put(COL_FilePath, sFilePath);
            Val.put(COL_FileDescription, sFileDescription);
            Val.put(COL_Timestamp, sTimeStamp);

            long rows = db.insert(TABLE_multimediafile, null, Val);
            Log.i("save multimediafile", String.valueOf(rows) + " " + sCaseReportID + " " + sFileID
                    + " " + sFileType + " " + sFilePath + " "
                    + sFileDescription);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long updateDataMultimediaifile(String sFileID, String sFileDescription, String sTimeStamp) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();

            Val.put(COL_FileDescription, sFileDescription);
            Val.put(COL_Timestamp, sTimeStamp);
            long rows = db.update(TABLE_multimediafile, Val, " FileID = ?",
                    new String[]{String.valueOf(sFileID)});

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long saveDataPhotoOfEvidence(String sFindEvidenceID, String sFileID) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FileID, sFileID);
            Val.put(COL_FindEvidenceID, sFindEvidenceID);

            long rows = db.insert(TABLE_photoofevidence, null, Val);
            Log.i("save photoofevidence", String.valueOf(rows) + " " + sFileID
                    + " " + sFindEvidenceID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectDataPhotoOfEvidences(String sCaseReportID, String sEvidencesID,
                                                 String sFileType) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " +
                    TABLE_multimediafile + "." + COL_FileID + " , " +
                    TABLE_multimediafile + "." + COL_CaseReportID + " , " +
                    TABLE_multimediafile + "." + COL_FileType + " , " +
                    TABLE_multimediafile + "." + COL_FilePath + " , " +
                    TABLE_multimediafile + "." + COL_FileDescription + " , " +
                    TABLE_multimediafile + "." + COL_Timestamp +
                    " FROM "
                    + TABLE_multimediafile + "," + TABLE_photoofevidence + " WHERE "
                    + TABLE_photoofevidence + "." + COL_FindEvidenceID + " = '" + sEvidencesID + "' AND "
                    + TABLE_multimediafile + "." + COL_FileID + " = " + TABLE_photoofevidence + "." + COL_FileID + " AND "
                    + TABLE_multimediafile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TABLE_multimediafile + "." + COL_FileID + " DESC";
            Log.i("show Evidences", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show Evidences", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectDataPhotoOfEachPropertyloss(String sCaseReportID, String sPropertylossID,
                                                        String sFileType) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " +
                    TABLE_multimediafile + "." + COL_FileID + " , " +
                    TABLE_multimediafile + "." + COL_CaseReportID + " , " +
                    TABLE_multimediafile + "." + COL_FileType + " , " +
                    TABLE_multimediafile + "." + COL_FilePath + " , " +
                    TABLE_multimediafile + "." + COL_FileDescription + " , " +
                    TABLE_multimediafile + "." + COL_Timestamp +
                    " FROM "
                    + TABLE_multimediafile + "," + TABLE_photoofpropertyloss + " WHERE "
                    + TABLE_photoofpropertyloss + "." + COL_PropertyLossID + " = '" + sPropertylossID + "' AND "
                    + TABLE_multimediafile + "." + COL_FileID + " = " + TABLE_photoofpropertyloss + "." + COL_FileID + " AND "
                    + TABLE_multimediafile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TABLE_multimediafile + "." + COL_FileID + " DESC";
            Log.i("show propertyloss", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show propertyloss", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectDataPhotoOfEachEvidence(String sCaseReportID, String sFindEvidenceID,
                                                    String sFileType) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " +
                    TABLE_multimediafile + "." + COL_FileID + " , " +
                    TABLE_multimediafile + "." + COL_CaseReportID + " , " +
                    TABLE_multimediafile + "." + COL_FileType + " , " +
                    TABLE_multimediafile + "." + COL_FilePath + " , " +
                    TABLE_multimediafile + "." + COL_FileDescription + " , " +
                    TABLE_multimediafile + "." + COL_Timestamp +
                    " FROM "
                    + TABLE_multimediafile + "," + TABLE_photoofevidence + " WHERE "
                    + TABLE_photoofevidence + "." + COL_FindEvidenceID + " = '" + sFindEvidenceID + "' AND "
                    + TABLE_multimediafile + "." + COL_FileID + " = " + TABLE_photoofevidence + "." + COL_FileID + " AND "
                    + TABLE_multimediafile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TABLE_multimediafile + "." + COL_FileID + " DESC";
            Log.i("show FindEvidence", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show FindEvidence", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long saveDataPhotoOfPropertyloss(String sPropertyLossID, String sFileID) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FileID, sFileID);
            Val.put(COL_PropertyLossID, sPropertyLossID);

            long rows = db.insert(TABLE_photoofpropertyloss, null, Val);
            Log.i("save Propertyloss", String.valueOf(rows) + " " + sFileID
                    + " " + sPropertyLossID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long saveDataPhotoOfOutside(String sCaseReportID, String sFileID) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FileID, sFileID);
            Val.put(COL_CaseReportID, sCaseReportID);

            long rows = db.insert(TABLE_photoofoutside, null, Val);
            Log.i("save PhotoOfOutside", String.valueOf(rows) + " " + sFileID
                    + " " + sCaseReportID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectDataPhotoOfOutside(String sCaseReportID,
                                               String sFileType) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " +
                    TABLE_multimediafile + "." + COL_FileID + " , " +
                    TABLE_multimediafile + "." + COL_CaseReportID + " , " +
                    TABLE_multimediafile + "." + COL_FileType + " , " +
                    TABLE_multimediafile + "." + COL_FilePath + " , " +
                    TABLE_multimediafile + "." + COL_FileDescription + " , " +
                    TABLE_multimediafile + "." + COL_Timestamp +
                    " FROM "

                    + TABLE_multimediafile + "," + TABLE_photoofoutside + " WHERE "
                    + TABLE_multimediafile + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TABLE_photoofoutside + "." + COL_CaseReportID + " = " + TABLE_multimediafile + "." + COL_CaseReportID + " AND "
                    + TABLE_multimediafile + "." + COL_FileID + " = " + TABLE_photoofoutside + "." + COL_FileID + " AND "
                    + TABLE_multimediafile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TABLE_multimediafile + "." + COL_FileID + " DESC";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show Photooutside", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long DeletePhotoOfOutside(String sCaseReportID,
                                     String sFileID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_photoofoutside, COL_CaseReportID
                    + " =? AND " + COL_FileID + " =?", new String[]{
                    sCaseReportID, sFileID});

            db.close();
            Log.i("Deletephotoofoutside", String.valueOf(rows)
                    + sCaseReportID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long saveDataPhotoOfInside(String sFeatureInsideID, String sFileID) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FeatureInsideID, sFeatureInsideID);
            Val.put(COL_FileID, sFileID);


            long rows = db.insert(TABLE_photoofinside, null, Val);
            Log.i("save PhotoOfInside", String.valueOf(rows) + " " + sFileID
                    + " " + sFeatureInsideID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectDataPhotoOfInside(String sCaseReportID, String sFeatureInsideID,
                                              String sFileType) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " +
                    TABLE_multimediafile + "." + COL_FileID + " , " +
                    TABLE_multimediafile + "." + COL_CaseReportID + " , " +
                    TABLE_multimediafile + "." + COL_FileType + " , " +
                    TABLE_multimediafile + "." + COL_FilePath + " , " +
                    TABLE_multimediafile + "." + COL_FileDescription + " , " +
                    TABLE_multimediafile + "." + COL_Timestamp +
                    " FROM "
                    + TABLE_multimediafile + "," + TABLE_photoofinside + " WHERE "
                    + TABLE_photoofinside + "." + COL_FeatureInsideID + " = '" + sFeatureInsideID + "' AND "
                    + TABLE_multimediafile + "." + COL_FileID + " = " + TABLE_photoofinside + "." + COL_FileID + " AND "
                    + TABLE_multimediafile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TABLE_multimediafile + "." + COL_FileID + " DESC";
            Log.i("show inside", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show inside", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectDataFeatureInside(String sFeatureInsideID) {
        // TODO Auto-generated method stub
        try {

            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data


            String strSQL = "SELECT * FROM " + TABLE_FEATUREIN + " WHERE "
                    + COL_FeatureInsideID + " = '" + sFeatureInsideID + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                    arrData[4] = cursor.getString(4);
                    arrData[5] = cursor.getString(5);
                    arrData[6] = cursor.getString(6);
                    arrData[7] = cursor.getString(7);
                }
                Log.i("show FeatureInside", arrData[0]);
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long DeletePhotoOfEvidences(String sEvidencesID,
                                       String sFileID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_photoofevidence, COL_FindEvidenceID
                    + " =? AND " + COL_FileID + " =?", new String[]{
                    sEvidencesID, sFileID});

            db.close();
            Log.i("Deletephoto Evidences ", String.valueOf(rows)
                    + sEvidencesID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeletePhotoOfPropertyloss(String sPropertyLossID,
                                          String sFileID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_photoofpropertyloss, COL_PropertyLossID
                    + " =? AND " + COL_FileID + " =?", new String[]{
                    sPropertyLossID, sFileID});

            db.close();
            Log.i("Deletephoto Property ", String.valueOf(rows)
                    + sPropertyLossID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeletePhotoOfInside(String sFeatureInsideID,
                                    String sFileID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_photoofinside, COL_FeatureInsideID
                    + " =? AND " + COL_FileID + " =?", new String[]{
                    sFeatureInsideID, sFileID});

            db.close();
            Log.i("Deletephotoofinside", String.valueOf(rows)
                    + sFeatureInsideID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeletePhotoOfAllEvidence(String sFindEvidenceID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            mDb = this.getReadableDatabase(); // read Data
            String strSQL = "SELECT " + COL_FileID + " FROM " + TABLE_photoofevidence
                    + " WHERE " + COL_FindEvidenceID + " = '" + sFindEvidenceID + "'";
            Log.i("show FindEvidence", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);

                        long rows1 = db.delete(TABLE_multimediafile, COL_FileID
                                + " =?", new String[]{arrData[i][0]});
                        Log.i("Delete FindEvidence", arrData[i][0] + " " + rows1);
                        i++;
                    } while (cursor.moveToNext());

                }
                rows = db.delete(TABLE_photoofevidence, COL_RSID
                        + " =?", new String[]{sFindEvidenceID});

                Log.i("DeletephotoFindEvidence", String.valueOf(rows)
                        + sFindEvidenceID);
            }
            cursor.close();
            mDb.close();
            db.close();
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeletePhotoOfAllResultScene(String sRSID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            mDb = this.getReadableDatabase(); // read Data
            String strSQL = "SELECT " + COL_FileID + " FROM " + TABLE_photoofresultscene
                    + " WHERE " + COL_RSID + " = '" + sRSID + "'";
            Log.i("show resultscene", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);

                        long rows1 = db.delete(TABLE_multimediafile, COL_FileID
                                + " =?", new String[]{arrData[i][0]});
                        Log.i("Delete resultscene", arrData[i][0] + " " + rows1);
                        i++;
                    } while (cursor.moveToNext());

                }
                rows = db.delete(TABLE_photoofresultscene, COL_RSID
                        + " =?", new String[]{sRSID});

                Log.i("DeletephotoResultscene", String.valueOf(rows)
                        + sRSID);
            }
            cursor.close();
            mDb.close();
            db.close();
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeletePhotoOfAllPropertyLoss(String sPropertyLossID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            mDb = this.getReadableDatabase(); // read Data
            String strSQL = "SELECT " + COL_FileID + " FROM " + TABLE_photoofpropertyloss
                    + " WHERE " + COL_PropertyLossID + " = '" + sPropertyLossID + "'";
            Log.i("show PropertyLoss", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);

                        long rows1 = db.delete(TABLE_multimediafile, COL_FileID
                                + " =?", new String[]{arrData[i][0]});
                        Log.i("Delete PropertyLoss", arrData[i][0] + " " + rows1);
                        i++;
                    } while (cursor.moveToNext());

                }
                rows = db.delete(TABLE_photoofpropertyloss, COL_PropertyLossID
                        + " =?", new String[]{sPropertyLossID});

                Log.i("DeletephotoPropertyLoss", String.valueOf(rows)
                        + sPropertyLossID);
            }
            cursor.close();
            mDb.close();
            db.close();
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeletePhotoOfAllInside(String sFeatureInsideID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;
            long rows = 0;
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            mDb = this.getReadableDatabase(); // read Data
            String strSQL = "SELECT " + COL_FileID + " FROM " + TABLE_photoofinside
                    + " WHERE " + COL_FeatureInsideID + " = '" + sFeatureInsideID + "'";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);

                        long rows1 = db.delete(TABLE_multimediafile, COL_FileID
                                + " =?", new String[]{arrData[i][0]});
                        Log.i("show Delete inside", arrData[i][0] + " " + rows1);
                        i++;
                    } while (cursor.moveToNext());

                }
                rows = db.delete(TABLE_photoofinside, COL_FeatureInsideID
                        + " =?", new String[]{sFeatureInsideID});

                Log.i("Deletephotoofinside", String.valueOf(rows)
                        + sFeatureInsideID);
            }
            cursor.close();
            mDb.close();
            db.close();
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long DeletePhotoOfResultScene(String sRSID,
                                         String sFileID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_photoofresultscene, COL_RSID
                    + " =? AND " + COL_FileID + " =?", new String[]{
                    sRSID, sFileID});

            db.close();
            Log.i("DeletephotoResultScene", String.valueOf(rows)
                    + sRSID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public long saveDataPhotoOfResultScene(String sRSID, String sFileID) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put(COL_FileID, sFileID);
            Val.put(COL_RSID, sRSID);

            long rows = db.insert(TABLE_photoofresultscene, null, Val);
            Log.i("save photoofresultscene", String.valueOf(rows) + " "
                    + sFileID + " " + sRSID);
            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectDataMultimediaFile(String sCaseReportID,
                                               String sFileType) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_multimediafile + " WHERE "
                    + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + COL_FileType + " = '" + sFileType + "'" + " ORDER BY "
                    + COL_FileID + " DESC";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show Photo", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectDataAllMultimediaFile(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_multimediafile + " WHERE "
                    + COL_CaseReportID + " = '" + sCaseReportID + "' ORDER BY "
                    + COL_FileID + " DESC";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show Photo", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectDataMultimediaFile(String sFileID) {
        // TODO Auto-generated method stub
        try {
            String arrData[] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT * FROM " + TABLE_multimediafile + " WHERE "
                    + COL_FileID + " = '" + sFileID + "' ";
            Log.i("show", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);


            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public long DeleteMediaFile(String sCaseReportID,
                                String sFileID) {
        // TODO Auto-generated method stub
        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            long rows = db.delete(TABLE_multimediafile, COL_CaseReportID
                    + " =? AND " + COL_FileID + " =?", new String[]{
                    sCaseReportID, sFileID});

            db.close();
            Log.i("DeleteMultimediafile", String.valueOf(rows)
                    + sCaseReportID);
            return rows; // return rows deleted.

        } catch (Exception e) {
            return -1;
        }
    }

    public String[][] SelectDataPhotoOfEachResultScene(String sCaseReportID, String sResultSceneID,
                                                       String sFileType) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " +
                    TABLE_multimediafile + "." + COL_FileID + " , " +
                    TABLE_multimediafile + "." + COL_CaseReportID + " , " +
                    TABLE_multimediafile + "." + COL_FileType + " , " +
                    TABLE_multimediafile + "." + COL_FilePath + " , " +
                    TABLE_multimediafile + "." + COL_FileDescription + " , " +
                    TABLE_multimediafile + "." + COL_Timestamp +
                    " FROM "
                    + TABLE_multimediafile + "," + TABLE_photoofresultscene + " WHERE "
                    + TABLE_photoofresultscene + "." + COL_RSID + " = '" + sResultSceneID + "' AND "
                    + TABLE_multimediafile + "." + COL_FileID + " = " + TABLE_photoofresultscene + "." + COL_FileID + " AND "
                    + TABLE_multimediafile + "." + COL_FileType + " = '" + sFileType + "'"
                    + " ORDER BY " + TABLE_multimediafile + "." + COL_FileID + " DESC";
            Log.i("show resultscene", sResultSceneID + " " + strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);
                        arrData[i][2] = cursor.getString(2);
                        arrData[i][3] = cursor.getString(3);
                        arrData[i][4] = cursor.getString(4);
                        arrData[i][5] = cursor.getString(5);

                        Log.i("show resultscene", sResultSceneID + " " + arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectDataPhotoResultScene(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT " + TABLE_photoofresultscene + "." + COL_RSID + " , " + TABLE_photoofresultscene + "." + COL_FileID
                    + " FROM " + TABLE_resultscene + " , " + TABLE_photoofresultscene + " WHERE "
                    + TABLE_resultscene + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TABLE_photoofresultscene + "." + COL_RSID + " = " + TABLE_resultscene + "." + COL_RSID;

            Log.i("show resultscene", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);

                        Log.i("PhotoResultScene", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    public String[][] SelectDataPhotoPropertyloss(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT " + TABLE_photoofpropertyloss + "." + COL_PropertyLossID + " , " + TABLE_photoofpropertyloss + "." + COL_FileID
                    + " FROM " + TABLE_PROPERTYLOSS + " , " + TABLE_photoofpropertyloss + " WHERE "
                    + TABLE_PROPERTYLOSS + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TABLE_photoofpropertyloss + "." + COL_PropertyLossID + " = " + TABLE_PROPERTYLOSS + "." + COL_PropertyLossID;


            Log.i("show Propertyless", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);

                        Log.i("PhotoPropertyless", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectDataPhotoFindEvidence(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT " + TABLE_photoofevidence + "." + COL_FindEvidenceID + " , " + TABLE_photoofevidence + "." + COL_FileID
                    + " FROM " + TABLE_FindEevidence + " , " + TABLE_photoofevidence + " WHERE "
                    + TABLE_FindEevidence + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TABLE_photoofevidence + "." + COL_FindEvidenceID + " = " + TABLE_FindEevidence + "." + COL_FindEvidenceID;


            Log.i("show evidence", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);

                        Log.i("PhotoFindEvidence", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectDataPhotoOfFeatureOutside(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT " + TABLE_photoofoutside + "." + COL_CaseReportID + " , " + TABLE_photoofoutside + "." + COL_FileID
                    + " FROM " + TABLE_FEATUREOUT + " , " + TABLE_photoofoutside + " WHERE "
                    + TABLE_FEATUREOUT + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TABLE_photoofoutside + "." + COL_CaseReportID + " = " + TABLE_FEATUREOUT + "." + COL_CaseReportID;

            Log.i("show photo outside", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);

                        Log.i("PhotoOfFeatureOutside", arrData[i][0]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] SelectDataPhotoOfFeatureInside(String sCaseReportID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT " + TABLE_photoofinside + "." + COL_FeatureInsideID + " , " + TABLE_photoofinside + "." + COL_FileID
                    + " FROM " + TABLE_FEATUREIN + " , " + TABLE_photoofinside + " WHERE "
                    + TABLE_FEATUREIN + "." + COL_CaseReportID + " = '" + sCaseReportID + "' AND "
                    + TABLE_photoofinside + "." + COL_FeatureInsideID + " = " + TABLE_FEATUREIN + "." + COL_FeatureInsideID;
            Log.i("show photo Inside", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;
                    do {
                        arrData[i][0] = cursor.getString(0);
                        arrData[i][1] = cursor.getString(1);

                        Log.i("PhotoOfFeatureInside", arrData[i][0] + " " + arrData[i][1]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] investigatingCase(String officialID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT C.*,R.InquiryOfficialID,substr(ReceivingCaseDate,1,2) AS days,substr(ReceivingCaseDate,4,2) AS months, "+
                    "substr(ReceivingCaseDate,7,4) AS years  FROM casescene AS C,receivingcase AS R " +
                    "WHERE C.InvestigatorOfficialID = '" + officialID + "' " +
                    "AND C.CaseReportID = R.CaseReportID " +
                    "AND C.ReportStatus = 'investigating'  ORDER BY years DESC, months DESC, days DESC, ReceivingCaseTime DESC";

            Log.i("show investigatingCase", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;

                    do {
                        for (int j = 0; j < cursor.getColumnCount(); j++) {
                            arrData[i][j] = cursor.getString(j);


                        }
                        Log.i("investigatingCase", arrData[i][0] + " " + arrData[i][25]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[][] acceptCase(String officialID) {
        // TODO Auto-generated method stub
        try {
            String arrData[][] = null;

            mDb = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT C.*,R.InquiryOfficialID,substr(C.ReceivingCaseDate,1,2) AS days,substr(C.ReceivingCaseDate,4,2) AS months," +
                    "substr(C.ReceivingCaseDate,7,4) AS years FROM casescene AS C,receivingcase AS R " +
                    "WHERE R.InquiryOfficialID = '" + officialID + "' " +
                    "AND C.CaseReportID = R.CaseReportID " +
                    "AND R.ReceivingStatus = 'accept' ORDER BY years DESC, months DESC, days DESC, C.ReceivingCaseTime DESC";

          /*  String strSQL = "SELECT C.*,R.InquiryOfficialID FROM casescene AS C,receivingcase AS R " +
                    "WHERE R.InquiryOfficialID = '" + officialID + "' " +
                    "AND C.CaseReportID = R.CaseReportID " +
                    "AND R.ReceivingStatus = 'accept' ORDER BY C.ReceivingCaseDate ASC,C.ReceivingCaseTime DESC";
 SELECT C.*,R.InquiryOfficialID,MID(C.ReceivingCaseDate,1,2)AS days,MID(C.ReceivingCaseDate,4,2) AS months,
MID(C.ReceivingCaseDate,7,4) AS years FROM casescene AS C,receivingcase AS R WHERE R.InquiryOfficialID = 'INQ_PC04_1'
AND C.CaseReportID = R.CaseReportID AND R.ReceivingStatus = 'accept' ORDER BY years DESC, months DESC, days DESC, C.ReceivingCaseTime DESC*/
            Log.i("show acceptCase", strSQL);
            Cursor cursor = mDb.rawQuery(strSQL, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()][cursor
                            .getColumnCount()];

                    int i = 0;

                    do {
                        for (int j = 0; j < cursor.getColumnCount(); j++) {
                            arrData[i][j] = cursor.getString(j);


                        }
                        Log.i("acceptCase", arrData[i][0] + " " + arrData[i][1]);
                        i++;
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();
            mDb.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }
}
