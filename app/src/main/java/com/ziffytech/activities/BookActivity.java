package com.ziffytech.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.DoctorAdapter;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.BusinessModel;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class BookActivity extends CommonActivity {


    BusinessModel selected_business;

    public static ArrayList<DoctorModel> mDoctorArray;
    DoctorAdapter doctorAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctors);
        allowBack();
        setHeaderTitle("Doctors List");

        selected_business = ActiveModels.BUSINESS_MODEL;
        mDoctorArray = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        doctorAdapter = new DoctorAdapter(this, mDoctorArray);
        recyclerView.setAdapter(doctorAdapter);

        if(getIntent().hasExtra("name")){

            loadDataName();

        }if(getIntent().hasExtra("search")){

            Log.e("intent Data", getIntent().toUri(0));
            loadDataSearch();

        }else if(getIntent().hasExtra("cat_id")){

            loadData();
        }



    }

    private void loadDataName() {

        showPrgressBar();


        HashMap<String,String> params = new HashMap<>();
        params.put("doct_id",getIntent().getStringExtra("id"));
        params.put("city",common.getSession(ApiParams.CURRENT_CITY));

        // Log.e("IDDD",selected_business.getBus_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_DOCTORS_BY_NAME,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();



                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            JSONArray data=jsonObject.getJSONArray("data");

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<DoctorModel>>() {
                            }.getType();
                            mDoctorArray.clear();
                            mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(data.toString(), listType));
                            doctorAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                    }
                });

    }


    public void loadData(){


        showPrgressBar();


        HashMap<String,String> params = new HashMap<>();
        params.put("cat_id",getIntent().getStringExtra("cat_id"));
        params.put("city",common.getSession(ApiParams.CURRENT_CITY));


        // Log.e("IDDD",selected_business.getBus_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_DOCTORS,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();



                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            JSONArray data=jsonObject.getJSONArray("data");

                            if(data.length()>0){

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<DoctorModel>>() {
                                }.getType();
                                mDoctorArray.clear();
                                mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(data.toString(), listType));
                                doctorAdapter.notifyDataSetChanged();

                            }else{
                                finish();
                                MyUtility.showToast("Data Not Found",BookActivity.this);
                            }



                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                            finish();
                            MyUtility.showToast("Data Not Found",BookActivity.this);

                        }

                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                    }
                });


    }


    private void loadDataSearch() {

        showPrgressBar();


        HashMap<String,String> params = new HashMap<>();
        params.put("distance",getIntent().getStringExtra("distance"));
        params.put("fee",getIntent().getStringExtra("fee"));
        params.put("rating",getIntent().getStringExtra("rating"));
        params.put("availability",getIntent().getStringExtra("availability"));
        params.put("gender",getIntent().getStringExtra("gender"));
        params.put("city",common.getSession(ApiParams.CURRENT_CITY));



        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_DOCTORS_BY_FILTER,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            JSONArray data=jsonObject.getJSONArray("data");

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<DoctorModel>>() {
                            }.getType();
                            mDoctorArray.clear();
                            mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(data.toString(), listType));
                            doctorAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                    }
                });

    }


}