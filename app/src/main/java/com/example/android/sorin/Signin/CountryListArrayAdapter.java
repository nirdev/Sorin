package com.example.android.sorin.Signin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.sorin.R;

import java.util.List;

/**
 * Created by Nir on 3/11/2016.
 */
public class CountryListArrayAdapter extends ArrayAdapter<CountrycodeListActivity.Country> {

    private final List<CountrycodeListActivity.Country> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
    }

    public CountryListArrayAdapter(Activity context, List<CountrycodeListActivity.Country> list) {
        super(context, R.layout.activity_countrycode_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_countrycode_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        return view;
    }
}