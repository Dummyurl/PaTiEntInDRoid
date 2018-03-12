package com.ziffytech.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.models.MedicationModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Mahesh on 13/01/18.
 */

public class MedicationActivity  extends CommonActivity {

    RecyclerView recyclerView;
    ArrayList<MedicationModel> arrayList;
    MedicationAdapter adapter;
    TextView notfoundtv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        setHeaderTitle("Medication");
        allowBack();
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MedicationActivity.this,MedicationAddActivity.class);
                startActivity(intent);
            }
        });

        notfoundtv=(TextView)findViewById(R.id.notfoundtv);
        notfoundtv.setVisibility(View.GONE);

        arrayList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MedicationAdapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!arrayList.isEmpty()){
            arrayList.clear();
        }
        getQuestions();
    }

    private void getQuestions() {

        if(!MyUtility.isConnected(this)) {
            MyUtility.showAlertMessage(this,MyUtility.INTERNET_ERROR);
            return;
        }

        showPrgressBar();


        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());


        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.GET_MEDICATION_ALL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        try {
                            JSONObject jsonObject=new JSONObject(responce);


                            if(jsonObject.getInt("responce")==1) {

                                notfoundtv.setVisibility(View.GONE);

                                JSONArray data = jsonObject.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {


                                    JSONObject jsonObject1 = data.getJSONObject(i);

                                    MedicationModel model = new MedicationModel();
                                    model.setId(jsonObject1.getString("id"));
                                    model.setImage(jsonObject1.getString("image"));
                                    model.setTitle(jsonObject1.getString("title"));
                                    model.setDesc(jsonObject1.getString("description"));
                                    model.setDate(jsonObject1.getString("created"));
                                    arrayList.add(model);


                                }

                                if (arrayList != null) {
                                    adapter.notifyDataSetChanged();
                                }

                            }else{

                                notfoundtv.setVisibility(View.VISIBLE);

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
                        notfoundtv.setVisibility(View.VISIBLE);

                        // common.setToastMessage(responce);
                    }
                });
    }


    public class MedicationAdapter  extends RecyclerView.Adapter<MedicationAdapter.MyAddHolder>{
        private ArrayList<MedicationModel> arrayList;
        private Context context;


        public MedicationAdapter(ArrayList<MedicationModel> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public MyAddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_medication, parent, false);
            return new MyAddHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(MyAddHolder holder, final int position) {


            holder.tvTitle.setText(arrayList.get(position).getTitle());
            holder.tvDesc.setText(arrayList.get(position).getDesc());
            holder.tvDate.setText(parseDateToddMM(arrayList.get(position).getDate().split(" ")[0]));

            Picasso.with(context).load(ConstValue.BASE_URL + "/uploads/report/" + arrayList.get(position).getImage()).into(holder.image);
            Log.e("Image",ConstValue.BASE_URL + "/uploads/report/" + arrayList.get(position).getImage());


            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(MedicationActivity.this,ActivityViewImager.class);
                    intent.putExtra("image",arrayList.get(position).getImage());
                    intent.putExtra("title",arrayList.get(position).getTitle());
                    startActivity(intent);
                }
            });

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MedicationActivity.this);
                    builder.setMessage("Are you sure you want to delete record?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                    //selected_index = position;
                                    deleteRow(position);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }


        public void deleteRow(final int position){
            HashMap<String,String> params = new HashMap<>();
            params.put("id",arrayList.get(position).getId());

            VJsonRequest vJsonRequest = new VJsonRequest(MedicationActivity.this, ApiParams.DELETE_MEDICATION,params,
                    new VJsonRequest.VJsonResponce(){
                        @Override
                        public void VResponce(String responce) {

                            arrayList.remove(position);
                            notifyDataSetChanged();
                        }
                        @Override
                        public void VError(String responce) {
                        }
                    });
        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        public  final class MyAddHolder extends RecyclerView.ViewHolder  {
            ImageView image,imgDelete;
            TextView tvTitle,tvDesc,tvDate;

            public MyAddHolder(View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.imageView);
                imgDelete=(ImageView) itemView.findViewById(R.id.delete);

                tvTitle=(TextView) itemView.findViewById(R.id.title);
                tvDesc=(TextView) itemView.findViewById(R.id.desc);
                tvDate=(TextView) itemView.findViewById(R.id.date);

            }

        }

        public String parseDateToddMM(String time) {
            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "dd-MMM-yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        }

    }

}
