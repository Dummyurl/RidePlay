package rnf.taxiad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * A base {@link AppCompatActivity} class for all activities.
 * Created by Rahil on 30/9/15.
 */
public class BaseActivity extends AppCompatActivity {

    public final int MENU_REQUEST = 1;

    public final int SURVEY_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void startActivity(Class<?> className) {
        Intent i = new Intent(this, className);
        startActivity(i);
    }

    protected void startActivityClearTop(Class<?> className) {
        Intent i = new Intent(this, className);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    protected void startActivityClearAll(Class<?> className) {
        Intent i = new Intent(this, className);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

//    protected void startActivityWithBundle(Class<?> className, Bundle bundle) {
//        Intent i = new Intent(this, className);
//        i.putExtra(Tag.GLOBAL_BUNDLE, bundle);
//        startActivity(i);
//    }


    protected void addFragment(Fragment fragment, boolean addToStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    }
}
