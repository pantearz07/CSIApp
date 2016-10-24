package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbInvestigatorsInScene;
import com.scdc.csiapp.tablemodel.TbOfficial;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 21/10/2559.
 */

public class ApiInvestigatorsInScene implements Serializable {
    TbInvestigatorsInScene tbInvestigatorsInScene;
    TbOfficial tbOfficial;
    public TbInvestigatorsInScene getTbInvestigatorsInScene() {
        return tbInvestigatorsInScene;
    }

    public void setTbInvestigatorsInScene(TbInvestigatorsInScene tbInvestigatorsInScene) {
        this.tbInvestigatorsInScene = tbInvestigatorsInScene;
    }

    public TbOfficial getTbOfficial() {
        return tbOfficial;
    }

    public void setTbOfficial(TbOfficial tbOfficial) {
        this.tbOfficial = tbOfficial;
    }
}
