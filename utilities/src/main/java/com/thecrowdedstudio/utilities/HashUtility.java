package com.thecrowdedstudio.utilities;

import android.util.Log;

public class HashUtility {

    public static int hashString(String str) {
        int hash = 0;
        try{
            str = str.toLowerCase();
            if(str.length() > 3){
                throw new Exception("Improper ID Format");
            }

            int multiplier = 1;
            for(int i = 0; i < str.length(); i++){
                char c = str.charAt(i);
                int add = c - 97;
                add *= multiplier;
                hash += add;
                multiplier *= 26;
            }
        } catch (Exception e){
            Log.e("HASH ERROR", e.toString());
            return -1;
        }

        return hash;
    }

}
