package com.ziffytech.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.models.DoctorModel;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by Mahesh on 11/01/18.
 */

public class MyDoctorAdapter extends RecyclerView.Adapter<MyDoctorAdapter.ProductHolder> implements Filterable {

    ArrayList<DoctorModel> list;
    Activity activity;
    String type;
    private ArrayList<DoctorModel> mFilteredList;

    public MyDoctorAdapter(Activity activity, ArrayList<DoctorModel> list,String type) {
        this.list = list;
        this.activity = activity;
        mFilteredList=list;
        this.type=type;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_doctors,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final DoctorModel categoryModel = mFilteredList.get(position);
        String path = categoryModel.getDoct_photo();

        Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/profile/" + path).into(holder.icon_image);

       // Log.e("Image",ConstValue.BASE_URL + "/uploads/profile/" + path);


        holder.lbl_title.setText(categoryModel.getDoct_name());
        holder.lbl_clinic_name.setText("Clinic Name:"+categoryModel.getBus_title());
        holder.lbl_speciality.setText(categoryModel.getDoct_speciality());
        holder.lbl_degree.setText(categoryModel.getDoct_degree()+","+categoryModel.getDoct_experience()+" year");


        if(type.equalsIgnoreCase("ask")){

            holder.book.setText("Select");
        }

    }



    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                Log.e("Text",charString);


                if (charString.isEmpty()) {

                    mFilteredList = list;

                } else {

                    ArrayList<DoctorModel> filteredList = new ArrayList<>();

                    for (DoctorModel model : list) {

                        if (model.getDoct_speciality().toLowerCase().contains(charString)) {

                            Log.e("At if",charString);

                            filteredList.add(model);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<DoctorModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView icon_image;
        TextView lbl_title;
        TextView lbl_degree;
        TextView lbl_clinic_name;
        TextView lbl_speciality;

        Button book;

        public ProductHolder(View itemView) {
            super(itemView);
            icon_image = (ImageView) itemView.findViewById(R.id.imageView);
            lbl_title = (TextView) itemView.findViewById(R.id.title);
            lbl_clinic_name = (TextView) itemView.findViewById(R.id.clinic);
            lbl_degree = (TextView) itemView.findViewById(R.id.degree);
            lbl_speciality=(TextView)itemView.findViewById(R.id.specilaity);


            book=(Button)itemView.findViewById(R.id.book);
        }
    }


}
