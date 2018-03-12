package com.ziffytech.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;



/**
 * Created by admn on 16/11/2017.
 */

public class AddMember extends CommonActivity {
    EditText dob,name,relation,age,enumber,height,weight;
    int year, month, day;
    Button button;
    String ageS;
    String Mygender="0";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_profile);
        setHeaderTitle("Add Member");
        allowBack();

        name=(EditText) findViewById(R.id.name);
        relation=(EditText) findViewById(R.id.relation);
        age=(EditText) findViewById(R.id.age);
        enumber=(EditText)findViewById(R.id.contact);

        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addmember();

            }
        });
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        dob=(EditText)findViewById(R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(AddMember.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);

                                    dob.setText(formatter.format(date).toString());
                                  getAge(year,month,day);
                                    age.setText(ageS);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, year, month, day);

                dd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dd.show();

            }
        });

        height = (EditText)findViewById(R.id.height);
        weight = (EditText)findViewById(R.id.weight);

        typeGenderSelection();
    }


    private void typeGenderSelection() {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radioMale) {
                    Mygender="0";
                } else if (checkedId == R.id.radioFemale) {
                    Mygender="1";
                }
            }
        });
    }

    private void addmember() {

        String myname=name.getText().toString();
        String myrelation=relation.getText().toString();
       String mage=age.getText().toString();

        String myh=height.getText().toString();
        String myw=weight.getText().toString();


        String contact=enumber.getText().toString();
        String mybdy=dob.getText().toString();



        boolean cancel = false;
        View focusView = null;

        if (myname.length() <= 0) {
            name.setError(getString(R.string.valid_required_fullname));
            return;}

        if (myrelation.length() <= 0) {
            relation.setError(getString(R.string.enter_relation));
            return;
        }
        if (mage.length() <= 0) {
            age.setError(getString(R.string.enter_age));
            return;
        }
       if (contact.length() != 10) {
           enumber.setError(getString(R.string.enter_phone));
            return;
        }

        if (mybdy.equalsIgnoreCase("")) {
            dob.setError(getString(R.string.enter_bdy));
            return;
        }


        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }

            showPrgressBar();

            HashMap<String,String> params = new HashMap<>();
            params.put("m_name",myname);
            params.put("relation",myrelation);
            params.put("age",mage);
            params.put("height", myh);
            params.put("weight", myw);
            params.put("birth_date",mybdy);
            params.put("alternate_number",contact);
            params.put("m_gender",Mygender);
            params.put("user_id",common.get_user_id());

            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.AddMember,params,
                    new VJsonRequest.VJsonResponce(){
                        @Override
                        public void VResponce(String responce) {

                            hideProgressBar();

                            try {
                                JSONObject jsonObject=new JSONObject(responce);

                                if(jsonObject.getInt("success")==1){
                                    finish();
                                    MyUtility.showToast("Record Added.",AddMember.this);
                                }else{

                                    MyUtility.showToast(jsonObject.getString("error"),AddMember.this);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void VError(String responce)
                        {
                            hideProgressBar();
                            MyUtility.showToast("Failed to add Record",AddMember.this);

                        }
                    });


    }


    public String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);

        ageS = ageInt.toString();

        return ageS;
    }

}
