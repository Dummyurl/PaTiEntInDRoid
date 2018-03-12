package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.ziffytech.Config.ApiParams;
import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.fragments.TimeSlotFragment;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.DoctorModel;
import com.ziffytech.models.TimeSlotModel;
import com.ziffytech.util.MyUtility;
import com.ziffytech.util.RoundedImageView;
import com.ziffytech.util.VJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class TimeSlotActivity extends CommonActivity implements DatePickerDialog.OnDateSetListener {
    DoctorModel selected_business;
    TextView textSalonName;
    ImageView imageLogo;
    TabLayout tabLayout;
    Calendar calender;
    private int c_day;
    private int c_month;
    private int c_year;
    SimpleDateFormat df, df2;
    String currentdate;
    Button buttonChooseDate;
    TextView buttonChooseDoctor;
    ViewPager viewPager;
    //HashMap<String,String> timeslotdata;
    SampleFragmentPagerAdapter sampleFragmentPagerAdapter;
    ArrayList<DoctorModel> mDoctorArray;
    private String days="";
    public static TimeSlotModel timeSlotModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);
        allowBack();
        setHeaderTitle("Book Appointment");
        mDoctorArray = new ArrayList<>();
        textSalonName = (TextView)findViewById(R.id.textSalonName);
        imageLogo = (ImageView)findViewById(R.id.salonImage);
        textSalonName.setTypeface(common.getCustomFont());

        selected_business = ActiveModels.DOCTOR_MODEL;
        Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/profile/" + selected_business.getBus_logo()).into(imageLogo);

        SimpleRatingBar ratingBar=(SimpleRatingBar)findViewById(R.id.rating);

        if(!selected_business.getRating().equalsIgnoreCase("") && !selected_business.getRating().equalsIgnoreCase("null") && selected_business.getRating()!=null){
            ratingBar.setRating(Float.parseFloat(selected_business.getRating()));
        }else{
            ratingBar.setRating(0);
        }


        textSalonName.setText(Html.fromHtml(selected_business.getBus_title()));

        TextView lbl_degree = (TextView)findViewById(R.id.degree);
        TextView lbl_speciality=(TextView)findViewById(R.id.specilaity);
        lbl_speciality.setText(selected_business.getDoct_speciality());
        lbl_degree.setText(selected_business.getDoct_degree()+","+selected_business.getDoct_experience()+" year");

        TextView charges=(TextView)findViewById(R.id.charges);
        charges.setText("Rs: "+selected_business.getConsult_fee());


        RoundedImageView doctImage=(RoundedImageView)findViewById(R.id.doctImage);
        Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/profile/" + selected_business.getDoct_photo()).into(doctImage);


        calender = Calendar.getInstance(TimeZone.getDefault());





        c_day = calender.get(Calendar.DAY_OF_MONTH);
        c_month = calender.get(Calendar.MONTH) + 1;
        c_year = calender.get(Calendar.YEAR);
        //df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        df2 = new SimpleDateFormat("HH:mm aa", Locale.ENGLISH);
        currentdate = c_year +"-"+ c_month+"-"+c_day;
        //df.format(calender.getTime());

        buttonChooseDate = (Button)findViewById(R.id.buttonChooseDate);
        buttonChooseDate.setText(currentdate);

        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });
        buttonChooseDoctor = (TextView) findViewById(R.id.buttonChooseDoctor);
        buttonChooseDoctor.setText(selected_business.getDoct_name());

        TextView tvAddress=(TextView)findViewById(R.id.address);
        tvAddress.setText(selected_business.getBus_google_street());


        TextView tvDirection=(TextView)findViewById(R.id.direction);
        tvDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TimeSlotActivity.this,MapActivity.class));
            }
        });


        // loadDoctorData();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        TextView txtTotalTime = (TextView)findViewById(R.id.totalTime);
        txtTotalTime.setText(selected_business.getStart_con_time());

        TextView txtTotalAmount = (TextView)findViewById(R.id.totalAmount);
        txtTotalAmount.setText(selected_business.getEnd_con_time());

        TextView days = (TextView)findViewById(R.id.days);
        days.setText(selected_business.getWorking_day());

        TextView time = (TextView)findViewById(R.id.time);
        time.setText(selected_business.getStart_con_time());

        loadWorkingDays();

    }

    public void loadWorkingDays(){



        Log.e("APICalled",currentdate);

        showPrgressBar();

        HashMap<String,String> params = new HashMap<>();
        params.put("doct_id",selected_business.getDoct_id());



        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.DOCTOR_DAYS_URL,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {
                        hideProgressBar();

                        loadSlotTask();


                        try {
                            JSONObject jsonObject=new JSONObject(responce);

                            if(jsonObject.getInt("responce")==1){

                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject obj=jsonArray.getJSONObject(i);

                                    days=obj.getString("working_days");
                                }



                            }else{
                                MyUtility.showToast("No time slot available.",TimeSlotActivity.this);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                        //progressBar1.setVisibility(View.GONE);

                        loadSlotTask();
                    }
                });

    }

    public void loadSlotTask(){

        Log.e("APICalled",currentdate);

        showPrgressBar();

        buttonChooseDoctor.setText(selected_business.getDoct_name());
        HashMap<String,String> params = new HashMap<>();
      //  params.put("bus_id",selected_business.getBus_id());
        params.put("bus_id",selected_business.getBus_id());
        params.put("doct_id",selected_business.getDoct_id());
        params.put("date",currentdate);



        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.TIME_SLOT_URL,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {


                            hideProgressBar();


                            try {
                                JSONObject jsonObject=new JSONObject(responce);
                                JSONObject data=jsonObject.getJSONObject("data");

                              //  JSONArray array=data.getJSONArray("morning");

                                Object json = new JSONTokener(data.toString()).nextValue();
                            if (json instanceof JSONObject) {



                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<TimeSlotModel>() {
                                    }.getType();
                                    timeSlotModel = gson.fromJson(data.toString(), listType);

                                    sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
                                    viewPager.setAdapter(sampleFragmentPagerAdapter);
                                    tabLayout.setupWithViewPager(viewPager);
                                    sampleFragmentPagerAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(getApplicationContext(), getString(R.string.not_timetable_set), Toast.LENGTH_SHORT).show();
                            }

                            } catch (JSONException e) {
                                hideProgressBar();
                                e.printStackTrace();
                                MyUtility.showAlertMessage(TimeSlotActivity.this,"Time slot not available.");
                            }

                    }
                    @Override
                    public void VError(String responce) {
                        hideProgressBar();
                        //progressBar1.setVisibility(View.GONE);
                    }
                });

    }

    public void DateDialog(){




        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                TimeSlotActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.setMinDate(now);
        now.add(Calendar.MONTH,+1);
        dpd.setMaxDate(now);

        if(!days.equalsIgnoreCase("")){

            Calendar sunday,mon,tue,wed,thu,fri,sat;

            List<Calendar> weekends = new ArrayList<>();



           /* for (int i = 0; i < 30 ; i++) {

                sunday = Calendar.getInstance();


                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                Log.e("Date",sdf.format(sunday.getTime()));

                //Log.e("Day", sunday.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US));
                //Log.e("Days",days);

                if (days.contains("sun")) {

                    Log.e("AT","not");

                    if (sunday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        Log.e("AT","matches");
                        weekends.add(sunday);
                    }
                }

                sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK)+    i));


              /*  if(!days.contains("mon")){
                    if (sunday.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        weekends.add(sunday);
                    }
                }

                if(!days.contains("tue")){
                    if (sunday.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        weekends.add(sunday);
                    }
                }

                if(!days.contains("wed")){
                    if (sunday.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                        weekends.add(sunday);
                    }
                }

                if(!days.contains("thu")){
                    if (sunday.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        weekends.add(sunday);
                    }
                }

                if(!days.contains("fri")){
                    if (sunday.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        weekends.add(sunday);
                    }
                }

                if(!days.contains("sat")){
                    if (sunday.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        weekends.add(sunday);
                    }
                }

                sunday.add(Calendar.DAY_OF_WEEK,1);

            }  */


           int weeks =6;

           for (int i = 0; i < (weeks * 7) ; i=i+7) {


                if(!days.contains("sun")){
                     sunday = Calendar.getInstance();
                     sunday.add(Calendar.DATE,-10);
                     sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK)+7+i));
                     weekends.add(sunday);
                }

                if(!days.contains("mon")){
                    mon = Calendar.getInstance();
                    mon.add(Calendar.DATE,-10);
                    mon.add(Calendar.DAY_OF_YEAR, (Calendar.MONDAY - mon.get(Calendar.DAY_OF_WEEK) +7+i));
                    weekends.add(mon);
                }

                if(!days.contains("tue")){
                    tue = Calendar.getInstance();
                    tue.add(Calendar.DATE,-10);
                    tue.add(Calendar.DAY_OF_YEAR, (Calendar.TUESDAY - tue.get(Calendar.DAY_OF_WEEK) +7+ i));
                    weekends.add(tue);
                }

                if(!days.contains("wed")){
                    wed = Calendar.getInstance();
                    wed.add(Calendar.DATE,-10);
                    wed.add(Calendar.DAY_OF_YEAR, (Calendar.WEDNESDAY - wed.get(Calendar.DAY_OF_WEEK) +7+ i));
                    weekends.add(wed);
                }

                if(!days.contains("thu")){
                    thu = Calendar.getInstance();
                    thu.add(Calendar.DATE,-10);
                    thu.add(Calendar.DAY_OF_YEAR, (Calendar.THURSDAY - thu.get(Calendar.DAY_OF_WEEK)  +7+ i));
                    weekends.add(thu);
                }

                if(!days.contains("fri")){
                    fri = Calendar.getInstance();
                    fri.add(Calendar.DATE,-10);
                    fri.add(Calendar.DAY_OF_YEAR, (Calendar.FRIDAY - fri.get(Calendar.DAY_OF_WEEK) + 7 + i));
                    weekends.add(fri);
                }

                if(!days.contains("sat")){
                    sat = Calendar.getInstance();
                    sat.add(Calendar.DATE,-10);
                    sat.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - sat.get(Calendar.DAY_OF_WEEK) + 7 + i));
                    weekends.add(sat);
                }



            }

            Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);
            dpd.setDisabledDays(disabledDays);


            }


        dpd.show(getFragmentManager(), "Datepickerdialog");


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        currentdate =year +"-"+ (monthOfYear+1)+"-"+dayOfMonth;
        c_year = year;
        c_day = dayOfMonth;
        c_month = monthOfYear+1;
        buttonChooseDate.setText(currentdate);
        loadSlotTask();
    }

    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { getString(R.string.tab_morning), getString(R.string.tab_afternoon), getString(R.string.tab_evening) };

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TimeSlotFragment();
            Bundle args = new Bundle();
            args.putString("date",currentdate);
            args.putInt("position",position);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabTitles[position];
        }
    }


    public static List<Calendar> saturdaysundaycount(Date d1, Date d2) {

        List<Calendar> list=new ArrayList<>();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        int sundays = 0;
        int saturday = 0;

        while (! c1.after(c2)) {

            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ){

                saturday++;

                list.add(c1);
            }

            if(c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                sundays++;

                list.add(c1);

            }

            c1.add(Calendar.DATE, 1);
        }

        //System.out.println("Saturday Count = "+saturday);
        //System.out.println("Sunday Count = "+sundays);
        return list;
    }
}
