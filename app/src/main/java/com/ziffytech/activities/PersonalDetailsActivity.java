package com.ziffytech.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Preferences;
import com.ziffytech.util.VJsonRequest;
import com.ziffytech.R;


/**
 * Created by admn on 11/11/2017.
 */

public class PersonalDetailsActivity extends CommonActivity {

    EditText name,email,phone,locality,bloodGroup,dob,height,weight,emer_contact;
    int year, month, day;
    String Mygender="0",mystatus="1";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details_activity);
        allowBack();
        setHeaderTitle(getString(R.string.PersonalDetails));
         Button    skip=(Button)findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(PersonalDetailsActivity.this,MedicalDetails.class);
                intent1.putExtra("new","new");
                startActivity(intent1);
            }
        });

        Calendar  c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.contact);
        locality = (EditText)findViewById(R.id.location);

        locality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent =
                            new PlaceAutocomplete
                                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(PersonalDetailsActivity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });


        dob = (EditText)findViewById(R.id.dob);
        height = (EditText)findViewById(R.id.height);
        weight = (EditText)findViewById(R.id.weight);
        emer_contact = (EditText)findViewById(R.id.emer_contact);
        bloodGroup = (EditText)findViewById(R.id.blood_group);


        bloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(PersonalDetailsActivity.this);
                builderSingle.setTitle("Select Blood Group");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PersonalDetailsActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("O+");
                arrayAdapter.add("O-");
                arrayAdapter.add("A+");
                arrayAdapter.add("B+");
                arrayAdapter.add("A-");
                arrayAdapter.add("B-");
                arrayAdapter.add("AB+");
                arrayAdapter.add("AB-");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(PersonalDetailsActivity.this);
                        builderInner.setMessage(strName);
                        bloodGroup.setText(strName);
                    }
                });
                builderSingle.show();
            }
        });


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dd = new DatePickerDialog(PersonalDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                                    String dateInString = year +"-"+ (monthOfYear + 1) +"-"+ dayOfMonth;
                                    Date date = formatter.parse(dateInString);
                                    dob.setText(formatter.format(date).toString());

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, year, month, day);

                dd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dd.show();

            }
        });


        sharedPreferences = getSharedPreferences(Preferences.MyPREFERENCES, Context.MODE_PRIVATE);

        Button button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personaldetails();
            }
        });



        //setup data

        email.setText(common.getSession(ApiParams.USER_EMAIL));
        name.setText(common.getSession(ApiParams.USER_FULLNAME));
        phone.setText(common.getSession(ApiParams.USER_PHONE));
        email.setEnabled(false);
        name.setEnabled(false);
        phone.setEnabled(false);


        if(getIntent().hasExtra("new")){



        }else{

            try {
                JSONObject data=new JSONObject(common.getSession(ApiParams.USER_JSON_DATA));


                if(data.getString("user_bdate").contains("0000")){
                    dob.setText("");
                }else{
                    dob.setText(data.getString("user_bdate"));
                }

                bloodGroup.setText(data.getString("blood_group"));
                height.setText(data.getString("height"));
                weight.setText(data.getString("weight"));
                emer_contact.setText(data.getString("e_contact"));
                locality.setText(data.getString("location"));

                String gender=data.getString("gender");
                Mygender=gender;
                String marriage=data.getString("matrial_status");
                mystatus=marriage;

                if(marriage.equalsIgnoreCase("1")){

                    //  Unmarried  SINGLE

                    RadioButton unmarr = (RadioButton) findViewById(R.id.single_radio);
                    unmarr.setChecked(true);

                }else{

                    //Married....

                    RadioButton single = (RadioButton) findViewById(R.id.marride);
                    single.setChecked(true);

                }

                if(gender.equalsIgnoreCase("1")){

                    // female

                    RadioButton female = (RadioButton) findViewById(R.id.radioFemale);
                    female.setChecked(true);

                }else{

                    // male

                    RadioButton male = (RadioButton) findViewById(R.id.radioMale);
                    male.setChecked(true);
                }


                skip.setVisibility(View.GONE);
                button.setText("UPDATE");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        typeGenderSelection();
        typeMaritalSelection();

    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress());


                locality.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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

    private void typeMaritalSelection() {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.readio_status);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.single_radio) {
                    mystatus="1";
                } else if (checkedId == R.id.marride) {
                    mystatus="0";
                }
            }
        });
    }


    private void personaldetails() {

        String Myemail=email.getText().toString();
        String myname=name.getText().toString();
        String myphone=phone.getText().toString();
        String local=locality.getText().toString();
        String mydob=dob.getText().toString();
        String myh=height.getText().toString();
        String myw=weight.getText().toString();
        String emer=emer_contact.getText().toString();
        String blood=bloodGroup.getText().toString();
        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(mydob)) {

            dob.setError(getString(R.string.dobi));

            return;

        }
        if (TextUtils.isEmpty(blood)) {

            bloodGroup.setError(getString(R.string.blood_grop));
            return;
        }

        if (TextUtils.isEmpty(myh)) {

            height.setError("Enter Valid Height");
            return;
        }

        if (TextUtils.isEmpty(myw)) {

            weight.setError("Enter Valid Weight");
            return;
        }

        if (TextUtils.isEmpty(emer) || emer.length()<10) {
            emer_contact.setError("Enter Valid Emergency Contact");
            return;
        }

        if (TextUtils.isEmpty(local)) {

            locality.setError("Enter Valid Locality");
            return;
        }

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }


        showPrgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.getSession(ApiParams.COMMON_KEY));
        params.put("user_bdate", mydob);
        params.put("blood_group", blood);
        params.put("matrial_status", mystatus);
        params.put("height", myh);
        params.put("weight", myw);
        params.put("gender", Mygender);
        params.put("e_contact", emer);
        params.put("location", local);


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.PERSON_DETAIL_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        hideProgressBar();
                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);

                            if(userdata.getInt("responce") == 1 ){

                                JSONObject data=userdata.getJSONObject("data");
                                common.setSession(ApiParams.USER_JSON_DATA, data.toString());

                                if(getIntent().hasExtra("new")){

                                    Intent intent=new Intent(PersonalDetailsActivity.this,MedicalDetails.class);
                                    intent.putExtra("new","new");
                                    startActivity(intent);

                                }else{

                                    finish();
                                    MyUtility.showToast("Personal Detail Updated!",PersonalDetailsActivity.this);
                                }

                            }else{
                                MyUtility.showAlertMessage(PersonalDetailsActivity.this,"Failed to Update");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            MyUtility.showAlertMessage(PersonalDetailsActivity.this,"Something went wrong.");
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void VError(String responce) {

                        hideProgressBar();
                        MyUtility.showAlertMessage(PersonalDetailsActivity.this,"Something went wrong.");
                        common.setToastMessage(responce);
                    }
                });

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_skip, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

          /*  case R.id.action_search:
                    Intent intent1 = new Intent(PersonalDetailsActivity.this, MainActivity.class);
                    startActivity(intent1);
*/
            //    break;



            case android.R.id.home:

                onBackPressed();

                return true;


            /*case R.id.item_logout:
                business_Medical_session.logoutUser();
                finish();
                break;*/
            default:


        }
        return super.onOptionsItemSelected(item);
    }

}
