package com.grad_proj.assembletickets.front;

import java.util.Date;

public class Event {
    private int id;
    private String date;
    private int timeHour;
    private int timeMin;
    private String eventName;
    private String eventContent;
    private Boolean alarmSet;

//    public Event(String date, String time, String eventName){
//        this.date = date;
//        this.time = time;
//        this.eventName = eventName;
//    }

    public int getId(){
        return this.id;
    }

    public String getDate() {
        return this.date;
    }

    public int getTimeHour() {
        return timeHour;
    }

    public int getTimeMin() {
        return timeMin;
    }

    public String getEventName() {
        return this.eventName;
    }

    public String getEventContent() {
        return eventContent;
    }

    public Boolean getAlarmSet() {
        return alarmSet;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
    }

    public void setTimeMin(int timeMin) {
        this.timeMin = timeMin;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public void setAlarmSet(Boolean alarmSet){
        this.alarmSet = alarmSet;
    }
}
