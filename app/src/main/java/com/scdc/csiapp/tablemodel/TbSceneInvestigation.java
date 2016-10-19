package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbSceneInvestigation implements Serializable {

    // From Table sceneinvestigation field name SceneInvestID
    public String SceneInvestID = "";

    // From Table sceneinvestigation field name CaseReportID
    public String CaseReportID = "";

    // From Table sceneinvestigation field name SceneInvestDate
    public String SceneInvestDate = "";

    // From Table sceneinvestigation field name SceneInvestTime
    public String SceneInvestTime = "";

    public String getSceneInvestID() {
        return SceneInvestID;
    }

    public void setSceneInvestID(String sceneInvestID) {
        SceneInvestID = sceneInvestID;
    }

    public String getCaseReportID() {
        return CaseReportID;
    }

    public void setCaseReportID(String caseReportID) {
        CaseReportID = caseReportID;
    }

    public String getSceneInvestDate() {
        return SceneInvestDate;
    }

    public void setSceneInvestDate(String sceneInvestDate) {
        SceneInvestDate = sceneInvestDate;
    }

    public String getSceneInvestTime() {
        return SceneInvestTime;
    }

    public void setSceneInvestTime(String sceneInvestTime) {
        SceneInvestTime = sceneInvestTime;
    }
}
