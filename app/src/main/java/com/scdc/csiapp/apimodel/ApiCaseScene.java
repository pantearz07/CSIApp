package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseScene;
import com.scdc.csiapp.tablemodel.TbCaseSceneType;
import com.scdc.csiapp.tablemodel.TbClueShown;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbFindEvidence;
import com.scdc.csiapp.tablemodel.TbGatewayCriminal;
import com.scdc.csiapp.tablemodel.TbNoticeCase;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbPropertyLoss;
import com.scdc.csiapp.tablemodel.TbProvince;
import com.scdc.csiapp.tablemodel.TbResultScene;
import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbSCDCcenter;
import com.scdc.csiapp.tablemodel.TbSceneFeatureInSide;
import com.scdc.csiapp.tablemodel.TbSceneFeatureOutside;
import com.scdc.csiapp.tablemodel.TbSceneInvestigation;
import com.scdc.csiapp.tablemodel.TbSubcaseSceneType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiCaseScene implements Serializable {
    // โหมดเตรียมไว้ ใช้บอกว่า online, offline
    String mode;

    // ตารางหลัก
    TbCaseScene tbCaseScene;

    // InvestigatorOfficialID คือ OfficialID เพื่อบอกว่าใครรับผิดชอบคดี
    TbOfficial tbOfficial;

    // ใช้เชื่อม tbNoticeCase เพื่อจะได้ตรวจสอบ CaseStatus ได้
    TbNoticeCase tbNoticeCase;

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

    // เก็บเวลาที่ออกตรวจสถานที่นั้นๆ
    List<TbSceneInvestigation> tbSceneInvestigations;
    List<ApiInvestigatorsInScene> apiInvestigatorsInScenes;
    // เก็บลักษณะภายนอก
    TbSceneFeatureOutside tbSceneFeatureOutside;

    List<TbSceneFeatureInSide> tbSceneFeatureInSide;

    // วัตถุพยานในคดีนั้นๆ รอยนิ้วมือ รอยเท้า
    List<TbFindEvidence> tbFindEvidences;

    // สภาพที่เกิดเหตุ
    List<TbResultScene> tbResultScenes;
    // tbGatewayCriminals
    List<TbGatewayCriminal> tbGatewayCriminals;
    // tbClueShowns
    List<TbClueShown> tbClueShowns;
    // ทรัพย์สินที่สูญหาย
    List<TbPropertyLoss> tbPropertyLosses;
    // ตือ มิเดีย เฉพาะ photo outside

    List<ApiMultimedia> apiMultimedia;

    public List<ApiMultimedia> getApiMultimedia() {
        return apiMultimedia;
    }

    public void setApiMultimedia(List<ApiMultimedia> apiMultimedia) {
        this.apiMultimedia = apiMultimedia;
    }

    public List<ApiInvestigatorsInScene> getApiInvestigatorsInScenes() {
        return apiInvestigatorsInScenes;
    }

    public void setApiInvestigatorsInScenes(List<ApiInvestigatorsInScene> apiInvestigatorsInScenes) {
        this.apiInvestigatorsInScenes = apiInvestigatorsInScenes;
    }

    public TbCaseScene getTbCaseScene() {
        return tbCaseScene;
    }

    public void setTbCaseScene(TbCaseScene tbCaseScene) {
        this.tbCaseScene = tbCaseScene;
    }

    public TbOfficial getTbOfficial() {
        return tbOfficial;
    }

    public void setTbOfficial(TbOfficial tbOfficial) {
        this.tbOfficial = tbOfficial;
    }

    public TbNoticeCase getTbNoticeCase() {
        return tbNoticeCase;
    }

    public void setTbNoticeCase(TbNoticeCase tbNoticeCase) {
        this.tbNoticeCase = tbNoticeCase;
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

    public List<TbSceneInvestigation> getTbSceneInvestigations() {
        return tbSceneInvestigations;
    }

    public void setTbSceneInvestigations(List<TbSceneInvestigation> tbSceneInvestigations) {
        this.tbSceneInvestigations = tbSceneInvestigations;
    }

    public TbSceneFeatureOutside getTbSceneFeatureOutside() {
        return tbSceneFeatureOutside;
    }

    public void setTbSceneFeatureOutside(TbSceneFeatureOutside tbSceneFeatureOutside) {
        this.tbSceneFeatureOutside = tbSceneFeatureOutside;
    }

    public List<TbPropertyLoss> getTbPropertyLosses() {
        return tbPropertyLosses;
    }

    public void setTbPropertyLosses(List<TbPropertyLoss> tbPropertyLosses) {
        this.tbPropertyLosses = tbPropertyLosses;
    }

    public List<TbSceneFeatureInSide> getTbSceneFeatureInSide() {
        return tbSceneFeatureInSide;
    }

    public void setTbSceneFeatureInSide(List<TbSceneFeatureInSide> tbSceneFeatureInSide) {
        this.tbSceneFeatureInSide = tbSceneFeatureInSide;
    }


    public List<TbFindEvidence> getTbFindEvidences() {
        return tbFindEvidences;
    }

    public void setTbFindEvidences(List<TbFindEvidence> tbFindEvidences) {
        this.tbFindEvidences = tbFindEvidences;
    }

    public List<TbResultScene> getTbResultScenes() {
        return tbResultScenes;
    }

    public void setTbResultScenes(List<TbResultScene> tbResultScenes) {
        this.tbResultScenes = tbResultScenes;
    }

    public List<TbGatewayCriminal> getTbGatewayCriminals() {
        return tbGatewayCriminals;
    }

    public void setTbGatewayCriminals(List<TbGatewayCriminal> tbGatewayCriminals) {
        this.tbGatewayCriminals = tbGatewayCriminals;
    }

    public List<TbClueShown> getTbClueShowns() {
        return tbClueShowns;
    }

    public void setTbClueShowns(List<TbClueShown> tbClueShowns) {
        this.tbClueShowns = tbClueShowns;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
