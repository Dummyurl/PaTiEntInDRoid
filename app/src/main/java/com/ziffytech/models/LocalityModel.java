package com.ziffytech.models;

/**
 * Created by subhashsanghani on 5/28/17.
 */

public class LocalityModel {
   private String locality_id;
    private String city_id;
    private String country_id;
    private String locality;
    private String locality_lat;
    private String locality_lon;
    private String status;
    private String country_name;
    private String city_name;
    private String doct_name;
    private String Clinic_name;

    public String getDoctor_name() {
        return doct_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doct_name = doctor_name;
    }

    public String getClinic_name() {
        return Clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        Clinic_name = clinic_name;
    }

    public String getLocality_id() {
        return locality_id;
    }

    public void setLocality_id(String locality_id) {
        this.locality_id = locality_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLocality_lat() {
        return locality_lat;
    }

    public void setLocality_lat(String locality_lat) {
        this.locality_lat = locality_lat;
    }

    public String getLocality_lon() {
        return locality_lon;
    }

    public void setLocality_lon(String locality_lon) {
        this.locality_lon = locality_lon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
