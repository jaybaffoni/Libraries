package com.thecrowdedstudio.libraries;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.thecrowdedstudio.database.DataSource;
import com.thecrowdedstudio.database.DatabaseColumn;
import com.thecrowdedstudio.database.DatabaseHelper;
import com.thecrowdedstudio.database.DatabaseObject;
import com.thecrowdedstudio.utilities.AppUtility;
import com.thecrowdedstudio.utilities.RestCaller;
import com.thecrowdedstudio.utilities.RestUtility;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RestCaller {

    private final String databaseName = "database";
    private final int databaseVersion = 1;
    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!AppUtility.getBooleanPref(this, "databaseConfigured")){
            DatabaseHelper dbHelper = setupDatabase();
            dataSource = new DataSource(dbHelper);
            AppUtility.savePref(this, "databaseConfigured", true);
        } else {
            dataSource = new DataSource(this, databaseName, databaseVersion);
        }

        dataSource.open();

//        User user = new User("my name2", "my email2", "my password2");
//        dataSource.createRow("users", user);
//
//        ArrayList<DatabaseObject> users = dataSource.getAllRows("users", User.class);
//        for(DatabaseObject object: users){
//            User u = (User) object;
//            u.setEmail("NO EMAIL");
//            dataSource.updateRow("users", u);
//        }
//
//        users = dataSource.getAllRows("users", User.class);
//        for(DatabaseObject object: users){
//            User u = (User) object;
//            Log.e("USER", u.toString());
//        }
        RestUtility restUtility = new RestUtility(this, this, "");
        restUtility.getObject("https://www.fantasyfootballnerd.com/service/schedule/json/6q3d8fxxq25m", "schedule");

    }

    private DatabaseHelper setupDatabase(){
        Log.e("DB", "Setting up");
        DatabaseHelper dbHelper = new DatabaseHelper(this, databaseName, databaseVersion);

        String tableName = "users";
        ArrayList<DatabaseColumn> columns = new ArrayList<DatabaseColumn>();
        columns.add(new DatabaseColumn("name", "text", true, false, false));
        columns.add(new DatabaseColumn("email", "text", true, false, false));
        columns.add(new DatabaseColumn("password", "text", true, false, false));
        dbHelper.createSqlStatement(tableName, columns);

        return dbHelper;
    }

    @Override
    public void networkSuccess(Object json, String callback) {
        Log.e("JSON", json.toString());
    }

    @Override
    public void networkFailure(String errorMessage) {

    }
}
