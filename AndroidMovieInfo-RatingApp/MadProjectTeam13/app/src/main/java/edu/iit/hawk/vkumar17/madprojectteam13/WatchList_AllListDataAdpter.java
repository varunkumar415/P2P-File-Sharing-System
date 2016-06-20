package edu.iit.hawk.vkumar17.madprojectteam13;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WatchList_AllListDataAdpter extends ArrayAdapter {
    List list = new ArrayList();

    public WatchList_AllListDataAdpter(Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView NAME, DES, USRNAME;
    }

    public void add(Object object) {
        super.add(object);
        list.add(object);

    }

    public int getCount() {
        return list.size();
    }

    public Object getTtem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        try {
            if (row == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.watched_allrow_layout, parent, false);
                layoutHandler = new LayoutHandler();
                layoutHandler.NAME = (TextView) row.findViewById(R.id.test_movie_name);
                layoutHandler.DES = (TextView) row.findViewById(R.id.test_user_des);

                layoutHandler.USRNAME = (TextView) row.findViewById(R.id.test_user_name);
                row.setTag(layoutHandler);
            } else {
                layoutHandler = (LayoutHandler) row.getTag();
            }
        } catch (Exception ex) {ex.printStackTrace();
            return null;
        }

            WatchList_DataProvider watchListDataProvider = (WatchList_DataProvider) this.getItem(position);
            layoutHandler.NAME.setText(watchListDataProvider.getName());
            layoutHandler.DES.setText(watchListDataProvider.getDes());

            layoutHandler.USRNAME.setText(watchListDataProvider.getUsrName());


            return row;


    }

}
