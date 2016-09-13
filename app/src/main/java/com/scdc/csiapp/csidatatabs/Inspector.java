package com.scdc.csiapp.csidatatabs;

/**
 * Created by Pantearz07 on 26/10/2558.
 */
class Inspector {
    String rank;
    String name;
    String position;
    int photoId;
    String tel;
    String agencyname;
    String stationname;
    Inspector(String rank,String name, String position, int photoId,String tel,String agencyname,String stationname) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.photoId = photoId;
        this.tel = tel;
        this.agencyname = agencyname;
        this.stationname = stationname;
    }
}