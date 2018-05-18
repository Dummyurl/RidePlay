package rnf.taxiad.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

import rnf.taxiad.R;

/**
 * Created by php-android02 on 13/1/16.
 */
public class CabServicesView extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    private ArrayList<String> values = new ArrayList<>();
    private AppCompatCheckBox checkboxUber, checkboxCarma, checkboxLyft, checkboxOther, checkboxPedicab;

    public CabServicesView(Context context) {
        super(context);
    }

    public CabServicesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CabServicesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CabServicesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.view_cab_services, this, false);
        checkboxUber = (AppCompatCheckBox) view.findViewById(R.id.checkboxUber);
        checkboxCarma = (AppCompatCheckBox) view.findViewById(R.id.checkboxCarma);
        checkboxLyft = (AppCompatCheckBox) view.findViewById(R.id.checkboxLyft);
        checkboxOther = (AppCompatCheckBox) view.findViewById(R.id.checkboxOther);
        checkboxPedicab = (AppCompatCheckBox) view.findViewById(R.id.checkboxPedicab);
        checkboxUber.setOnCheckedChangeListener(this);
        checkboxCarma.setOnCheckedChangeListener(this);
        checkboxLyft.setOnCheckedChangeListener(this);
        checkboxOther.setOnCheckedChangeListener(this);
        checkboxPedicab.setOnCheckedChangeListener(this);
        this.addView(view);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sparseBooleanArray.put(buttonView.getId(), isChecked);
        if (isChecked)
            values.add(buttonView.getText().toString());
        else
            values.remove(buttonView.getText().toString());
    }

    public boolean isSelected() {
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            int key = sparseBooleanArray.keyAt(i);
            boolean selected = sparseBooleanArray.get(key);
            if (selected)
                return true;
        }

        return false;
    }

    public ArrayList<String> getSelectedServices() {
        return values;
    }

    public void setSelected(String s) {
        if (s.length() == 0)
            return;
        String[] selected = null;
        if (!s.contains(",")) {
            selected = new String[]{s};
        } else
            selected = s.split(",", 5);
        if (selected == null)
            return;
        for (int i = 0; i < selected.length; i++) {
            String name = selected[i];
            if (name.trim().equalsIgnoreCase(checkboxUber.getText().toString()))
                checkboxUber.setChecked(true);
            else if (name.trim().equalsIgnoreCase(checkboxCarma.getText().toString()))
                checkboxCarma.setChecked(true);
            else if (name.trim().equalsIgnoreCase(checkboxLyft.getText().toString()))
                checkboxLyft.setChecked(true);
            else if (name.trim().equalsIgnoreCase(checkboxOther.getText().toString()))
                checkboxOther.setChecked(true);
            else if (name.trim().equalsIgnoreCase(checkboxPedicab.getText().toString()))
                checkboxPedicab.setChecked(true);
        }
    }
}
