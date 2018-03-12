package com.ziffytech.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.models.MemberModel;
import com.ziffytech.models.QuestionModel;

import java.util.ArrayList;

/**
 * Created by Mahesh on 09/01/18.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyAddHolder>{
    private ArrayList<QuestionModel> arrayList;
    private Context context;
    private String type;


    public QuestionAdapter(ArrayList<QuestionModel> arrayList, Context context, String type) {
        this.arrayList = arrayList;
        this.context = context;
        this.type=type;
    }

    @Override
    public MyAddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);
        return new MyAddHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(MyAddHolder holder, int position) {

        holder.subject.setText(arrayList.get(position).getSubject());
        holder.desc.setText("Description: "+arrayList.get(position).getDescription());
        holder.name.setText("Doctor: "+arrayList.get(position).getDoctorName());
        holder.date.setText(arrayList.get(position).getCreated());

        if(arrayList.get(position).getFlag().equalsIgnoreCase("0")){

            // 0 means  new msg

            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.litegrey));

        }else{

            // 1 means no  new msg
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.white));

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static final class MyAddHolder extends RecyclerView.ViewHolder  {
        TextView subject,desc,date,name;
        RelativeLayout parent;


        public MyAddHolder(View itemView) {
            super(itemView);

            subject = (TextView) itemView.findViewById(R.id.subject);
            desc = (TextView) itemView.findViewById(R.id.desc);
            name = (TextView) itemView.findViewById(R.id.doctorName);
            date = (TextView) itemView.findViewById(R.id.date);

            parent=(RelativeLayout)itemView.findViewById(R.id.parent);


        }

    }

}
