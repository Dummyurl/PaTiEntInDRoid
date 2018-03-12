package com.ziffytech.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.adapters.PhotosAdapter;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.BusinessModel;
import com.ziffytech.models.PhotosModel;
import com.ziffytech.util.CommonClass;
import com.ziffytech.util.VJsonRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import com.ziffytech.R;


/**
 * Created by LENOVO on 7/10/2016.
 */
public class PhotosFragment extends Fragment {
    CommonClass common;
    ArrayList<PhotosModel> mPhotoArray;
    PhotosAdapter photoAdapter;
    RecyclerView businessRecyclerView;

    Activity act;
    Bundle args;
    BusinessModel selected_business;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        act = getActivity();
        common = new CommonClass(act);
        selected_business = ActiveModels.BUSINESS_MODEL;

        args = this.getArguments();

        mPhotoArray = new ArrayList<>();
        bindView(rootView);
        loadData();

        return  rootView;
    }
    public void bindView(View rootView){
        businessRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final GridLayoutManager layoutManager
                = new GridLayoutManager(getActivity(), 2);
        businessRecyclerView.setLayoutManager(layoutManager);

        photoAdapter = new PhotosAdapter(getActivity(),mPhotoArray);
        businessRecyclerView.setAdapter(photoAdapter);
    }
    public void loadData(){
        HashMap<String,String> params = new HashMap<>();
        params.put("bus_id",selected_business.getBus_id());

        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.BUSINESS_PHOTOS,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<PhotosModel>>() {
                        }.getType();
                        mPhotoArray.clear();
                        mPhotoArray.addAll((Collection<? extends PhotosModel>) gson.fromJson(responce, listType));
                        photoAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);

                    }
                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }


}
