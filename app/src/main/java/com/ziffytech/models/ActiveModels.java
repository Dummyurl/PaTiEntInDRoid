package com.ziffytech.models;

import java.util.ArrayList;

/**
 * Created by subhashsanghani on 5/22/17.
 */

public class ActiveModels {
    public static BusinessModel BUSINESS_MODEL;
    public static CategoryModel CATEGORY_MODEL;
    public static PhotosModel PHOTOS_MODEL;
    public static ReviewsModel REVIEWS_MODEL;
    public static ServicesModel SERVICES_MODEL;
    public static SlotsModel SELECTED_SLOT;
    public static DoctorModel DOCTOR_MODEL;
    public static ArrayList<ServicesModel> LIST_SERVICES_MODEL;
    public static ArrayList<BusinessModel> LIST_BUSINESS_MODEL;
    public  static ArrayList<MemberModel>List_Member;
    public static void reset(){
        BUSINESS_MODEL = null;
        CATEGORY_MODEL = null;
        PHOTOS_MODEL = null;
        REVIEWS_MODEL = null;
        SERVICES_MODEL = null;
        SELECTED_SLOT = null;
        LIST_SERVICES_MODEL = null;
        LIST_BUSINESS_MODEL = null;
        List_Member= null;
    }
}
