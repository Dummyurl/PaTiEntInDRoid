package com.ziffytech.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.models.ReviewsModel;
import com.ziffytech.util.CommonClass;
import com.ziffytech.R;


/**
 * Created by LENOVO on 4/19/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ProductHolder> {
    private Activity context;
    private CommonClass common;
    private ArrayList<ReviewsModel> postItems;


    public ReviewsAdapter(Activity con, ArrayList<ReviewsModel> array){
            context = con;
            postItems = array;
        common = new CommonClass(con);
    }
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reviews,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final ReviewsModel categoryModel = postItems.get(position);
        String path = categoryModel.getUser_image();

        Picasso.with(context).load(ConstValue.BASE_URL + "/uploads/profile/"  +  path).placeholder(R.mipmap.ic_user_empty).into(holder.imageIcon);



        holder.textReview.setText(categoryModel.getReviews());
        holder.textUser.setText(categoryModel.getUser_fullname());
        holder.textDate.setText(categoryModel.getOn_date());
        holder.ratingBar1.setRating(Float.valueOf(categoryModel.getRatings()));
        holder.datetime.setText(common.printDifference2(categoryModel.getOn_date()));
    }



    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView textReview ;
        ImageView imageIcon ;
        TextView textUser ;
        TextView textDate ;
        RatingBar ratingBar1;
        TextView datetime;
        public ProductHolder(View itemView) {
            super(itemView);
            textReview = (TextView)itemView.findViewById(R.id.txtReview);
            imageIcon = (ImageView)itemView.findViewById(R.id.imgIcon);
            textUser = (TextView)itemView.findViewById(R.id.txtUser);
            textDate = (TextView)itemView.findViewById(R.id.txtDate);
            textUser.setTypeface(common.getCustomFont());
            ratingBar1 = (RatingBar)itemView.findViewById(R.id.ratingBar1);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
        }
    }



}
