package com.ziffytech.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.util.CommonClass;

/**
 * Created by subhashsanghani on 5/22/17.
 */

public abstract class CommonActivity extends AppCompatActivity {
    public CommonClass common;
    private KProgressHUD progressHUD;
    private TextView mLocatonTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        common = new CommonClass(this);
        super.onCreate(savedInstanceState);


    }
    public void allowBack(){
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    public Typeface getCustomFont(){
        return  Typeface.createFromAsset(getAssets(), "LobsterTwo-BoldItalic.ttf");
    }
    public void setHeaderTitle(String title,String location){
      //  Typeface font = getCustomFont();

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custome_header_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.action_bar_title);
         mLocatonTextView = (TextView) mCustomView.findViewById(R.id.action_bar_location);
        //  mTitleTextView.setTypeface(font);
        mTitleTextView.setText(title);
        mLocatonTextView.setText(location);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        mLocatonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent =
                            new PlaceAutocomplete
                                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(CommonActivity.this);
                    startActivityForResult(intent, 123);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });

    }

    public void setHeaderTitle(String title){
        //  Typeface font = getCustomFont();

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custome_header_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.action_bar_title);
        TextView mLocatonTextView = (TextView) mCustomView.findViewById(R.id.action_bar_location);
        mTitleTextView.setText(title);
        mLocatonTextView.setVisibility(View.GONE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }


    public void setProgressBarAnimation(ProgressBar progressBar1){
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar1, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration (5000); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.setRepeatCount(AlphaAnimation.INFINITE);
        animation.start ();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPrgressBar(){

        progressHUD =new KProgressHUD(this);
        progressHUD   //.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
               // .setLabel("Please wait")
               // .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
               // .setDimAmount(0.5f)
                .show();
    }

    public void hideProgressBar(){

        if(progressHUD.isShowing()){
            progressHUD.dismiss();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress());

                common.setSession(ApiParams.CURRENT_CITY,place.getName()+"");
                mLocatonTextView.setText(common.getSession(ApiParams.CURRENT_CITY));


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
