package com.rmlabs.rishabmangla.myspends.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rishabmangla on 25/12/15.
 */
public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "MySpends";

    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_SPENDS = "spends";
    public static final String KEY_MONTH_KEYS = "month_keys";

    public final static String DEFAULT_DELIMITER = ",,/,";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public void storeVariable(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String retrieveVariable(String key) {
        return pref.getString(key, null);
    }

    public void storeInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     *
     * @param key
     * @param value
     * @return true if key was already present
     * false otherwise
     */
    public boolean addMonthlySpending(String key, int value) {
        int prevAmount = pref.getInt(key, 0);
        editor.putInt(key, value + prevAmount);
        editor.commit();
        if(prevAmount > 0)
            return true;
        else
            return false;
    }

    public int retieveMonthlySpending(String key) {
        return pref.getInt(key, 0);
    }

    public void addTotalSpending(int amount) {
        editor.putInt(KEY_SPENDS, pref.getInt(KEY_SPENDS, 0) + amount);
        editor.commit();
    }

    public int retrieveTotalSpending() {
        return pref.getInt(KEY_SPENDS, 0);
    }

    public void storeMonthKeys(String value) {
        String default_val = pref.getString(KEY_MONTH_KEYS, null);
        if (default_val != null) {
            editor.putString(KEY_MONTH_KEYS, default_val + DEFAULT_DELIMITER + value);
        } else {
            editor.putString(KEY_MONTH_KEYS, value);
        }
        editor.apply();
    }

    public String[] retrieveMonthKeys() {
        String val = pref.getString(KEY_MONTH_KEYS, null);
        if (val == null) return new String[]{};
        else return val.split(DEFAULT_DELIMITER);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

}
