package com.ziffytech.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziffytech.models.PatientModel;

import java.util.ArrayList;
import com.ziffytech.R;



/**
 * Created by Light link on 04/07/2016.
 */
public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ProductHolder> {

    ArrayList<PatientModel> list;
    Activity activity;
    public PatientAdapter(Activity activity, ArrayList<PatientModel> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_doctors,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final PatientModel categoryModel = list.get(position);
     /*   String path = categoryModel.getDoct_photo();

        Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/business/" + path).into(holder.icon_image);
*/
        holder.lbl_title.setText(categoryModel.getPatientName());
        holder.lbl_degree.setText(categoryModel.getClinic());

      /*  holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ActiveModels.LIST_SERVICES_MODEL = PriceFragment.mServiceArray;
                Intent intent = new Intent(activity, TimeSlotActivity.class);
                activity.startActivity(intent);
            }
        });*/

    }



    @Override
    public int getItemCount() {

        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView icon_image;
        TextView lbl_title;
        TextView lbl_degree;
        Button book;
        public ProductHolder(View itemView) {
            super(itemView);
           // icon_image = (ImageView) itemView.findViewById(R.id.imageView);
            lbl_title = (TextView) itemView.findViewById(R.id.title);
            lbl_degree = (TextView) itemView.findViewById(R.id.degree);
          // book=(Button)itemView.findViewById(R.id.book);
        }
    }


}
