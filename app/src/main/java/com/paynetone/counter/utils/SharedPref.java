package com.paynetone.counter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.core.base.log.Logger;
import com.paynetone.counter.model.EmployeeModel;
import com.paynetone.counter.model.PaynetModel;
import com.paynetone.counter.network.NetWorkController;

public class SharedPref {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean autoCommit = true;
    private static volatile SharedPref instance;

    public static SharedPref getInstance(Context context) {
        if (instance == null)
            instance = new SharedPref(context);
        return instance;
    }

    public SharedPref(Context activity) {
        this.autoCommit = true;
        // pref = PreferenceManager.getDefaultSharedPreferences(mActivityBase);
        pref = activity.getSharedPreferences(Constants.KEY_SHARE_PREFERENCES, Activity.MODE_PRIVATE);
        editor = pref.edit();
    }

    public SharedPref(Context activity, String name) {
        this.autoCommit = true;
        // pref = PreferenceManager.getDefaultSharedPreferences(mActivityBase);
        pref = activity
                .getSharedPreferences(name, Activity.MODE_PRIVATE);
        editor = pref.edit();
    }

    public SharedPref(Context activity, boolean autoCommit) {
        this.autoCommit = autoCommit;
        // pref = PreferenceManager.getDefaultSharedPreferences(mActivityBase);
        pref = activity
                .getSharedPreferences(Constants.KEY_SHARE_PREFERENCES, Activity.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        if (autoCommit) {
            commit();
        }

    }

    public String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        if (autoCommit) {
            commit();
        }
    }

    public void putInt(String key, int value){
        editor.putInt(key,value);
        if (autoCommit) commit();
    }

    public long getLong(String key, long defaultValue) {
        return pref.getLong(key, defaultValue);
    }

    public void commit() {
        editor.commit();
    }

    public void clear() {
        try {
            editor.remove(Constants.KEY_PAYNET);
            editor.remove(Constants.KEY_SHARE_PREFERENCES);
            editor.remove(Constants.KEY_ANDROID_PAYMENT_MODE);
            editor.remove(Constants.KEY_EMPLOYEE);
            editor.commit();
        } catch (Exception ex) {
            Logger.w(ex);
        }
    }

    public void saveEmployee(EmployeeModel employeeModel) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.KEY_EMPLOYEE, NetWorkController.getGson().toJson(employeeModel));
        editor.apply();
    }

    public void savePaynet(PaynetModel paynetModel) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.KEY_PAYNET, NetWorkController.getGson().toJson(paynetModel));
        editor.apply();
    }

    public EmployeeModel getEmployeeModel() {
        String userData = pref.getString(Constants.KEY_EMPLOYEE, "");
        if (!userData.equals("")) {
            return NetWorkController.getGson().fromJson(userData, EmployeeModel.class);
        } else {
            return null;
        }
    }

    public PaynetModel getPaynet() {
        String userData = pref.getString(Constants.KEY_PAYNET, "");
        if (!userData.equals("")) {
            return NetWorkController.getGson().fromJson(userData, PaynetModel.class);
        } else {
            return null;
        }
    }
}
