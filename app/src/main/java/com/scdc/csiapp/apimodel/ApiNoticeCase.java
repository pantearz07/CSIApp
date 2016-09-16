package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseSceneType;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbNoticeCase;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbProvince;
import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbSCDCcenter;
import com.scdc.csiapp.tablemodel.TbSubcaseSceneType;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiNoticeCase {
    //ตารางหลัก
    TbNoticeCase tbNoticeCase;

    // InquiryOfficialID คือ OfficialID เพื่อบอกว่าใครรับผิดชอบคดี
    TbOfficial tbOfficial;

    // ใช้ใน tbCaseScene เพื่อบอกประเภทคดี
    TbCaseSceneType tbCaseSceneType;
    TbSubcaseSceneType tbSubcaseSceneType;

    // ใช้ใน tbCaseScene เพื่อบอกว่าสถานีตำรวจไหนรับผิดชอบ
    TbPoliceStation tbPoliceStation;
    TbPoliceAgency tbPoliceAgency;

    // ใช้ใน tbCaseScene เพื่อบอกสถานที่เกิดเหตุ
    TbDistrict tbDistrict;
    TbProvince tbProvince;
    TbAmphur tbAmphur;

    // ใช้ใน tbCaseScene กรณี AccessType = investigator
    TbSCDCagency tbSCDCagency;
    TbSCDCcenter tbSCDCcenter;


}
