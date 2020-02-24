package com.thecrowdedstudio.xmldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thecrowdedstudio.utilities.AppUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkorderListActivity extends AppCompatActivity {

    WorkorderAdapter workorderAdapter;
    ArrayList<Workorder> workorders = new ArrayList<Workorder>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_list);

        listView = findViewById(R.id.listview);

        workorders.add(new Workorder("3137", "WAPPR"));
        workorders.add(new Workorder("2020", "INPRG"));
        workorders.add(new Workorder("2567", "COMP"));

        JSONArray jsonArray = new JSONArray();
        String config = AppUtility.getStringPref(this, "config");
        try {
            JSONObject jsonObject = new JSONObject(config);
            jsonArray = jsonObject.getJSONArray("workorderitem");
        } catch (Exception e){
            Log.e("JSON ERROR", e.toString());
        }

        workorderAdapter = new WorkorderAdapter(this, workorders, jsonArray);
        listView.setAdapter(workorderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkorderListActivity.this, WorkorderDetailActivity.class);
                startActivity(intent);
            }
        });

    }
}
