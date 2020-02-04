package com.thecrowdedstudio.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thecrowdedstudio.utilities.AppUtility;
import com.thecrowdedstudio.utilities.CommUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    EditText userName, passwordEdit;
    Switch mSwitch;

    AlertDialog dialog;

    //Define these in subclass
    Class signUpClass = null;
    Class homeClass = null;
    String urlPrefix = null;
    int contentView = R.layout.activity_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);
        if(signUpClass == null) {
            Log.e("ERROR", "Variable signUpClass must be defined");
            return;
        }
        if(homeClass == null) {
            Log.e("ERROR", "Variable homeClass must be defined");
            return;
        }
        if(urlPrefix == null){
            Log.e("ERROR", "Variable urlPrefix must be defined");
        }

        getSupportActionBar().hide();

        userName = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.passwordEdit);
        mSwitch = findViewById(R.id.switch1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        boolean autoSignIn = AppUtility.getBooleanPref(this, "auto");
        if(autoSignIn){
            Intent intent = new Intent(SignInActivity.this, homeClass);
            SignInActivity.this.startActivity(intent);
            finish();
        }

    }

    public void signIn(View view){

        if(emptyFields()){
            CommUtility.alert(this, "Could not sign in", "All fields are required");
            return;
        }

        dialog.show();

        final String username = userName.getText().toString();
        final String password = passwordEdit.getText().toString();
        final boolean auto = mSwitch.isChecked();

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", username);
        params.put("password", password);

        String mJSONURLString = urlPrefix + "users/validate";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                mJSONURLString,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(dialog.isShowing()){
                            dialog.cancel();
                        }

                        try {
                            if(response.has("error")){
                                CommUtility.alert(SignInActivity.this, "Could not sign in", response.getString("error"));
                            } else {

                                AppUtility.savePref(SignInActivity.this, "id", response.getInt("id"));
                                AppUtility.savePref(SignInActivity.this, "email", response.getString("email"));
                                AppUtility.savePref(SignInActivity.this, "displayname", response.getString("displayname"));
                                AppUtility.savePref(SignInActivity.this, "password", password);
                                AppUtility.savePref(SignInActivity.this, "auto", auto);

                                Intent intent = new Intent(SignInActivity.this, homeClass);
                                SignInActivity.this.startActivity(intent);
                                finish();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommUtility.alert(SignInActivity.this, "Could not sign in", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                        if(dialog.isShowing()){
                            dialog.cancel();
                        }

                        CommUtility.alert(SignInActivity.this, "Could not sign in", error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    public void signUp(View view){
        Intent intent = new Intent(this, signUpClass);
        startActivity(intent);
        finish();
    }

    private boolean emptyFields(){
        if("".equals(userName.getText().toString()) || "".equals(passwordEdit.getText().toString())){
            return true;
        }
        return false;
    }
}
