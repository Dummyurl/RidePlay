package rnf.taxiad.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.models.DriverSession;

/**
 * Created by Rahil on 15/12/15.
 */
public class TaxiAdd extends Application {

    private final String username = "rnfdev";
    private final String password = "rnfdev@1234";

    @Override
    public void onCreate() {
        super.onCreate();
//       setupAuthenticator();
        Fresco.initialize(this);
        DriverSession.init(getApplicationContext());
        GlobalUtils.setStatesAbbrevation();
        FileDownloader.init(this);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
                System.exit(1);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/download");
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "RidePlay_Error.txt");
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            final List<String> message = getExceptionMessageChain(e);
            for (String str : message) {
                pw.println(str);
            }
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static List<String> getExceptionMessageChain(Throwable throwable) {
        List<String> result = new ArrayList<String>();
        while (throwable != null) {
            result.add(throwable.getMessage());
            throwable = throwable.getCause();
        }
        return result; //["THIRD EXCEPTION", "SECOND EXCEPTION", "FIRST EXCEPTION"]
    }

    private void setupAuthenticator() {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,
                        password.toCharArray());
            }

        });
    }
}
