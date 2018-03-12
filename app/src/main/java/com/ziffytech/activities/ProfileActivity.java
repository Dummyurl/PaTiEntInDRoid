package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;


/**
 * Created by Mahesh on 29/11/17.
 */

public class ProfileActivity extends CommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_activity);
        allowBack();
        setHeaderTitle("My Profile");

    }

    @Override
    protected void onStart() {
        super.onStart();

        setUpViews();

    }

    private void setUpViews() {

        TextView tvName=(TextView)findViewById(R.id.tvName);
        tvName.setText(common.getSession(ApiParams.USER_FULLNAME));

        TextView tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvEmail.setText(common.getSession(ApiParams.USER_EMAIL));

        Button btnPersondetails=(Button)findViewById(R.id.btnpersonalDetails);
        btnPersondetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,PersonalDetailsActivity.class));

            }
        });

        Button btnMedicaldetails=(Button)findViewById(R.id.btnMedicalDetails);
        btnMedicaldetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,MedicalDetails.class));

            }
        });


        Button btnLifeStyle=(Button)findViewById(R.id.btnLifeStyleDetails);
        btnLifeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,LifeStyleActivity.class));

            }
        });

        Button btnFamilyMebers=(Button)findViewById(R.id.btnFamilyMebers);
        btnFamilyMebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,RelativeProfile.class));

            }
        });


        Button btnUpdateProfile=(Button)findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,UpdateProfileActivity.class));

            }
        });

        Button btnMedication=(Button)findViewById(R.id.btnMedication);
        btnMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ProfileActivity.this,MedicationActivity.class));

            }
        });

    }
}
