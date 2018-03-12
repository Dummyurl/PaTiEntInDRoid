package com.ziffytech.models;

import java.util.Locale;

/**
 * Created by subhashsanghani on 5/22/17.
 */

public class ServicesModel {
    public boolean isclicked=false;
    private String id;
    private String bus_id;
    private String service_title;
    private String service_price;
    private String service_discount;
    private String business_approxtime;
    public ServicesModel(boolean isclicked,String id,String strAmount,String serviceName, String discount, String time, String bus_id)
    {

        this.isclicked=isclicked;
        this.id = id;

        this.service_price=strAmount;
        this.service_title = serviceName;
        this.service_discount = discount;
        this.business_approxtime = time;
        this.bus_id = bus_id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getService_title() {
        return service_title;
    }

    public void setService_title(String service_title) {
        this.service_title = service_title;
    }

    public String getService_price() {
        return service_price;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public String getService_discount() {
        return service_discount;
    }

    public void setService_discount(String service_discount) {
        this.service_discount = service_discount;
    }

    public String getBusiness_approxtime() {
        return business_approxtime;
    }

    public void setBusiness_approxtime(String business_approxtime) {
        this.business_approxtime = business_approxtime;
    }

    public boolean isChecked() {
        return isclicked;
    }
    public void setChecked(boolean selected) {
        this.isclicked = selected;
    }

    public String getDiscountAmount(){
        Double amount = Double.parseDouble(service_price);
        Double discount = Double.parseDouble(service_discount) * amount / 100;
        amount = amount - discount;
        return String.format(Locale.ENGLISH, "%.2f", amount);
    }
}
