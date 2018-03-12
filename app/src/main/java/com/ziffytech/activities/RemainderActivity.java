package com.ziffytech.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import com.ziffytech.R;


/**
 * Created by Mahesh on 17/10/17.
 */

public class RemainderActivity extends CommonActivity {


    Button proceed;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);

        allowBack();

        setHeaderTitle("Remainders");

       // bindView();

    }
    public void bindView(){






    }


    @Override
    public void onBackPressed() {

        onBackPressed();

    }

}
