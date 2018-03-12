package com.ziffytech.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.models.MainCatModel;
import com.ziffytech.util.RecyclerItemClickListener;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mahesh on 13/01/18.
 */

public class DoctorMainCategoriesActivity extends CommonActivity {


    RecyclerView categoryRecyclerView;
    ArrayList<MainCatModel> categoryArray;
    MainCategoryAdapter categoryAdapter;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_main_catgeories);

        setHeaderTitle("Find Doctor");
        allowBack();

        categoryArray = new ArrayList<>();


       /* LinearLayout lldoctors=(LinearLayout)findViewById(R.id.llDoctors);
        lldoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DoctorMainCategoriesActivity.this,FindDoctorActivity.class));
            }
        });

        LinearLayout  lldentist=(LinearLayout)findViewById(R.id.llDentist);
        lldentist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DoctorMainCategoriesActivity.this,FindDoctorActivity.class));
            }
        });

        LinearLayout llalternative=(LinearLayout)findViewById(R.id.llAlternative);
        llalternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DoctorMainCategoriesActivity.this,FindDoctorActivity.class));
            }
        });

        LinearLayout llther=(LinearLayout)findViewById(R.id.llther);
        llther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DoctorMainCategoriesActivity.this,FindDoctorActivity.class));
            }
        });

        */


        EditText editSearch=(EditText)findViewById(R.id.editSearch);
        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DoctorMainCategoriesActivity.this,LocationActivity.class);
                startActivity(intent);
            }
        });

        bindView();
        loadData();
    }
    public void bindView(){


        categoryRecyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new MainCategoryAdapter(this,categoryArray);
        categoryRecyclerView.setAdapter(categoryAdapter);

        categoryRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent  i=new Intent(DoctorMainCategoriesActivity.this,FindDoctorActivity.class);
                i.putExtra("id",categoryArray.get(position).getId());
                startActivity(i);

            }
        }));


    }

    public void loadData(){

        if(!categoryArray.isEmpty()){
            categoryArray.clear();
        }

        showPrgressBar();

        VJsonRequest vJsonRequest = new VJsonRequest(DoctorMainCategoriesActivity.this, ApiParams.MAIN_CATEGORY_LIST,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();
                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            if(jsonObject.getInt("responce")==1){


                                JSONArray data=jsonObject.getJSONArray("data");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<MainCatModel>>() {
                                }.getType();
                                categoryArray.clear();
                                categoryArray.addAll((Collection<? extends MainCatModel>) gson.fromJson(data.toString(), listType));
                                categoryAdapter.notifyDataSetChanged();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                    }
                });


    }


    public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ProductHolder> {

        ArrayList<MainCatModel> list;
        Activity activity;
        public MainCategoryAdapter( Activity activity, ArrayList<MainCatModel> list) {
            this.list = list;
            this.activity = activity;

        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_category_item,parent,false);

            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            final MainCatModel categoryModel = list.get(position);
           // String path = categoryModel.getImage();

            if(categoryModel.getCat_img()!=null){
                Picasso.with(activity).load(ConstValue.BASE_URL +  "uploads/admin/category/" + categoryModel.getCat_img()).into(holder.icon_image);
            }
            holder.lbl_title.setText(categoryModel.getCategory());


        }



        @Override
        public int getItemCount() {
            return list.size();
        }

        class ProductHolder extends RecyclerView.ViewHolder {
            ImageView icon_image;
            TextView lbl_title;
            public ProductHolder(View itemView) {
                super(itemView);
                icon_image = (ImageView) itemView.findViewById(R.id.img);
                lbl_title = (TextView) itemView.findViewById(R.id.catName);
            }
        }


    }


}
