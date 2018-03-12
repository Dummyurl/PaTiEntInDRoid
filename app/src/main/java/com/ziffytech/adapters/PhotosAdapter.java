package com.ziffytech.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.models.PhotosModel;
import com.ziffytech.util.CommonClass;

import java.util.ArrayList;
import com.ziffytech.R;


/**
 * Created by LENOVO on 4/19/2016.
 */
public class PhotosAdapter  extends RecyclerView.Adapter<PhotosAdapter.ProductHolder> {
    private Activity activity;
    private CommonClass common;
    private ArrayList<PhotosModel> postItems;

    public PhotosAdapter(Activity con, ArrayList<PhotosModel> array){
        activity = con;
        postItems = array;
        common = new CommonClass(con);




    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_photos,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final PhotosModel categoryModel = postItems.get(position);
        String path = categoryModel.getPhoto_image();

        Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/business/businessphoto/" +  path).into(holder.imageView);

        holder.textName.setText(categoryModel.getPhoto_title());


    }



    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textName;

        public ProductHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textName = (TextView) itemView.findViewById(R.id.txtTitle);

        }
    }





}
