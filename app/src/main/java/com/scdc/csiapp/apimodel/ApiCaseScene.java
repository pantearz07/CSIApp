package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbAmphur;
import com.scdc.csiapp.tablemodel.TbCaseScene;
import com.scdc.csiapp.tablemodel.TbCaseSceneType;
import com.scdc.csiapp.tablemodel.TbDistrict;
import com.scdc.csiapp.tablemodel.TbFindEvidence;
import com.scdc.csiapp.tablemodel.TbInvestigatorsInScene;
import com.scdc.csiapp.tablemodel.TbNoticeCase;
import com.scdc.csiapp.tablemodel.TbOfficial;
import com.scdc.csiapp.tablemodel.TbPoliceAgency;
import com.scdc.csiapp.tablemodel.TbPoliceStation;
import com.scdc.csiapp.tablemodel.TbPropertyLoss;
import com.scdc.csiapp.tablemodel.TbProvince;
import com.scdc.csiapp.tablemodel.TbResultScene;
import com.scdc.csiapp.tablemodel.TbResultSceneType;
import com.scdc.csiapp.tablemodel.TbSCDCagency;
import com.scdc.csiapp.tablemodel.TbSCDCcenter;
import com.scdc.csiapp.tablemodel.TbSceneFeatureInSide;
import com.scdc.csiapp.tablemodel.TbSceneFeatureOutside;
import com.scdc.csiapp.tablemodel.TbSceneInvestigation;
import com.scdc.csiapp.tablemodel.TbSubcaseSceneType;

import java.util.List;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiCaseScene {
    // ตารางหลัก
    TbCaseScene tbCaseScene;

    // InvestigatorOfficialID คือ OfficialID เพื่อบอกว่าใครรับผิดชอบคดี
    TbOfficial tbOfficial;

    // ใช้เชื่อม tbNoticeCase เพื่อจะได้ตรวจสอบ CaseStatus ได้
    TbNoticeCase tbNoticeCase;

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

    // เก็บรายชื่อผู้เกี่ยวข้อง
    List<Investigators> investigatorsList;

    class Investigators {
        TbInvestigatorsInScene tbInvestigatorsInScene;
        TbOfficial tbOfficial;
    }

    // เก็บเวลาที่ออกตรวจสถานที่นั้นๆ
    List<TbSceneInvestigation> tbSceneInvestigations;

    // เก็บลักษณะภายนอก
    TbSceneFeatureOutside tbSceneFeatureOutside;

    // เก็บข้อมูลลักษณะภายใน และไฟล์มิเดีย
    List<SceneFeatureInSide> sceneFeatureInSideList;

    class SceneFeatureInSide {
        TbSceneFeatureInSide tbSceneFeatureInSide;
        List<ApiMultimedia> apiMultimedias;
    }

    // ตือ มิเดีย เฉพาะ photo outside
    List<ApiMultimedia> apiMultimediasOutSide;

    // วัตถุพยานในคดีนั้นๆ รอยนิ้วมือ รอยเท้า
    List<FindEvidence> findEvidenceList;

    class FindEvidence {
        TbFindEvidence tbFindEvidence;
        List<ApiMultimedia> apiMultimedias;
    }

    // สภาพที่เกิดเหตุ
    class ResultScene {
        TbResultScene tbResultScene;
        TbResultSceneType tbResultSceneType;
        List<ApiMultimedia> apiMultimedias;
    }

    // ทรัพย์สินที่สูญหาย
    List<TbPropertyLoss> tbPropertyLosses;


}
