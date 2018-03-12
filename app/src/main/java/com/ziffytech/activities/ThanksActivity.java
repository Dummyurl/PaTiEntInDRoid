package com.ziffytech.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.BusinessModel;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.RoundedImageView;
import com.ziffytech.util.VJsonRequest;
import com.ziffytech.R;


public class ThanksActivity extends CommonActivity {
    TextView txtPatientName, txtAge, txtGender;
    TextView txtBusinessName, txtDoctorName, txtDate, txtTimeSlot;
    DoctorModel selected_business;

    private String userId="0";

    RoundedImageView doctorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        allowBack();
        setHeaderTitle("Confirm Appointment");


        selected_business = ActiveModels.DOCTOR_MODEL;

        txtDoctorName = (TextView) findViewById(R.id.doctorName);
        txtDate = (TextView) findViewById(R.id.bookedDate);
        txtTimeSlot = (TextView) findViewById(R.id.bookedTime);
        txtBusinessName = (TextView) findViewById(R.id.clinicName);

        txtDoctorName.setText("Doctor : "+selected_business.getDoct_name());
        txtBusinessName.setText(selected_business.getBus_title());
        txtDate.setText("Booking Date"+" : "+getIntent().getExtras().getString("date"));
        txtTimeSlot.setText("Booking Time"+" : "+getIntent().getExtras().getString("timeslot"));


        doctorImage=(RoundedImageView)findViewById(R.id.doctorImage);
        Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/profile/" + selected_business.getDoct_photo()).into(doctorImage);

        txtPatientName = (TextView) findViewById(R.id.patientName);
        txtAge = (TextView) findViewById(R.id.age);
        txtGender = (TextView) findViewById(R.id.gender);

        try{

            JSONObject data=new JSONObject(common.getSession(ApiParams.USER_JSON_DATA));

            txtPatientName.setText(common.getSession(ApiParams.USER_FULLNAME));
            txtAge.setText(common.getSession(ApiParams.USER_EMAIL));
            txtGender.setText(common.getSession(ApiParams.USER_PHONE));

        }catch (Exception e){

            e.printStackTrace();
        }


        Button confirm=(Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirm();
            }
        });


        Button member=(Button)findViewById(R.id.addMember);
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ThanksActivity.this,RelativeProfile.class);
                intent.putExtra("result","result");
                startActivityForResult(intent,1);            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            txtPatientName.setText(data.getExtras().getString("name"));
            txtAge.setText("Age:"+data.getExtras().getString("age"));

            if(data.getExtras().getString("gender").equalsIgnoreCase("0")){

                txtGender.setText("Gender:"+" Male");

            }else if(data.getExtras().getString("gender").equalsIgnoreCase("1")){

                txtGender.setText("Gender:"+" Female");

            }


            userId=data.getExtras().getString("user_id");


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void confirm() {


        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }


        Log.e("Token",getIntent().getExtras().getString("time_token"));

        showPrgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put("doct_id", selected_business.getDoct_id());
        params.put("bus_id", selected_business.getBus_id());
        params.put("user_id", common.get_user_id());
        params.put("sub_user_id", userId);
        params.put("appointment_date", getIntent().getExtras().getString("date"));
        params.put("start_time", getIntent().getExtras().getString("timeslot"));
        params.put("time_token", getIntent().getExtras().getString("time_token"));


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.BOOKING_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if(userdata.getInt("responce")==1){

                                AlertDialog.Builder ad=new AlertDialog.Builder(ThanksActivity.this);
                                ad.setMessage("Thank you for Booking an Appointment");
                                ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        finish();
                                        Intent intent = new Intent(ThanksActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                });
                                AlertDialog dialog=ad.create();
                                dialog.setCancelable(false);
                                dialog.show();

                            }else{
                                MyUtility.showAlertMessage(ThanksActivity.this,"Failed to Book Appointment");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce) {

                        hideProgressBar();
                        common.setToastMessage(responce);
                    }
                });
    }

}
