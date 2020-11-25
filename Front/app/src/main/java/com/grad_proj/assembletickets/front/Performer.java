package com.grad_proj.assembletickets.front;

import java.io.Serializable;

public class Performer implements Serializable {
    int id;
    String name;
    String imgSrc;
//    Boolean setAlarm;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

//    public Boolean getSetAlarm() {
//        return setAlarm;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

//    public void setSetAlarm(Boolean setAlarm) {
//        this.setAlarm = setAlarm;
//    }
}
