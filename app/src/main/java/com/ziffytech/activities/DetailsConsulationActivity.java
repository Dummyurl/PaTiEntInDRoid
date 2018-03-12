package com.ziffytech.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.ConsultAdapter;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.BusinessModel;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.VJsonRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DetailsConsulationActivity extends CommonActivity {


    BusinessModel selected_business;

    public static ArrayList<DoctorModel> mDoctorArray;
    ConsultAdapter doctorAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctors);

        allowBack();

        selected_business = ActiveModels.BUSINESS_MODEL;
        mDoctorArray = new ArrayList<DoctorModel>();

        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        doctorAdapter = new ConsultAdapter(this, mDoctorArray);
        recyclerView.setAdapter(doctorAdapter);

        loadData();

    }

    public void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("bus_id", selected_business.getBus_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_DOCTORS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<DoctorModel>>() {
                        }.getType();
                        mDoctorArray.clear();
                        mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(responce, listType));
                        doctorAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }

}

