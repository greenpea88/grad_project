package com.grad_proj.assembletickets.front;

import java.io.Serializable;
import java.util.ArrayList;

public class Show implements Serializable {
    int id;
    String title;
    int type;
    String startDate;
    String endDate;
    String ticketOpen;
    String time;
    int runningTime;
    String price;
    String buyTicket;
    String posterSrc;
    String venue;
    String registeredTime;

//    ArrayList<Performer> performerList = new ArrayList<Performer>();

    public int getId() {
        return id;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getTicketOpen() {
        return ticketOpen;
    }

    public String getBuyTicket() {
        return buyTicket;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public String getPosterSrc() {
        return posterSrc;
    }

//    public ArrayList<Performer> getPerformerList() {
//        return performerList;
//    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getVenue() {
        return venue;
    }

    public String getRegisteredTime() {
        return registeredTime;
    }

    //MARK: - setter
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTicketOpen(String ticketOpen) {
        this.ticketOpen = ticketOpen;
    }

    public void setBuyTicket(String buyTicket) {
        this.buyTicket = buyTicket;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public void setPosterSrc(String posterSrc) {
        this.posterSrc = posterSrc;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

//    public void setPerformerList(ArrayList<Performer> performerList) {
//        this.performerList = performerList;
//    }

    public void setRegisteredTime(String registeredTime) {
        this.registeredTime = registeredTime;
    }
}
