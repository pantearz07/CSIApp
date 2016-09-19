package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseSceneType;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbNoticeCase;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbProvince;
import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbSCDCcenter;
import com.scdc.csiapp.tablemodel.TbSubcaseSceneType;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiNoticeCase {
    //ตารางหลัก
    TbNoticeCase tbNoticeCase;

    // InvestigatorOfficialID คือ OfficialID เพื่อบอกว่าใครรับผิดชอบคดี
    TbOfficial tbOfficial;

    // ใช้ใน tbCaseScene เพื่อบอกประเภทคดี
    TbCaseSceneType tbCaseSceneType;
    TbSubcaseSceneType tbSubcaseSceneType;

    // ใช้ใน tbCaseScene เพื่อบอกว่าสถานีตำรวจไหนรับผิดชอบ
    TbPoliceStation tbPoliceStation;
    TbPoliceAgency tbPoliceAgency;

    // ใช้ใน tbCaseScene เพื่อบอกสถานที่เกิดเหตุ
    TbDistrict tbDistrict;
    TbProvince tbProvince;
    TbAmphur tbAmphur;

    // ใช้ใน tbCaseScene กรณี AccessType = investigator
    TbSCDCagency tbSCDCagency;
    TbSCDCcenter tbSCDCcenter;

    public TbNoticeCase getTbNoticeCase() {
        return tbNoticeCase;
    }

    public void setTbNoticeCase(TbNoticeCase tbNoticeCase) {
        this.tbNoticeCase = tbNoticeCase;
    }

    public TbOfficial getTbOfficial() {
        return tbOfficial;
    }

    public void setTbOfficial(TbOfficial tbOfficial) {
        this.tbOfficial = tbOfficial;
    }

    public TbCaseSceneType getTbCaseSceneType() {
        return tbCaseSceneType;
    }

    public void setTbCaseSceneType(TbCaseSceneType tbCaseSceneType) {
        this.tbCaseSceneType = tbCaseSceneType;
    }

    public TbSubcaseSceneType getTbSubcaseSceneType() {
        return tbSubcaseSceneType;
    }

    public void setTbSubcaseSceneType(TbSubcaseSceneType tbSubcaseSceneType) {
        this.tbSubcaseSceneType = tbSubcaseSceneType;
    }

    public TbPoliceStation getTbPoliceStation() {
        return tbPoliceStation;
    }

    public void setTbPoliceStation(TbPoliceStation tbPoliceStation) {
        this.tbPoliceStation = tbPoliceStation;
    }

    public TbPoliceAgency getTbPoliceAgency() {
        return tbPoliceAgency;
    }

    public void setTbPoliceAgency(TbPoliceAgency tbPoliceAgency) {
        this.tbPoliceAgency = tbPoliceAgency;
    }

    public TbDistrict getTbDistrict() {
        return tbDistrict;
    }

    public void setTbDistrict(TbDistrict tbDistrict) {
        this.tbDistrict = tbDistrict;
    }

    public TbProvince getTbProvince() {
        return tbProvince;
    }

    public void setTbProvince(TbProvince tbProvince) {
        this.tbProvince = tbProvince;
    }

    public TbAmphur getTbAmphur() {
        return tbAmphur;
    }

    public void setTbAmphur(TbAmphur tbAmphur) {
        this.tbAmphur = tbAmphur;
    }

    public TbSCDCagency getTbSCDCagency() {
        return tbSCDCagency;
    }

    public void setTbSCDCagency(TbSCDCagency tbSCDCagency) {
        this.tbSCDCagency = tbSCDCagency;
    }

    public TbSCDCcenter getTbSCDCcenter() {
        return tbSCDCcenter;
    }

    public void setTbSCDCcenter(TbSCDCcenter tbSCDCcenter) {
        this.tbSCDCcenter = tbSCDCcenter;
    }
}
