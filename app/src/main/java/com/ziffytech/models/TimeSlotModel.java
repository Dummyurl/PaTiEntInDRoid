package com.ziffytech.models;

import java.util.ArrayList;

/**
 * Created by subhashsanghani on 5/24/17.
 */

public class TimeSlotModel {
    private ArrayList<SlotsModel> morning;
    private ArrayList<SlotsModel> afternoon;
    private ArrayList<SlotsModel> evening;


    public ArrayList<SlotsModel> getMorning() {
        return morning;
    }

    public void setMorning(ArrayList<SlotsModel> morning) {
        this.morning = morning;
    }

    public ArrayList<SlotsModel> getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(ArrayList<SlotsModel> afternoon) {
        this.afternoon = afternoon;
    }

    public ArrayList<SlotsModel> getEvening() {
        return evening;
    }

    public void setEvening(ArrayList<SlotsModel> evening) {
        this.evening = evening;
    }
}
