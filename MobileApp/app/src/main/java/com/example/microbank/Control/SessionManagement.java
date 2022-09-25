package com.example.microbank.Control;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.microbank.data.Model.Customer;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String FIRST_NAME = "session_firstname";
    String LAST_NAME = "session_lastname";
    String IS_SPECIAL = "session_specialReq";
    String TRIGGER_SET = "Trigger_Created";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveSession(Customer customer, boolean isSpecial){
        String id = customer.getCustomer_id();
        String first_name = customer.getFirst_Name();
        String last_name = customer.getLast_Name();
        editor.putString(SESSION_KEY,id).commit();
        editor.putString(FIRST_NAME,first_name).commit();
        editor.putString(LAST_NAME,last_name).commit();
        editor.putBoolean(IS_SPECIAL,isSpecial).commit();
    }
    public String  getSession(){
        return sharedPreferences.getString(SESSION_KEY,"Over");
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,"Over").commit();
        editor.remove(FIRST_NAME);
        editor.remove(LAST_NAME);
        editor.remove(IS_SPECIAL);
    }

    public String getFirstName(){
        return sharedPreferences.getString(FIRST_NAME,null);
    }

    public boolean isSpecialRequest(){
        return sharedPreferences.getBoolean(IS_SPECIAL, false);
    }

    public void setTrigger(){
        editor.putBoolean(TRIGGER_SET,true).commit();
    }
    public void resetTrigger(){
        editor.putBoolean(TRIGGER_SET,false).commit();
    }
    public boolean getTrigger(){
        return sharedPreferences.getBoolean(TRIGGER_SET,false);
    }
}
