package com.thecrowdedstudio.xmldemo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.thecrowdedstudio.utilities.HashUtility;
import com.thecrowdedstudio.utilities.LayoutBuilder;

import org.json.JSONArray;

import java.util.List;

public class WorkorderAdapter extends ArrayAdapter<Workorder> {
    Context context;
    List<Workorder> workorders;
    JSONArray jsonArray;

    public WorkorderAdapter(Context context, List<Workorder> workorders, JSONArray jsonArray) {
        super(context, 0, workorders);
        this.context = context;
        this.workorders = workorders;
        this.jsonArray = jsonArray;
    }

    private class ViewHolder {
        ViewGroup root;
        TextView wonum;
        TextView status;
        TextView site;
        TextView description;
        TextView location;
        TextView asset;
        TextView lead;
        TextView reportedDate;
        TextView scheduledStart;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder;
            Workorder workorder = workorders.get(position);

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.workorder_item, null);
                holder = new ViewHolder();
                holder.root = convertView.findViewById(R.id.root);
                LayoutBuilder.build(context, holder.root, jsonArray, true);

                holder.wonum = convertView.findViewById(HashUtility.hashString("won"));
                holder.status = convertView.findViewById(HashUtility.hashString("sts"));

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

//            holder.wonum.setText(workorder.getString("wonum"));
//            holder.status.setText(workorder.getString("status"));
            holder.wonum.setText(workorder.getWonum());
            holder.status.setText(workorder.getStatus());

        } catch (Exception e){
            Log.e("BUILD ERROR", e.toString());
        }


        return convertView;
    }

}
