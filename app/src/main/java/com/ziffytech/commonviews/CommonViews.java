package com.ziffytech.commonviews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;


/**
 * Created by subhashsanghani on 10/18/16.
 */
public class CommonViews {
    public View get_common_view(int view_id, Activity context, HashMap<String,String> map){
        LayoutInflater inflater;
        switch (view_id){
            case R.layout.row_category: {
                inflater = LayoutInflater.from(context);
                View convertView= inflater.inflate(R.layout.row_category, null);
                TextView textName = (TextView)convertView.findViewById(R.id.title);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
                textName.setText(map.get("title"));
                Picasso.with(context).load(ConstValue.BASE_URL + "/uploads/admin/category/" + map.get("image")).into(imageView);
                return convertView;
            }
            case R.layout.row_business_list: {
                inflater = LayoutInflater.from(context);
                View convertView= inflater.inflate(R.layout.row_category, null);
                TextView textName = (TextView)convertView.findViewById(R.id.title);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
                textName.setText(map.get("title"));
                Picasso.with(context).load(ConstValue.BASE_URL + "/uploads/admin/category/" + map.get("image")).into(imageView);
                return convertView;
            }
        }
        return null;
    }
}
