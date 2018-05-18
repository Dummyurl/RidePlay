package rnf.taxiad.activities;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by rnf-new on 6/3/17.
 */

public class GifStart {

    private static GifStart instance;
    private Context context;
    private final String FILE = "Gifstart";
    private boolean isShow = true;

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
        writeToFile();
    }

    private GifStart(Context c) {
        super();
        this.context = c;
        readFile();
    }

    public synchronized static GifStart getInstance(Context c) {
        if (instance == null)
            instance = new GifStart(c);

        return instance;
    }

    private void readFile() {
        try {
            FileInputStream fileInputStream = context.openFileInput(FILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.isShow = (boolean) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile() {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(isShow);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
