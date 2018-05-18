package rnf.taxiad.adapters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;

/**
 * Created by Rahil on 11/1/16.
 */
public class VideoAdapter<F extends Fragment> extends FragmentStatePagerAdapter {
    private final Class<F> fragmentClass;
    private final String[] projection;
    private Cursor cursor;
    private HashMap<Integer, Fragment> fragments = new HashMap<>();

    public VideoAdapter(FragmentManager fm, Class<F> fragmentClass, String[] projection, Cursor cursor) {
        super(fm);
        this.fragmentClass = fragmentClass;
        this.projection = projection;
        this.cursor = cursor;
    }

    @Override
    public F getItem(int position) {
        if (cursor == null) // shouldn't happen
            return null;
        cursor.moveToPosition(position);
        F frag;
        try {
            frag = fragmentClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Bundle args = new Bundle();
        args.putInt("position", position);
        for (int i = 0; i < projection.length; ++i) {
            args.putString(projection[i], cursor.getString(i));
        }
        frag.setArguments(args);
        fragments.put(position, frag);
        return frag;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }

    public void swapCursor(Cursor c) {
        if (cursor == c)
            return;
        this.cursor = c;
        notifyDataSetChanged();
    }

    public Fragment getCurrent(int position) {
        if (position >= fragments.size())
            return null;
        return fragments.get(position);
    }


    public Cursor getCursor() {
        return cursor;
    }




}
//
//    private int count = 10;
//    SparseArray<Fragment> fragments = new SparseArray<Fragment>();
//
//    public VideoAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        Fragment fragment = fragments.get(position);
//        if (fragment == null) {
//            FragmentVideoView fragmentVideoView = new FragmentVideoView();
//            fragment=fragmentVideoView;
//        }
//
//        return fragment;
//    }
//
//
//    @Override
//    public int getCount() {
//        return 10;
//    }
//
//    public void addFragment(Fragment fragment) {
//        fragments.put(getCount() - 1, fragment);
//        notifyDataSetChanged();
//    }


//}
