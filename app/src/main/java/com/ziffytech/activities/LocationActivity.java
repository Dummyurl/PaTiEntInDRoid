package com.ziffytech.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.adapters.LocalityAdapter;
import com.ziffytech.models.DoctorSearchModel;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ziffytech.R;


public class LocationActivity extends CommonActivity {
    ArrayList<DoctorSearchModel> localityArray;
    ArrayList<DoctorSearchModel> searchArray;
    RecyclerView recyclerView;
    LocalityAdapter localityAdapter;




    EditText editSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setHeaderTitle("Search Doctor");
        allowBack();
        localityArray = new ArrayList<>();
        searchArray = new ArrayList<>();
        editSearch = (EditText) findViewById(R.id.editSearch);
        recyclerView = (RecyclerView) findViewById(R.id.rv_artist);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        localityAdapter = new LocalityAdapter(this,searchArray);
        recyclerView.setAdapter(localityAdapter);

        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                searchArray.clear();
                for (DoctorSearchModel str : localityArray) {

                    if(str!=null && str.getDoct_name()!=null && str.getBus_title()!=null){

                        String name=(str.getDoct_name().toLowerCase() +" ( "+str.getBus_title().toLowerCase()+" )");

                        if(name.contains(editSearch.getText().toString())){

                            searchArray.add(str);

                        }


                    }

                }

                localityAdapter.notifyDataSetChanged();
            }
        });

        loadData();
    }
    public void loadData(){

        showPrgressBar();

        VJsonRequest vJsonRequest = new VJsonRequest(LocationActivity.this, ApiParams.GET_LOCALITY,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            JSONArray data=jsonObject.getJSONArray("data");


                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<DoctorSearchModel>>() {
                        }.getType();
                        localityArray.clear();
                        searchArray.clear();
                        localityArray.addAll((Collection<? extends DoctorSearchModel>) gson.fromJson(data.toString(), listType));
                        searchArray.addAll((Collection<? extends DoctorSearchModel>) gson.fromJson(data.toString(), listType));
                        localityAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            hideProgressBar();
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
