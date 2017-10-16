package com.example.minhvan.firebasedemo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Minh Van on 3/10/2017.
 */

public class CustomListViewAdapter extends BaseAdapter{
    Activity activity;
    ArrayList<User> userArrayList;
    LayoutInflater mInflater;

    public CustomListViewAdapter(Activity activity, ArrayList<User> userArrayList) {
        this.activity = activity;
        this.userArrayList = userArrayList;
    }

    //class hold reference of each view
    static class ViewHolder {
        TextView textUser;
        TextView textEmail;

    }
    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return userArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            mInflater = (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_item,parent,false);
            viewHolder = new CustomListViewAdapter.ViewHolder();

            viewHolder.textUser = (TextView) convertView.findViewById(R.id.listName);
            viewHolder.textEmail = (TextView) convertView.findViewById(R.id.listEmail);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textUser.setText(userArrayList.get(i).getName());
        viewHolder.textEmail.setText(userArrayList.get(i).getEmail());
        return convertView;
    }
}
