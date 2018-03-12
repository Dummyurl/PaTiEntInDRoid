package com.ziffytech.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mahesh on 17/10/17.
 */

public class MedicineOrderAdressActivity extends CommonActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText pincode, city, full_address, Pref_time;
    int mHour, mMinute;
    String user_id="";
    String aMpM = "AM";
    Button submit;
    String   currentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        setHeaderTitle("Add Address");
        allowBack();


        bindView();

    }

    public void bindView() {

        pincode = (EditText) findViewById(R.id.pin);
        city = (EditText) findViewById(R.id.mycitye);
        full_address = (EditText) findViewById(R.id.address);
        Pref_time = (EditText) findViewById(R.id.time);

        Pref_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog();
            }

        });


        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Order();
            }

            //  startActivity(new Intent(MedicineOrderAdressActivity.this,MedicineOrderActivity.class));


        });


    }

    public void DateDialog(){

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MedicineOrderAdressActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        now.add(Calendar.MONTH,+1);
        dpd.setMaxDate(now);
        dpd.show(getFragmentManager(), "Datepickerdialog");



      /*  DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {

               final String   currentdate =year +"-"+ (monthOfYear+1)+"-"+dayOfMonth;

                // Process to get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                final Calendar datetime = Calendar.getInstance();


                TimePickerDialog tpd = new TimePickerDialog(MedicineOrderAdressActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (datetime.getTimeInMillis() < c.getTimeInMillis()) {

                                    MyUtility.showToast("Invalid Time",MedicineOrderAdressActivity.this);

                                }

                                if(hourOfDay >11)
                                {
                                    aMpM = "PM";
                                }

                                if(hourOfDay>11)
                                {
                                    mHour = hourOfDay - 12;
                                }
                                else
                                {
                                    mHour = hourOfDay;
                                }
                                Pref_time.setText(currentdate +" "+ String.valueOf(mHour) + ":" + minute + aMpM);

                            }
                        }, mHour, mMinute, false);


                tpd.show();

            }};


        Calendar  calender = Calendar.getInstance(TimeZone.getDefault());
        int  c_day = calender.get(Calendar.DAY_OF_MONTH);
        int c_month = calender.get(Calendar.MONTH) + 1;
        int c_year = calender.get(Calendar.YEAR);

        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, c_year, c_month - 1, c_day);

        final Calendar minDate = Calendar.getInstance();
        minDate.setTimeInMillis(new Date().getTime());
        minDate.set(Calendar.HOUR_OF_DAY, minDate.getMinimum(Calendar.HOUR_OF_DAY));
        minDate.set(Calendar.MINUTE, minDate.getMinimum(Calendar.MINUTE));
        minDate.set(Calendar.SECOND, minDate.getMinimum(Calendar.SECOND));
        minDate.set(Calendar.MILLISECOND, minDate.getMinimum(Calendar.MILLISECOND));

        dpDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        dpDialog.show();

        */

    }




    public void Order() {

        String mypin = pincode.getText().toString().trim();
        String mycity = city.getText().toString().trim();
        String myAddress = full_address.getText().toString().trim();
        String preftime = Pref_time.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if(TextUtils.isEmpty(mypin)) {
            pincode.setError(getString(R.string.enterpin));
            focusView = pincode;
            focusView.requestFocus();
            return;
        }

        if (mypin.length()<6) {
            pincode.setError("Enter Valid Pin");
            focusView = pincode;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(myAddress)) {
            full_address.setError(getString(R.string.enteraddress));
            focusView = full_address;
            focusView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(preftime)) {
            Pref_time.setError(getString(R.string.enetertime));
            focusView = Pref_time;
            focusView.requestFocus();
            return;
        }

            Intent intent = new Intent(MedicineOrderAdressActivity.this, MedicineOrderActivity.class);
            intent.putExtra(ApiParams.PINCODE, mypin);
            intent.putExtra(ApiParams.CITY, mycity);
            intent.putExtra(ApiParams.ADDRESS, myAddress);
            intent.putExtra(ApiParams.Time, preftime);
            startActivity(intent);
      }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        currentdate =year +"-"+ (monthOfYear+1)+"-"+dayOfMonth;

        Calendar now = Calendar.getInstance();
        TimePickerDialog timepickerdialog = TimePickerDialog.newInstance(
                MedicineOrderAdressActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        timepickerdialog.setMinTime(now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),now.get(Calendar.SECOND));
        timepickerdialog.show(getFragmentManager(), "Timepickerdialog");


    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        if(hourOfDay >11)
        {
            aMpM = "PM";
        }

        if(hourOfDay>11)
        {
            mHour = hourOfDay - 12;
        }
        else
        {
            mHour = hourOfDay;
        }

        Pref_time.setText(currentdate +" "+ String.valueOf(mHour) + ":" + minute +" "+ aMpM);

    }
}



