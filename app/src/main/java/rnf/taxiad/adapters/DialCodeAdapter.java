package rnf.taxiad.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rnf.taxiad.R;
import rnf.taxiad.helpers.PhoneNumberUtils;

/**
 * Created by Rahil on 27/5/16.
 */
public class DialCodeAdapter extends ArrayAdapter<PhoneNumberUtils.PhoneNumbers> {

    public DialCodeAdapter(Context context, List<PhoneNumberUtils.PhoneNumbers> objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if (v != null) {
            TextView tv = (TextView) v;
            tv.setTextColor(Color.BLACK);
            tv.setText(getItem(position).toString());

        }
        return v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if (v != null) {
            TextView tv = (TextView) v;
            tv.setTextColor(Color.WHITE);
            tv.setText(getItem(position).dial_code);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_arrow_dropdown, 0);
        }
        return v;
    }
}
