package com.ziffytech.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.adapters.MemberAdapter;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.MemberModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.RecyclerItemClickListener;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.ziffytech.R;


/**
 * Created by admn on 16/11/2017.
 */

public class RelativeProfile extends CommonActivity {

    RecyclerView recyclerView;
    ArrayList<MemberModel> arrayList;
    MemberAdapter adapter;
    TextView notfoundtv;

    String type="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relativepatient);
        setHeaderTitle("Family Members");
        allowBack();
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RelativeProfile.this,AddMember.class);
                startActivity(intent);
            }
        });
        arrayList = new ArrayList<MemberModel>();



        if(getIntent().hasExtra("result")){
           // button.setVisibility(View.VISIBLE);
            type=getIntent().getStringExtra("result");
        }



        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MemberAdapter(arrayList,getApplicationContext(),type);

        notfoundtv=(TextView)findViewById(R.id.notfoundtv);
        notfoundtv.setVisibility(View.GONE);

        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                if(getIntent().hasExtra("result")){


                    Bundle b = new Bundle();
                    b.putString("name",arrayList.get(position).getM_name());
                    b.putString("age",arrayList.get(position).getAge());
                    b.putString("gender",arrayList.get(position).getM_gender());
                    b.putString("user_id",arrayList.get(position).getId());
                    Intent intent = new Intent();
                    intent.putExtras(b);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                }else{

                    new AlertDialog.Builder(RelativeProfile.this)
                            .setMessage("Are you sure you want to delete record?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    deleteMember(arrayList.get(position).getId());

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                }
                            })
                            .create().show();

                }


            }
        }));






    }

    @Override
    protected void onStart() {
        super.onStart();
        getMember();

    }

    private void getMember() {

        if(!arrayList.isEmpty()){

            arrayList.clear();
        }

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }

        showPrgressBar();


        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.get_member, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            if(jsonObject.getBoolean("success")){

                                notfoundtv.setVisibility(View.GONE);

                                JSONArray data=jsonObject.getJSONArray("data");



                                Gson gson = new Gson();
                        Type listType = new TypeToken<List<MemberModel>>() {
                        }.getType();
                        ArrayList<MemberModel> arraylist = (ArrayList<MemberModel>) gson.fromJson(data.toString(), listType);

                        arrayList.addAll(arraylist);
                        ActiveModels.List_Member = arrayList;
                        if (arrayList!=null){
                            adapter.notifyDataSetChanged();

                        }

                        }else{

                                notfoundtv.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            hideProgressBar();
                            notfoundtv.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce)
                    {
                        hideProgressBar();
                        common.setToastMessage(responce);
                    }
                });
    }


    private void deleteMember(String id) {

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }

        showPrgressBar();


        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.delete_member, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            if(jsonObject.getInt("success")==1){
                                getMember();
                                adapter.notifyDataSetChanged();
                                MyUtility.showToast("Record Deleted.",RelativeProfile.this);
                            }else{
                                MyUtility.showToast("Record failed to Delete.",RelativeProfile.this);
                            }




                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce)
                    {
                        hideProgressBar();
                    }
                });
    }

}
