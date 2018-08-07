package com.forestsoftware.offlimit.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


import java.util.HashMap;

/**
 * Created by HP-PC on 3/23/2018.
 */

public class SessionManager
{
    private static final String THRESHOLDVALUE = "thresholdvalue";
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;


    private Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;



    // All Shared Preferences Keys

    // User name (make variable public to access from outside)

    // Email address (make variable public to access from outside)

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(Const.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */






    /**
     * Quick check for login
     * **/
    // Get Login State





    public  int getThresholdValue()
    {
        return pref.getInt(THRESHOLDVALUE, 0);

    }
    public  void setThresholdValue(int thresholdValue)
    {
        editor.putInt(THRESHOLDVALUE, thresholdValue);
        editor.commit();

    }
//    public boolean getVibrateValue()
//    {
//        return pref.getBoolean(VIBRATEONDISTRESSRECEIVED,false);
//    }
//    public void setVibrateOnDistressReceived(boolean vibrate)
//    {
//        editor.putBoolean(VIBRATEONDISTRESSRECEIVED,vibrate);
//        editor.commit();
//    }



}
