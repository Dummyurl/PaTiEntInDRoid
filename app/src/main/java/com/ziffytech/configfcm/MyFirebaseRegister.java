package com.ziffytech.configfcm;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.HashMap;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.util.CommonClass;
import com.ziffytech.util.VJsonRequest;



public class MyFirebaseRegister {
    Activity _context;
    CommonClass common;
    public MyFirebaseRegister(Activity context) {
        this._context = context;
        common = new CommonClass(_context);

    }
    public void RegisterUser(String user_id){
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("doctappo");

        Log.e("FCM:",token);

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        params.put("token",token);
        params.put("device","android");

        VJsonRequest vJsonRequest = new VJsonRequest(_context, ApiParams.REGISTER_FCM_URL,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {


                    }
                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });
    }


}
