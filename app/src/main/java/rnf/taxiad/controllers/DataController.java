package rnf.taxiad.controllers;

import android.content.Context;

import rnf.taxiad.abstrats.AbstractDataController;

/**
 * Created by Rahil on 3/3/16.
 */
public class DataController extends AbstractDataController {

    private static DataController instance;


    private DataController(Context context) {
        super(context);
    }

    public synchronized static DataController getInstance(Context context) {
        if (instance == null)
            instance = new DataController(context);
        return instance;
    }
}
