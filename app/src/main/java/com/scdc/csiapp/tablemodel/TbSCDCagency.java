package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbSCDCagency implements Serializable {
    // From Table scdcagency field name SCDCAgencyCode
    public String SCDCAgencyCode = "";

    // From Table scdcagency field name SCDCCenterID
    public String SCDCCenterID = "";

    // From Table scdcagency field name SCDCAgencyName
    public String SCDCAgencyName = "";

    public String getSCDCAgencyCode() {
        return SCDCAgencyCode;
    }

    public void setSCDCAgencyCode(String SCDCAgencyCode) {
        this.SCDCAgencyCode = SCDCAgencyCode;
    }

    public String getSCDCCenterID() {
        return SCDCCenterID;
    }

    public void setSCDCCenterID(String SCDCCenterID) {
        this.SCDCCenterID = SCDCCenterID;
    }

    public String getSCDCAgencyName() {
        return SCDCAgencyName;
    }

    public void setSCDCAgencyName(String SCDCAgencyName) {
        this.SCDCAgencyName = SCDCAgencyName;
    }
}
