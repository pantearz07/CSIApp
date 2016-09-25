package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbScheduleGroup;
import com.scdc.csiapp.tablemodel.TbScheduleInvInGroup;
import com.scdc.csiapp.tablemodel.TbScheduleInvestigates;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiSchedule implements Serializable {
    // ตารางหลัก
    TbScheduleInvestigates tbScheduleInvestigates;

    // บอกว่าหน่วยไหนรับผิดชอบ
    TbSCDCagency tbSCDCagency;

    List<ScheduleGroup> scheduleGroupList;

    class ScheduleGroup {
        //เป็นลำดับกลุ่มที่อะไร
        TbScheduleGroup tbScheduleGroup;

        // ในกลุ่มนั้นมีใครบ้าง
        class ScheduleInvInGroup {
            TbScheduleInvInGroup tbScheduleInvInGroup;
            //เฉพาะคนที่ได้รับมอบหมาย
            List<ApiProfile> apiProfiles;
        }
    }

}
