package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;

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


public class UpdateProfileActivity extends CommonActivity {
    EditText editEmail,  editPhone, editFullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        allowBack();
        setHeaderTitle(getString(R.string.update_profile));
        editEmail = (EditText)findViewById(R.id.txtEmail);
        editFullname = (EditText)findViewById(R.id.txtFirstname);
        editPhone = (EditText)findViewById(R.id.txtPhone);

        editEmail.setText(common.getSession(ApiParams.USER_EMAIL));
        editFullname.setText(common.getSession(ApiParams.USER_FULLNAME));
        editPhone.setText(common.getSession(ApiParams.USER_PHONE));
        editEmail.setEnabled(false);


        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    public void register(){

        final String fullname = editFullname.getText().toString();
        final String phone = editPhone.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.

        if (TextUtils.isEmpty(fullname)) {
            editFullname.setError(getString(R.string.valid_required_fullname));
            focusView = editFullname;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            editPhone.setError("Phone number required");
            focusView = editPhone;
            cancel = true;
        }

        if (phone.length() < 10) {
            editPhone.setError(getString(R.string.valid_required_phone));
            focusView = editPhone;
            focusView.requestFocus();
            return;
        }



        if (cancel) {
            if (focusView!=null)
                focusView.requestFocus();
        } else {
            HashMap<String,String> params = new HashMap<>();
            params.put("user_fullname",fullname);
            params.put("user_phone",phone);
            params.put("user_id",common.get_user_id());


            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.UPDATEPROFILE_URL,params,
                    new VJsonRequest.VJsonResponce(){
                        @Override
                        public void VResponce(String responce) {

                            try {
                                JSONObject jsonObject = new JSONObject(responce);

                                if (jsonObject.getInt("responce") == 1) {

                                    common.setSession(ApiParams.USER_FULLNAME, fullname);
                                    common.setSession(ApiParams.USER_PHONE, phone);


                                    MyUtility.hideKeyboard(editFullname,UpdateProfileActivity.this);

                                    finish();
                                   // Intent intent = new Intent(UpdateProfileActivity.this, Profi.class);
                                   // startActivity(intent);

                                } else {

                                    MyUtility.showToast("Failed to update.", UpdateProfileActivity.this);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void VError(String responce) {
                            common.setToastMessage(responce);
                        }
                    });
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
