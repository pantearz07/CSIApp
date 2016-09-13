package com.scdc.csiapp.policetabs;


/**
 * Created by Pantearz07 on 13/10/2558.
 */
class Person {
    String rank;
    String name;
    String position;
    int photoId;
    String tel;
    String agencyname;
    String stationname;
    Person(String rank,String name, String position, int photoId,String tel,String agencyname,String stationname) {
        this.rank = rank;
        this.name = name;
        this.position = position;
        this.photoId = photoId;
        this.tel = tel;
        this.agencyname = agencyname;
        this.stationname = stationname;
    }
}