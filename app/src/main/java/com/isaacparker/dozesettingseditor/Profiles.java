package com.isaacparker.dozesettingseditor;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by isaac on 1/11/15.
 */
public class Profiles {

    //User Profiles
    public static ArrayList<Profile> profileList = new ArrayList<>();
    //Hardcoded Profiles
    public static ArrayList<Profile> defaultProfileList;

    public static void loadDefaultProfiles(){
        defaultProfileList = new ArrayList<>();
        Profile defaultProfile = new Profile("Stock", "inactive_to=1800000,sensing_to=240000,locating_to=30000,location_accuracy=20.0,motion_inactive_to=600000,idle_after_inactive_to=1800000,idle_pending_to=300000,max_idle_pending_to=600000,idle_pending_factor=2.0,idle_to=3600000,max_idle_to=21600000,idle_factor=2,min_time_to_alarm=3600000,max_temp_app_whitelist_duration=300000,mms_temp_app_whitelist_duration=60000,sms_temp_app_whitelist_duration=20000");
        Profile freebee269Profile = new Profile("freebee269", "inactive_to=600000,sensing_to=0,locating_to=0,location_accuracy=20.0,motion_inactive_to=0,idle_after_inactive_to=0,idle_pending_to=120000,max_idle_pending_to=120000,idle_pending_factor=2.0,idle_to=1800000,max_idle_to=21600000,idle_factor=2,min_time_to_alarm=3600000,max_temp_app_whitelist_duration=300000,mms_temp_app_whitelist_duration=60000,sms_temp_app_whitelist_duration=20000");
        Profile geraldRudiProfile = new Profile("GeraldRudi", "inactive_to=30000,sensing_to=0,locating_to=0,location_accuracy=20.0,motion_inactive_to=0,idle_after_inactive_to=0,idle_pending_to=60000,max_idle_pending_to=120000,idle_pending_factor=2.0,idle_to=900000,max_idle_to=21600000,idle_factor=2,min_time_to_alarm=600000,max_temp_app_whitelist_duration=10000,mms_temp_app_whitelist_duration=10000,sms_temp_app_whitelist_duration=10000");
        Profile tuhinxp04Profile = new Profile("tuhinxp04", "inactive_to=30000,sensing_to=0,locating_to=0,location_accuracy=20.0,motion_inactive_to=0,idle_after_inactive_to=0,idle_pending_to=12000,max_idle_pending_to=12000,idle_pending_factor=2.0,idle_to=60000,max_idle_to=21600000,idle_factor=2,min_time_to_alarm=3600000,max_temp_app_whitelist_duration=300000,mms_temp_app_whitelist_duration=60000,sms_temp_app_whitelist_duration=20000");
        defaultProfileList.add(defaultProfile);
        defaultProfileList.add(freebee269Profile);
        defaultProfileList.add(geraldRudiProfile);
        defaultProfileList.add(tuhinxp04Profile);
    }

    public static void loadUserProfiles(SharedPreferences sharedPref, Gson gson) {
        String gsonUserProfile = sharedPref.getString("UserProfiles" , null);
        Type type = new TypeToken<ArrayList<Profile>>(){}.getType();
        if(gsonUserProfile != null){
            profileList = gson.fromJson(gsonUserProfile, type);
        }
    }

    public static void saveUserProfiles(SharedPreferences sharedPref, Gson gson){
        sharedPref.edit().putString("UserProfiles", gson.toJson(profileList)).commit();
    }
}
