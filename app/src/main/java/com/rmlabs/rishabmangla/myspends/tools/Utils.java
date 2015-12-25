package com.rmlabs.rishabmangla.myspends.tools;

import android.util.Log;

import com.rmlabs.rishabmangla.myspends.Helper.PrefManager;
import com.rmlabs.rishabmangla.myspends.R;
import com.rmlabs.rishabmangla.myspends.app.Config;

import java.util.StringTokenizer;

/**
 * Created by rishabmangla on 25/12/15.
 */
public class Utils {

    public static boolean checkIfBankSms(String address, String body) {
        String[] bankNameList = Config.BANK_NAMES;
        String[] keywords = Config.MSG_KEYWORDS;
        if(Utils.stringContainsItemFromList(address, bankNameList)
                && Utils.stringContainsItemFromList(body, keywords)){
            return true;
        }
        return false;
    }

    public static Integer getAmountSpent(String body) {
        body = body.replace(",","");
        StringTokenizer st = new StringTokenizer(body, ". \n");
        while(st.hasMoreTokens()){
            String currentToken = st.nextToken().toLowerCase();
            if(currentToken.equals(Config.RS_DELIMITER)
                    || currentToken.equals(Config.INR_DELIMITER)){
                try {
                    return Integer.parseInt(st.nextToken());
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    return 0;
                }
            }
        }
        return 0;
    }

    public static boolean stringContainsItemFromList(String inputString, String[] items) {
        for(int i =0; i < items.length; i++) {
            if(inputString.toLowerCase().contains(items[i])) {
                return true;
            }
        }
        return false;
    }

}
