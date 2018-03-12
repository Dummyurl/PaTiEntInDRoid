package com.ziffytech.models;

/**
 * Created by subhashsanghani on 5/24/17.
 */

public class SlotsModel {
    private String slot;
    private Boolean is_booked;
    private int time_token;

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public Boolean getIs_booked() {
        return is_booked;
    }

    public void setIs_booked(Boolean is_booked) {
        this.is_booked = is_booked;
    }

    public int getTime_token() {
        return time_token;
    }

    public void setTime_token(int time_token) {
        this.time_token = time_token;
    }
}
