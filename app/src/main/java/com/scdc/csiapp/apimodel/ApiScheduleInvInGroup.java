package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbScheduleInvInGroup;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 23/10/2559.
 */

public class ApiScheduleInvInGroup implements Serializable {
    String mode3;
    TbScheduleInvInGroup tbScheduleInvInGroup;
    TbOfficial tbOfficial;

    public TbScheduleInvInGroup getTbScheduleInvInGroup() {
        return tbScheduleInvInGroup;
    }

    public void setTbScheduleInvInGroup(TbScheduleInvInGroup tbScheduleInvInGroup) {
        this.tbScheduleInvInGroup = tbScheduleInvInGroup;
    }

    public TbOfficial getTbOfficial() {
        return tbOfficial;
    }

    public void setTbOfficial(TbOfficial tbOfficial) {
        this.tbOfficial = tbOfficial;
    }

    public String getMode3() {
        return mode3;
    }

    public void setMode3(String mode3) {
        this.mode3 = mode3;
    }
}
