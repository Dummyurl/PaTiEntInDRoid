package com.ziffytech.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziffytech.activities.BookActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.CategoryModel;
import com.ziffytech.R;


/**
 * Created by Light link on 04/07/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ProductHolder> {

    ArrayList<CategoryModel> list;
    Activity activity;
    public CategoryAdapter( Activity activity, ArrayList<CategoryModel> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final CategoryModel categoryModel = list.get(position);
        String path = categoryModel.getImage();

        Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/admin/category/" + path).into(holder.icon_image);

        holder.lbl_title.setText(categoryModel.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActiveModels.CATEGORY_MODEL = list.get(position);
                Intent intent = new Intent(activity,BookActivity.class);
                intent.putExtra("cat_id",ActiveModels.CATEGORY_MODEL.getId());
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
        public ProductHolder(View itemView) {
            super(itemView);
            icon_image = (ImageView) itemView.findViewById(R.id.imageView);
            lbl_title = (TextView) itemView.findViewById(R.id.title);
        }
    }


}
