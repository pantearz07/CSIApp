package com.scdc.csiapp.apimodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pantearz07 on 14/10/2559.
 */

public class ApiListOfficial implements Serializable {

    /**
     * status : success
     * data : {"action":"list_official","reason":"ดาวน์โหลดข้อมูลเรียบร้อยแล้ว","result":[{"tbOfficial":{"OfficialID":"INV_SCDCC04_1","FirstName":"พรพรรณ","LastName":"ประสงค์ธรรม","Alias":"46","Rank":"ร.ต.อ.หญิง","Position":"นวท. (สบ2)","SubPossition":"","PhoneNumber":"818646042","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv1"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}},{"tbOfficial":{"OfficialID":"INV_SCDCC04_2","FirstName":"วรากุล","LastName":"กาญจนกัญโห","Alias":"44","Rank":"พ.ต.ท.","Position":"นวท. (สบ3)","SubPossition":"","PhoneNumber":"811454536","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv2"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}},{"tbOfficial":{"OfficialID":"INV_SCDCC04_3","FirstName":"นันทกาล","LastName":"ตาลจินดา","Alias":"45","Rank":"พ.ต.ท.หญิง","Position":"นวท. (สบ2)","SubPossition":"","PhoneNumber":"81779954","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv3"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}},{"tbOfficial":{"OfficialID":"INV_SCDCC04_4","FirstName":"มัตติกา","LastName":"สุขประเสริฐ","Alias":"47","Rank":"ร.ต.อ.หญิง","Position":"นวท. (สบ2)","SubPossition":"","PhoneNumber":"818628977","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv4"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}},{"tbOfficial":{"OfficialID":"INV_SCDCC04_5","FirstName":"เอื้องฟ้า","LastName":"จันทะเกิด","Alias":"43","Rank":"พ.ต.ท.หญิง","Position":"นวท. (สบ3)","SubPossition":"","PhoneNumber":"817121277","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv5"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}}]}
     */

    public String status;
    /**
     * action : list_official
     * reason : ดาวน์โหลดข้อมูลเรียบร้อยแล้ว
     * result : [{"tbOfficial":{"OfficialID":"INV_SCDCC04_1","FirstName":"พรพรรณ","LastName":"ประสงค์ธรรม","Alias":"46","Rank":"ร.ต.อ.หญิง","Position":"นวท. (สบ2)","SubPossition":"","PhoneNumber":"818646042","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv1"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}},{"tbOfficial":{"OfficialID":"INV_SCDCC04_2","FirstName":"วรากุล","LastName":"กาญจนกัญโห","Alias":"44","Rank":"พ.ต.ท.","Position":"นวท. (สบ3)","SubPossition":"","PhoneNumber":"811454536","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv2"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}},{"tbOfficial":{"OfficialID":"INV_SCDCC04_3","FirstName":"นันทกาล","LastName":"ตาลจินดา","Alias":"45","Rank":"พ.ต.ท.หญิง","Position":"นวท. (สบ2)","SubPossition":"","PhoneNumber":"81779954","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv3"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}},{"tbOfficial":{"OfficialID":"INV_SCDCC04_4","FirstName":"มัตติกา","LastName":"สุขประเสริฐ","Alias":"47","Rank":"ร.ต.อ.หญิง","Position":"นวท. (สบ2)","SubPossition":"","PhoneNumber":"818628977","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv4"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}},{"tbOfficial":{"OfficialID":"INV_SCDCC04_5","FirstName":"เอื้องฟ้า","LastName":"จันทะเกิด","Alias":"43","Rank":"พ.ต.ท.หญิง","Position":"นวท. (สบ3)","SubPossition":"","PhoneNumber":"817121277","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv5"},"tbPoliceStation":null,"tbSCDCagency":{"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}}]
     */

    public DataEntity data;

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

    public class DataEntity {
        private String action;
        private String reason;
        /**
         * tbOfficial : {"OfficialID":"INV_SCDCC04_1","FirstName":"พรพรรณ","LastName":"ประสงค์ธรรม","Alias":"46","Rank":"ร.ต.อ.หญิง","Position":"นวท. (สบ2)","SubPossition":"","PhoneNumber":"818646042","OfficialEmail":"crimescene004@gmail.com","OfficialDisplayPic":"","AccessType":"investigator","SCDCAgencyCode":"SCDCA401","PoliceStationID":null,"id_users":"inv1"}
         * tbPoliceStation : null
         * tbSCDCagency : {"SCDCAgencyCode":"SCDCA401","SCDCCenterID":"SCDCC04","SCDCAgencyName":"กลุ่มงานตรวจสถานที่เกิดเหตุ (ขอนแก่น)"}
         */

        public List<ApiOfficial> result;

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

        public List<ApiOfficial> getResult() {
            return result;
        }

        public void setResult(List<ApiOfficial> result) {
            this.result = result;
        }


    }
}
