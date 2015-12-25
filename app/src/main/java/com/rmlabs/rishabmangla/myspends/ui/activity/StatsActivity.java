package com.rmlabs.rishabmangla.myspends.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rmlabs.rishabmangla.myspends.Helper.PrefManager;
import com.rmlabs.rishabmangla.myspends.Loader.SmsLoaderTask;
import com.rmlabs.rishabmangla.myspends.R;

public class StatsActivity extends AppCompatActivity {

    LinearLayout mLayout;
    TextView mTotalSpends;
    ProgressBar mProgressWheel;
    ProgressBar mProgressWheelTotalAmount;
    PrefManager prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = new PrefManager(getApplicationContext());
        mLayout = (LinearLayout) findViewById(R.id.linear_layout);
        mTotalSpends = (TextView) findViewById(R.id.total_spends);
        mProgressWheel = (ProgressBar) findViewById(R.id.progress_wheel);
        mProgressWheelTotalAmount = (ProgressBar) findViewById(R.id.progress_wheel_total_amount);

        loadAndViewSpends();
    }

    //start the loader if the app is started first time else load from db
    private void loadAndViewSpends() {
        if(new PrefManager(getApplicationContext()).retrieveTotalSpending() != 0){
            showTotalSpending();
            retriveAndAddMonthlySpending();
        } else {
            startSmsLoader();
        }
    }

    public void startSmsLoader() {
        SmsLoaderTask task = new SmsLoaderTask(this);
        task.execute();
    }

    public void showTotalSpending() {
        int totalSpends = new PrefManager(this).retrieveTotalSpending();
        mProgressWheel.setVisibility(View.GONE);
        mProgressWheelTotalAmount.setVisibility(View.GONE);
        mTotalSpends.setText("Rs."+String.valueOf(totalSpends));
    }

    private void retriveAndAddMonthlySpending() {
        String[] monthKeys = new PrefManager(this).retrieveMonthKeys();
        for(String month : monthKeys){
            String amount = String.valueOf(new PrefManager(this).retieveMonthlySpending(month));
            if(month != null && amount != null)
                addMonthlySpending(month, amount);
        }
    }

    public void addMonthlySpending(String month, String totalSpends) {
        TextView monthView = (TextView) getLayoutInflater().inflate(R.layout.view_header, null);
        TextView amountView = (TextView) getLayoutInflater().inflate(R.layout.view_content, null);
        monthView.setText(month);
        mLayout.addView(monthView);
        amountView.setText("Rs."+totalSpends);
        mLayout.addView(amountView);
    }

}
