package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceCenter;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbRegistrationGCM;
import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbSCDCcenter;
import com.scdc.csiapp.tablemodel.TbUsers;

import java.util.List;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiProfile {
    TbOfficial tbOfficial;

    // ใช้ใน tbOfficial กรณี AccessType = investigator
    TbSCDCagency tbSCDCagency;
    TbSCDCcenter tbSCDCcenter;

    // ใช้ใน tbOfficial กรณี AccessType = inquiryofficial
    TbPoliceStation tbPoliceStation;
    TbPoliceAgency tbPoliceAgency;
    TbPoliceCenter tbPoliceCenter;

    // ข้อมูล Username, Password(MD5)
    TbUsers tbUsers;

    // Token ของ User ตามอุปกรณ์แต่ละชิ้น
    List<TbRegistrationGCM> tbRegistrationGCMList;
}
