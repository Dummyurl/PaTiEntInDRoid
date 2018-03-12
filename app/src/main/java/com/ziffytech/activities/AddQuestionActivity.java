package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.models.QuestionModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Mahesh on 09/01/18.
 */

public class AddQuestionActivity extends CommonActivity {

    EditText txtDoctor,txtSubject, txtDesc;
    private String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);
        allowBack();
        setHeaderTitle("Ask Question");

        id="";

        txtDoctor=(EditText)findViewById(R.id.txtDoctor);
        txtSubject = (EditText)findViewById(R.id.txtSubject);
        txtDesc = (EditText)findViewById(R.id.txtDescr);

        Button button = (Button)findViewById(R.id.btnSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }
        });

        txtDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(AddQuestionActivity.this,MyDoctorsActivity.class);
                i.putExtra("from","ques");
                startActivityForResult(i,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            txtDoctor.setText(data.getExtras().getString("name"));
            id= data.getExtras().getString("id");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    public void register(){



        String sub = txtSubject.getText().toString();
        String desc = txtDesc.getText().toString();


        // Check for a valid email address.

        if (TextUtils.isEmpty(id)) {
            txtDoctor.setError("Select Doctor");
            txtDoctor.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(sub)) {
            txtSubject.setError("Enter Subject");
            txtSubject.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            txtDesc.setError("Enter Description");
            txtDesc.requestFocus();
            return;
        }


        txtDoctor.setError(null);
        txtSubject.setError(null);
        txtDesc.setError(null);



        showPrgressBar();

            HashMap<String,String> params = new HashMap<>();
            params.put("title",sub);
            params.put("description",desc);
            params.put("doct_id",id);
            params.put("user_id",common.get_user_id());


            VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.ADD_QUES,params,
                    new VJsonRequest.VJsonResponce(){
                        @Override
                        public void VResponce(String responce) {

                            hideProgressBar();

                            try {
                                JSONObject jsonObject=new JSONObject(responce);

                                int status=jsonObject.getInt("responce");

                                if(status==1){

                                    finish();

                                    MyUtility.showToast("Question Added Successfully!!!",AddQuestionActivity.this);


                                }else{

                                    MyUtility.showToast("Failed to Add",AddQuestionActivity.this);


                                }


                            } catch (JSONException e) {
                                hideProgressBar();
                                e.printStackTrace();
                            }


                        }
                        @Override
                        public void VError(String responce)
                        {
                            hideProgressBar();
                            common.setToastMessage(responce);
                        }
                    });
        }

    }


