package com.rmlabs.rishabmangla.myspends.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.rmlabs.rishabmangla.myspends.Helper.PrefManager;
import com.rmlabs.rishabmangla.myspends.tools.Utils;

import java.text.SimpleDateFormat;

public class MessageReceiver extends BroadcastReceiver {
    Context mContext;

    public MessageReceiver() {
    }

    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            if (sms != null) {
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    String smsBody = smsMessage.getMessageBody().toString();
                    String address = smsMessage.getOriginatingAddress();
                    Long timeStamp = smsMessage.getTimestampMillis();

                    if (Utils.checkIfBankSms(address, smsBody)) {
                        int amount = Utils.getAmountSpent(smsBody);
                        if (amount > 0) {
                            String monthKey = new SimpleDateFormat("MMMM-yy").format(timeStamp);

                            //if month key is not added, add it
                            if(!(new PrefManager(mContext).addMonthlySpending(monthKey, amount))){
                                new PrefManager(mContext).storeMonthKeys(monthKey);
                            }
                            new PrefManager(mContext).addTotalSpending(amount);
                        }
                    }
                }
            }
        }
    }
}
