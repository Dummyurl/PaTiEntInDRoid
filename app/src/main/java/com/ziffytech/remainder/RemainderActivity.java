
package com.ziffytech.remainder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.ziffytech.R;
import com.ziffytech.activities.CommonActivity;
import com.ziffytech.activities.RelativeProfile;
import com.ziffytech.adapters.RemainderAdapter;
import com.ziffytech.chat.DataProvider;
import com.ziffytech.util.RecyclerItemClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;


public class RemainderActivity extends CommonActivity {

    private RecyclerView mList;
    private RemainderAdapter mAdapter;
    private TextView mNoReminderView;
    private List<Reminder> mTest;
    private DataProvider rb ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_remainder);

        allowBack();
        setHeaderTitle("Remainders");

        FloatingActionButton  mAddReminderButton = (FloatingActionButton) findViewById(R.id.add_reminder);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReminderAddActivity.class);
                startActivity(intent);
            }
        });

        mTest=new ArrayList<>();
        rb = new DataProvider();

        mNoReminderView = (TextView) findViewById(R.id.no_reminder_text);
        mNoReminderView.setVisibility(View.GONE);


        mList = (RecyclerView) findViewById(R.id.reminder_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(layoutManager);
        mAdapter = new RemainderAdapter(this,mTest);
        mList.setAdapter(mAdapter);


        final AlarmReceiver mAlarmReceiver = new AlarmReceiver();

        mList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                Log.e("AT","ITEM CLICK");

                AlertDialog.Builder ad=  new AlertDialog.Builder(RemainderActivity.this);
                ad.setMessage("Are you sure you want to delete remainder?");
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        rb.deleteReminder(mTest.get(position),RemainderActivity.this);
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.notifyDataSetChanged();
                        mAlarmReceiver.cancelAlarm(getApplicationContext(), mTest.get(position).getID());

                        onStart();


                    }
                });
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                ad.create().show();



            }
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();

        getData();

    }

    private void getData() {
        if(!mTest.isEmpty()){
            mTest.clear();
        }
        mTest = rb.getAllReminders(this,common.get_user_id());
        if (mTest.isEmpty()) {
            mNoReminderView.setVisibility(View.VISIBLE);
        }else{
            mNoReminderView.setVisibility(View.GONE);
            mAdapter = new RemainderAdapter(this,mTest);
            mList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }

    }
}
