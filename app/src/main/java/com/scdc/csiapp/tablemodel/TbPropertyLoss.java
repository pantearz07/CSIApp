package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbPropertyLoss implements Serializable {
    public String TB_PropertyLoss = "propertyloss";

    // From Table propertyloss field name PropertyLossID
    public String COL_PropertyLossID = "PropertyLossID";
    public String PropertyLossID = "";

    // From Table propertyloss field name CaseReportID
    public String COL_CaseReportID = "CaseReportID";
    public String CaseReportID = "";

    // From Table propertyloss field name PropertyLossName
    public String COL_PropertyLossName = "PropertyLossName";
    public String PropertyLossName = "";

    // From Table propertyloss field name PropertyLossNumber
    public String COL_PropertyLossNumber = "PropertyLossNumber";
    public String PropertyLossNumber = "";

    // From Table propertyloss field name PropertyLossUnit
    public String COL_PropertyLossUnit = "PropertyLossUnit";
    public String PropertyLossUnit = "";

    // From Table propertyloss field name PropertyLossPosition
    public String COL_PropertyLossPosition = "PropertyLossPosition";
    public String PropertyLossPosition = "";

    // From Table propertyloss field name PropInsurance
    public String COL_PropInsurance = "PropInsurance";
    public String PropInsurance = "";

    public String getPropertyLossID() {
        return PropertyLossID;
    }

    public void setPropertyLossID(String propertyLossID) {
        PropertyLossID = propertyLossID;
    }

    public String getCaseReportID() {
        return CaseReportID;
    }

    public void setCaseReportID(String caseReportID) {
        CaseReportID = caseReportID;
    }

    public String getPropertyLossName() {
        return PropertyLossName;
    }

    public void setPropertyLossName(String propertyLossName) {
        PropertyLossName = propertyLossName;
    }

    public String getPropertyLossNumber() {
        return PropertyLossNumber;
    }

    public void setPropertyLossNumber(String propertyLossNumber) {
        PropertyLossNumber = propertyLossNumber;
    }

    public String getPropertyLossUnit() {
        return PropertyLossUnit;
    }

    public void setPropertyLossUnit(String propertyLossUnit) {
        PropertyLossUnit = propertyLossUnit;
    }

    public String getPropertyLossPosition() {
        return PropertyLossPosition;
    }

    public void setPropertyLossPosition(String propertyLossPosition) {
        PropertyLossPosition = propertyLossPosition;
    }

    public String getPropInsurance() {
        return PropInsurance;
    }

    public void setPropInsurance(String propInsurance) {
        PropInsurance = propInsurance;
    }
}
