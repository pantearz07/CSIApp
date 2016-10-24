package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbScheduleInvestigates;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pantearz07 on 22/10/2559.
 */

public class ApiScheduleInvestigates implements Serializable {
    String mode;
    TbScheduleInvestigates tbScheduleInvestigates;
    TbSCDCagency tbSCDCagency;
    List<ApiScheduleGroup> apiScheduleGroup;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public TbScheduleInvestigates getTbScheduleInvestigates() {
        return tbScheduleInvestigates;
    }

    public void setTbScheduleInvestigates(TbScheduleInvestigates tbScheduleInvestigates) {
        this.tbScheduleInvestigates = tbScheduleInvestigates;
    }

    public List<ApiScheduleGroup> getApiScheduleGroup() {
        return apiScheduleGroup;
    }

    public void setApiScheduleGroup(List<ApiScheduleGroup> apiScheduleGroup) {
        this.apiScheduleGroup = apiScheduleGroup;
    }

    public TbSCDCagency getTbSCDCagency() {
        return tbSCDCagency;
    }

    public void setTbSCDCagency(TbSCDCagency tbSCDCagency) {
        this.tbSCDCagency = tbSCDCagency;
    }
}
