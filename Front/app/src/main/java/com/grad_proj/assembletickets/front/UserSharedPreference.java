package com.grad_proj.assembletickets.front;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class UserSharedPreference {

    static final String PREF_ID_TOKEN = "userid";
    static final String PREF_USER_NAME = "username";
    static final String PREF_EMAIL = "email";
    static final String PREF_BIRTH = "birth";
    static final String PREF_GENDER = "gender";

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

    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setUserEmail(Context ctx, String userEmail) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, userEmail);
        editor.commit();
    }

    public static String getUserEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }

    public static void setUserBirth(Context ctx, String userBirth) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_BIRTH, userBirth);
        editor.commit();
    }

    public static String getUserBirth(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_BIRTH, "");
    }

    public static void setUserGender(Context ctx, String userGender) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_GENDER, userGender);
        editor.commit();
    }

    public static String getUserGender(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_GENDER, "");
    }

    public static void clearAll(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_ID_TOKEN);
        editor.remove(PREF_EMAIL);
        editor.remove(PREF_BIRTH);
        editor.remove(PREF_GENDER);
        editor.commit();
    }

}