package com.ziffytech.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
 * Created by admn on 15/11/2017.
 */

public class ForgotPassword extends CommonActivity {
    EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        setHeaderTitle("Forgot Password");
        allowBack();
         editText=(EditText)findViewById(R.id.txtEmail);

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
                startActivity(intent);*/
              forgetPassword();
            }
        });
    }

    private void forgetPassword() {
        String email = editText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editText.setError(getString(R.string.valid_required_email));
            return;

        }
        if (!isValidEmail(email)) {
            editText.setError(getString(R.string.valid_email));
            return;
        }


        if(!MyUtility.isConnected(this)) {

            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }



        showPrgressBar();

            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);

            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.FORGOT_PASSWORD_URL, params,
                    new VJsonRequest.VJsonResponce() {
                        @Override
                        public void VResponce(String responce) {
                            hideProgressBar();
                            JSONObject userdata = null;
                            try {
                                userdata = new JSONObject(responce);

                                if(userdata.getInt("responce")==1) {

                                    Log.e("Response",userdata.toString());
                                    MyUtility.showAlertMessage(ForgotPassword.this,userdata.getString("success"));

                                }else{

                                    MyUtility.showAlertMessage(ForgotPassword.this,"Provided email id not registered with us.");

                                }
                                } catch (JSONException e) {
                                e.printStackTrace();
                                hideProgressBar();
                                MyUtility.showAlertMessage(ForgotPassword.this,MyUtility.DATA_ERROR);

                            }
                        }
                        @Override
                        public void VError(String responce) {
                            hideProgressBar();
                            MyUtility.showAlertMessage(ForgotPassword.this,MyUtility.DATA_ERROR);

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
}
