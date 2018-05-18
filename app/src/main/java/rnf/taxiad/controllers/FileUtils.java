package rnf.taxiad.controllers;

import android.content.Context;
import android.os.Environment;
import android.webkit.URLUtil;

import java.io.File;
import java.util.List;

import rnf.taxiad.models.Ads;

/**
 * Created by Rahil on 3/3/16.
 */
public class FileUtils {

    public static String FOLDER_NAME = ".InnertubeVideos";
    private Context context;

    public FileUtils(Context context) {
        super();
        this.context = context;
    }

    public void deleteAll(final List<Ads> ads) {
        if (ads == null)
            return;
        final Thread thread = new Thread() {
            @Override
            public void run() {
                delete(ads);
            }
        };
        thread.start();
    }

    public synchronized void delete(List<Ads> ads) {
        final String path = FileUtils.getVideoFolderPath(context);
        final File f = new File(path);
        if (f.exists()) {
            File[] files = f.listFiles();
            for (File file : files) {
                for (Ads a : ads) {
                    final String fileName = FileUtils.getFileName(a._adUrl);
                    if (file.getName().equals(fileName))
                        file.delete();
                }
            }
        }


    }

    public synchronized void deleteAll() {
        final String path = FileUtils.getVideoFolderPath(context);
        final File f = new File(path);
        if (f.exists()) {
            File[] files = f.listFiles();
            for (File file : files) {

                file.delete();

            }

        }


    }


    public static String getVideoFolderPath(Context c) {
        String main = Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME;
        File mediaStorageDir = new File(main);
        if (!mediaStorageDir.exists())
            mediaStorageDir.mkdirs();
        return mediaStorageDir.getPath();
    }


    public static String getFileName(String url) {
        String name = URLUtil.guessFileName(url, null, null);
        if (name == null || name.length() == 0)
            name = url.substring(url.lastIndexOf('/') + 1);
        return name;
    }
}
