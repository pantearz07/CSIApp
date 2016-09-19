package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbNoticeCase implements Serializable {
    public static final String TB_NoticeCase = "noticecase";
    // From Table NoticeCase field name NoticeCaseID
    public static final String COL_NoticeCaseID = "NoticeCaseID";
    public String NoticeCaseID = "";

    // From Table NoticeCase field name Mobile_CaseID
    public static final String COL_Mobile_CaseID = "Mobile_CaseID";
    public String Mobile_CaseID = "";

    // From Table NoticeCase field name InquiryOfficialID
    public static final String COL_InquiryOfficialID = "InquiryOfficialID";
    public String InquiryOfficialID = "";

    // From Table NoticeCase field name InvestigatorOfficialID
    public static final String COL_InvestigatorOfficialID = "InvestigatorOfficialID";
    public String InvestigatorOfficialID = "";

    // From Table NoticeCase field name SCDCAgencyCode
    public static final String COL_SCDCAgencyCode = "SCDCAgencyCode";
    public String SCDCAgencyCode = "";

    // From Table NoticeCase field name CaseTypeID
    public static final String COL_CaseTypeID = "CaseTypeID";
    public String CaseTypeID = "";

    // From Table NoticeCase field name SubCaseTypeID
    public static final String COL_SubCaseTypeID = "SubCaseTypeID";
    public String SubCaseTypeID = "";

    // From Table NoticeCase field name CaseStatus
    public static final String COL_CaseStatus = "CaseStatus";
    public String CaseStatus = "";

    // From Table NoticeCase field name PoliceStationID
    public static final String COL_PoliceStationID = "PoliceStationID";
    public String PoliceStationID = "";

    // From Table NoticeCase field name CaseTel
    public static final String COL_CaseTel = "CaseTel";
    public String CaseTel = "";

    // From Table NoticeCase field name ReceivingCaseDate
    public static final String COL_ReceivingCaseDate = "ReceivingCaseDate";
    public String ReceivingCaseDate = "";

    // From Table NoticeCase field name ReceivingCaseTime
    public static final String COL_ReceivingCaseTime = "ReceivingCaseTime";
    public String ReceivingCaseTime = "";

    // From Table NoticeCase field name HappenCaseDate
    public static final String COL_HappenCaseDate = "HappenCaseDate";
    public String HappenCaseDate = "";

    // From Table NoticeCase field name HappenCaseTime
    public static final String COL_HappenCaseTime = "HappenCaseTime";
    public String HappenCaseTime = "";

    // From Table NoticeCase field name KnowCaseDate
    public static final String COL_KnowCaseDate = "KnowCaseDate";
    public String KnowCaseDate = "";

    // From Table NoticeCase field name KnowCaseTime
    public static final String COL_KnowCaseTime = "KnowCaseTime";
    public String KnowCaseTime = "";

    // From Table NoticeCase field name SceneNoticeDate
    public static final String COL_SceneNoticeDate = "SceneNoticeDate";
    public String SceneNoticeDate = "";

    // From Table NoticeCase field name SceneNoticeTime
    public static final String COL_SceneNoticeTime = "SceneNoticeTime";
    public String SceneNoticeTime = "";

    // From Table NoticeCase field name CompleteSceneDate
    public static final String COL_CompleteSceneDate = "CompleteSceneDate";
    public String CompleteSceneDate = "";

    // From Table NoticeCase field name CompleteSceneTime
    public static final String COL_CompleteSceneTime = "CompleteSceneTime";
    public String CompleteSceneTime = "";

    // From Table NoticeCase field name LocaleName
    public static final String COL_LocaleName = "LocaleName";
    public String LocaleName = "";

    // From Table NoticeCase field name DISTRICT_ID
    public static final String COL_DISTRICT_ID = "DISTRICT_ID";
    public String DISTRICT_ID = "";

    // From Table NoticeCase field name AMPHUR_ID
    public static final String COL_AMPHUR_ID = "AMPHUR_ID";
    public String AMPHUR_ID = "";

    // From Table NoticeCase field name PROVINCE_ID
    public static final String COL_PROVINCE_ID = "PROVINCE_ID";
    public String PROVINCE_ID = "";

    // From Table NoticeCase field name Latitude
    public static final String COL_Latitude = "Latitude";
    public String Latitude = "";

    // From Table NoticeCase field name Longitude
    public static final String COL_Longitude = "Longitude";
    public String Longitude = "";

    // From Table NoticeCase field name SuffererPrename
    public static final String COL_SuffererPrename = "SuffererPrename";
    public String SuffererPrename = "";

    // From Table NoticeCase field name SuffererName
    public static final String COL_SuffererName = "SuffererName";
    public String SuffererName = "";

    // From Table NoticeCase field name SuffererStatus
    public static final String COL_SuffererStatus = "SuffererStatus";
    public String SuffererStatus = "";

    // From Table NoticeCase field name SuffererPhoneNum
    public static final String COL_SuffererPhoneNum = "SuffererPhoneNum";
    public String SuffererPhoneNum = "";

    // From Table NoticeCase field name CircumstanceOfCaseDetail
    public static final String COL_CircumstanceOfCaseDetail = "CircumstanceOfCaseDetail";
    public String CircumstanceOfCaseDetail = "";

    // From Table NoticeCase field name LastUpdateDate
    public static final String COL_LastUpdateDate = "LastUpdateDate";
    public String LastUpdateDate = "";

    // From Table NoticeCase field name LastUpdateTime
    public static final String COL_LastUpdateTime = "LastUpdateTime";
    public String LastUpdateTime = "";

    public String getNoticeCaseID() {
        return NoticeCaseID;
    }

    public void setNoticeCaseID(String noticeCaseID) {
        NoticeCaseID = noticeCaseID;
    }

    public String getMobile_CaseID() {
        return Mobile_CaseID;
    }

    public void setMobile_CaseID(String mobile_CaseID) {
        Mobile_CaseID = mobile_CaseID;
    }

    public String getInquiryOfficialID() {
        return InquiryOfficialID;
    }

    public void setInquiryOfficialID(String inquiryOfficialID) {
        InquiryOfficialID = inquiryOfficialID;
    }

    public String getInvestigatorOfficialID() {
        return InvestigatorOfficialID;
    }

    public void setInvestigatorOfficialID(String investigatorOfficialID) {
        InvestigatorOfficialID = investigatorOfficialID;
    }

    public String getSCDCAgencyCode() {
        return SCDCAgencyCode;
    }

    public void setSCDCAgencyCode(String SCDCAgencyCode) {
        this.SCDCAgencyCode = SCDCAgencyCode;
    }

    public String getCaseTypeID() {
        return CaseTypeID;
    }

    public void setCaseTypeID(String caseTypeID) {
        CaseTypeID = caseTypeID;
    }

    public String getSubCaseTypeID() {
        return SubCaseTypeID;
    }

    public void setSubCaseTypeID(String subCaseTypeID) {
        SubCaseTypeID = subCaseTypeID;
    }

    public String getCaseStatus() {
        return CaseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        CaseStatus = caseStatus;
    }

    public String getPoliceStationID() {
        return PoliceStationID;
    }

    public void setPoliceStationID(String policeStationID) {
        PoliceStationID = policeStationID;
    }

    public String getCaseTel() {
        return CaseTel;
    }

    public void setCaseTel(String caseTel) {
        CaseTel = caseTel;
    }

    public String getReceivingCaseDate() {
        return ReceivingCaseDate;
    }

    public void setReceivingCaseDate(String receivingCaseDate) {
        ReceivingCaseDate = receivingCaseDate;
    }

    public String getReceivingCaseTime() {
        return ReceivingCaseTime;
    }

    public void setReceivingCaseTime(String receivingCaseTime) {
        ReceivingCaseTime = receivingCaseTime;
    }

    public String getHappenCaseDate() {
        return HappenCaseDate;
    }

    public void setHappenCaseDate(String happenCaseDate) {
        HappenCaseDate = happenCaseDate;
    }

    public String getHappenCaseTime() {
        return HappenCaseTime;
    }

    public void setHappenCaseTime(String happenCaseTime) {
        HappenCaseTime = happenCaseTime;
    }

    public String getKnowCaseDate() {
        return KnowCaseDate;
    }

    public void setKnowCaseDate(String knowCaseDate) {
        KnowCaseDate = knowCaseDate;
    }

    public String getKnowCaseTime() {
        return KnowCaseTime;
    }

    public void setKnowCaseTime(String knowCaseTime) {
        KnowCaseTime = knowCaseTime;
    }

    public String getSceneNoticeDate() {
        return SceneNoticeDate;
    }

    public void setSceneNoticeDate(String sceneNoticeDate) {
        SceneNoticeDate = sceneNoticeDate;
    }

    public String getSceneNoticeTime() {
        return SceneNoticeTime;
    }

    public void setSceneNoticeTime(String sceneNoticeTime) {
        SceneNoticeTime = sceneNoticeTime;
    }

    public String getCompleteSceneDate() {
        return CompleteSceneDate;
    }

    public void setCompleteSceneDate(String completeSceneDate) {
        CompleteSceneDate = completeSceneDate;
    }

    public String getCompleteSceneTime() {
        return CompleteSceneTime;
    }

    public void setCompleteSceneTime(String completeSceneTime) {
        CompleteSceneTime = completeSceneTime;
    }

    public String getLocaleName() {
        return LocaleName;
    }

    public void setLocaleName(String localeName) {
        LocaleName = localeName;
    }

    public String getDISTRICT_ID() {
        return DISTRICT_ID;
    }

    public void setDISTRICT_ID(String DISTRICT_ID) {
        this.DISTRICT_ID = DISTRICT_ID;
    }

    public String getAMPHUR_ID() {
        return AMPHUR_ID;
    }

    public void setAMPHUR_ID(String AMPHUR_ID) {
        this.AMPHUR_ID = AMPHUR_ID;
    }

    public String getPROVINCE_ID() {
        return PROVINCE_ID;
    }

    public void setPROVINCE_ID(String PROVINCE_ID) {
        this.PROVINCE_ID = PROVINCE_ID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getSuffererPrename() {
        return SuffererPrename;
    }

    public void setSuffererPrename(String suffererPrename) {
        SuffererPrename = suffererPrename;
    }

    public String getSuffererName() {
        return SuffererName;
    }

    public void setSuffererName(String suffererName) {
        SuffererName = suffererName;
    }

    public String getSuffererStatus() {
        return SuffererStatus;
    }

    public void setSuffererStatus(String suffererStatus) {
        SuffererStatus = suffererStatus;
    }

    public String getSuffererPhoneNum() {
        return SuffererPhoneNum;
    }

    public void setSuffererPhoneNum(String suffererPhoneNum) {
        SuffererPhoneNum = suffererPhoneNum;
    }

    public String getCircumstanceOfCaseDetail() {
        return CircumstanceOfCaseDetail;
    }

    public void setCircumstanceOfCaseDetail(String circumstanceOfCaseDetail) {
        CircumstanceOfCaseDetail = circumstanceOfCaseDetail;
    }

    public String getLastUpdateDate() {
        return LastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        LastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateTime() {
        return LastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        LastUpdateTime = lastUpdateTime;
    }
}
