package com.thecrowdedstudio.utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class LayoutBuilder {

    public static void build(Context ctx, ViewGroup root, JSONArray json, boolean vertical) throws Exception {

        for(int i = 0; i < json.length(); i++){
            JSONObject jsonObject = json.getJSONObject(i);
            String type = jsonObject.getString("viewtype");
            Log.e("JSON", jsonObject.toString());
            if(jsonObject.has("id")) Log.e("HASH", "" + HashUtility.hashString(jsonObject.getString("id")));

            switch(type){
                case "text":
                    TextView textView = new TextView(ctx);
                    if(jsonObject.has("label")) textView.setText(jsonObject.getString("label"));
                    if(jsonObject.has("fontsize")) textView.setTextSize(jsonObject.getInt("fontsize"));
                    textView = (TextView) setProperties(textView, jsonObject, vertical);
                    root.addView(textView);
                    break;
                case "input":
                    EditText editText = new EditText(ctx);
                    if(jsonObject.has("default")) editText.setHint(jsonObject.getString("default"));
                    editText = (EditText) setProperties(editText, jsonObject, vertical);
                    root.addView(editText);
                    break;
                case "button":
                    Button button  = new Button(ctx);
                    if(jsonObject.has("label")) button.setText(jsonObject.getString("label"));
                    button = (Button) setProperties(button, jsonObject, vertical);
                    root.addView(button);
                    break;
                case "horizontal-list":
                    LinearLayout hll = new LinearLayout(ctx);
                    ((LinearLayout) hll).setOrientation(LinearLayout.HORIZONTAL);
                    hll = (LinearLayout) setProperties(hll, jsonObject, vertical);
                    build(ctx, hll, jsonObject.getJSONArray("items"), false);
                    root.addView(hll);
                    break;
                case "vertical-list":
                    LinearLayout vll = new LinearLayout(ctx);
                    ((LinearLayout) vll).setOrientation(LinearLayout.VERTICAL);
                    vll = (LinearLayout) setProperties(vll, jsonObject, vertical);
                    build(ctx, vll, jsonObject.getJSONArray("items"), true);
                    root.addView(vll);
                    break;
            }
        }

        return;

    }

    private static View setProperties(View view, JSONObject json, boolean vertical){
        try {
            LinearLayout.LayoutParams param;
            if(vertical){
                param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                if(json.has("width")) param.weight = json.getInt("width");
            } else {
                param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                if(json.has("width")) param.weight = json.getInt("width");
            }

            view.setLayoutParams(param);
            if(json.has("id")) view.setId(HashUtility.hashString(json.getString("id")));
        } catch (Exception e){
            Log.e("PROPERTIES ERROR", e.toString());
        }

        return view;
    }

}
