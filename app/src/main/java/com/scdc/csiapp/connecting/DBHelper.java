package com.scdc.csiapp.connecting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Pantearz07 on 15/9/2559.
 */
public class DBHelper extends SQLiteAssetHelper {
    SQLiteDatabase mDb;
    SQLiteQueryBuilder mQb;
    private static final String DB_NAME = "csi_db";
    private static final int DB_VERSION = 1;
    //--ข้อมูลทั่วไป-- 16tables//
    // district
    public static final String TABLE_DISTRICT = "district";
    public static final String COL_DISTRICT_ID = "DISTRICT_ID";
    public static final String COL_DISTRICT_CODE = "DISTRICT_CODE";
    public static final String COL_DISTRICT_NAME = "DISTRICT_NAME";
    //Table Amphur
    public static final String TABLE_amphur = "amphur";
    public static final String COL_AMPHUR_ID = "AMPHUR_ID";
    public static final String COL_AMPHUR_CODE = "AMPHUR_CODE";
    public static final String COL_AMPHUR_NAME = "AMPHUR_NAME";
    public static final String COL_POSTCODE = "POSTCODE";
    public static final String COL_AMPHUR_POLYGON = "amphur_polygon";
    public static final String COL_AMPHUR_LATITUDE = "amphur_latitude";
    public static final String COL_AMPHUR_LONGITUDE = "amphur_longitude";
    // province
    public static final String TABLE_PROVINCE = "province";
    public static final String COL_PROVINCE_ID = "PROVINCE_ID";
    public static final String COL_PROVINCE_CODE = "PROVINCE_CODE";
    public static final String COL_PROVINCE_NAME = "PROVINCE_NAME";
    public static final String COL_PROVINCE_STATUS = "province_status";
    // geography
    public static final String TABLE_geography = "geography";
    public static final String COL_GEO_ID = "GEO_ID";
    public static final String COL_GEO_NAME = "GEO_NAME";
    // TABLE casescenetype
    public static final String TABLE_casescenetype = "casescenetype";
    public static final String COL_CaseTypeID = "CaseTypeID";
    public static final String COL_CaseTypeName = "CaseTypeName";
    public static final String COL_casetype_min = "casetype_min";
    public static final String COL_casetype_max = "casetype_max";
    public static final String COL_casetype_icon = "casetype_icon";
    public static final String COL_casetype_colormin = "casetype_colormin";
    public static final String COL_casetype_colormedium = "casetype_colormedium";
    public static final String COL_casetype_colorhigh = "casetype_colorhigh";
    public static final String COL_casetype_status = "casetype_status";
    // subcasescenetype
    public static final String TABLE_subcasescenetype = "subcasescenetype";
    public static final String COL_SubCaseTypeID = "SubCaseTypeID";
    public static final String COL_SubCaseTypeName = "SubCaseTypeName";
    // composition
    public static final String TABLE_composition = "composition";
    public static final String COL_ComPosID = "ComPosID";
    public static final String COL_ComPosName = "ComPosName";
    public static final String COL_ComPosAbbr = "ComPosAbbr";
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
    //policeposition
    public static final String TABLE_policeposition = "policeposition";
    public static final String COL_PositionID = "PositionID";
    public static final String COL_PositionName = "PositionName";
    public static final String COL_PositionAbbr = "PositionAbbr";
    // policerank
    public static final String TABLE_policerank = "policerank";
    public static final String COL_RankID = "RankID";
    public static final String COL_RankName = "RankName";
    public static final String COL_RankAbbr = "RankAbbr";
    // policeagency
    public static final String TABLE_policeagency = "policeagency";
    public static final String COL_PoliceAgencyID = "PoliceAgencyID";
    public static final String COL_PoliceAgencyCode = "PoliceAgencyCode";
    public static final String COL_PoliceAgencyName = "PoliceAgencyName";
    // policecenter
    public static final String TABLE_policecenter = "policecenter";
    public static final String COL_PoliceCenterID = "PoliceCenterID";
    public static final String COL_PoliceName = "PoliceName";
    // policestation
    public static final String TABLE_policestation = "policestation";
    public static final String COL_PoliceStationID = "PoliceStationID";
    public static final String COL_PoliceStationCode = "PoliceStationCode";
    public static final String COL_PoliceStationName = "PoliceStationName";
    // scdccenter
    public static final String TABLE_scdccenter = "scdccenter";
    public static final String COL_SCDCCenterID = "SCDCCenterID";
    public static final String COL_SCDCCenterName = "SCDCCenterName";
    public static final String COL_SCDCCenterProvince = "SCDCCenterProvince";
    // scdcagency
    public static final String TABLE_scdcagency = "scdcagency";
    public static final String COL_SCDCAgencyCode = "SCDCAgencyCode";
    public static final String COL_SCDCAgencyName = "SCDCAgencyName";
    //--end ข้อมูลทั่วไป--//

    // official /
    public static final String TABLE_official = "official";
    public static final String COL_OfficialID = "OfficialID";
    public static final String COL_FirstName = "FirstName";
    public static final String COL_LastName = "LastName";
    public static final String COL_Alias = "Alias";
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
    public DBHelper(Context context) {
        //super(context, DB_NAME, null, DB_VERSION);
        super(context, DB_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DB_VERSION);

    }
}
