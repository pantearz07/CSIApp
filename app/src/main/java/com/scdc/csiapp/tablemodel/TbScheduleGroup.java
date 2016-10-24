package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbScheduleGroup implements Serializable {
    // From Table schedulegroup field name ScheduleGroupID
    public String ScheduleGroupID = "";

    // From Table schedulegroup field name ScheduleInvestigateID
    public String ScheduleInvestigateID = "";

    public String getScheduleInvestigateID() {
        return ScheduleInvestigateID;
    }

    public void setScheduleInvestigateID(String scheduleInvestigateID) {
        ScheduleInvestigateID = scheduleInvestigateID;
    }

    public String getScheduleGroupID() {
        return ScheduleGroupID;
    }

    public void setScheduleGroupID(String scheduleGroupID) {
        ScheduleGroupID = scheduleGroupID;
    }
}
