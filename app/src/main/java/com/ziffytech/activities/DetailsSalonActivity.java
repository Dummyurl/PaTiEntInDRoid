package com.ziffytech.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.ziffytech.Config.ConstValue;
import com.ziffytech.R;
import com.ziffytech.fragments.DetailsFragment;
import com.ziffytech.fragments.DoctorsFragment;
import com.ziffytech.fragments.PhotosFragment;
import com.ziffytech.fragments.PriceFragment;
import com.ziffytech.fragments.ReviewsFragment;
import com.ziffytech.models.ActiveModels;
import com.ziffytech.models.BusinessModel;
import com.ziffytech.util.CommonClass;

public class DetailsSalonActivity extends CommonActivity {

    BusinessModel selected_business;
    TextView txtTotalTime, txtTotalAmount, txtServiceCharges;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_salon);
        common = new CommonClass(this);
        ActiveModels.LIST_SERVICES_MODEL = null;
        Typeface font = Typeface.createFromAsset(getAssets(), "LobsterTwo-BoldItalic.ttf");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        allowBack();
        ImageView bannerImage = (ImageView)findViewById(R.id.bannerImage);
        TextView textName = (TextView)findViewById(R.id.title);
        textName.setTypeface(font);

        RatingBar ratingbar = (RatingBar)findViewById(R.id.ratingBar1);
        ratingbar.setRating(0);

//        RoundedImageView logoImage = (RoundedImageView)findViewById(R.id.logoImage);
        selected_business = ActiveModels.BUSINESS_MODEL;




        //ActionBar mActionBar = getSupportActionBar();
        //mActionBar.setDisplayShowHomeEnabled(false);
        //mActionBar.setDisplayShowTitleEnabled(false);
        //LayoutInflater mInflater = LayoutInflater.from(this);

        //View mCustomView = mInflater.inflate(R.layout.custome_header_bar, null);
        //TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.action_bar_title);
        //    mTitleTextView.setText(selected_business.getBus_title());

        //mTitleTextView.setTypeface(font);
        //mActionBar.setCustomView(mCustomView);
        //mActionBar.setDisplayShowCustomEnabled(true);
        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nsv);
        scrollView.setFillViewport (true);

        txtTotalTime = (TextView)findViewById(R.id.totalTime);
        txtTotalAmount = (TextView)findViewById(R.id.totalAmount);
        txtServiceCharges = (TextView)findViewById(R.id.servicecharge);

        LinearLayout linearContinue  = (LinearLayout)findViewById(R.id.linearContinue);
        linearContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ActiveModels.LIST_SERVICES_MODEL = PriceFragment.mServiceArray;
                    Intent intent = new Intent(DetailsSalonActivity.this, TimeSlotActivity.class);
                    startActivity(intent);

            }
        });

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        //PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        //tabsStrip.setViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        if(selected_business != null) {
            Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/business/" + selected_business.getBus_logo()).into(bannerImage);
            textName.setText(selected_business.getBus_title());
            ratingbar.setRating(Float.parseFloat(selected_business.getAvg_rating()));
            txtServiceCharges.setText(selected_business.getBus_fee());
            String[] timesplit = selected_business.getBus_con_time().split(":");
            txtTotalTime.setText(timesplit[0]+getString(R.string.hr)+timesplit[1]+getString(R.string.min));
            txtTotalAmount.setText(selected_business.getBus_fee());
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_service);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_clinic);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_tab_doctor);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_tab_reviews);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_tab_photos);
    }
    public void updatePrice(Double totalAmount, String[] timesplit){
        txtTotalAmount.setText(String.format("%.2f",totalAmount));
        txtTotalTime.setText(timesplit[0]+getString(R.string.hr)+timesplit[1]+getString(R.string.min));
    }
    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 5;
        private String tabTitles[] = new String[] { getString(R.string.tab_service), getString(R.string.tab_details),getString(R.string.tab_doctors), getString(R.string.tab_reviews), getString(R.string.tab_photo) };
        private int tabIcons[] = {R.drawable.ic_tab_service, R.drawable.ic_action_clinic, R.drawable.ic_tab_doctor, R.drawable.ic_tab_reviews, R.drawable.ic_tab_photos};

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0){
                fragment = new PriceFragment();

            }else if(position == 1){
                fragment = new DetailsFragment();
            }else if(position == 2){
                fragment = new DoctorsFragment();
            }else if(position == 3){
                fragment = new ReviewsFragment();
            }else if(position == 4){
                fragment = new PhotosFragment();
            }
            if(fragment!=null) {
                Bundle args = new Bundle();
                fragment.setArguments(args);
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            /*Drawable image =  ContextCompat.getDrawable(getApplicationContext(), tabIcons[0]) ;//getApplicationContext().getResources().getDrawable(tabIcons[0],getApplicationContext().getTheme()); //ContextCompat.getDrawable(getApplicationContext(), tabIcons[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString("   " + tabTitles[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
*/

              return tabTitles[position];
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
