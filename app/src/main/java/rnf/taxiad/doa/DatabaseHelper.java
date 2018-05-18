package rnf.taxiad.doa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Rahil on 14/12/15.
 */
public class DatabaseHelper {

    private static DatabaseHelper instance;
    private SQLiteHelper helper;

    public synchronized static DatabaseHelper get(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    private DatabaseHelper(Context context) {
        helper = new SQLiteHelper(context);
    }

    public SQLiteDatabase getDatabase(boolean isWritable) {
        return isWritable ? helper.getWritableDatabase() : helper.getReadableDatabase();
    }
}
