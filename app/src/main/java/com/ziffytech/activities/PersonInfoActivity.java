package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;
import com.ziffytech.R;

import java.util.HashMap;

public class PersonInfoActivity extends CommonActivity {
    EditText editEmail,  editPhone, editFullname;

    String choose_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        allowBack();
        setHeaderTitle(getString(R.string.contact_info));

        choose_date = getIntent().getExtras().getString("date");
        editEmail = (EditText)findViewById(R.id.txtEmail);
        editFullname = (EditText)findViewById(R.id.txtFirstname);
        editPhone = (EditText)findViewById(R.id.txtPhone);

        editEmail.setText(common.getSession(ApiParams.USER_EMAIL));
        editFullname.setText(common.getSession(ApiParams.USER_FULLNAME));
        editPhone.setText(common.getSession(ApiParams.USER_PHONE));

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",common.get_user_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.USERDATA_URL,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {
                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);
                            editEmail.setText(userdata.getString("user_email"));
                            editFullname.setText(userdata.getString("user_fullname"));
                            editPhone.setText(userdata.getString("user_phone"));
                            editEmail.setEnabled(false);
                            editFullname.setEnabled(false);
                            editPhone.setEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });


        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    public void register(){

        String email = editEmail.getText().toString();
        String fullname = editFullname.getText().toString();
        String phone = editPhone.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            common.setToastMessage(getString(R.string.valid_required_email));
            focusView = editEmail;
            cancel = true;
        }
        if (!isValidEmail(email)) {
            common.setToastMessage(getString(R.string.valid_email));
            focusView = editEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(fullname)) {
            common.setToastMessage(getString(R.string.valid_required_fullname));
            focusView = editFullname;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            common.setToastMessage(getString(R.string.valid_required_phone));
            focusView = editPhone;
            cancel = true;
        }
        if (cancel) {
            if (focusView!=null)
                focusView.requestFocus();
        } else {
            HashMap<String,String> map = new HashMap<>();
            map.put("doct_id",ActiveModels.DOCTOR_MODEL.getDoct_id());
            map.put("bus_id",ActiveModels.BUSINESS_MODEL.getBus_id());
            map.put("user_fullname",fullname);
            map.put("user_phone",phone);
            map.put("user_email",email);
            map.put("start_time", ActiveModels.SELECTED_SLOT.getSlot());
            map.put("time_token", String.valueOf(ActiveModels.SELECTED_SLOT.getTime_token()));
            map.put("appointment_date",choose_date);
            map.put("user_id",common.get_user_id());

            Intent intent = null;

            intent = new Intent(PersonInfoActivity.this, ThanksActivity.class);
            intent.putExtra("date",choose_date);
            intent.putExtra("timeslot",ActiveModels.SELECTED_SLOT.getSlot());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

           /* String str_services = "";
            for (int i = 0; i < ActiveModels.LIST_SERVICES_MODEL.size(); i++) {
                if (ActiveModels.LIST_SERVICES_MODEL.get(i).isChecked()) {
                    if (str_services.equalsIgnoreCase("")) {
                        str_services = ActiveModels.LIST_SERVICES_MODEL.get(i).getId();
                    } else {
                        str_services = str_services + "," + ActiveModels.LIST_SERVICES_MODEL.get(i).getId();
                    }
                }
            }
            map.put("services",str_services);
            //CommonAsyTask commonTask = new CommonAsyTask(nameValuePairs, ApiParams.BOOKAPPOINTMENT_URL, handler, true, this);
            //commonTask.execute();

            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.BOOKAPPOINTMENT_URL,map,
                    new VJsonRequest.VJsonResponce(){
                        @Override
                        public void VResponce(String responce) {
                            Intent intent = new Intent(PersonInfoActivity.this, PaymentActivity.class);
                            intent.putExtra("order_details",responce);
                            startActivity(intent);
                            /*JSONObject appointmentData = null;
                            try {
                                appointmentData = new JSONObject(responce);


                                String messageType = getString(R.string.appoitnment_confirm_message_part1)+appointmentData.getString("id")+ " "+getString(R.string.appoitnment_confirm_message_part1);
                                common.setToastMessage(getString(R.string.appoitnment_confirm_message_part1)+appointmentData.getString("id")+ " "+getString(R.string.appoitnment_confirm_message_part1));
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss", Locale.UK);
                                try {
                                    Date testDate = formatter.parse(choose_date);
                                    Date testTime = formatter2.parse(ActiveModels.SELECTED_SLOT.getSlot());
                                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);


                                    Calendar myAlarmDate = Calendar.getInstance();
                                    myAlarmDate.setTimeInMillis(System.currentTimeMillis());
                                    myAlarmDate.set(testDate.getYear(), testDate.getMonth(), testDate.getDate(), testTime.getHours(), testTime.getMinutes(), 0);

                                    Intent _myIntent = new Intent(PersonInfoActivity.this, NotifyService.class);
                                    _myIntent.putExtra("MyMessage",messageType);
                                    PendingIntent _myPendingIntent = PendingIntent.getBroadcast(PersonInfoActivity.this, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(),_myPendingIntent);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                Intent intent = null;

                                intent = new Intent(PersonInfoActivity.this, ThanksActivity.class);
                                intent.putExtra("date",choose_date);
                                intent.putExtra("timeslot",ActiveModels.SELECTED_SLOT.getSlot());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        }
                        @Override
                        public void VError(String responce) {
                            common.setToastMessage(responce);
                        }
                    });

                    */
        }

    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}
