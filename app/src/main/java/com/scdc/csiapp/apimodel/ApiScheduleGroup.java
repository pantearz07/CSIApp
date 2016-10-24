package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbScheduleGroup;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pantearz07 on 23/10/2559.
 */

public class ApiScheduleGroup implements Serializable {
    String mode2;
    TbScheduleGroup tbScheduleGroup;
    List<ApiScheduleInvInGroup> apiScheduleInvInGroup;

    public TbScheduleGroup getTbScheduleGroup() {
        return tbScheduleGroup;
    }

    public void setTbScheduleGroup(TbScheduleGroup tbScheduleGroup) {
        this.tbScheduleGroup = tbScheduleGroup;
    }

    public List<ApiScheduleInvInGroup> getApiScheduleInvInGroup() {
        return apiScheduleInvInGroup;
    }

    public void setApiScheduleInvInGroup(List<ApiScheduleInvInGroup> apiScheduleInvInGroup) {
        this.apiScheduleInvInGroup = apiScheduleInvInGroup;
    }

    public String getMode2() {
        return mode2;
    }

    public void setMode2(String mode2) {
        this.mode2 = mode2;
    }
}
