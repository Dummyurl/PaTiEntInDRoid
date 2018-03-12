package com.ziffytech.dialogues;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.ziffytech.activities.TimeSlotActivity;

import java.util.ArrayList;

import com.ziffytech.adapters.DoctorAdapter;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.util.RecyclerItemClickListener;
import com.ziffytech.R;



/**
 * Created by Dell on 21-11-2016.
 */

public class DoctorChooseDialog extends Dialog {

    private Activity mContext;
    private SharedPreferences sharedPreferences;
    ArrayList<DoctorModel> mDoctorArray;
    DoctorAdapter doctorAdapter;
    RecyclerView recyclerView;
    public DoctorChooseDialog(final Activity context, ArrayList<DoctorModel> arrayList) {
        super(context);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.dialog_doctors);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        this.mContext = context;
        mDoctorArray = arrayList;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        } catch (Exception ex) {

        }

        recyclerView = (RecyclerView) this.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                ActiveModels.DOCTOR_MODEL = mDoctorArray.get(position);
                if (mContext instanceof TimeSlotActivity){
                    ((TimeSlotActivity) mContext).loadSlotTask();
                }
                dismiss();
            }
        }));
        doctorAdapter = new DoctorAdapter(context,mDoctorArray);
        recyclerView.setAdapter(doctorAdapter);
    }


    public void dismiss() {
        super.dismiss();
    }

}
