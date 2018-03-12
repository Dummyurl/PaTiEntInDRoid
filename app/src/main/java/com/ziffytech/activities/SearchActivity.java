package com.ziffytech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import com.ziffytech.R;



public class SearchActivity extends CommonActivity {
    SeekBar seekBar,seekBarFee,seekBarRating;
    TextView txtArea,txtAreaFee,txtAreaRating;
    Switch switch1;
   // AutoCompleteTextView searchText;
    Bundle bundleloclaity;


    RadioGroup radioAvail,radioGender;

    private String gender="",availability="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        allowBack();
        setHeaderTitle(getString(R.string.hint_search));

      //  searchText = (AutoCompleteTextView) findViewById(R.id.search_keyword);
      /*  searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // searchDoctor();
            }
        });
        */

        txtArea = (TextView) findViewById(R.id.textArea);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        switch1 = (Switch) findViewById(R.id.switch1);

        seekBar.setMax(100);
        txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
        seekBar.setProgress(common.getSessionInt("radius"));

        if (!common.containKeyInSession("nearby_enable")) {
            common.setSessionBool("nearby_enable", true);
        }
        switch1.setChecked(common.getSessionBool("nearby_enable"));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                common.setSessionBool("nearby_enable", b);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                common.setSessionInt("radius", i);
                txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Fee

        txtAreaFee = (TextView) findViewById(R.id.textAreaFee);
        seekBarFee = (SeekBar) findViewById(R.id.seekBarFee);

        seekBarFee.setMax(999);
        //txtAreaFee.setText(common.getSessionInt("radius") + getString(R.string.KM));
        seekBarFee.setProgress(0);


        seekBarFee.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               // common.setSessionInt("radius", i);
                txtAreaFee.setText("Rs. "+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Rating

        txtAreaRating = (TextView) findViewById(R.id.textAreaRating);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);

        seekBarRating.setMax(5);
       // txtAreaRating.setText(common.getSessionInt("radius") + getString(R.string.KM));
         seekBarRating.setProgress(0);


        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               // common.setSessionInt("radius", i);
                txtAreaRating.setText(i+"");
              //  txtAreaRating.setText(common.getSessionInt("radius") + getString(R.string.KM));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Availbility

        availibiltiy();
        genderSelection();
    }

    private void genderSelection() {

        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.male) {
                    gender="male";
                } else if (checkedId == R.id.female) {
                    gender="female";
                }
            }
        });
    }

    private void availibiltiy() {
        radioAvail = (RadioGroup) findViewById(R.id.radioAvail);
        radioAvail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.today) {
                    availability="today";
                } else if (checkedId == R.id.tomorrow) {
                    availability="tomorrow";
                } else if (checkedId == R.id.home) {
                    availability="home";
                }
            }
        });
    }



    public void SearchButtonClick(View view){


            Intent intent = new Intent(SearchActivity.this,BookActivity.class);
            intent.putExtra("search","search");
            intent.putExtra("distance",txtArea.getText().toString());
            intent.putExtra("fee",txtAreaFee.getText().toString());
            intent.putExtra("rating",txtAreaRating.getText().toString());
            intent.putExtra("availability",availability);
            intent.putExtra("gender",gender);
            startActivity(intent);

    }
    public void LocalityViewClick(View view){
        Intent intent = new Intent(SearchActivity.this,LocationActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            ((TextView)findViewById(R.id.chooseLocality)).setText(data.getExtras().getString("locality"));
            bundleloclaity = data.getExtras();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
