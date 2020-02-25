package com.thecrowdedstudio.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DataSource {

    protected SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    Gson gson = new Gson();

    public DataSource(Context context, String name, int version){
        dbHelper = new DatabaseHelper(context, name, version);
    }

    public DataSource(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createRow(String tableName, DatabaseObject object){
        long insertId = -1;
        ContentValues content = new ContentValues();
        try {
            JSONObject json = new JSONObject(gson.toJson(object));
            for(Iterator<String> iterator = json.keys(); iterator.hasNext();){
                String key = iterator.next();
                if(!key.equals("id")){
                    content.put(key, json.get(key).toString());
                }
            }
            insertId = database.insert(tableName, null, content);
            Log.e("row created", insertId + "");
            object.setId(insertId);
        } catch (JSONException e){
            Log.e("ERROR", e.toString());
        }

    }

    public void updateRow(String tableName, DatabaseObject object){
        ContentValues content = new ContentValues();
        try {
            JSONObject json = new JSONObject(gson.toJson(object));
            for(Iterator<String> iterator = json.keys(); iterator.hasNext();){
                String key = iterator.next();
                if(!key.equals("id")){
                    content.put(key, json.get(key).toString());
                }
            }
            database.update(tableName, content, "id = ?",
                    new String[] { String.valueOf(object.getId()) });
        } catch (JSONException e){
            Log.e("ERROR", e.toString());
        }
    }

    public ArrayList<DatabaseObject> getAllRows(String tableName, Class className){
        ArrayList<DatabaseObject> list = new ArrayList<DatabaseObject>();
        Cursor cursor = database.query(tableName, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            JSONObject json = new JSONObject();
            for(int i = 0; i < cursor.getColumnCount(); i++){
                try {
                    if(cursor.getString(i) != null){
                        json.put(cursor.getColumnName(i), cursor.getString(i));
                    } else {
                        json.put(cursor.getColumnName(i), "");
                    }
                } catch (JSONException e){
                    Log.e("ERROR", e.toString());
                }
            }

            DatabaseObject object = (DatabaseObject) gson.fromJson(json.toString(), className);
            list.add(object);
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }

    public void deleteRow(String tableName, int id){
        database.delete(tableName, "id = " + id, null);
    }

}
