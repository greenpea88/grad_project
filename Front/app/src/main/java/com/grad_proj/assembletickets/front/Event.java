package com.grad_proj.assembletickets.front;

public class Event {
    private String date;
    private String time;
    private String eventName;
    private String eventContent;

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

    public String getEventContent() {
        return eventContent;
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

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }
}
