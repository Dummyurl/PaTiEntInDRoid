package com.ziffytech.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziffytech.activities.LoginActivity;
import com.ziffytech.activities.ThanksActivity;
import com.ziffytech.activities.TimeSlotActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ziffytech.models.SlotsModel;
import com.ziffytech.util.CommonClass;
import com.ziffytech.R;


/**
 * Created by subhashsanghani on 1/16/17.
 */

public class TimeSlotFragment extends Fragment {
    ArrayList<SlotsModel> arrayList;

    CommonClass common;
    RecyclerView timeSlotRecyclerView;
    private String time_token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeslot, container, false);

        common = new CommonClass(getActivity());
        timeSlotRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        timeSlotRecyclerView.setLayoutManager(layoutManager);
        TimeAdapter adapter = new TimeAdapter();
        timeSlotRecyclerView.setAdapter(adapter);

            if (getArguments().getInt("position") == 0){
                arrayList = TimeSlotActivity.timeSlotModel.getMorning();
                time_token="1";
            }else if (getArguments().getInt("position") == 1){
                arrayList = TimeSlotActivity.timeSlotModel.getAfternoon();
                time_token="2";

            }else if (getArguments().getInt("position") == 2){
                arrayList = TimeSlotActivity.timeSlotModel.getEvening();
                time_token="3";

            }




        /*timeSlotRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SlotsModel chooseMap = arrayList.get(i);
                if (!chooseMap.getIs_booked()) {
                    Intent intent = new Intent(getActivity(), ServicesActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("slot", chooseMap);
                    b.putString("date", bundleE.getString("date"));
                    intent.putExtras(b);
                    startActivity(intent);

                }else{
                    common.setToastMessage(getString(R.string.already_booked));
                }
            }
        });
        */
        return rootView;
    }

    class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ProductHolder> {


        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_time_slot,parent,false);

            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            final SlotsModel slotsModel = arrayList.get(position);

            if (slotsModel.getIs_booked()){
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                holder.imgClock.setImageResource(R.drawable.ic_clock_white);
            }else {
                holder.itemView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                holder.imgClock.setImageResource(R.drawable.ic_clock_gray);
            }

            holder.timeslot.setText(parseTime(slotsModel.getSlot()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SlotsModel chooseMap = arrayList.get(position);
                    if (!chooseMap.getIs_booked()) {
                        if (common.is_user_login()){

                        /*    ActiveModels.SELECTED_SLOT = slotsModel;
                            Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
                            Bundle b = new Bundle();
                            b.putString("slot", slotsModel.getSlot());
                            b.putString("date", getArguments().getString("date"));
                            intent.putExtras(b);
                            getActivity().startActivity(intent);

                            */

                            Intent intent = null;
                            intent = new Intent(getActivity(), ThanksActivity.class);
                            intent.putExtra("date",getArguments().getString("date"));
                            intent.putExtra("timeslot",slotsModel.getSlot());
                            intent.putExtra("time_token",time_token);
                            startActivity(intent);


                        }else{
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivity(intent);
                        }
                    }else{
                        common.setToastMessage(getString(R.string.already_booked));
                    }
                }
            });
        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ProductHolder extends RecyclerView.ViewHolder {
            ImageView imgClock;
            TextView timeslot;
            public ProductHolder(View itemView) {
                super(itemView);
                imgClock = (ImageView)itemView.findViewById(R.id.clockimage);
                timeslot = (TextView) itemView.findViewById(R.id.timeslot);
            }
        }


    }
    public String parseTime(String time) {
        String inputPattern = "H:mm:ss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
