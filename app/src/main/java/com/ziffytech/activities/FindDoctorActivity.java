package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.ziffytech.Config.ApiParams;
import com.ziffytech.adapters.CategoryAdapter;
import com.ziffytech.models.CategoryModel;
import com.ziffytech.util.VJsonRequest;
import com.ziffytech.R;


/**
 * Created by Mahesh on 16/10/17.
 */

public class FindDoctorActivity extends CommonActivity {

    ArrayList<CategoryModel> categoryArray;
    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    ProgressBar progressBar1;
    Toolbar toolbar;
    SeekBar seekBar;
    TextView txtArea;
    Switch switch1;
    AutoCompleteTextView searchText;
    Bundle bundleloclaity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_layout);
        allowBack();
        setHeaderTitle("Select Category");
        categoryArray = new ArrayList<>();
        TextView textView1 = (TextView)findViewById(R.id.textView);
        textView1.setTypeface(getCustomFont());
        bindView();
        loadData();
    }
    public void bindView(){

        txtArea = (TextView) findViewById(R.id.textArea);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        switch1 = (Switch) findViewById(R.id.switch1);
        searchText = (AutoCompleteTextView) findViewById(R.id.search_keyword);

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // searchDoctor();
            }
        });

        categoryRecyclerView = (RecyclerView) findViewById(R.id.rv_artist);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this,categoryArray);
        categoryRecyclerView.setAdapter(categoryAdapter);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        setProgressBarAnimation(progressBar1);

        seekBar.setMax(100);
        txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
        seekBar.setProgress(common.getSessionInt("radius"));

        if (!common.containKeyInSession("nearby_enable")) {
            common.setSessionBool("nearby_enable", true);
        }
        switch1.setChecked(common.getSessionBool("nearby_enable"));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                common.setSessionBool("nearby_enable", b);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                common.setSessionInt("radius", i);
                txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        FloatingActionButton btnFilter=(FloatingActionButton)findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FindDoctorActivity.this,SearchActivity.class));
            }
        });

    }

    public void SearchButtonClick(View view){


        Intent intent = new Intent(FindDoctorActivity.this,BookActivity.class);
        Bundle b = new Bundle();
        if (!TextUtils.isEmpty(searchText.getText())) {
            b.putString("search", searchText.getText().toString());
        }
        if (bundleloclaity!=null){
            b.putString("lat",bundleloclaity.getString("latitude"));
            b.putString("lon",bundleloclaity.getString("longitude"));
            b.putString("locality",bundleloclaity.getString("locality"));
            b.putString("locality_id",bundleloclaity.getString("locality_id"));
        }
        intent.putExtras(b);
        startActivity(intent);

    }
    public void LocalityViewClick(View view){
        Intent intent = new Intent(FindDoctorActivity.this,LocationActivity.class);
        startActivity(intent);
    }




    public void loadData(){


        HashMap<String,String> params = new HashMap<>();
        params.put("cat_id",getIntent().getStringExtra("id"));

        VJsonRequest vJsonRequest = new VJsonRequest(FindDoctorActivity.this, ApiParams.CATEGORY_LIST,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                        if(jsonObject.getInt("responce")==1){


                            JSONArray data=jsonObject.getJSONArray("data");

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<CategoryModel>>() {
                            }.getType();
                            categoryArray.clear();
                            categoryArray.addAll((Collection<? extends CategoryModel>) gson.fromJson(data.toString(), listType));
                            categoryAdapter.notifyDataSetChanged();
                            progressBar1.setVisibility(View.GONE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                        progressBar1.setVisibility(View.GONE);
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_location:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void searchViewClick(View view){
        Intent intent = new Intent(FindDoctorActivity.this,SearchActivity.class);
        startActivity(intent);
    }
}
