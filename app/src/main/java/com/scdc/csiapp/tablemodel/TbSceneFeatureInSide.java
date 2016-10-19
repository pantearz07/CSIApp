package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbSceneFeatureInSide implements Serializable {

//    protected String COL_FeatureInsideID = "FeatureInsideID";
    public String FeatureInsideID = "";

    // From Table scenefeatureinside field name CaseReportID
    public String CaseReportID = "";

    // From Table scenefeatureinside field name FloorNo
    public String FloorNo = "";

    // From Table scenefeatureinside field name CaveNo
    public String CaveNo = "";

    // From Table scenefeatureinside field name FrontInside
    public String FrontInside = "";

    // From Table scenefeatureinside field name LeftInside
    public String LeftInside = "";

    // From Table scenefeatureinside field name RightInside
    public String RightInside = "";

    // From Table scenefeatureinside field name BackInside
    public String BackInside = "";

    // From Table scenefeatureinside field name CenterInside
    public String CenterInside = "";

    public String getFeatureInsideID() {
        return FeatureInsideID;
    }

    public void setFeatureInsideID(String featureInsideID) {
        FeatureInsideID = featureInsideID;
    }

    public String getCaseReportID() {
        return CaseReportID;
    }

    public void setCaseReportID(String caseReportID) {
        CaseReportID = caseReportID;
    }

    public String getFloorNo() {
        return FloorNo;
    }

    public void setFloorNo(String floorNo) {
        FloorNo = floorNo;
    }

    public String getCaveNo() {
        return CaveNo;
    }

    public void setCaveNo(String caveNo) {
        CaveNo = caveNo;
    }

    public String getFrontInside() {
        return FrontInside;
    }

    public void setFrontInside(String frontInside) {
        FrontInside = frontInside;
    }

    public String getLeftInside() {
        return LeftInside;
    }

    public void setLeftInside(String leftInside) {
        LeftInside = leftInside;
    }

    public String getRightInside() {
        return RightInside;
    }

    public void setRightInside(String rightInside) {
        RightInside = rightInside;
    }

    public String getBackInside() {
        return BackInside;
    }

    public void setBackInside(String backInside) {
        BackInside = backInside;
    }

    public String getCenterInside() {
        return CenterInside;
    }

    public void setCenterInside(String centerInside) {
        CenterInside = centerInside;
    }
}
