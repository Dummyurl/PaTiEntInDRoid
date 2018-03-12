package com.ziffytech.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.RepliesAdapter;
import com.ziffytech.models.QuestionModel;
import com.ziffytech.util.MyUtility;
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

public class QuestionReplies extends CommonActivity {



    TextView txtTitle,txtDesc,txtDoctorMsg,txtDoctorDate,txtMyMsg,txtMyMsgDate;
    EditText editReplay;
    ImageButton btnAdd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);
        setHeaderTitle("Details");
        allowBack();


        txtTitle=(TextView)findViewById(R.id.subject);
        txtDesc=(TextView)findViewById(R.id.desc);

        txtDoctorMsg=(TextView)findViewById(R.id.doctor_msg);
        txtDoctorDate=(TextView)findViewById(R.id.doctor_msg_date);

        txtMyMsg=(TextView)findViewById(R.id.my_msg);
        txtMyMsgDate=(TextView)findViewById(R.id.my_msg_date);

        editReplay=(EditText)findViewById(R.id.msg_edit);
        btnAdd=(ImageButton)findViewById(R.id.send_btn);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editReplay.getText().toString().length()>0){

                    addReply(editReplay.getText().toString());

                }else{

                    MyUtility.showToast("Enter Your Reply",QuestionReplies.this);
                }

            }
        });

        setUpData();

        getQuestionsDoctorLast();
        getQuestionsMyLast();


        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.llreply);

        if(getIntent().getStringExtra("flag").equalsIgnoreCase("0")){

            relativeLayout.setVisibility(View.GONE);

        }else{

            relativeLayout.setVisibility(View.VISIBLE);

        }

        getDoctorReplay();

    }

    private void setUpData() {

        txtTitle.setText(getIntent().getStringExtra("subject"));
        txtDesc.setText("Description: "+getIntent().getStringExtra("desc"));

    }

    private void getDoctorReplay() {

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("q_reply_id", getIntent().getStringExtra("q_reply_id"));
        params.put("", common.get_user_id());
        params.put("id", getIntent().getStringExtra("id"));


        Log.e("Doctor",new Gson().toJson(params));



        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_DOCTOR_REPLY, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {


                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            JSONArray data=jsonObject.getJSONArray("query");

                            for(int i=0;i<data.length();i++){

                                JSONObject jsonObject1=data.getJSONObject(i);

                                txtDoctorMsg.setText(jsonObject1.getString("description"));
                                txtDoctorDate.setText(jsonObject1.getString("created"));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void VError(String responce)
                    {
                        common.setToastMessage(responce);
                    }
                });
    }


    private void getQuestionsDoctorLast() {

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("q_reply_id", getIntent().getStringExtra("q_reply_id"));
        params.put("                           ", common.get_user_id());
        params.put("id", getIntent().getStringExtra("id"));


        Log.e("Doctor",new Gson().toJson(params));



        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_DOCTOR_QUES, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {


                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            JSONArray data=jsonObject.getJSONArray("query");

                            for(int i=0;i<data.length();i++){

                                JSONObject jsonObject1=data.getJSONObject(i);

                                txtDoctorMsg.setText(jsonObject1.getString("description"));
                                txtDoctorDate.setText(jsonObject1.getString("created"));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void VError(String responce)
                    {
                        common.setToastMessage(responce);
                    }
                });
    }

    private void getQuestionsMyLast() {

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }



        HashMap<String, String> params = new HashMap<>();
        params.put("q_reply_id", getIntent().getStringExtra("q_reply_id"));
        params.put("p_to", getIntent().getStringExtra("doctor_id"));
        params.put("id",getIntent().getStringExtra("id"));


        Log.e("MY",new Gson().toJson(params));


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_MY_QUES, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {


                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            JSONArray data=jsonObject.getJSONArray("query");

                            for(int i=0;i<data.length();i++){

                                JSONObject jsonObject1=data.getJSONObject(i);
                                txtMyMsg.setText(jsonObject1.getString("description"));
                                txtMyMsgDate.setText(jsonObject1.getString("created"));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void VError(String responce)
                    {
                        common.setToastMessage(responce);
                    }
                });
    }

    public void addReply(String txt){

        showPrgressBar();
        HashMap<String,String> params = new HashMap<>();
        params.put("q_reply_id",getIntent().getStringExtra("q_reply_id"));
        params.put("p_to",getIntent().getStringExtra("doctor_id"));
        params.put("p_from",common.get_user_id());
        params.put("title",getIntent().getStringExtra("subject"));
        params.put("description",txt);


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.ADD_REPLY,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            int status=jsonObject.getInt("responce");

                            if(status==1){

                                finish();

                                MyUtility.showToast("Reply Added Successfully!!!",QuestionReplies.this);


                            }else{

                                MyUtility.showToast("Failed to Add",QuestionReplies.this);


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
