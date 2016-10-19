package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbFindEvidence implements Serializable {
    // From Table findevidence field name FindEvidenceID
    public String FindEvidenceID = "";

    // From Table findevidence field name CaseReportID
    public String CaseReportID = "";

    // From Table findevidence field name SceneInvestID
    public String SceneInvestID = "";

    // From Table findevidence field name EvidenceTypeID
    public String EvidenceTypeID = "";

    // From Table findevidence field name EvidenceNumber
    public String EvidenceNumber = "";

    // From Table findevidence field name FindEvidenceZone
    public String FindEvidenceZone = "";

    // From Table findevidence field name FindEvidencecol
    public String FindEvidencecol = "";

    // From Table findevidence field name Marking
    public String Marking = "";

    // From Table findevidence field name Parceling
    public String Parceling = "";

    // From Table findevidence field name EvidencePerformed
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

    public String getEvidenceTypeID() {
        return EvidenceTypeID;
    }

    public void setEvidenceTypeID(String evidenceTypeID) {
        EvidenceTypeID = evidenceTypeID;
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
