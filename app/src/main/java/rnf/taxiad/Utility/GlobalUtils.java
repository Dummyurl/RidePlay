package rnf.taxiad.Utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rnf.taxiad.activities.LoginActivity;
import rnf.taxiad.cryptography.CryptLib;
import rnf.taxiad.models.DriverSession;

/**
 * Created by Rahil on 7/10/15.
 */
public final class GlobalUtils {

    public static String key = "a0123456789bcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ0123456789";
    public static Map<String, String> STATE_MAP = null;

    /**
     * hide software keyboard
     */
    public static void hideKeyboard(EditText edt) {
        try {
            InputMethodManager imm = (InputMethodManager) edt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getTimestamp(String date, String currentFormat) {
        SimpleDateFormat format = new SimpleDateFormat(currentFormat);
        try {
            Date newDate = format.parse(date);
            return newDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static void clearFileDir(Context context) {
        File cache = context.getFilesDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static void showError(Context c, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static String getHtml(String data) {
        String html = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + data;
        return html;
    }

    public static String join(ArrayList<String> values) {
        ArrayList<String> list = new ArrayList<>();
        for (String o : values
                ) {
            if (o != null && !o.equals("null"))
                list.add(o);
        }
        return TextUtils.join(", ", list);
    }

    public static String encrypt(Map<String, String> params) {
        String encryptedString = "";
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(entry.getKey());
                encodedParams.append('=');
                encodedParams.append(entry.getValue());
                encodedParams.append('&');
            }
            encryptedString = CryptLib.convert(encodedParams.toString(), CryptLib.EncryptMode.ENCRYPT, key);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public static String decrypt(String json) {
        String decyptedString = "";
        try {
            final JSONObject obj = new JSONObject(json);
            final String data = obj.getString("Value");
            decyptedString = CryptLib.convert(data, CryptLib.EncryptMode.DECRYPT, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decyptedString;
    }

    public static String getBytesDownloaded(int progress, long totalBytes) {
        //Greater than 1 MB
        long bytesCompleted = (progress * totalBytes) / 100;
        if (totalBytes >= 1000000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000000)) + "MB");
        }
        if (totalBytes >= 1000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000)) + "Kb");

        } else {
            return ("" + bytesCompleted + "/" + totalBytes);
        }
    }

    public static int getPercentage(int totalBytes, int downloaded) {
        float total = totalBytes;
        float download = downloaded;
        float v = download / total;
        v = v * 100;
        int value = (int) v;
        return value;
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(System.currentTimeMillis());
    }

    public static String formatTimestamp(long timestamp, String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timestamp);
    }

    public static String stringForTime(final int timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
//        if (hours > 0) {
//
//        } else {
//            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
//        }
    }

    public static double getTwoDecimal(double value) {
        NumberFormat format = NumberFormat.getIntegerInstance(Locale.US);
        format.setMaximumFractionDigits(3);
        format.setMinimumFractionDigits(3);
        format.setGroupingUsed(false);
        String str = format.format(value);
        return Double.valueOf(str);
    }

//    private class GetThumnail extends AsyncTask<Bitmap, Void, Bitmap> {
//
//        private Uri videoURI;
//        private long current = 0;
//
//        public GetThumnail(Uri videoURI) {
//            this.videoURI = videoURI;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            isRunning = true;
//            if (videoView != null)
//                current = videoView.getCurrentPosition();
//        }
//
//        @Override
//        protected Bitmap doInBackground(Bitmap... params) {
//            if (videoURI == null)
//                return null;
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(getActivity(), videoURI);
//            Bitmap bitmap = retriever.getFrameAtTime(current, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            if (bitmap != null && imageView != null)
//                imageView.setImageBitmap(bitmap);
//            isRunning = false;
//        }
//    }


    public static void saveLastDownloaded(Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        AppPreferenceManager.getInstance(context).saveString(Constants.LAST_DOWNLOADED,
                simpleDateFormat.format(System.currentTimeMillis()));

    }


    public static void logout(Context context, String message) {
        DriverSession.get().removeSession();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("message", message);
        context.startActivity(i);
    }

    public static boolean isMyAppLauncherDefault(Context context) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final String myPackageName = context.getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) context.getPackageManager();
        packageManager.getPreferredActivities(filters, activities, null);
        for (ComponentName activity : activities) {
            if (myPackageName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /*public static void resetPreferredLauncherAndOpenChooser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, rnf.taxiad.activities.DummyLauncher.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.addCategory(Intent.CATEGORY_DEFAULT);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }*/

    public static void setStatesAbbrevation() {
        if (STATE_MAP == null) {
            STATE_MAP = new HashMap<String, String>();
            STATE_MAP.put("Alabama", "AL");
            STATE_MAP.put("Alaska", "AK");
            STATE_MAP.put("Alberta", "AB");
            STATE_MAP.put("Arizona", "AZ");
            STATE_MAP.put("Arkansas", "AR");
            STATE_MAP.put("British Columbia", "BC");
            STATE_MAP.put("California", "CA");
            STATE_MAP.put("Colorado", "CO");
            STATE_MAP.put("Connecticut", "CT");
            STATE_MAP.put("Delaware", "DE");
            STATE_MAP.put("District Of Columbia", "DC");
            STATE_MAP.put("Florida", "FL");
            STATE_MAP.put("Georgia", "GA");
            STATE_MAP.put("Guam", "GU");
            STATE_MAP.put("Hawaii", "HI");
            STATE_MAP.put("Idaho", "ID");
            STATE_MAP.put("Illinois", "IL");
            STATE_MAP.put("Indiana", "IN");
            STATE_MAP.put("Iowa", "IA");
            STATE_MAP.put("Kansas", "KS");
            STATE_MAP.put("Kentucky", "KY");
            STATE_MAP.put("Louisiana", "LA");
            STATE_MAP.put("Maine", "ME");
            STATE_MAP.put("Manitoba", "MB");
            STATE_MAP.put("Maryland", "MD");
            STATE_MAP.put("Massachusetts", "MA");
            STATE_MAP.put("Michigan", "MI");
            STATE_MAP.put("Minnesota", "MN");
            STATE_MAP.put("Mississippi", "MS");
            STATE_MAP.put("Missouri", "MO");
            STATE_MAP.put("Montana", "MT");
            STATE_MAP.put("Nebraska", "NE");
            STATE_MAP.put("Nevada", "NV");
            STATE_MAP.put("New Brunswick", "NB");
            STATE_MAP.put("New Hampshire", "NH");
            STATE_MAP.put("New Jersey", "NJ");
            STATE_MAP.put("New Mexico", "NM");
            STATE_MAP.put("New York", "NY");
            STATE_MAP.put("Newfoundland", "NF");
            STATE_MAP.put("North Carolina", "NC");
            STATE_MAP.put("North Dakota", "ND");
            STATE_MAP.put("Northwest Territories", "NT");
            STATE_MAP.put("Nova Scotia", "NS");
            STATE_MAP.put("Nunavut", "NU");
            STATE_MAP.put("Ohio", "OH");
            STATE_MAP.put("Oklahoma", "OK");
            STATE_MAP.put("Ontario", "ON");
            STATE_MAP.put("Oregon", "OR");
            STATE_MAP.put("Pennsylvania", "PA");
            STATE_MAP.put("Prince Edward Island", "PE");
            STATE_MAP.put("Puerto Rico", "PR");
            STATE_MAP.put("Quebec", "QC");
            STATE_MAP.put("Rhode Island", "RI");
            STATE_MAP.put("Saskatchewan", "SK");
            STATE_MAP.put("South Carolina", "SC");
            STATE_MAP.put("South Dakota", "SD");
            STATE_MAP.put("Tennessee", "TN");
            STATE_MAP.put("Texas", "TX");
            STATE_MAP.put("Utah", "UT");
            STATE_MAP.put("Vermont", "VT");
            STATE_MAP.put("Virgin Islands", "VI");
            STATE_MAP.put("Virginia", "VA");
            STATE_MAP.put("Washington", "WA");
            STATE_MAP.put("West Virginia", "WV");
            STATE_MAP.put("Wisconsin", "WI");
            STATE_MAP.put("Wyoming", "WY");
            STATE_MAP.put("Yukon Territory", "YT");
        }

    }

    public static String getStateAbbrevation(String state) {
        if (STATE_MAP != null) {
            if (STATE_MAP.containsKey(state)) {
                return STATE_MAP.get(state);
            } else {
                return state;
            }
        }
        return "";
    }


    public static String getAddressUrl(String lat, String longt) {
        //return "http://nominatim.openstreetmap.org/reverse?format=json&lat=" + lat + "&lon=" + longt + "&zoom=18&addressdetails=1";
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + longt + "&senser=true&addressdetails=1";
    }

    public static String convertStringIntoMd5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }

    public synchronized static void writeLogs(String message) {
        String logFilePath = Environment.getExternalStorageDirectory().toString() + "/RidePlay_Logs";
        File dir = new File(logFilePath);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(logFilePath, "logs.txt");
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        Date date = new Date(System.currentTimeMillis());

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("\n"+date.toString() + "\n\n" + message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}


