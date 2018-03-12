package com.ziffytech.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import com.ziffytech.models.MemberModel;
import com.ziffytech.R;


/**
 * Created by admn on 20/11/2017.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyAddHolder>{
    private ArrayList<MemberModel> arrayList;
    private Context context;
    private String type;


    public MemberAdapter(ArrayList<MemberModel> arrayList, Context context,String type) {
        this.arrayList = arrayList;
        this.context = context;
        this.type=type;
    }

    @Override
    public MyAddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_list, parent, false);
        return new MyAddHolder(layoutView, context, arrayList);
    }

    @Override
    public void onBindViewHolder(MyAddHolder holder, int position) {

        holder.tv_card_add_name.setText(arrayList.get(position).getM_name());
        holder.tv_card_contact_no.setText("Contact Number:"+arrayList.get(position).getAlternate_number());
        holder.relation.setText("Relation:"+arrayList.get(position).getRelation());

        holder.age.setText("Age:"+arrayList.get(position).getAge());

        Log.e("Gender",arrayList.get(position).getM_gender());

        if(arrayList.get(position).getM_gender().equalsIgnoreCase("0")){
            holder.gender.setText("Gender:"+" Male");
        }else if(arrayList.get(position).getM_gender().equalsIgnoreCase("1")){
            holder.gender.setText("Gender:"+" Female");
        }

        holder.bday.setText("Birth-date:"+arrayList.get(position).getBirth_date());

        if(type.equalsIgnoreCase("result")){

            holder.imageView.setVisibility(View.GONE);
            holder.submit.setVisibility(View.VISIBLE);

        }else{

            holder.submit.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);


        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static final class MyAddHolder extends RecyclerView.ViewHolder  {


        TextView tv_card_add_name, tv_card_contact_no,relation, age, bday,gender;
        ImageView imageView;
        ArrayList<MemberModel> arrayList;
        Context context;
        RelativeLayout layout;
        Button submit;

        public MyAddHolder(View itemView, Context context, ArrayList<MemberModel> arrayList) {
            super(itemView);

            this.arrayList = arrayList;
            this.context = context;

            layout=(RelativeLayout)itemView.findViewById(R.id.parent);
            tv_card_add_name = (TextView) itemView.findViewById(R.id.name);
            tv_card_contact_no = (TextView) itemView.findViewById(R.id.contact);
            relation = (TextView) itemView.findViewById(R.id.relation);
            age = (TextView) itemView.findViewById(R.id.age);
            bday = (TextView) itemView.findViewById(R.id.bday);
            gender = (TextView) itemView.findViewById(R.id.gender);
            submit = (Button) itemView.findViewById(R.id.btnSubmit);
            imageView = (ImageView) itemView.findViewById(R.id.delete);


        }



    }

}
