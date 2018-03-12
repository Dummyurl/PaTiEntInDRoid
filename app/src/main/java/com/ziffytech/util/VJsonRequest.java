package com.ziffytech.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ziffytech.R;
import com.ziffytech.Config.ApiParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by Lenovo on 10-05-2017.
 */

public class VJsonRequest {
    VJsonResponce vresponce;
    Activity instance;
    String url;
    Map<String, String> params;
    JSONObject parmsJson;
    //int method;

    public VJsonRequest(Activity activity, String url, VJsonResponce  vresponce){
            this.instance = activity;
            this.vresponce = vresponce;
            this.url = url;
            //method = Request.Method.GET;
            this.params = new HashMap<>();
            requestStringData();
    }

    public VJsonRequest(Activity activity, String url, Map<String, String> parms, VJsonResponce  vresponce){
        this.instance = activity;
        this.vresponce = vresponce;
        this.url = url;
        //this.method = Request.Method.POST;
        this.params = parms;
        parmsJson =     new JSONObject(params);

        requestStringData();
    }
    public interface VJsonResponce
    {
        public void VResponce(String responce);
        public void VError(String responce);
    }
    private void requestStringData(){
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String responseString)
                    {
                        try {
                            JSONObject response = new JSONObject(responseString);

                            Log.e("API_RESPONSE",response.toString());

                           // if (response.getString(ApiParams.PARM_RESPONCE)){

                                vresponce.VResponce(response.toString());
                          //  }else{
                             //   vresponce.VError(response.getString(ApiParams.PARM_ERROR));
                         //   }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        vresponce.VError(error.getMessage());
                        // hide the progress dialog
                        //  Log.e("inside","Error");
                        checkInternetConncetion(error,instance);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(instance);
        requestQueue.add(strRequest);
    }
    private void requestData() {
        JsonObjectRequest request = new JsonObjectRequest(
                url,parmsJson,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean(ApiParams.PARM_RESPONCE)){
                                vresponce.VResponce(response.getString(ApiParams.PARM_DATA));
                            }else{
                                vresponce.VError(response.getString(ApiParams.PARM_ERROR));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                vresponce.VError(error.getMessage());
                // hide the progress dialog
                //  Log.e("inside","Error");
                checkInternetConncetion(error,instance);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //request.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(instance);
        requestQueue.add(request);
    }
    public void  checkInternetConncetion(VolleyError volleyError, Activity activity){
        if (volleyError instanceof NetworkError || volleyError instanceof AuthFailureError || volleyError instanceof ParseError || volleyError instanceof NoConnectionError){
            //NoInternetDialog noInternetDialog = new NoInternetDialog(activity);
            //noInternetDialog.show();
            Toast.makeText(activity,activity.getString(R.string.error_no_internet),Toast.LENGTH_SHORT).show();
        }else if(volleyError instanceof ServerError){
            Toast.makeText(activity,activity.getString(R.string.error_can_not_connect_to_server),Toast.LENGTH_SHORT).show();
        }else if (volleyError instanceof TimeoutError){
            Toast.makeText(activity,activity.getString(R.string.error_can_not_connect_to_server),Toast.LENGTH_SHORT).show();
        }

    }



}
