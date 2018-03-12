package com.ziffytech.dialogues;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.R;



/**
 * Created by Dell on 21-11-2016.
 */

public class DoctorInfoDialog extends Dialog {

    private Activity mContext;
    private SharedPreferences sharedPreferences;
    DoctorModel doctorModel;

    public DoctorInfoDialog(final Activity context, DoctorModel arrayList) {
        super(context);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.dialog_doctor_info);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        this.mContext = context;
        doctorModel = arrayList;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        } catch (Exception ex) {

        }

        TextView lbl_title = (TextView) this.findViewById(R.id.title) ;
        TextView lbl_degree = (TextView) this.findViewById(R.id.degree) ;
        TextView lbl_speciality = (TextView) this.findViewById(R.id.speciality) ;
        TextView lbl_about = (TextView) this.findViewById(R.id.about) ;
        TextView lbl_email = (TextView) this.findViewById(R.id.email) ;
        TextView lbl_phone = (TextView) this.findViewById(R.id.phone) ;
        ImageView icon_image = (ImageView) this.findViewById(R.id.imageView);
        String path = doctorModel.getDoct_photo();

        Picasso.with(context).load(ConstValue.BASE_URL + "/uploads/business/" + path).into(icon_image);

        lbl_title.setText(doctorModel.getDoct_name());
        lbl_degree.setText(doctorModel.getDoct_degree());
        lbl_speciality.setText(doctorModel.getDoct_speciality());
        lbl_email.setText(doctorModel.getDoct_email());
        lbl_phone.setText(doctorModel.getDoct_phone());
        lbl_about.setText(Html.fromHtml(doctorModel.getDoct_about()));
    }


    public void dismiss() {
        super.dismiss();
    }

}
