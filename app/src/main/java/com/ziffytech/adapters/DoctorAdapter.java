package com.ziffytech.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.activities.TimeSlotActivity;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.DoctorModel;

import java.util.ArrayList;
import com.ziffytech.R;



/**
 * Created by Light link on 04/07/2016.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ProductHolder> {

    ArrayList<DoctorModel> list;
    Activity activity;
    public DoctorAdapter(Activity activity, ArrayList<DoctorModel> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_doctors,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final DoctorModel categoryModel = list.get(position);
        String path = categoryModel.getDoct_photo();

        Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/profile/" + path).into(holder.icon_image);

        Log.e("Image",ConstValue.BASE_URL + "/uploads/profile/" + path);

        holder.lbl_title.setText(categoryModel.getDoct_name());
        holder.lbl_degree.setText(categoryModel.getDoct_degree()+","+categoryModel.getDoct_experience()+" year");
        holder.lbl_clinic_name.setText(categoryModel.getBus_title());
        holder.lbl_speciality.setText(categoryModel.getDoct_speciality());
        holder.charges.setText("Charges: Rs "+categoryModel.getConsult_fee());

        if(!categoryModel.getRating().equalsIgnoreCase("") && !categoryModel.getRating().equalsIgnoreCase("null") && categoryModel.getRating()!=null){
            holder.rating.setRating(Float.parseFloat(categoryModel.getRating()));
        }else{
            holder.rating.setRating(0);
        }



        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActiveModels.DOCTOR_MODEL = categoryModel;
                Intent intent = new Intent(activity, TimeSlotActivity.class);
                activity.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView icon_image;
        TextView lbl_title;
        TextView lbl_degree;
        TextView lbl_speciality;
        TextView lbl_clinic_name;
        TextView charges;
        Button book;
        SimpleRatingBar rating;

        public ProductHolder(View itemView) {
            super(itemView);
            icon_image = (ImageView) itemView.findViewById(R.id.imageView);
            lbl_title = (TextView) itemView.findViewById(R.id.title);
            lbl_degree = (TextView) itemView.findViewById(R.id.degree);
            lbl_clinic_name = (TextView) itemView.findViewById(R.id.clinic);
            lbl_speciality=(TextView)itemView.findViewById(R.id.specilaity);

            rating=(SimpleRatingBar)itemView.findViewById(R.id.rating);

            charges=(TextView)itemView.findViewById(R.id.charges);

            book=(Button)itemView.findViewById(R.id.book);
        }
    }


}
