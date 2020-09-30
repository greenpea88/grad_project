package com.grad_proj.assembletickets.front;

import java.util.ArrayList;
import java.util.Date;

public class Show {
    String sName;
    int startDate;
    int endDate;
    String sTime;
    String type;
    String price;
    Date ticketOpen;
    String buyTicket;
    int runningTime;

    public int getEndDate() {
        return endDate;
    }

    public String getsTime() {
        return sTime;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public Date getTicketOpen() {
        return ticketOpen;
    }

    public String getBuyTicket() {
        return buyTicket;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public ArrayList<Performer> getPerformerList() {
        return performerList;
    }

    String picSrc;
    ArrayList<Performer> performerList = new ArrayList<Performer>();

    public String getsName() {
        return sName;
    }

    public int getStartDate() {
        return startDate;
    }

    //MARK: - setter
    public void setsName(String sName) {
        this.sName = sName;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTicketOpen(Date ticketOpen) {
        this.ticketOpen = ticketOpen;
    }

    public void setBuyTicket(String buyTicket) {
        this.buyTicket = buyTicket;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public void setPerformerList(ArrayList<Performer> performerList) {
        this.performerList = performerList;
    }
}
