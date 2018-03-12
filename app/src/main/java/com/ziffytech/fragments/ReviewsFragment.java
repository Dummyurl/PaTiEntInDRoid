package com.ziffytech.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.adapters.ReviewsAdapter;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.BusinessModel;
import com.ziffytech.models.ReviewsModel;
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
public class ReviewsFragment extends Fragment {
    CommonClass common;
    ArrayList<ReviewsModel> mReviewsArray;
    ReviewsAdapter serviceChargeAdapter;
    RecyclerView businessRecyclerView;

    Activity act;
    Bundle args;
    BusinessModel selected_business;

    RatingBar ratingbar;
    EditText txtComment;
    Button btnAddReview;
    RelativeLayout topLayout;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        act = getActivity();

        common = new CommonClass(act);
        selected_business = ActiveModels.BUSINESS_MODEL;

        args = this.getArguments();

        mReviewsArray = new ArrayList<ReviewsModel>();
        bundView(rootView);

        loadData();


        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviewDialog();
            }
        });


        return  rootView;
    }
    public void bundView(View rootView){
        businessRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        businessRecyclerView.setLayoutManager(layoutManager);

        serviceChargeAdapter = new ReviewsAdapter(getActivity(),mReviewsArray);
        businessRecyclerView.setAdapter(serviceChargeAdapter);

        topLayout = (RelativeLayout)rootView.findViewById(R.id.topLayout);
        btnAddReview = (Button)rootView.findViewById(R.id.addreview);
    }
    public void loadData(){
        HashMap<String,String> params = new HashMap<>();
        params.put("bus_id",selected_business.getBus_id());

        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.BUSINESS_REVIEWS,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<ReviewsModel>>() {
                        }.getType();
                        mReviewsArray.clear();
                        mReviewsArray.addAll((Collection<? extends ReviewsModel>) gson.fromJson(responce, listType));
                        serviceChargeAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);

                    }
                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }
    public void openReviewDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_review, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        ratingbar = (RatingBar)view.findViewById(R.id.ratingBar1);
        txtComment = (EditText)view.findViewById(R.id.editText1);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(getString(R.string.add_review), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if (!TextUtils.isEmpty(txtComment.getText()))
                            sendData();
                        else
                            txtComment.setError(getString(R.string.required));
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void sendData(){
        HashMap<String,String> params = new HashMap<>();

        params.put("bus_id", selected_business.getBus_id());
        params.put("user_id", common.getSession(ApiParams.COMMON_KEY));
        params.put("reviews", txtComment.getText().toString());
        params.put("rating", String.valueOf(ratingbar.getRating()));

        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.ADD_BUSINESS_REVIEWS,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<ReviewsModel>() {
                        }.getType();
                        mReviewsArray.add((ReviewsModel) gson.fromJson(responce, listType));
                        serviceChargeAdapter.notifyDataSetChanged();
                        //progressBar1.setVisibility(View.GONE);
                        if (alertDialog!=null) {
                            alertDialog.dismiss();
                            alertDialog = null;
                        }
                    }
                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
