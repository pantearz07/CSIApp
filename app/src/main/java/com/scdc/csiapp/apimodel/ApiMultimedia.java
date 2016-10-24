package com.scdc.csiapp.apimodel;

import com.scdc.csiapp.tablemodel.TbMultimediaFile;
import com.scdc.csiapp.tablemodel.TbPhotoOfEvidence;
import com.scdc.csiapp.tablemodel.TbPhotoOfInside;
import com.scdc.csiapp.tablemodel.TbPhotoOfOutside;
import com.scdc.csiapp.tablemodel.TbPhotoOfPropertyless;
import com.scdc.csiapp.tablemodel.TbPhotoOfResultscene;

import java.io.Serializable;

/**
 * Created by cbnuke on 9/16/16.
 */
public class ApiMultimedia implements Serializable {
    // ตารางหลัก
    TbMultimediaFile tbMultimediaFile;
    TbPhotoOfOutside tbPhotoOfOutside;
    TbPhotoOfInside tbPhotoOfInside;
    TbPhotoOfEvidence tbPhotoOfEvidence;
    TbPhotoOfResultscene tbPhotoOfResultscene;
    TbPhotoOfPropertyless tbPhotoOfPropertyless;
    // ข้อมูลตัวนั้นมีโอกาสเป็นภายนอกได้หลายคดี
//    List<TbPhotoOfOutside> tbPhotoOfOutsides;
//
//    // ข้อมูลตัวนั้นมีโอกาสเป็นภายในได้หลายคดี
//    List<TbPhotoOfInside> tbPhotoOfInsides;
//
//    List<TbPhotoOfEvidence> tbPhotoOfEvidences;
//
//    List<TbPhotoOfResultscene> tbPhotoOfResultscenes;

    public TbMultimediaFile getTbMultimediaFile() {
        return tbMultimediaFile;
    }

    public void setTbMultimediaFile(TbMultimediaFile tbMultimediaFile) {
        this.tbMultimediaFile = tbMultimediaFile;
    }

    public TbPhotoOfOutside getTbPhotoOfOutside() {
        return tbPhotoOfOutside;
    }

    public void setTbPhotoOfOutside(TbPhotoOfOutside tbPhotoOfOutside) {
        this.tbPhotoOfOutside = tbPhotoOfOutside;
    }

    public TbPhotoOfInside getTbPhotoOfInside() {
        return tbPhotoOfInside;
    }

    public void setTbPhotoOfInside(TbPhotoOfInside tbPhotoOfInside) {
        this.tbPhotoOfInside = tbPhotoOfInside;
    }

    public TbPhotoOfEvidence getTbPhotoOfEvidence() {
        return tbPhotoOfEvidence;
    }

    public void setTbPhotoOfEvidence(TbPhotoOfEvidence tbPhotoOfEvidence) {
        this.tbPhotoOfEvidence = tbPhotoOfEvidence;
    }

    public TbPhotoOfResultscene getTbPhotoOfResultscene() {
        return tbPhotoOfResultscene;
    }

    public void setTbPhotoOfResultscene(TbPhotoOfResultscene tbPhotoOfResultscene) {
        this.tbPhotoOfResultscene = tbPhotoOfResultscene;
    }

    public TbPhotoOfPropertyless getTbPhotoOfPropertyless() {
        return tbPhotoOfPropertyless;
    }

    public void setTbPhotoOfPropertyless(TbPhotoOfPropertyless tbPhotoOfPropertyless) {
        this.tbPhotoOfPropertyless = tbPhotoOfPropertyless;
    }
    //    public List<TbPhotoOfOutside> getTbPhotoOfOutsides() {
//        return tbPhotoOfOutsides;
//    }
//
//    public void setTbPhotoOfOutsides(List<TbPhotoOfOutside> tbPhotoOfOutsides) {
//        this.tbPhotoOfOutsides = tbPhotoOfOutsides;
//    }
//
//    public List<TbPhotoOfInside> getTbPhotoOfInsides() {
//        return tbPhotoOfInsides;
//    }
//
//    public void setTbPhotoOfInsides(List<TbPhotoOfInside> tbPhotoOfInsides) {
//        this.tbPhotoOfInsides = tbPhotoOfInsides;
//    }
//
//    public List<TbPhotoOfEvidence> getTbPhotoOfEvidences() {
//        return tbPhotoOfEvidences;
//    }
//
//    public void setTbPhotoOfEvidences(List<TbPhotoOfEvidence> tbPhotoOfEvidences) {
//        this.tbPhotoOfEvidences = tbPhotoOfEvidences;
//    }
//
//    public List<TbPhotoOfResultscene> getTbPhotoOfResultscenes() {
//        return tbPhotoOfResultscenes;
//    }
//
//    public void setTbPhotoOfResultscenes(List<TbPhotoOfResultscene> tbPhotoOfResultscenes) {
//        this.tbPhotoOfResultscenes = tbPhotoOfResultscenes;
//    }
}
