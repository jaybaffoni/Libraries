package com.thecrowdedstudio.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

public class SignUpActivity extends AppCompatActivity {

    EditText emailEdit, displayEdit, passwordEdit, confirmEdit;

    AlertDialog dialog;

    //Define these in subclass
    Class signInClass = null;
    Class homeClass = null;
    String urlPrefix = null;
    int contentView = R.layout.activity_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);
        if(signInClass == null) {
            Log.e("ERROR", "Variable signInClass must be defined");
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

        emailEdit = findViewById(R.id.emailEdit);
        displayEdit = findViewById(R.id.displayEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        confirmEdit = findViewById(R.id.confirmEdit);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

    }

    public void signIn(View view){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUp(View view){

        if(emptyFields()){
            CommUtility.alert(this, "Could not sign up", "All fields are required");
            return;
        }

        String email = emailEdit.getText().toString();
        String display = displayEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confirm  = confirmEdit.getText().toString();

        if (!password.equals(confirm)) {
            CommUtility.alert(this, "Could not sign up", "Passwords do not match");
            return;
        }

        dialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("display", display);
        params.put("password", password);

        String mJSONURLString = urlPrefix + "users";

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
                                CommUtility.alert(SignUpActivity.this, "Could not sign up", response.getString("error"));
                            } else {

                                AppUtility.savePref(SignUpActivity.this, "id", response.getInt("id"));
                                AppUtility.savePref(SignUpActivity.this, "email", response.getString("email"));
                                AppUtility.savePref(SignUpActivity.this, "displayname", response.getString("displayname"));

                                Intent intent = new Intent(SignUpActivity.this, homeClass);
                                SignUpActivity.this.startActivity(intent);
                                finish();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommUtility.alert(SignUpActivity.this, "Could not sign up", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        if(dialog.isShowing()){
                            dialog.cancel();
                        }

                        CommUtility.alert(SignUpActivity.this, "Could not sign up", error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);

    }

    private boolean emptyFields(){
        if("".equals(emailEdit.getText().toString()) || "".equals(displayEdit.getText().toString()) || "".equals(passwordEdit.getText().toString()) || "".equals(confirmEdit.getText().toString())){
            return true;
        }
        return false;
    }

}