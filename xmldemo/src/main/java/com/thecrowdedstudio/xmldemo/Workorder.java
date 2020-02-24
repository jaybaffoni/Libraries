package com.thecrowdedstudio.xmldemo;

import android.util.Log;

import org.json.JSONObject;

import java.util.Date;

public class Workorder {

    JSONObject jsonObject;
    String wonum;
    String status;

    public Workorder(String wonum, String status){
        this.wonum = wonum;
        this.status = status;
    }

    public Workorder(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public String getString(String key){
        try{
            return jsonObject.getString(key);
        } catch (Exception e){
            Log.e("JSON ERROR", e.toString());
        }
        return "";
    }

    public int getInt(String key){
        try{
            return jsonObject.getInt(key);
        } catch (Exception e){
            Log.e("JSON ERROR", e.toString());
        }
        return -1;
    }

    public String getWonum() {
        return wonum;
    }

    public String getStatus() {
        return status;
    }

    //TODO get date from json
}
