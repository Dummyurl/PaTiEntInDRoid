package com.ziffytech.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.QuestionAdapter;
import com.ziffytech.models.QuestionModel;
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

/**
 * Created by Mahesh on 09/01/18.
 */

public class AskQuestionActiivty  extends CommonActivity {

    RecyclerView recyclerView;
    ArrayList<QuestionModel> arrayList;
    QuestionAdapter adapter;
    String type="";
    TextView txtNoData;

    //0 new msg 1 old msg

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        setHeaderTitle("ASK QUESTIONS");
        allowBack();
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AskQuestionActiivty.this,AddQuestionActivity.class);
                startActivity(intent);
            }
        });

        txtNoData=(TextView)findViewById(R.id.txtNoData);

        arrayList = new ArrayList<>();
        if(getIntent().hasExtra("result")){
            // button.setVisibility(View.VISIBLE);
            type=getIntent().getStringExtra("result");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new QuestionAdapter(arrayList,getApplicationContext(),type);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {


                Intent i=new Intent(AskQuestionActiivty.this,AskQuestionDetails.class);
                i.putExtra("subject",arrayList.get(position).getSubject());
                i.putExtra("desc",arrayList.get(position).getDescription());
                i.putExtra("id",arrayList.get(position).getId());
                i.putExtra("q_reply_id",arrayList.get(position).getQ_reply_id());
                i.putExtra("doctor_id",arrayList.get(position).getDoctor_id());
                i.putExtra("doctor_name",arrayList.get(position).getDoctorName());
                i.putExtra("flag",arrayList.get(position).getFlag());
                startActivity(i);


            }
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!arrayList.isEmpty()){
            arrayList.clear();
        }
        getQuestions();
    }

    private void getQuestions() {

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }

        showPrgressBar();


        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_QUES, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            JSONArray data=jsonObject.getJSONArray("data");

                            if(data.length()>0){

                                txtNoData.setVisibility(View.GONE);


                            for(int i=0;i<data.length();i++){


                                JSONObject jsonObject1=data.getJSONObject(i);

                                QuestionModel model=new QuestionModel();
                                model.setId(jsonObject1.getString("id"));
                                model.setSubject(jsonObject1.getString("title"));
                                model.setDescription(jsonObject1.getString("description"));
                                model.setQ_reply_id(jsonObject1.getString("q_reply_id"));
                                model.setCreated(jsonObject1.getString("created"));
                                model.setDoctor_id(jsonObject1.getString("p_to"));
                                model.setFlag(jsonObject1.getString("read"));
                                model.setDoctorName(jsonObject1.getString("doct_name"));

                                arrayList.add(model);
                            }


                            if (arrayList!=null){
                                adapter.notifyDataSetChanged();
                            }


                            }else{

                                txtNoData.setVisibility(View.VISIBLE);
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
                        common.setToastMessage(responce);
                    }
                });
    }
}
