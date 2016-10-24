package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbScheduleInvestigates implements Serializable {
    // From Table scheduleinvestigates field name ScheduleInvestigateID
    public String ScheduleInvestigateID = "";

    // From Table scheduleinvestigates field name ScheduleDate
    public String ScheduleDate = "";

    // From Table scheduleinvestigates field name ScheduleMonth
    public String ScheduleMonth = "";

    // From Table scheduleinvestigates field name SCDCAgencyCode
    public String SCDCAgencyCode = "";

    public String getScheduleInvestigateID() {
        return ScheduleInvestigateID;
    }

    public void setScheduleInvestigateID(String scheduleInvestigateID) {
        ScheduleInvestigateID = scheduleInvestigateID;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public String getScheduleMonth() {
        return ScheduleMonth;
    }

    public void setScheduleMonth(String scheduleMonth) {
        ScheduleMonth = scheduleMonth;
    }

    public String getSCDCAgencyCode() {
        return SCDCAgencyCode;
    }

    public void setSCDCAgencyCode(String SCDCAgencyCode) {
        this.SCDCAgencyCode = SCDCAgencyCode;
    }
}
