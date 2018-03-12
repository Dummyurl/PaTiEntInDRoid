package com.ziffytech.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.activities.BookActivity;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.CategoryModel;
import com.ziffytech.remainder.Reminder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Light link on 04/07/2016.
 */
public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.ProductHolder> {

    List<Reminder> list;
    Activity activity;
    public RemainderAdapter(Activity activity, List<Reminder> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_items,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final Reminder categoryModel = list.get(position);


        String letter = "A";

        if(categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
            letter = categoryModel.getTitle().substring(0, 1);
            ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
            int color = mColorGenerator.getRandomColor();
            TextDrawable mDrawableBuilder = TextDrawable.builder()
                    .buildRound(letter, color);
            holder.mThumbnailImage.setImageDrawable(mDrawableBuilder);
        }

        holder.mTitleText.setText(categoryModel.getTitle());
        holder.mMedicineName.setText(categoryModel.getmMedicine());
        String doseType = "<b>" + "Doses:" + "</b> " +categoryModel.getmDoseType() +"("+categoryModel.getmDoseRepeatType()+")";
        holder.mDoseType.setText(Html.fromHtml(doseType));
        String date = "<b>" +"Date:" + "</b> "+categoryModel.getDate() +" To "+categoryModel.getmEndDate();
        holder.mDateAndTimeText.setText(Html.fromHtml(date));
        holder.mActiveImage.setImageResource(R.drawable.ic_notifications_on_white_24dp);
        String time = "<b>" + "Time:" + "</b>"+categoryModel.getTime();
        holder.recycle_time.setText(Html.fromHtml(time));

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        private TextView mTitleText,mMedicineName,mDoseType, mDateAndTimeText,recycle_time;
        private ImageView mActiveImage , mThumbnailImage;


        public ProductHolder(View itemView) {
            super(itemView);

            mTitleText = (TextView) itemView.findViewById(R.id.recycle_title);
            mMedicineName = (TextView) itemView.findViewById(R.id.med_names);
            mDoseType = (TextView) itemView.findViewById(R.id.dose_type);
            mDateAndTimeText = (TextView) itemView.findViewById(R.id.recycle_date_time);
            mActiveImage = (ImageView) itemView.findViewById(R.id.active_image);
            mThumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);

            recycle_time=(TextView) itemView.findViewById(R.id.recycle_time);
        }
    }


}
