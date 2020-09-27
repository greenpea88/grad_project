package com.grad_proj.assembletickets.front;

public class Event {
    private String date;
    private String time;
    private String eventName;

//    public Event(String date, String time, String eventName){
//        this.date = date;
//        this.time = time;
//        this.eventName = eventName;
//    }

    public String getDate() {
        return this.date;
    }

    public String getTime(){
        return this.time;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
