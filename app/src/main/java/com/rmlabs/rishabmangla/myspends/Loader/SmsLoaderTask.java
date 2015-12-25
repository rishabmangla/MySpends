package com.rmlabs.rishabmangla.myspends.Loader;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.rmlabs.rishabmangla.myspends.Helper.PrefManager;
import com.rmlabs.rishabmangla.myspends.ui.activity.StatsActivity;
import com.rmlabs.rishabmangla.myspends.tools.Utils;


/**
 * THIS ASYNC TASK IS ONLY EXECUTED WHEN THE APP IS LAUNCHED THE FIRST TIME
 * Backround thread to load the messages and calculate the monthly spends
 * output is posted and Month is added after all the spending is calculated in the background
 * Finally total spending is calculated and posted to activity
 */

public class SmsLoaderTask extends AsyncTask<Integer, String, Integer>{

	private Activity mContext;
	private int totalSpends = 0;

	public SmsLoaderTask(Activity context) {
		mContext = context;
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {

		String[] projection = {"address", "body", "date"};
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor msgCursor = contentResolver.query(Uri.parse("content://sms/inbox"), projection, null, null, null);
        int addressIndex = msgCursor.getColumnIndex("address");
        int bodyIndex = msgCursor.getColumnIndex("body");
        int timeIndex = msgCursor.getColumnIndex("date");
        String address;
        int prevMonth = -1;

        if (!msgCursor.moveToFirst()) {
            return 0;
        }

        do {
        	address = msgCursor.getString(addressIndex);
            String currentSmsBody = msgCursor.getString(bodyIndex);
            Long currentSmsTimeStamp = msgCursor.getLong(timeIndex);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(currentSmsTimeStamp);
            int currentMonth = cal.get(Calendar.MONTH);

            if(Utils.checkIfBankSms(address, currentSmsBody)){
                int amount = Utils.getAmountSpent(currentSmsBody);
                if(amount > 0){
                    totalSpends += amount;
                    if(currentMonth != prevMonth){
                        String monthKey = new SimpleDateFormat("MMMM-yy").format(currentSmsTimeStamp);
                        publishProgress(monthKey, String.valueOf(totalSpends));
                        new PrefManager(mContext).storeMonthKeys(monthKey);
                        new PrefManager(mContext).addMonthlySpending(monthKey, totalSpends);
                        new PrefManager(mContext).addTotalSpending(totalSpends);
                        totalSpends = 0;
                        prevMonth = currentMonth;
                    }
                }
            }
        } while (msgCursor.moveToNext());
        msgCursor.close();
		return totalSpends;
	}

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(mContext instanceof StatsActivity)
            ((StatsActivity)mContext).addMonthlySpending(values[0], values[1]);
    }

    @Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
        if(mContext instanceof StatsActivity)
            ((StatsActivity)mContext).showTotalSpending();
	}

}
