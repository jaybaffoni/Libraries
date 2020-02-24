package com.thecrowdedstudio.xmldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thecrowdedstudio.utilities.AppUtility;
import com.thecrowdedstudio.utilities.HashUtility;
import com.thecrowdedstudio.utilities.LayoutBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    ViewGroup root;

    Button update;
    TextView output;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        root = findViewById(R.id.root);

        String jsonString = AppUtility.getStringPref(this, "config");
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            processConfigFile(jsonObject);
        } catch (Exception e){
            Log.e("JSON ERROR", e.toString());
        }

    }

    private void bindLayout(){
        update = findViewById(HashUtility.hashString("upd"));
        output = findViewById(HashUtility.hashString("out"));
        input = findViewById(HashUtility.hashString("inp"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.setText(input.getText().toString());
                input.setText("");
            }
        });
    }

    private void processConfigFile(JSONObject json){
        try {

            JSONArray mainLayout = json.getJSONArray("homelayout");
            LayoutBuilder.build(this, root, mainLayout, true);

            bindLayout();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PROCESS ERROR", e.toString());
        }
    }
}
