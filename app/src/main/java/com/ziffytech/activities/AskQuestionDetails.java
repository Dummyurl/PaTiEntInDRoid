package com.ziffytech.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.AskQuestionRepliesAdapter;
import com.ziffytech.models.AskQuesMessage;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Mahesh on 21/05/17.
 */

public class AskQuestionDetails extends CommonActivity {

    private TextView title,desc,doctor;
    String messageText;

    private EditText messageET;
    private ListView messagesContainer;
    private ImageView sendBtn;
    private AskQuestionRepliesAdapter adapter;
    private ArrayList<AskQuesMessage> chatHistory;
    private ProgressBar load;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_ques_replies);
        allowBack();
        setHeaderTitle("Details");
        setUpViews();
    }

    private void setUpViews() {

        this.title = (TextView)findViewById(R.id.title);
        this.desc = (TextView)findViewById(R.id.desc);
        this.doctor = (TextView)findViewById(R.id.doctorName);

        title.setText(getIntent().getStringExtra("subject"));
        desc.setText("Description: "+getIntent().getStringExtra("desc"));
        doctor.setText("Doctor: "+getIntent().getStringExtra("doctor_name"));


        load=(ProgressBar)findViewById(R.id.chatload);


        messagesContainer = (ListView) findViewById(R.id.messagesContainer);


        messageET = (EditText) findViewById(R.id.msg_edit);
        sendBtn = (ImageView) findViewById(R.id.send_btn);


        chatHistory = new ArrayList<AskQuesMessage>();
        if(!chatHistory.isEmpty()){
            chatHistory.clear();
        }

        adapter = new AskQuestionRepliesAdapter(AskQuestionDetails.this, chatHistory);
        messagesContainer.setAdapter(adapter);
        loadData();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 messageText = messageET.getText().toString();

                if (TextUtils.isEmpty(messageText)) {
                    return;

                }

                if(MyUtility.isConnected(AskQuestionDetails.this)){

                        messageET.setText("");

                        //new SetSupport().execute(messageText);
                        saveSupportMessage(messageText);

                }else{

                    Toast.makeText(AskQuestionDetails.this,"Check your internet connection",Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                finish();
                onBackPressed();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayMessage(AskQuesMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadData(){



        if(MyUtility.isConnected(this)){

            getDiscussion();

        }else{

            Toast.makeText(this,"Check your internet connection...",Toast.LENGTH_LONG);
        }


    }




   private  void getDiscussion(){


       if(!MyUtility.isConnected(this)) {
           MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
           return;
       }

       HashMap<String, String> params = new HashMap<>();
       params.put("question_id", getIntent().getStringExtra("id"));

       Log.e("replies",new Gson().toJson(params));



       VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_REPLIES, params,
               new VJsonRequest.VJsonResponce() {
                   @Override
                   public void VResponce(String responce) {

                       load.setVisibility(View.GONE);



                       try {
                           JSONObject jsonObject=new JSONObject(responce);

                           JSONArray  vjsonArray1 = jsonObject.getJSONArray("data");

                           if(vjsonArray1.length()>0) {

                               for (int i = 0; i < vjsonArray1.length(); i++) {

                                   JSONObject c = vjsonArray1.getJSONObject(i);

                                   AskQuesMessage msg = new AskQuesMessage();
                                   msg.setId(1);
                                   msg.setFirst(false);
                                   msg.setMe(true);
                                  if (c.getString("reply_from").equalsIgnoreCase(common.get_user_id())) {
                                       msg.setMe(true);
                                   } else  {
                                       msg.setMe(false);
                                   }
                                   msg.setSupportId("");
                                   msg.setImage("");
                                   msg.setMessage(c.getString("description"));
                                   msg.setDate(c.getString("created"));
                                   chatHistory.add(msg);


                               }

                               adapter.notifyDataSetChanged();
                               scroll();

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

    private void saveSupportMessage(final String messageText){


        showPrgressBar();
        HashMap<String,String> params = new HashMap<>();
        params.put("doct_id",getIntent().getStringExtra("doctor_id"));
        params.put("question_id",getIntent().getStringExtra("id"));
        params.put("user_id",common.get_user_id());
        params.put("description",messageText);


        Log.e("Add replies",new Gson().toJson(params));



        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.ADD_REPLY,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            int status=jsonObject.getInt("responce");

                            if(status==1){


                                AskQuesMessage message = new AskQuesMessage();
                                message.setId(1);
                                SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
                                String date = df.format(Calendar.getInstance().getTime());
                                message.setDate(date);
                                message.setMe(true);
                                message.setSupportId("");
                                message.setImage("");
                                message.setFirst(false);
                                message.setMessage(messageText);
                                displayMessage(message);

                            }else{

                                MyUtility.showToast("Failed to Add",AskQuestionDetails.this);


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

