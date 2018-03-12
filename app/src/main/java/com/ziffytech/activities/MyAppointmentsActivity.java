package com.ziffytech.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.models.AppointmentModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.RoundedImageView;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MyAppointmentsActivity extends CommonActivity {
    ArrayList<AppointmentModel> appointmentArray;
    AppointmentAdapter adapter;
    AlertDialog alertDialog;
    TextView notfoundtv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        appointmentArray = new ArrayList<>();

        notfoundtv=(TextView)findViewById(R.id.notfoundtv);
        notfoundtv.setVisibility(View.GONE);

        allowBack();
        setHeaderTitle(getString(R.string.my_appointments));

        ListView listView = (ListView)findViewById(R.id.listview);
        adapter = new AppointmentAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 deleteConfirm(position);
            }
        });

        showPrgressBar();

        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",common.get_user_id());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.MYAPPOINTMENTS_URL,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        hideProgressBar();

                        JSONObject userdata = null;
                        try {
                            userdata = new JSONObject(responce);


                            if(userdata.getBoolean("responce")){

                                JSONArray data=userdata.getJSONArray("data");

                                if(data.length()>0){

                                    notfoundtv.setVisibility(View.GONE);


                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<AppointmentModel>>() {
                                    }.getType();
                                    appointmentArray.clear();
                                    appointmentArray.addAll((Collection<? extends AppointmentModel>) gson.fromJson(data.toString(), listType));
                                    adapter.notifyDataSetChanged();

                                }else{

                                    notfoundtv.setVisibility(View.VISIBLE);

                                }


                            }else{
                                MyUtility.showAlertMessage(MyAppointmentsActivity.this,"Failed to retrieve appointment");
                            }

                        } catch (JSONException e) {
                            hideProgressBar();
                            notfoundtv.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                       // common.setToastMessage(responce);
                        notfoundtv.setVisibility(View.VISIBLE);

                    }
                });
    }


    public void deleteConfirm(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cancel_confirmation)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        //selected_index = position;
                        deleteRow(position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void deleteRow(final int position){
        AppointmentModel map = appointmentArray.get(position);
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",common.get_user_id());
        params.put("app_id",map.getId());

        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.CANCELAPPOINTMENTS_URL,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {

                        appointmentArray.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });
    }




    class AppointmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (appointmentArray==null)
                return 0;
            else
                return appointmentArray.size();
        }

        @Override
        public AppointmentModel getItem(int i) {
            return appointmentArray.get(i);

        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.row_appointment, null);
            }
            AppointmentModel appointment = appointmentArray.get(i);

            TextView txtDate = (TextView)view.findViewById(R.id.txtDate);
            TextView txtTime = (TextView)view.findViewById(R.id.txtTime);
            TextView txtName = (TextView)view.findViewById(R.id.txtName);
            TextView txtPhone = (TextView)view.findViewById(R.id.txtPhone);
            TextView payment = (TextView)view.findViewById(R.id.payment);
            TextView txtClinic = (TextView)view.findViewById(R.id.txtClinic);
            TextView txtClinicAddress = (TextView)view.findViewById(R.id.txtClincAddress);
            TextView txtClinicPhone = (TextView)view.findViewById(R.id.txtClincPhone);

            Button btnRate = (Button)view.findViewById(R.id.rate);
            Button btnCancel = (Button)view.findViewById(R.id.cancel);
            Button btnSchedule = (Button)view.findViewById(R.id.reschedule);
            btnSchedule.setVisibility(View.GONE);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteConfirm(i);
                }
            });

            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showRateDialog(i);

                }
            });

            if(isExpire(appointment.getAppointment_date())){

                btnRate.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }else{


                btnRate.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }




            txtDate.setText(parseDateToddMM(appointment.getAppointment_date()));
            txtTime.setText(parseTime(appointment.getStart_time()));
            txtName.setText(appointment.getApp_name());
            txtPhone.setText(appointment.getApp_phone());

            txtClinic.setText(appointment.getBus_title());
            txtClinicAddress.setText(appointment.getDoct_name());
            txtClinicPhone.setText(appointment.getDoct_degree());

            return view;
        }


        private void showRateDialog(int i){

            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.dialog_rate_doctor, null);
            final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(MyAppointmentsActivity.this);
            dialog.setView(alertLayout);

            TextView tvName=(TextView)alertLayout.findViewById(R.id.doctorName);
            final SimpleRatingBar rating=(SimpleRatingBar)alertLayout.findViewById(R.id.rating);



            Button btnRate=(Button)alertLayout.findViewById(R.id.btnRate);
            RoundedImageView image=(RoundedImageView)alertLayout.findViewById(R.id.doctorImage);
            final AppointmentModel map = appointmentArray.get(i);
            Picasso.with(MyAppointmentsActivity.this).load(ConstValue.BASE_URL + "/uploads/profile/" + map.getDoct_photo()).into(image);
            tvName.setText(map.getDoct_name());
            final android.support.v7.app.AlertDialog alert=dialog.create();

            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(rating.getRating() > 0){

                        HashMap<String,String> params = new HashMap<>();
                        params.put("user_id",common.get_user_id());
                        params.put("doct_id",map.getDoct_id());
                        params.put("ratings",rating.getRating()+"");

                        VJsonRequest vJsonRequest = new VJsonRequest(MyAppointmentsActivity.this, ApiParams.RATE_DOCTOR,params,
                                new VJsonRequest.VJsonResponce(){
                                    @Override
                                    public void VResponce(String responce) {

                                        alert.dismiss();
                                        MyUtility.showToast("Rated Successfully!",MyAppointmentsActivity.this);

                                    }
                                    @Override
                                    public void VError(String responce) {
                                        common.setToastMessage(responce);
                                    }
                                });

                    }else{

                        MyUtility.showToast("Give rating first",MyAppointmentsActivity.this);
                    }
                }
            });



            alert.show();
        }
    }
    public String parseDateToddMM(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
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
    public String parseTime(String time) {
        String inputPattern = "HH:mm:ss";
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog!=null){
            alertDialog.dismiss();
            alertDialog = null;
        }
    }



    private boolean isExpire(String date){
        if(date.isEmpty() || date.trim().equals("")){
            return false;
        }else{
            SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-dd-MM"); // Jan-20-2015 1:30:55 PM
            Date d=null;
            Date d1=null;
            String today=getToday("yyyy-dd-MM");
            try {
                //System.out.println("expdate>> "+date);
                //System.out.println("today>> "+today+"\n\n");
                d = sdf.parse(date);
                d1 = sdf.parse(today);
                if(d1.compareTo(d) <0){// not expired
                    return false;
                }else if(d.compareTo(d1)==0){// both date are same
                    if(d.getTime() < d1.getTime()){// not expired
                        return false;
                    }else if(d.getTime() == d1.getTime()){//expired
                        return true;
                    }else{//expired
                        return true;
                    }
                }else{//expired
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    public static String getToday(String format){
        Date date = new Date();
        return new SimpleDateFormat(format).format(date);
    }
}
