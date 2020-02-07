package com.thecrowdedstudio.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    ArrayList<String> sqlStatements = new ArrayList<String>();

    public DatabaseHelper(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("DB", "Created");
        for(String statement: sqlStatements){
            db.execSQL(statement);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createSqlStatement(String tableName, ArrayList<DatabaseColumn> columns){
        String statement = "create table " + tableName + "(id integer primary key autoincrement, ";
        for(DatabaseColumn column: columns){
            statement = statement + column.toString() + ", ";
        }
        statement = statement.substring(0, statement.length() - 2);
        statement = statement + ");";
        Log.e("SQL", statement);
        sqlStatements.add(statement);
    }
}
