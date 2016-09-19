package com.scdc.csiapp.apimodel;

import java.util.List;

/**
 * Created by cbnuke on 9/19/16.
 */
public class ApiListNoticeCase {

    /**
     * status : success
     * data : {"action":"list_noticecase","reason":"โหลดรายการคดีสำเร็จ","result":[{"tbNoticeCase":{"NoticeCaseID":"NC04_000002","Mobile_CaseID":"","InquiryOfficialID":"INQ_PC04_1","InvestigatorOfficialID":"","SCDCAgencyCode":"SCDCA401","CaseTypeID":"CT03","SubCaseTypeID":"SCT08","CaseStatus":"notice","PoliceStationID":"40001","CaseTel":"123456","ReceivingCaseDate":"2016-09-19","ReceivingCaseTime":"13:33:00","HappenCaseDate":"2016-09-20","HappenCaseTime":"01:00:00","KnowCaseDate":"2016-09-20","KnowCaseTime":"02:00:00","SceneNoticeDate":"","SceneNoticeTime":"","CompleteSceneDate":"","CompleteSceneTime":"","LocaleName":"KKU","DISTRICT_ID":"6388","AMPHUR_ID":"718","PROVINCE_ID":"49","Latitude":"16.45235386575796","Longitude":"102.83641489323736","SuffererPrename":"นาย","SuffererName":"near","SuffererStatus":"เจ้าของบ้าน","SuffererPhoneNum":"123456","CircumstanceOfCaseDetail":"KKU","LastUpdateDate":"2016-09-19","LastUpdateTime":"13:35:00"},"tbOfficial":{"OfficialID":"INQ_PC04_1","FirstName":"ภูมี","LastName":"อีคะละ","Alias":"","Rank":"พ.ต.ท.","Position":"พงส.","SubPossition":"","PhoneNumber":"","OfficialEmail":"","OfficialDisplayPic":"","AccessType":"inquiryofficial","SCDCAgencyCode":"","PoliceStationID":"40001","id_users":"inq1"},"tbCaseSceneType":{"CaseTypeID":"CT03","CaseTypeName":"เพลิงไหม้","casetype_min":"","casetype_max":"","casetype_icon":"","casetype_colormin":"","casetype_colormedium":"","casetype_colorhigh":"","casetype_status":""},"tbSubcaseSceneType":{"SubCaseTypeID":"SCT08","CaseTypeID":"CT03","SubCaseTypeName":"วางเพลิง"},"tbPoliceStation":{"PoliceStationID":"40001","PoliceAgencyID":"40","PoliceStationName":"เมืองขอนแก่น"},"tbPoliceAgency":{"PoliceAgencyID":"40","PoliceCenterID":"4","PoliceAgencyName":"ขอนแก่น"},"tbDistrict":{"DISTRICT_ID":"6388","DISTRICT_CODE":"621001","DISTRICT_NAME":"บึงสามัคคี   ","AMPHUR_ID":"718"},"tbProvince":{"PROVINCE_ID":"49","PROVINCE_CODE":"62","PROVINCE_NAME":"กำแพงเพชร ","GEO_ID":"2","province_status":"0"},"tbAmphur":{"AMPHUR_ID":"718","AMPHUR_CODE":"6210","AMPHUR_NAME":"บึงสามัคคี   ","POSTCODE":"62210","GEO_ID":"2","PROVINCE_ID":"49","amphur_polygon":""},"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"},"tbSCDCcenter":{"SCDCCenterID":"SCDCC04","SCDCCenterName":"ศูนย์พิสูจน์หลักฐาน 4","SCDCCenterProvince":"(ขอนแก่น)"}},{"tbNoticeCase":{"NoticeCaseID":"NC_000001","Mobile_CaseID":"","InquiryOfficialID":"INQ_PC04_1","InvestigatorOfficialID":"INV_SCDCC04_1","SCDCAgencyCode":"SCDCA401","CaseTypeID":"CT01","SubCaseTypeID":"SCT01","CaseStatus":"assign","PoliceStationID":"40001","CaseTel":"1234","ReceivingCaseDate":"2016-09-19","ReceivingCaseTime":"10:35:00","HappenCaseDate":"2016-09-19","HappenCaseTime":"03:00:00","KnowCaseDate":"2016-09-19","KnowCaseTime":"06:00:00","SceneNoticeDate":"","SceneNoticeTime":"","CompleteSceneDate":"","CompleteSceneTime":"","LocaleName":"KKU","DISTRICT_ID":"3491","AMPHUR_ID":"393","PROVINCE_ID":"28","Latitude":"16.44392100976896","Longitude":"102.81168365495603","SuffererPrename":"นาย","SuffererName":"พีรวิทย์","SuffererStatus":"เจ้าของบ้าน","SuffererPhoneNum":"123456","CircumstanceOfCaseDetail":"ซักที่","LastUpdateDate":"2016-09-19","LastUpdateTime":"11:00:00"},"tbOfficial":{"OfficialID":"INQ_PC04_1","FirstName":"ภูมี","LastName":"อีคะละ","Alias":"","Rank":"พ.ต.ท.","Position":"พงส.","SubPossition":"","PhoneNumber":"","OfficialEmail":"","OfficialDisplayPic":"","AccessType":"inquiryofficial","SCDCAgencyCode":"","PoliceStationID":"40001","id_users":"inq1"},"tbCaseSceneType":{"CaseTypeID":"CT01","CaseTypeName":"ทรัพย์","casetype_min":"","casetype_max":"","casetype_icon":"","casetype_colormin":"","casetype_colormedium":"","casetype_colorhigh":"","casetype_status":""},"tbSubcaseSceneType":{"SubCaseTypeID":"SCT01","CaseTypeID":"CT01","SubCaseTypeName":"ลักทรัพย์"},"tbPoliceStation":{"PoliceStationID":"40001","PoliceAgencyID":"40","PoliceStationName":"เมืองขอนแก่น"},"tbPoliceAgency":{"PoliceAgencyID":"40","PoliceCenterID":"4","PoliceAgencyName":"ขอนแก่น"},"tbDistrict":{"DISTRICT_ID":"3491","DISTRICT_CODE":"400101","DISTRICT_NAME":"ในเมือง","AMPHUR_ID":"393"},"tbProvince":{"PROVINCE_ID":"28","PROVINCE_CODE":"40","PROVINCE_NAME":"ขอนแก่น","GEO_ID":"3","province_status":"1"},"tbAmphur":{"AMPHUR_ID":"393","AMPHUR_CODE":"4001","AMPHUR_NAME":"เมืองขอนแก่น","POSTCODE":"40000","GEO_ID":"3","PROVINCE_ID":"28","amphur_polygon":""},"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"},"tbSCDCcenter":{"SCDCCenterID":"SCDCC04","SCDCCenterName":"ศูนย์พิสูจน์หลักฐาน 4","SCDCCenterProvince":"(ขอนแก่น)"}}]}
     */

    private String status;
    /**
     * action : list_noticecase
     * reason : โหลดรายการคดีสำเร็จ
     * result : [{"tbNoticeCase":{"NoticeCaseID":"NC04_000002","Mobile_CaseID":"","InquiryOfficialID":"INQ_PC04_1","InvestigatorOfficialID":"","SCDCAgencyCode":"SCDCA401","CaseTypeID":"CT03","SubCaseTypeID":"SCT08","CaseStatus":"notice","PoliceStationID":"40001","CaseTel":"123456","ReceivingCaseDate":"2016-09-19","ReceivingCaseTime":"13:33:00","HappenCaseDate":"2016-09-20","HappenCaseTime":"01:00:00","KnowCaseDate":"2016-09-20","KnowCaseTime":"02:00:00","SceneNoticeDate":"","SceneNoticeTime":"","CompleteSceneDate":"","CompleteSceneTime":"","LocaleName":"KKU","DISTRICT_ID":"6388","AMPHUR_ID":"718","PROVINCE_ID":"49","Latitude":"16.45235386575796","Longitude":"102.83641489323736","SuffererPrename":"นาย","SuffererName":"near","SuffererStatus":"เจ้าของบ้าน","SuffererPhoneNum":"123456","CircumstanceOfCaseDetail":"KKU","LastUpdateDate":"2016-09-19","LastUpdateTime":"13:35:00"},"tbOfficial":{"OfficialID":"INQ_PC04_1","FirstName":"ภูมี","LastName":"อีคะละ","Alias":"","Rank":"พ.ต.ท.","Position":"พงส.","SubPossition":"","PhoneNumber":"","OfficialEmail":"","OfficialDisplayPic":"","AccessType":"inquiryofficial","SCDCAgencyCode":"","PoliceStationID":"40001","id_users":"inq1"},"tbCaseSceneType":{"CaseTypeID":"CT03","CaseTypeName":"เพลิงไหม้","casetype_min":"","casetype_max":"","casetype_icon":"","casetype_colormin":"","casetype_colormedium":"","casetype_colorhigh":"","casetype_status":""},"tbSubcaseSceneType":{"SubCaseTypeID":"SCT08","CaseTypeID":"CT03","SubCaseTypeName":"วางเพลิง"},"tbPoliceStation":{"PoliceStationID":"40001","PoliceAgencyID":"40","PoliceStationName":"เมืองขอนแก่น"},"tbPoliceAgency":{"PoliceAgencyID":"40","PoliceCenterID":"4","PoliceAgencyName":"ขอนแก่น"},"tbDistrict":{"DISTRICT_ID":"6388","DISTRICT_CODE":"621001","DISTRICT_NAME":"บึงสามัคคี   ","AMPHUR_ID":"718"},"tbProvince":{"PROVINCE_ID":"49","PROVINCE_CODE":"62","PROVINCE_NAME":"กำแพงเพชร ","GEO_ID":"2","province_status":"0"},"tbAmphur":{"AMPHUR_ID":"718","AMPHUR_CODE":"6210","AMPHUR_NAME":"บึงสามัคคี   ","POSTCODE":"62210","GEO_ID":"2","PROVINCE_ID":"49","amphur_polygon":""},"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"},"tbSCDCcenter":{"SCDCCenterID":"SCDCC04","SCDCCenterName":"ศูนย์พิสูจน์หลักฐาน 4","SCDCCenterProvince":"(ขอนแก่น)"}},{"tbNoticeCase":{"NoticeCaseID":"NC_000001","Mobile_CaseID":"","InquiryOfficialID":"INQ_PC04_1","InvestigatorOfficialID":"INV_SCDCC04_1","SCDCAgencyCode":"SCDCA401","CaseTypeID":"CT01","SubCaseTypeID":"SCT01","CaseStatus":"assign","PoliceStationID":"40001","CaseTel":"1234","ReceivingCaseDate":"2016-09-19","ReceivingCaseTime":"10:35:00","HappenCaseDate":"2016-09-19","HappenCaseTime":"03:00:00","KnowCaseDate":"2016-09-19","KnowCaseTime":"06:00:00","SceneNoticeDate":"","SceneNoticeTime":"","CompleteSceneDate":"","CompleteSceneTime":"","LocaleName":"KKU","DISTRICT_ID":"3491","AMPHUR_ID":"393","PROVINCE_ID":"28","Latitude":"16.44392100976896","Longitude":"102.81168365495603","SuffererPrename":"นาย","SuffererName":"พีรวิทย์","SuffererStatus":"เจ้าของบ้าน","SuffererPhoneNum":"123456","CircumstanceOfCaseDetail":"ซักที่","LastUpdateDate":"2016-09-19","LastUpdateTime":"11:00:00"},"tbOfficial":{"OfficialID":"INQ_PC04_1","FirstName":"ภูมี","LastName":"อีคะละ","Alias":"","Rank":"พ.ต.ท.","Position":"พงส.","SubPossition":"","PhoneNumber":"","OfficialEmail":"","OfficialDisplayPic":"","AccessType":"inquiryofficial","SCDCAgencyCode":"","PoliceStationID":"40001","id_users":"inq1"},"tbCaseSceneType":{"CaseTypeID":"CT01","CaseTypeName":"ทรัพย์","casetype_min":"","casetype_max":"","casetype_icon":"","casetype_colormin":"","casetype_colormedium":"","casetype_colorhigh":"","casetype_status":""},"tbSubcaseSceneType":{"SubCaseTypeID":"SCT01","CaseTypeID":"CT01","SubCaseTypeName":"ลักทรัพย์"},"tbPoliceStation":{"PoliceStationID":"40001","PoliceAgencyID":"40","PoliceStationName":"เมืองขอนแก่น"},"tbPoliceAgency":{"PoliceAgencyID":"40","PoliceCenterID":"4","PoliceAgencyName":"ขอนแก่น"},"tbDistrict":{"DISTRICT_ID":"3491","DISTRICT_CODE":"400101","DISTRICT_NAME":"ในเมือง","AMPHUR_ID":"393"},"tbProvince":{"PROVINCE_ID":"28","PROVINCE_CODE":"40","PROVINCE_NAME":"ขอนแก่น","GEO_ID":"3","province_status":"1"},"tbAmphur":{"AMPHUR_ID":"393","AMPHUR_CODE":"4001","AMPHUR_NAME":"เมืองขอนแก่น","POSTCODE":"40000","GEO_ID":"3","PROVINCE_ID":"28","amphur_polygon":""},"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"},"tbSCDCcenter":{"SCDCCenterID":"SCDCC04","SCDCCenterName":"ศูนย์พิสูจน์หลักฐาน 4","SCDCCenterProvince":"(ขอนแก่น)"}}]
     */

    private DataEntity data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String action;
        private String reason;
        /**
         * tbNoticeCase : {"NoticeCaseID":"NC04_000002","Mobile_CaseID":"","InquiryOfficialID":"INQ_PC04_1","InvestigatorOfficialID":"","SCDCAgencyCode":"SCDCA401","CaseTypeID":"CT03","SubCaseTypeID":"SCT08","CaseStatus":"notice","PoliceStationID":"40001","CaseTel":"123456","ReceivingCaseDate":"2016-09-19","ReceivingCaseTime":"13:33:00","HappenCaseDate":"2016-09-20","HappenCaseTime":"01:00:00","KnowCaseDate":"2016-09-20","KnowCaseTime":"02:00:00","SceneNoticeDate":"","SceneNoticeTime":"","CompleteSceneDate":"","CompleteSceneTime":"","LocaleName":"KKU","DISTRICT_ID":"6388","AMPHUR_ID":"718","PROVINCE_ID":"49","Latitude":"16.45235386575796","Longitude":"102.83641489323736","SuffererPrename":"นาย","SuffererName":"near","SuffererStatus":"เจ้าของบ้าน","SuffererPhoneNum":"123456","CircumstanceOfCaseDetail":"KKU","LastUpdateDate":"2016-09-19","LastUpdateTime":"13:35:00"}
         * tbOfficial : {"OfficialID":"INQ_PC04_1","FirstName":"ภูมี","LastName":"อีคะละ","Alias":"","Rank":"พ.ต.ท.","Position":"พงส.","SubPossition":"","PhoneNumber":"","OfficialEmail":"","OfficialDisplayPic":"","AccessType":"inquiryofficial","SCDCAgencyCode":"","PoliceStationID":"40001","id_users":"inq1"}
         * tbCaseSceneType : {"CaseTypeID":"CT03","CaseTypeName":"เพลิงไหม้","casetype_min":"","casetype_max":"","casetype_icon":"","casetype_colormin":"","casetype_colormedium":"","casetype_colorhigh":"","casetype_status":""}
         * tbSubcaseSceneType : {"SubCaseTypeID":"SCT08","CaseTypeID":"CT03","SubCaseTypeName":"วางเพลิง"}
         * tbPoliceStation : {"PoliceStationID":"40001","PoliceAgencyID":"40","PoliceStationName":"เมืองขอนแก่น"}
         * tbPoliceAgency : {"PoliceAgencyID":"40","PoliceCenterID":"4","PoliceAgencyName":"ขอนแก่น"}
         * tbDistrict : {"DISTRICT_ID":"6388","DISTRICT_CODE":"621001","DISTRICT_NAME":"บึงสามัคคี   ","AMPHUR_ID":"718"}
         * tbProvince : {"PROVINCE_ID":"49","PROVINCE_CODE":"62","PROVINCE_NAME":"กำแพงเพชร ","GEO_ID":"2","province_status":"0"}
         * tbAmphur : {"AMPHUR_ID":"718","AMPHUR_CODE":"6210","AMPHUR_NAME":"บึงสามัคคี   ","POSTCODE":"62210","GEO_ID":"2","PROVINCE_ID":"49","amphur_polygon":""}
         * tbSCDCagency : {"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}
         * tbSCDCcenter : {"SCDCCenterID":"SCDCC04","SCDCCenterName":"ศูนย์พิสูจน์หลักฐาน 4","SCDCCenterProvince":"(ขอนแก่น)"}
         */

        private List<ApiNoticeCase> result;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public List<ApiNoticeCase> getResult() {
            return result;
        }

        public void setResult(List<ApiNoticeCase> result) {
            this.result = result;
        }
    }
}
