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
    String picSrc;
    ArrayList<Performer> performerList = new ArrayList<Performer>();
}
