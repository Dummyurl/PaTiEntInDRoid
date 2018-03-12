package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.util.HashMap;

import com.ziffytech.Config.ApiParams;

import com.ziffytech.R;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends CommonActivity {
    EditText txtNewPass, txtCPass, txtRPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        allowBack();
        setHeaderTitle(getString(R.string.change_password));

        txtNewPass = (EditText)findViewById(R.id.txtNewPassword);
        txtCPass = (EditText)findViewById(R.id.txtCurrentPassword);
        txtRPass = (EditText)findViewById(R.id.txtRePassword);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }
        });


    }
    public void register(){



        String newPassword = txtNewPass.getText().toString();
        String currentPassword = txtCPass.getText().toString();
        String rePassword = txtRPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.

        if (TextUtils.isEmpty(currentPassword)) {
            txtCPass.setError(getString(R.string.valid_required_current_password));
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            txtNewPass.setError(getString(R.string.valid_required_new_password));
            return;
        }

        if (newPassword.length() < 6) {
            txtNewPass.setError(getString(R.string.password_short));
            return;
        }

        if (newPassword.length() > 15) {
            txtNewPass.setError("Password Length too large");
            return;

        }

        if(!newPassword.equalsIgnoreCase(rePassword)){

            txtRPass.setError("Password not matching ");
            return;
        }


            HashMap<String,String> params = new HashMap<>();
            params.put("c_password",currentPassword);
            params.put("n_password",newPassword);
            params.put("r_password",rePassword);
            params.put("user_id",common.get_user_id());

            showPrgressBar();


            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.CHANGE_PASSWORD_URL,params,
                    new VJsonRequest.VJsonResponce(){
                        @Override
                        public void VResponce(String responce) {
                            //common.setToastMessage(responce);
                            hideProgressBar();

                            try {
                                JSONObject jsonObject=new JSONObject(responce);

                                if(jsonObject.getBoolean("responce")){

                                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }else{

                                    MyUtility.showToast(jsonObject.getString("error"),ChangePasswordActivity.this);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        @Override
                        public void VError(String responce)
                        {
                            hideProgressBar();
                           // common.setToastMessage(responce);
                        }
                    });
        }



}
