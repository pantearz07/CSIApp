package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbScheduleInvInGroup implements Serializable {
    // From Table scheduleinvingroup field name ScheduleGroupID
    public String ScheduleGroupID = "";

    // From Table scheduleinvingroup field name InvOfficialID
    public String InvOfficialID = "";

    public String getScheduleGroupID() {
        return ScheduleGroupID;
    }

    public void setScheduleGroupID(String scheduleGroupID) {
        ScheduleGroupID = scheduleGroupID;
    }

    public String getInvOfficialID() {
        return InvOfficialID;
    }

    public void setInvOfficialID(String invOfficialID) {
        InvOfficialID = invOfficialID;
    }
}
