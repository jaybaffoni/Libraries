package com.thecrowdedstudio.utilities;

import android.content.Context;

public class AppUtility {

    public static void savePref(Context ctx, String key, String value){
        ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static void savePref(Context ctx, String key, int value){
        ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public static void savePref(Context ctx, String key, boolean value){
        ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static String getStringPref(Context ctx, String key){
        return ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE).getString(key, "");
    }

    public static int getIntPref(Context ctx, String key){
        return ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE).getInt(key, -1);
    }

    public static boolean getBooleanPref(Context ctx, String key){
        return ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public static void clearPrefs(Context ctx){
        ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().clear().apply();
    }

}
