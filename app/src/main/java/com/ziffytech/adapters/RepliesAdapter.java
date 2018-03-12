package com.ziffytech.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziffytech.R;
import com.ziffytech.models.QuestionModel;

import java.util.ArrayList;

/**
 * Created by Mahesh on 09/01/18.
 */

public class RepliesAdapter  extends RecyclerView.Adapter<RepliesAdapter.MyAddHolder>{
    private ArrayList<QuestionModel> arrayList;
    private Context context;


    public RepliesAdapter(ArrayList<QuestionModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
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
        holder.desc.setText(arrayList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static final class MyAddHolder extends RecyclerView.ViewHolder  {
        TextView subject,desc;


        public MyAddHolder(View itemView) {
            super(itemView);

            subject = (TextView) itemView.findViewById(R.id.subject);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }

    }

}
