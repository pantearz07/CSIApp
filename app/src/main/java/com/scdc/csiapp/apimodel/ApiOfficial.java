package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbSCDCagency;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 14/10/2559.
 */

public class ApiOfficial implements Serializable {
    // โหมดเตรียมไว้ ใช้บอกว่า online, offline
    String mode;
    TbOfficial tbOfficial;

    TbPoliceStation tbPoliceStation;
    TbSCDCagency tbSCDCagency;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public TbOfficial getTbOfficial() {
        return tbOfficial;
    }

    public void setTbOfficial(TbOfficial tbOfficial) {
        this.tbOfficial = tbOfficial;
    }

    public TbPoliceStation getTbPoliceStation() {
        return tbPoliceStation;
    }

    public void setTbPoliceStation(TbPoliceStation tbPoliceStation) {
        this.tbPoliceStation = tbPoliceStation;
    }

    public TbSCDCagency getTbSCDCagency() {
        return tbSCDCagency;
    }

    public void setTbSCDCagency(TbSCDCagency tbSCDCagency) {
        this.tbSCDCagency = tbSCDCagency;
    }

}
