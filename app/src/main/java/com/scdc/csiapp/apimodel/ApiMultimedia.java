package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.scdc.csiapp.tablemodel.TbPhotoOfInside;
import com.scdc.csiapp.tablemodel.TbPhotoOfOutside;
import com.scdc.csiapp.tablemodel.TbResultScene;
import com.scdc.csiapp.tablemodel.TbSceneFeatureInSide;

import java.util.List;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiMultimedia {
    // ตารางหลัก
    TbMultimediaFile tbMultimediaFile;

    // ข้อมูลตัวนั้นมีโอกาสเป็นภายนอกได้หลายคดี
    List<TbPhotoOfOutside> tbPhotoOfOutsideList;

    // ข้อมูลตัวนั้นมีโอกาสเป็นภายในได้หลายคดี
    List<TbPhotoOfInside> tbPhotoOfInsides;

    // ข้อมูลผลตรวจหรือวัตถุในคดี
    List<TbResultScene> tbResultScenes;

}
