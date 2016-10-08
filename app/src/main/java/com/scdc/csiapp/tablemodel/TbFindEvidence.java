package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbFindEvidence implements Serializable {
    public String TB_FindEvidence = "findevidence";
    // From Table findevidence field name FindEvidenceID
    public String COL_FindEvidenceID = "FindEvidenceID";
    public String FindEvidenceID = "";

    // From Table findevidence field name CaseReportID
    public String COL_CaseReportID = "CaseReportID";
    public String CaseReportID = "";

    // From Table findevidence field name SceneInvestID
    public String COL_SceneInvestID = "SceneInvestID";
    public String SceneInvestID = "";

    // From Table findevidence field name EvidenceTypeName
    public String COL_EvidenceTypeName = "EvidenceTypeName";
    public String EvidenceTypeName = "";

    // From Table findevidence field name EvidenceNumber
    public String COL_EvidenceNumber = "EvidenceNumber";
    public String EvidenceNumber = "";

    // From Table findevidence field name FindEvidenceZone
    public String COL_FindEvidenceZone = "FindEvidenceZone";
    public String FindEvidenceZone = "";

    // From Table findevidence field name FindEvidencecol
    public String COL_FindEvidencecol = "FindEvidencecol";
    public String FindEvidencecol = "";

    // From Table findevidence field name Marking
    public String COL_Marking = "Marking";
    public String Marking = "";

    // From Table findevidence field name Parceling
    public String COL_Parceling = "Parceling";
    public String Parceling = "";

    // From Table findevidence field name EvidencePerformed
    public String COL_EvidencePerformed = "EvidencePerformed";
    public String EvidencePerformed = "";

    public String getFindEvidenceID() {
        return FindEvidenceID;
    }

    public void setFindEvidenceID(String findEvidenceID) {
        FindEvidenceID = findEvidenceID;
    }

    public String getCaseReportID() {
        return CaseReportID;
    }

    public void setCaseReportID(String caseReportID) {
        CaseReportID = caseReportID;
    }

    public String getSceneInvestID() {
        return SceneInvestID;
    }

    public void setSceneInvestID(String sceneInvestID) {
        SceneInvestID = sceneInvestID;
    }

    public String getEvidenceTypeName() {
        return EvidenceTypeName;
    }

    public void setEvidenceTypeName(String evidenceTypeName) {
        EvidenceTypeName = evidenceTypeName;
    }

    public String getEvidenceNumber() {
        return EvidenceNumber;
    }

    public void setEvidenceNumber(String evidenceNumber) {
        EvidenceNumber = evidenceNumber;
    }

    public String getFindEvidenceZone() {
        return FindEvidenceZone;
    }

    public void setFindEvidenceZone(String findEvidenceZone) {
        FindEvidenceZone = findEvidenceZone;
    }

    public String getFindEvidencecol() {
        return FindEvidencecol;
    }

    public void setFindEvidencecol(String findEvidencecol) {
        FindEvidencecol = findEvidencecol;
    }

    public String getMarking() {
        return Marking;
    }

    public void setMarking(String marking) {
        Marking = marking;
    }

    public String getParceling() {
        return Parceling;
    }

    public void setParceling(String parceling) {
        Parceling = parceling;
    }

    public String getEvidencePerformed() {
        return EvidencePerformed;
    }

    public void setEvidencePerformed(String evidencePerformed) {
        EvidencePerformed = evidencePerformed;
    }
}
