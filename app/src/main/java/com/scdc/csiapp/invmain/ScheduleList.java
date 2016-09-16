package com.scdc.csiapp.invmain;

/**
 * Created by Pantearz07 on 15/10/2558.
 */
public class ScheduleList {
    String ScheduleID;
    String Schedule_Date;
    String Schedule_Month;
    String ScheduleGroupID;

    ScheduleList(String ScheduleID,
                 String Schedule_Date,
                 String Schedule_Month,
                 String ScheduleGroupID) {
        this.ScheduleID= ScheduleID;
        this.Schedule_Date = Schedule_Date;
        this.Schedule_Month = Schedule_Month;
        this.ScheduleGroupID = ScheduleGroupID;
    }
}
