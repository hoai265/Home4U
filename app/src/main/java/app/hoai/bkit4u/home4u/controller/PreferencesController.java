package app.hoai.bkit4u.home4u.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

/**
 * Created by hoaipc on 11/10/15.
 */
public class PreferencesController
{
    private SharedPreferences mPreference;
    private final String PREF_NAME = "config";

    private static PreferencesController mInstance;

    private PreferencesController(Context context)
    {
        this.mPreference = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public static PreferencesController getInstance()
    {
        return mInstance;
    }

    public static void createInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new PreferencesController(context);
        }
    }

    public void setWifiName(String wifiName)
    {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString("wifi_name",wifiName);
        editor.commit();
    }

    public void setWifiPass(String wifiPass)
    {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString("wifi_pass",wifiPass);
        editor.commit();
    }

    public String getWifiName()
    {
        return mPreference.getString("wifi_name", "");
    }

    public String getWifiPass()
    {
        return mPreference.getString("wifi_pass", "");
    }
}
