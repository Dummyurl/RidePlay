package rnf.taxiad.controllers;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import rnf.taxiad.activities.GifStart;

/**
 * Created by rnf-new on 20/3/17.
 */

public class WelcomeStart {
    private static WelcomeStart instance;
    private Context context;
    private final String FILE = "Welcomestart";
    private boolean isShow = true;

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
        writeToFile();
    }

    private WelcomeStart(Context c) {
        super();
        this.context = c;
        readFile();
    }

    public synchronized static WelcomeStart getInstance(Context c) {
        if (instance == null)
            instance = new WelcomeStart(c);

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

