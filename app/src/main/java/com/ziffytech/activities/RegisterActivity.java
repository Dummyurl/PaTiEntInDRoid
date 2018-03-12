package com.ziffytech.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.Preferences;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import com.ziffytech.R;


public class RegisterActivity extends CommonActivity {
    EditText editEmail, editPassword, editPhone, editFullname,c_passwrd;
    int user_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button btnCountry;
    LinearLayout terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        allowBack();
        setHeaderTitle(getString(R.string.register_new));

        editEmail = (EditText)findViewById(R.id.txtEmail);
        editPassword = (EditText)findViewById(R.id.txtPassword);
        editFullname = (EditText)findViewById(R.id.txtFirstname);
        editPhone = (EditText)findViewById(R.id.txtPhone);
        c_passwrd=(EditText)findViewById(R.id.c_pass);


        terms=(LinearLayout)findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(ConstValue.BASE_URL+"index.php/admin/policy"));
                startActivity(i);

            }
        });


        sharedPreferences = getSharedPreferences(Preferences.MyPREFERENCES, Context.MODE_PRIVATE);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        btnCountry=(Button)findViewById(R.id.btnCountry);
        btnCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String [] arr=getResources().getStringArray(R.array.country_arrays);
                final String [] code=getResources().getStringArray(R.array.country_code);


                AlertDialog.Builder ad=new AlertDialog.Builder(RegisterActivity.this);
                ad.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        if(arr[i].equalsIgnoreCase("India")){

                            btnCountry.setText("+91");

                        }else{

                            btnCountry.setText(code[i]);

                        }

                    }
                });

                ad.create().show();

            }
        });
    }
    public void register(){

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String fullname = editFullname.getText().toString();
        String phone = editPhone.getText().toString();
        String c_pass=c_passwrd.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(fullname)) {
            editFullname.setError(getString(R.string.valid_required_fullname));
            focusView = editFullname;
            focusView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            editPhone.setError(getString(R.string.valid_required_phone));
            focusView = editPhone;
            focusView.requestFocus();
            return;
        }

        if (phone.length() < 10) {
            editPhone.setError(getString(R.string.valid_required_phone));
            focusView = editPhone;
            focusView.requestFocus();
            return;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.valid_required_email));
            focusView = editEmail;
            focusView.requestFocus();
            return;
        }
        if (!isValidEmail(email)) {
            editEmail.setError(getString(R.string.valid_email));
            focusView = editEmail;
            focusView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.valid_required_password));
            focusView = editPassword;
            focusView.requestFocus();
            return;
        }

         if (password.length() < 6) {
             editPassword.setError(getString(R.string.password_short));
             focusView = editPassword;
             focusView.requestFocus();
             return;

        }  if (password.length() > 15) {
            editPassword.setError("Password Length too large");
            focusView = editPassword;
            focusView.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(c_pass)) {
            c_passwrd.setError("Enter Password Again");
            focusView = editPassword;
            focusView.requestFocus();
            return;
        }


        if (!password.equalsIgnoreCase(c_pass)) {
            c_passwrd.setError(getString(R.string.password_not_match));
            focusView = c_passwrd;
            focusView.requestFocus();
            return;
        }


        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }


        showPrgressBar();

        HashMap<String,String> params = new HashMap<>();
        params.put("user_fullname",fullname);
        params.put("user_phone",phone);
        params.put("user_email",email);
        params.put("user_password",password);
        params.put("country_code",btnCountry.getText().toString());


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.REGISTER_URL,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if(userdata.getInt("responce")==1){

                                JSONObject data=userdata.getJSONObject("data");

                                common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                                common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                common.setSession(ApiParams.USER_JSON_DATA, data.toString());

                                finish();
                                Intent intent = new Intent(RegisterActivity.this, PersonalDetailsActivity.class);
                                intent.putExtra("new","new");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);




                            }else{
                                MyUtility.showAlertMessage(RegisterActivity.this,"Provided phone or email id already Registered with us,If you forgotten your password then reset your password.");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            e.printStackTrace();
                        }



                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
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
    private final Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (message.getData().containsKey(ApiParams.PARM_RESPONCE)){
                if (message.getData().getBoolean(ApiParams.PARM_RESPONCE)){
                    ArrayList<HashMap<String,String>> loginArray =  (ArrayList<HashMap<String,String>>) message.getData().getSerializable(ApiParams.PARM_DATA);
                    if(loginArray!=null) {
                        HashMap<String, String> userdata = loginArray.get(0);
                        Intent intent = null;

                        common.setSession(ApiParams.COMMON_KEY, userdata.get("user_id"));
                        common.setSession(ApiParams.USER_FULLNAME, userdata.get("user_fullname"));
                        common.setSession(ApiParams.USER_EMAIL, userdata.get("user_email"));
                        common.setSession(ApiParams.USER_PHONE,userdata.get("user_phone"));

                        intent = new Intent(RegisterActivity.this, PersonalDetailsActivity.class);
                        intent.putExtra("new","new");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    common.setToastMessage(message.getData().getString(ApiParams.PARM_ERROR));
                }
            }
        }
    };


}
