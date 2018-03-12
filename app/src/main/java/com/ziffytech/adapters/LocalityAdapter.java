package com.ziffytech.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziffytech.activities.BookActivity;
import com.ziffytech.models.DoctorSearchModel;

import java.util.ArrayList;
import com.ziffytech.R;



/**
 * Created by Light link on 04/07/2016.
 */
public class LocalityAdapter extends RecyclerView.Adapter<LocalityAdapter.ProductHolder> {

    ArrayList<DoctorSearchModel> list;
    Activity activity;
    public LocalityAdapter(Activity activity, ArrayList<DoctorSearchModel> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_locality,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final DoctorSearchModel categoryModel = list.get(position);

        holder.lbl_title.setText(categoryModel.getDoct_name() +" ( "+categoryModel.getBus_title()+" )");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, BookActivity.class);
                intent.putExtra("name",categoryModel.getDoct_name());
                intent.putExtra("id",categoryModel.getDoct_id());
                intent.putExtra("email",categoryModel.getDoct_email());
                activity.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView lbl_title;
        public ProductHolder(View itemView) {
            super(itemView);
            lbl_title = (TextView) itemView.findViewById(R.id.title);
        }
    }


}
