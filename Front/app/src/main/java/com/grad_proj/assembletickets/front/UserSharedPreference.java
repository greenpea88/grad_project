package com.grad_proj.assembletickets.front;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class UserSharedPreference {

    static final String PREF_ID_TOKEN = "userid";
    static final String PREF_EMAIL_TOKEN = "email";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setIdToken(Context ctx, String userName) {
        Log.d("sharedpref", "save id" + getIdToken(ctx));
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ID_TOKEN, userName);
        editor.commit();
    }

    public static String getIdToken(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_ID_TOKEN, "");
    }

    public static void clearIdToken(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_ID_TOKEN);
        editor.commit();
    }

    public static void setUserEmail(Context ctx, String userEmail) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL_TOKEN, userEmail);
        editor.commit();
    }

    public static String getUserEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_EMAIL_TOKEN, "");
    }

    public static void clearAll(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
    }

}