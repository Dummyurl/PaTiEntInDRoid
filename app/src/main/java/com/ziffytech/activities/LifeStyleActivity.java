package com.ziffytech.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.google.gson.Gson;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;
import com.ziffytech.R;


/**
 * Created by admn on 13/11/2017.
 */

public class LifeStyleActivity extends CommonActivity {
    EditText occupation, smoking, alchohol, activity_level, food;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.life_style);
        setHeaderTitle("Life Style Details");
        allowBack();



        occupation = (EditText) findViewById(R.id.occupation);
        occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(LifeStyleActivity.this);
                builderSingle.setTitle("Select Occupation");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LifeStyleActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("IT");
                arrayAdapter.add("Health");
                arrayAdapter.add("Government");
                arrayAdapter.add("Private");
                arrayAdapter.add("Others");


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
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(LifeStyleActivity.this);
                        builderInner.setMessage(strName);
                        occupation.setText(strName);

                    }
                });
                builderSingle.show();
            }
        });

        smoking = (EditText) findViewById(R.id.Smoking);

        smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(LifeStyleActivity.this);
                builderSingle.setTitle(R.string.smoking);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LifeStyleActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("1");
                arrayAdapter.add("2");
                arrayAdapter.add("3");
                arrayAdapter.add("4");
                arrayAdapter.add("None");
                arrayAdapter.add("more than 4");


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
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(LifeStyleActivity.this);
                        builderInner.setMessage(strName);
                        smoking.setText(strName);

                    }
                });
                builderSingle.show();
            }
        });

        alchohol = (EditText) findViewById(R.id.Alcohol);

        alchohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(LifeStyleActivity.this);
                builderSingle.setTitle("Alcohol Consumption");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LifeStyleActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Daily");
                arrayAdapter.add("Weakly");
                arrayAdapter.add("Social");
                arrayAdapter.add("None");


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
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(LifeStyleActivity.this);
                        builderInner.setMessage(strName);
                        alchohol.setText(strName);

                    }
                });
                builderSingle.show();
            }
        });

        activity_level = (EditText) findViewById(R.id.Activity);

        activity_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(LifeStyleActivity.this);
                builderSingle.setTitle("Activity Level");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LifeStyleActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Lazy");
                arrayAdapter.add("Sitting");
                arrayAdapter.add("Mildly");
                arrayAdapter.add("Active");
                arrayAdapter.add("Super Active");


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
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(LifeStyleActivity.this);
                        builderInner.setMessage(strName);
                        activity_level.setText(strName);

                    }
                });
                builderSingle.show();
            }
        });
        food = (EditText) findViewById(R.id.food);


        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(LifeStyleActivity.this);
                builderSingle.setTitle("Food Preferences");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LifeStyleActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Veg");
                arrayAdapter.add("NonVeg");
                arrayAdapter.add("Egg");
                arrayAdapter.add("Jain");


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
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(LifeStyleActivity.this);
                        builderInner.setMessage(strName);
                        food.setText(strName);

                    }
                });
                builderSingle.show();
            }
        });


        button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                style();
            }
        });


        if(getIntent().hasExtra("new")){



        }else{

            try {

                button.setText("UPDATE");

                JSONObject data=new JSONObject(common.getSession(ApiParams.USER_JSON_DATA));

                if(data.getString("occupation").equalsIgnoreCase("null")){
                    occupation.setText("");
                }else{
                    occupation.setText(data.getString("occupation"));
                }
                if(data.getString("smoking").equalsIgnoreCase("null")){
                    smoking.setText("");
                }else{
                    smoking.setText(data.getString("smoking"));
                }
                if(data.getString("alcohol").equalsIgnoreCase("null")){
                    alchohol.setText("");
                }else{
                    alchohol.setText(data.getString("alcohol"));
                }
                if(data.getString("activity_level").equalsIgnoreCase("null")){
                    activity_level.setText("");
                }else{
                    activity_level.setText(data.getString("activity_level"));
                }
                if(data.getString("food").equalsIgnoreCase("null")){
                    food.setText("");
                }else{
                    food.setText(data.getString("food"));
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void style() {

        String MyOccupation = occupation.getText().toString();
        String Mysmoke = smoking.getText().toString();
        String Myalchohol = alchohol.getText().toString();
        String myactivity = activity_level.getText().toString();
        String Myfood = food.getText().toString();


        if (MyOccupation.length() <= 0 &&
                Mysmoke.length() <= 0  &&
                Myalchohol.length() <= 0 &&
                myactivity.length() <= 0 &&
                Myfood.length() <= 0) {

            Intent intent=new Intent(LifeStyleActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else{

            showPrgressBar();


            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", common.get_user_id());
            params.put("occupation", MyOccupation);
            params.put("smoking", Mysmoke);
            params.put("alcohol", Myalchohol);
            params.put("activity_level", myactivity);
            params.put("food", Myfood);

            Log.e("params",new Gson().toJson(params));


            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.LIFESTYLE_DETAIL_URL, params,
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
                                        Intent intent=new Intent(LifeStyleActivity.this,MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else{
                                        finish();
                                        MyUtility.showToast("Lifestyle Detail Updated!",LifeStyleActivity.this);
                                    }
                                }else{
                                    MyUtility.showAlertMessage(LifeStyleActivity.this,"Failed to Update");
                                }

                            } catch (JSONException e) {

                                MyUtility.showAlertMessage(LifeStyleActivity.this,"Failed to Update");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void VError(String responce) {
                           // common.setToastMessage(responce);
                            hideProgressBar();
                            MyUtility.showAlertMessage(LifeStyleActivity.this,"Failed to Update");
                        }
                    });

        }


    }



}

