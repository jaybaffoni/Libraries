package com.thecrowdedstudio.xmldemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.thecrowdedstudio.utilities.AppUtility;
import com.thecrowdedstudio.utilities.CommUtility;
import com.thecrowdedstudio.utilities.HashUtility;
import com.thecrowdedstudio.utilities.LayoutBuilder;
import com.thecrowdedstudio.utilities.RestCaller;
import com.thecrowdedstudio.utilities.RestUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements RestCaller {

    ViewGroup root;

    ImageView logoImageView;
    EditText usernameEdit;
    EditText passwordEdit;
    Button loginButton;

    RestUtility restUtility;
    String url;

    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);

        logoImageView = findViewById(R.id.logo);
        loader = new ProgressDialog(this);
        loader.setMessage("Loading...");
        loader.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        url = AppUtility.getStringPref(this, "hosturl");
        restUtility = new RestUtility(this, this, url);
        if("".equals(url)){
            updateHostUrl();
        } else {
            restUtility = new RestUtility(this, this, url);
            loader.show();
            restUtility.getObject("config.json", "config");
        }

    }

    public void goToHome(){
        Intent intent = new Intent(this, WorkorderListActivity.class);
        startActivity(intent);
    }

    public void bindLayout(){
        usernameEdit = findViewById(HashUtility.hashString("uid"));
        passwordEdit = findViewById(HashUtility.hashString("pw"));
        loginButton = findViewById(HashUtility.hashString("log"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if("".equals(username) || "".equals(password)){
                    CommUtility.alert(MainActivity.this, "Error", "All fields must be filled in");
                } else {
                    goToHome();
                }
            }
        });
    }

    private void processConfigFile(JSONObject json){
        try {
            String logoUrl = json.getString("logourl");
            String companyName = json.getString("companyname");
            getSupportActionBar().setTitle(companyName);
            Glide.with(this).load(logoUrl).into(logoImageView);

            root.removeAllViews();
            JSONArray mainLayout = json.getJSONArray("mainlayout");
            LayoutBuilder.build(this, root, mainLayout, true);

            bindLayout();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PROCESS ERROR", e.toString());
        }
    }

    @Override
    public void networkSuccess(Object json, String callback) {
        if(loader.isShowing()) loader.dismiss();
        JSONObject jsonObject = (JSONObject) json;
        switch(callback) {
            case "config":
                AppUtility.savePref(this, "config", json.toString());
                processConfigFile(jsonObject);
                break;
            default:
                //TODO
                break;
        }
    }

    @Override
    public void networkFailure(String errorMessage) {
        if(loader.isShowing()) loader.dismiss();
        Log.e("ERROR", errorMessage);
    }

    private void updateHostUrl(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(url);
        builder.setView(input);
        builder.setTitle("Host URL");
        builder.setMessage("Please update your server URL")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        url = input.getText().toString();
                        AppUtility.savePref(MainActivity.this, "hosturl", url);
                        restUtility.setUrlPrefix(url);
                        loader.show();
                        restUtility.getObject("config.json", "config");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.settings:
                updateHostUrl();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
