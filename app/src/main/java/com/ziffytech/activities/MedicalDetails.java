package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;
import com.ziffytech.R;


/**
 * Created by admn on 13/11/2017.
 */

public class MedicalDetails extends CommonActivity {
    EditText alegry,p_medication,c_medication,chronic,injuries,surgery;
    Button button,skip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_details);
        allowBack();
        setHeaderTitle("Medical Details");
      //  Intent intent = getIntent();

        skip=(Button)findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MedicalDetails.this,LifeStyleActivity.class);
                intent1.putExtra("new","new");
                startActivity(intent1);
            }
        });

        alegry=(EditText)findViewById(R.id.Allergies);
        p_medication=(EditText)findViewById(R.id.past_medication);
        c_medication=(EditText)findViewById(R.id.current_medication);
        chronic=(EditText)findViewById(R.id.chrinoc);
        injuries=(EditText)findViewById(R.id.Injuries);
        surgery=(EditText)findViewById(R.id.Surgeries);
        button=(Button)findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicaldetails();
            }
        });


        if(getIntent().hasExtra("new")){



        }else{

            try {

                skip.setVisibility(View.GONE);
                button.setText("UPDATE");

                JSONObject data=new JSONObject(common.getSession(ApiParams.USER_JSON_DATA));
                Log.e("Data",data.toString());
                Log.e("allergies",data.getString("allergies"));


                if(data.getString("allergies").equalsIgnoreCase("null")){
                    alegry.setText("");
                }else{
                    alegry.setText(data.getString("allergies"));
                }
                if(data.getString("p_medicine").equalsIgnoreCase("null")){
                    p_medication.setText("");
                }else{
                    p_medication.setText(data.getString("p_medicine"));
                }
                if(data.getString("c_medicine").equalsIgnoreCase("null")){
                    c_medication.setText("");
                }else{
                    c_medication.setText(data.getString("c_medicine"));
                }
                if(data.getString("diseases").equalsIgnoreCase("null")){
                    chronic.setText("");
                }else{
                    chronic.setText(data.getString("diseases"));
                }
                if(data.getString("injuries").equalsIgnoreCase("null")){
                    injuries.setText("");
                }else{
                    injuries.setText(data.getString("injuries"));
                }
                if(data.getString("surgeries").equalsIgnoreCase("null")){
                    surgery.setText("");
                }else{
                    surgery.setText(data.getString("surgeries"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void medicaldetails() {
        String malegry=alegry.getText().toString();
        String m_medication=p_medication.getText().toString();
        String mc_medication=c_medication.getText().toString();
        String mchronic=chronic.getText().toString();
        String minjuries=injuries.getText().toString();
        String msurgery=surgery.getText().toString();


        // Check for a valid email address.


       /* if (malegry.length()<=0) {
            alegry.setError("Allergies Field Required");
            return;
        }

        if (mc_medication.length()<=0) {
            c_medication.setError("Current Medicine Field Required");
            return;
        }
        if (m_medication.length()<=0) {
            p_medication.setError("Past Medicine Field Required");
            return;
        }
        if (mchronic.length()<=0) {
            chronic.setError("Chronic Diseases Field Required");
            return;
        }

        if (minjuries.length()<=0) {
            injuries.setError("Injuries Field Required");
            return;
        } if (msurgery.length()<=0)  {
            surgery.setError("Surgeries Field Required");
            return;
        }

        */


        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }


        showPrgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.getSession(ApiParams.COMMON_KEY));
        params.put("allergies", malegry);
        params.put("c_medicine", mc_medication);
        params.put("p_medicine", m_medication);
        params.put("diseases", mchronic);
        params.put("injuries", minjuries);
        params.put("surgeries", msurgery);


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.Medical_DETAIL_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if(userdata.getInt("responce")==1){

                                JSONObject data=userdata.getJSONObject("data");
                                common.setSession(ApiParams.USER_JSON_DATA, data.toString());

                                if(getIntent().hasExtra("new")){

                                    Intent intent=new Intent(MedicalDetails.this,LifeStyleActivity.class);
                                    intent.putExtra("new","new");
                                    startActivity(intent);

                                }else{

                                    finish();
                                    MyUtility.showToast("Medical Detail Updated!",MedicalDetails.this);
                                }

                            }else{
                                MyUtility.showAlertMessage(MedicalDetails.this,"Failed to Update");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            MyUtility.showAlertMessage(MedicalDetails.this,"Failed to Update");
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {

                        hideProgressBar();
                        MyUtility.showAlertMessage(MedicalDetails.this,"Failed to Update");
                    }
                });
    }
}
