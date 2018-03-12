package com.ziffytech.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.R;
import com.ziffytech.adapters.CategoryAdapter;
import com.ziffytech.configfcm.MyFirebaseRegister;
import com.ziffytech.models.CategoryModel;
import com.ziffytech.remainder.*;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends CommonActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<CategoryModel> categoryArray;
    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    ProgressBar progressBar1;
    Toolbar toolbar;
    CardView findDoctor,medicalRecords,remainders,orderMedicine;

    private String bookingNumber="",emergencyNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(common.getSession(ApiParams.CURRENT_CITY)!=null && !common.getSession(ApiParams.CURRENT_CITY).equalsIgnoreCase(""))
        {
            setHeaderTitle("Home",common.getSession(ApiParams.CURRENT_CITY));

        }else{

            common.setSession(ApiParams.CURRENT_CITY,"Pune");
            setHeaderTitle("Home",common.getSession(ApiParams.CURRENT_CITY));

        }
        categoryArray = new ArrayList<>();
        allowBack();


        if (!common.getSessionBool("fcm_registered") && common.is_user_login()){
            MyFirebaseRegister fireReg = new MyFirebaseRegister(this);
            fireReg.RegisterUser(common.get_user_id());
        }

        TextView textView1 = (TextView)findViewById(R.id.textView);
        textView1.setTypeface(getCustomFont());
        bindView();
        //loadData();

        Log.e("LoggedUserData",common.getSession(ApiParams.USER_JSON_DATA));

    }
    public void bindView(){
        categoryRecyclerView = (RecyclerView) findViewById(R.id.rv_artist);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 3);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this,categoryArray);
        categoryRecyclerView.setAdapter(categoryAdapter);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        setProgressBarAnimation(progressBar1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        findDoctor=(CardView)findViewById(R.id.cv_find_a_doctor);
        medicalRecords=(CardView)findViewById(R.id.cv_records);
        remainders=(CardView)findViewById(R.id.cv_remainders);
        orderMedicine=(CardView)findViewById(R.id.cv_order_medicine);


        findDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,DoctorMainCategoriesActivity.class);
                startActivity(intent);

            }
        });

        medicalRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,MedicalRecords.class);
                startActivity(intent);

            }
        });

        remainders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, com.ziffytech.remainder.RemainderActivity.class);
                startActivity(intent);

            }
        });


        orderMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,MedicineOrderAdressActivity.class);
                startActivity(intent);

            }
        });


        CardView cv_book=(CardView)findViewById(R.id.cv_book);
        cv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = "+91"+bookingNumber;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        CardView cv_chat=(CardView)findViewById(R.id.cv_chat);
        cv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);

            }
        });

        CardView cv_askquetions=(CardView)findViewById(R.id.cv_askquetions);
        cv_askquetions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AskQuestionActiivty.class);
                startActivity(intent);

            }
        });


        CardView btnEmergency=(CardView)findViewById(R.id.cv_emer);
        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = "+91"+emergencyNumber;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        if (!common.is_user_login()){

            nav_Menu.findItem(R.id.nav_appointment).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_password).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_login).setVisible(true);
            navHeader.findViewById(R.id.txtFullName).setVisibility(View.GONE);
            navHeader.findViewById(R.id.textEmailId).setVisibility(View.GONE);

        }else{
            nav_Menu.findItem(R.id.nav_appointment).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            nav_Menu.findItem(R.id.nav_password).setVisible(true);
            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            navHeader.findViewById(R.id.txtFullName).setVisibility(View.VISIBLE);
            navHeader.findViewById(R.id.textEmailId).setVisibility(View.VISIBLE);
            ((TextView)navHeader.findViewById(R.id.txtFullName)).setText(common.getSession(ApiParams.USER_FULLNAME));
            ((TextView)navHeader.findViewById(R.id.textEmailId)).setText(common.getSession(ApiParams.USER_EMAIL));
        }
        super.onResume();
    }

    public void loadData(){
        VJsonRequest vJsonRequest = new VJsonRequest(MainActivity.this, ApiParams.CATEGORY_LIST,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<CategoryModel>>() {
                        }.getType();
                        categoryArray.clear();
                        categoryArray.addAll((Collection<? extends CategoryModel>) gson.fromJson(responce, listType));
                        categoryAdapter.notifyDataSetChanged();
                        progressBar1.setVisibility(View.GONE);

                    }
                    @Override
                    public void VError(String responce) {
                        progressBar1.setVisibility(View.GONE);
                    }
                });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_password) {
            Intent intent = new Intent(MainActivity.this,ChangePasswordActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_appointment) {
            Intent intent = new Intent(MainActivity.this,MyAppointmentsActivity.class);
            startActivity(intent);

        }else if(id == R.id.nav_logout){
            common.logOut();
        }else if(id == R.id.nav_login){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_share){
            shareApp();
        }else if(id == R.id.nav_rating){
            reviewOnApp();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void searchViewClick(View view){
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        login();
    }


    public void login() {


       // showPrgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_CONTACTS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                      //  hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if(userdata.getInt("responce")==1){

                                JSONArray arr=userdata.getJSONArray("data");

                                for(int i=0;i<arr.length();i++){
                                    JSONObject obj=arr.getJSONObject(i);
                                    bookingNumber=obj.getString("book_no");
                                    emergencyNumber=obj.getString("emergency_no");
                                }


                                JSONObject data=userdata.getJSONObject("user");

                                common.setSession(ApiParams.COMMON_KEY, data.getString("user_id"));
                                common.setSession(ApiParams.USER_EMAIL, data.getString("user_email"));
                                common.setSession(ApiParams.USER_FULLNAME, data.getString("user_fullname"));
                                common.setSession(ApiParams.USER_PHONE, data.getString("user_phone"));
                                common.setSession(ApiParams.USER_JSON_DATA, data.toString());


                            }else{
                                common.logOut();
                            }

                        } catch (JSONException e) {
                           // hideProgressBar();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce) {
                       // hideProgressBar();
                    }
                });

    }

}
