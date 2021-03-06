package com.thecrowdedstudio.utilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestUtility {

    RestCaller caller;
    String urlPrefix;
    Context ctx;

    public RestUtility(Context ctx, RestCaller caller, String urlPrefix){
        this.caller = caller;
        this.urlPrefix = urlPrefix;
        this.ctx = ctx;
    }

    public void getObject(String path, final String callback){
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlPrefix + path,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        caller.networkSuccess(response, callback);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        caller.networkFailure(error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

    }

    public void getArray(String path, final String callback){
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlPrefix + path,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        caller.networkSuccess(response, callback);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        caller.networkFailure(error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);

    }

    public void post(String path, JSONObject json, final String callback){
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                urlPrefix + path,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        caller.networkSuccess(response, callback);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        caller.networkFailure(error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void put(String path, JSONObject json, final String callback){
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                urlPrefix + path,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        caller.networkSuccess(response, callback);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        caller.networkFailure(error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void delete(String path, JSONObject json, final String callback){
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                urlPrefix + path,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        caller.networkSuccess(response, callback);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        caller.networkFailure(error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
