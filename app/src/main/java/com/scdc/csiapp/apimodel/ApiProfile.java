package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbRegistrationGCM;
import com.scdc.csiapp.tablemodel.TbUsers;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiProfile implements Serializable {
    // ตารางหลัก
    private TbOfficial tbOfficial;

    // ใช้ใน tbOfficial กรณี AccessType = investigator
    private String SCDCAgencyCode;

    // ใช้ใน tbOfficial กรณี AccessType = inquiryofficial
    private String PoliceStationID;

    // ข้อมูล Username, Password(MD5)
    private TbUsers tbUsers;

    // Token ของ User ตามอุปกรณ์แต่ละชิ้น (ยังไม่ได้ใช้งานหรอก)
    private List<TbRegistrationGCM> tbRegistrationGCMList;

    public TbOfficial getTbOfficial() {
        return tbOfficial;
    }

    public void setTbOfficial(TbOfficial tbOfficial) {
        this.tbOfficial = tbOfficial;
    }

    public String getSCDCAgencyCode() {
        return SCDCAgencyCode;
    }

    public void setSCDCAgencyCode(String SCDCAgencyCode) {
        this.SCDCAgencyCode = SCDCAgencyCode;
    }

    public String getPoliceStationID() {
        return PoliceStationID;
    }

    public void setPoliceStationID(String policeStationID) {
        PoliceStationID = policeStationID;
    }

    public TbUsers getTbUsers() {
        return tbUsers;
    }

    public void setTbUsers(TbUsers tbUsers) {
        this.tbUsers = tbUsers;
    }

    public List<TbRegistrationGCM> getTbRegistrationGCMList() {
        return tbRegistrationGCMList;
    }

    public void setTbRegistrationGCMList(List<TbRegistrationGCM> tbRegistrationGCMList) {
        this.tbRegistrationGCMList = tbRegistrationGCMList;
    }
}
