package com.ziffytech.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.adapters.DoctorAdapter;
import com.ziffytech.dialogues.DoctorCounsultInfoDialog;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.BusinessModel;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.CommonClass;
import com.ziffytech.util.RecyclerItemClickListener;
import com.ziffytech.util.VJsonRequest;
import com.ziffytech.R;


/**
 * Created by LENOVO on 7/10/2016.
 */
public class ConsulationDoctorsFragment extends Fragment   {
    CommonClass common;
    Activity act;
    Bundle args;
    BusinessModel selected_business;

    public static ArrayList<DoctorModel> mDoctorArray;
    DoctorAdapter doctorAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctors, container, false);
        act = getActivity();
        common = new CommonClass(act);
            selected_business = ActiveModels.BUSINESS_MODEL;
            mDoctorArray = new ArrayList<DoctorModel>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        doctorAdapter = new DoctorAdapter(getActivity(),mDoctorArray);
        recyclerView.setAdapter(doctorAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {

                DoctorCounsultInfoDialog doctorInfoDialog = new DoctorCounsultInfoDialog(getActivity(),mDoctorArray.get(position));
                doctorInfoDialog.show();
            }
        }));
        loadData();


        args = this.getArguments();


        return  rootView;
    }
    public void loadData(){
        HashMap<String,String> params = new HashMap<>();
        params.put("bus_id",selected_business.getBus_id());

        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.GET_DOCTORS,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<DoctorModel>>() {
                        }.getType();
                        mDoctorArray.clear();
                        mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(responce, listType));
                        doctorAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);

                    }
                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }
}
