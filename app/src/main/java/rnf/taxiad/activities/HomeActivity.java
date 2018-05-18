package rnf.taxiad.activities;

import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import rnf.taxiad.R;
import rnf.taxiad.Utility.AppPreferenceManager;
import rnf.taxiad.Utility.GlobalUtils;
import rnf.taxiad.Utility.MyCountDownTimer;
import rnf.taxiad.adapters.VideoAdapter;
import rnf.taxiad.controllers.AdsController;
import rnf.taxiad.controllers.BroadcastController;
import rnf.taxiad.controllers.VideoDownloader;
import rnf.taxiad.fragments.FragmentVideoView;
import rnf.taxiad.helpers.Broadcaster;
import rnf.taxiad.helpers.DownloadCompleted;
import rnf.taxiad.helpers.GetAdsListClient;
import rnf.taxiad.interfaces.OnAdsReceived;
import rnf.taxiad.locations.LocationService;
import rnf.taxiad.models.Ads;
import rnf.taxiad.models.DriverSession;
import rnf.taxiad.models.LocationModule;
import rnf.taxiad.providers.ItemsContract;
import rnf.taxiad.providers.TaxiContentProvider;
import rnf.taxiad.views.ScrollViewPager;

public class HomeActivity extends LocationServicesActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnLongClickListener, DownloadCompleted {

    private final int LOADER_ID = 0101;
    private DrawerLayout drawerLayout;
    private VideoAdapter adapter;
    public ScrollViewPager videoPager;
    private AudioManager audio;
    private DonutProgress donutProgress;
    private AlertDialog alertDialog;
    private MyCountDownTimer myCountDownTimer;
    private ViewAnimator refreshanimator;
    private LocationModule locationModule;
    private AdsController adsController;

    private Animation rotateAnim;
    ImageView btnRefresh;

    FragmentVideoView fragmentVideoView;

    private Intent viewDemoIntent;
    private Intent checkOutIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*if (!GlobalUtils.isMyAppLauncherDefault(HomeActivity.this))
            GlobalUtils.resetPreferredLauncherAndOpenChooser(HomeActivity.this);*/
        setupViews();
        init();
        adsController = new AdsController(this);
    }

    private void init() {
        rotateAnim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.rotate_anim);
        rotateAnim.setDuration(1000);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        myCountDownTimer = new MyCountDownTimer(15 * 1000, 1 * 1000, HomeActivity.this);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        getContentResolver().registerContentObserver(ItemsContract.Videos.CONTENT_URI, true, observer);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        startService(new Intent(this, LocationService.class).putExtra("fromHome","yes"));
    }


    private void setupViews() {
        btnRefresh = (ImageView) findViewById(R.id.btnRefresh);
        refreshanimator = (ViewAnimator) findViewById(R.id.refreshanimator);
        donutProgress = (DonutProgress) findViewById(R.id.arc_progress);
        videoPager = (ScrollViewPager) findViewById(R.id.videoPager);
        videoPager.setOffscreenPageLimit(1);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        adapter = new VideoAdapter(getSupportFragmentManager(),
                FragmentVideoView.class, ItemsContract.Videos.PROJECTION_ALL, null);
        videoPager.setAdapter(adapter);
        videoPager.addOnPageChangeListener(this);
        videoPager.setInterval(AutoScrollViewPager.DEFAULT_INTERVAL);
        videoPager.setDirection(AutoScrollViewPager.RIGHT);
        videoPager.setCycle(true);
        videoPager.setScrollDurationFactor(3);
        videoPager.startAutoScroll();
        findViewById(R.id.radioGroup).setOnClickListener(this);
//        findViewById(R.id.radioGroup).setOnLongClickListener(this);
        Broadcaster.get().addDownloadCompleteListener(this);
        showLastDownloaded();
        findViewById(R.id.btnMenu).setOnClickListener(this);
        final TextView textView = (TextView) findViewById(R.id.tvClickForInfo);
        textView.setText(Html.fromHtml(getString(R.string.msg_interested_in_advertising)));
        findViewById(R.id.tvClickForInfo).setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        (findViewById(R.id.tvCheckOut)).setOnClickListener(this);
        (findViewById(R.id.tvDemo)).setOnClickListener(this);
    }


    private void adjustArrow() {
        View vLeft = findViewById(R.id.indicatorLeft);
        View vRight = findViewById(R.id.indicatorRight);
        if (adapter.getCount() <= 1) {
            vLeft.setVisibility(View.GONE);
            vRight.setVisibility(View.GONE);
            return;
        }
        int current = videoPager.getCurrentItem();

        if (current > 0 && current < adapter.getCount() - 1) {
            vLeft.setVisibility(View.VISIBLE);
            vRight.setVisibility(View.VISIBLE);
            return;
        }

        if (current == 0) {
            vLeft.setVisibility(View.GONE);
            vRight.setVisibility(View.VISIBLE);
        }

        if (current == adapter.getCount() - 1) {
            vLeft.setVisibility(View.VISIBLE);
            vRight.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        setupNavigationView();
        renderVolume();
        if (videoPager != null) {
            refreshFragment(videoPager.getCurrentItem());
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMenu) {
            drawerLayout.openDrawer(Gravity.LEFT);
            myCountDownTimer.start();
        } else if (v.getId() == R.id.tvClickForInfo)
            startActivity(ClickInfoActivity.class);
        else if (v.getId() == R.id.radioGroup)
            checkAndAdjustVolume();
        else if (v.getId() == R.id.btnRefresh) {
            //getSupportLoaderManager().restartLoader(LOADER_ID, null, HomeActivity.this);

            if (locationModule != null) {
                getAdsListClient.makeRequest(locationModule.getCurrentCity(),
                        locationModule.getCurrentCountry(), locationModule.getCurrentState(),
                        locationModule.getCurrentCounty(), locationModule.getCurrentZipCode(),
                        locationModule.getCountryCode());
                startRefeshAnimation(true);
            }

        } else if (v.getId() == R.id.tvCheckOut) {
            drawerLayout.closeDrawer(Gravity.LEFT);
           /* Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.rideplay.tv/advertisers/#pricing-packages"));
            startActivity(i);*/

            Intent i = new Intent(HomeActivity.this, TextToFeatureActivity.class);
            startActivity(i);
        } else if (v.getId() == R.id.tvDemo) {
            drawerLayout.closeDrawer(Gravity.LEFT);
           /* Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.youtube.com/watch?v=dx6gRLPXnHc"));
            startActivity(i);*/
            startActivity(new Intent(HomeActivity.this, ViewDemoActivity.class));
        }

    }

    public void startRefeshAnimation(boolean flag) {
        if (flag) {
            btnRefresh.startAnimation(rotateAnim);
            btnRefresh.setClickable(false);
        } else {
            rotateAnim.cancel();
            rotateAnim.reset();
            btnRefresh.setClickable(true);
        }

    }

    public void checkAndAdjustVolume() {
        final AppCompatRadioButton button = (AppCompatRadioButton) findViewById(R.id.radioVolume);
        if (button.isChecked())
            mute();
        else
            volume();
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(Gravity.LEFT);
        if (myCountDownTimer != null)
            myCountDownTimer.cancel();

        switch (menuItem.getItemId()) {
            case R.id.account:
                startActivity(ManageAccountActivity.class);
                break;
//            case R.id.download:
//                break;
            case R.id.logout:
                if (alertDialog == null)
                    createDialog();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).
                        setTextColor(ContextCompat.getColor(this, R.color.color_button));
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).
                        setTextColor(ContextCompat.getColor(this, R.color.color_button));
                break;

            /*case R.id.link_checkout:
                if (menuItem.getIntent() != null)
                    startActivity(menuItem.getIntent());
                break;

            case R.id.link_demo:
                if (menuItem.getIntent() != null)
                    startActivity(menuItem.getIntent());
                break;*/
        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        adjustArrow();
//        refreshFragment(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String whereClause = "_ads_priority = 0, _ads_priority";
        CursorLoader loader = new CursorLoader(
                this,
                ItemsContract.Videos.CONTENT_URI,
                ItemsContract.Videos.PROJECTION_ALL,
                null,
                null,
                whereClause);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if (adapter != null)
            adapter.swapCursor(c);
//        adapter.notifyDataSetChanged();
        if (videoPager != null) {
            refreshFragment(videoPager.getCurrentItem());
            adjustArrow();
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, HomeActivity.this);
        }
    };

    @Override
    public void onLocationChanged(LocationModule locationModule) {
        super.onLocationChanged(locationModule);
        this.locationModule = locationModule;
        try {
            if (adapter == null || videoPager == null)
                return;
            final Fragment fragment = adapter.getCurrent(videoPager.getCurrentItem());
            if (fragment == null)
                return;
            final String adId = fragment.getArguments().getString(ItemsContract.Videos._AD_ID, null);
            final String advertiserId = fragment.getArguments().getString(ItemsContract.Videos._ADVERTISER_ID, null);
            final String driverId = fragment.getArguments().getString(ItemsContract.Videos._USER_ID, null);
//            final String driverId = String.valueOf(DriverSession.get().getDriver().userID);
            final String advertisementName = fragment.getArguments().getString(ItemsContract.Videos._ADVERTISER_NAME, null);
            if (locationModule.getLastAddress() == null || locationModule.getCurrentAddress() == null)
                return;
            BroadcastController.sendGhostCarBroadcast(this, locationModule.getLastAddress(),
                    locationModule.getCurrentAddress(), driverId, advertisementName,
                    adId, advertiserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            renderVolume();
        }
    };

    private void renderVolume() {
        final AppCompatRadioButton button = (AppCompatRadioButton) findViewById(R.id.radioVolume);
        int volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (volume == 0)
            button.setChecked(false);
        else
            button.setChecked(true);
    }

    private void volume() {
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
                AudioManager.FLAG_PLAY_SOUND);
    }

    private void mute() {
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    @Override
    public boolean onLongClick(View v) {
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
        startRefeshAnimation(false);
    }


    @Override
    public void onProgress(int total, int downloaded) {
        showProgress(total, downloaded);
    }

    @Override
    public void onCompleted() {
        showLastDownloaded();
//        if (adapter != null)
//            adapter.notifyDataSetChanged();
        if (getSupportFragmentManager() != null)
            getSupportLoaderManager().restartLoader(LOADER_ID, null, HomeActivity.this);
    }

    @Override
    public void onError(String adId) {

    }

    @Override
    public void onCompleted(String id) {
//        if (videoPager != null && videoPager.getCurrentItem() == 0)
//            refreshFragment(videoPager.getCurrentItem());
    }


    public void setScrollTime(int timeInMillis, int position) {
        videoPager.stopAutoScroll();
        videoPager.setInterval(timeInMillis);
        videoPager.startAutoScroll();
    }

//    public void stopScroll() {
//        videoPager.stopAutoScroll();
//    }
//
//    public void scroll() {
//        videoPager.scrollOnce();
//    }

    public void checkIfNeedsToDownload(String path) {
        final File f = new File(path);
        if (!f.exists())
            startDownload();
    }

    private void refreshFragment(int position) {
        Fragment fragment = adapter.getCurrent(position);
        if (fragment == null)
            return;
        fragmentVideoView = (FragmentVideoView) fragment;
        if (fragmentVideoView.getUserVisibleHint()) {
            fragmentVideoView.refresh();
        }
    }

    public void onScroll() {
        if (videoPager == null)
            return;
        Fragment fragment = adapter.getCurrent(videoPager.getCurrentItem());
        if (fragment == null)
            return;
        FragmentVideoView fragmentVideoView = (FragmentVideoView) fragment;
        fragmentVideoView.statsForImage();
    }

    private void showProgress(int total, int downloaded) {
        try {
            findViewById(R.id.lastDownloadLayout).setVisibility(View.GONE);
            findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
            refreshanimator.setVisibility(View.INVISIBLE);
            int progress = GlobalUtils.getPercentage(total, downloaded);
            donutProgress.setProgress(progress);
            TextView tv = (TextView) findViewById(R.id.tvBytes);
            String[] bytes = {android.text.format.Formatter.formatFileSize(getApplicationContext(), downloaded),
                    android.text.format.Formatter.formatFileSize(getApplicationContext(), total)};
            String text = TextUtils.join(" of ", bytes);
            tv.setText(text);
        } catch (Exception e) {
        }
    }

    private void showLastDownloaded() {
        try {
            findViewById(R.id.lastDownloadLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.progressLayout).setVisibility(View.GONE);
            refreshanimator.setVisibility(View.VISIBLE);
            long timestamp = AppPreferenceManager.getInstance(this).getLong(VideoDownloader.TIME, 0L);
            if (timestamp == 0L)
                return;
            final TextView tv = (TextView) findViewById(R.id.tvDownloadDate);
            tv.setText(DateFormat.format("dd MMM yyyy hh:mm a", timestamp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createDialog() {
        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Are you sure to logout?");
        b.setPositiveButton("OK", onClickListener);
        b.setNegativeButton("CANCEL", onClickListener);
        b.setCancelable(false);
        alertDialog = b.create();
    }

    private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                if (!isBound) {
                    unbindService(connection);
                    isBound = false;
                }
                startService(new Intent(getApplicationContext(), LocationService.class).putExtra("fromHome","no"));
                DriverSession.get().removeSession();
                startActivityClearAll(LoginActivity.class);
            }
            dialog.dismiss();
        }
    };

    public String getCurrentAdsId() {
        if (videoPager != null) {
            if (adapter != null) {
                Cursor cursor = adapter.getCursor();
                if (cursor != null && cursor.getCount() > 0) ;
                {
                    int currentpos = videoPager.getCurrentItem();
                    cursor.moveToPosition(currentpos);
                    int index = cursor.getColumnIndex(ItemsContract.Videos._AD_ID);
                    String adsid = cursor.getString(index);
                    return adsid;

                }
            }
        }
        return null;

    }


    private void setupNavigationView() {
        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        String[] a = {DriverSession.get().getDriver().fname == null ? "" : DriverSession.get().getDriver().fname,
                DriverSession.get().getDriver().lname == null ? "" : DriverSession.get().getDriver().lname};
        String fullName = TextUtils.join(" ", a);
        tvName.setText(fullName);
        tvEmail.setText(DriverSession.get().getDriver().email == null ? "" : DriverSession.get().getDriver().email);
        // Menu menu = navigationView.getMenu();

        String label = DriverSession.get().getDriver().driver_advocate_prog == null ? ""
                : DriverSession.get().getDriver().driver_advocate_prog;
        String checkOut = DriverSession.get().getDriver().link_to_checkout == null ? ""
                : DriverSession.get().getDriver().link_to_checkout;
        String demo = DriverSession.get().getDriver().link_to_demo == null ? ""
                : DriverSession.get().getDriver().link_to_demo;
        /*menu.findItem(R.id.driver_program).setTitle
                (DriverSession.get().getDriver().link_to_checkout == null ? "" :
                        Html.fromHtml(
                                "<b>User/Pass: </b>" + "<small>" + label + "<small/>"));
        menu.findItem(R.id.link_checkout).setTitle
                (DriverSession.get().getDriver().link_to_checkout == null ? "" :
                        Html.fromHtml(
                                "<b>Demo: </b>" +
                                        "<small>" + checkOut + "</small>"));
        menu.findItem(R.id.link_demo).setTitle
                (DriverSession.get().getDriver().link_to_demo == null ? "" :
                        Html.fromHtml("<b>Checkout: </b>" + "<small>" + demo + "</small>"));*/

        if (Patterns.WEB_URL.matcher(checkOut).matches()) {
            if (!checkOut.startsWith("http://") && !checkOut.startsWith("https://"))
                checkOut = "http://" + checkOut;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(checkOut));
            checkOutIntent = i;
            /*menu.findItem(R.id.link_checkout).setIntent(i);
            menu.findItem(R.id.link_checkout).setTitle
                    (Html.fromHtml(
                            "<b>Checkout: </b>" +
                                    "<small><a href=>" + checkOut + "</a></small>"));*/
        }
        if (Patterns.WEB_URL.matcher(demo).matches()) {
            if (!demo.startsWith("http://") && !demo.startsWith("https://"))
                demo = "http://" + demo;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(demo));
            viewDemoIntent = i;
           /* menu.findItem(R.id.link_demo).setIntent(i);
            menu.findItem(R.id.link_demo).setTitle
                    (Html.fromHtml(
                            "<b>Demo: </b>" +
                                    "<small><a href=>" + demo + "</a></small>"));*/
        }
    }

    public boolean scroll() {
        if (adapter.getCount() <= 1)
            return false;
        videoPager.scrollOnce();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private GetAdsListClient getAdsListClient = new GetAdsListClient(HomeActivity.this, new OnAdsReceived() {
        @Override
        public void onReceived(JSONArray jsonArray, String city, String country, String state, String county, String zipcode) {
            startRefeshAnimation(false);
            if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                drawerLayout.closeDrawer(Gravity.LEFT);
            if (jsonArray == null || jsonArray.length() == 0)
                return;
            if (locationModule != null) {
                if (locationModule.getLastCity().trim().length() != 0 && !locationModule.getLastCity().equalsIgnoreCase(city)
                        || locationModule.getLastCountry().trim().length() != 0 && !locationModule.getLastCountry().equalsIgnoreCase(country)
                        || locationModule.getLastState().trim().length() != 0 && !locationModule.getLastState().equalsIgnoreCase(state)
                        || locationModule.getLastCounty().trim().length() != 0 && !locationModule.getLastCounty().equalsIgnoreCase(county)
                        || locationModule.getLastZipCode().trim().length() != 0 && !locationModule.getLastZipCode().equalsIgnoreCase(zipcode))
                    deleteOldData(locationModule.getLastCity(), locationModule.getLastCountry(), locationModule.getLastState(), locationModule.getLastCounty(), locationModule.getLastZipCode());
                deleteAdsWhichAreNotAvailable(jsonArray);
                final List<Ads> ads = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        final Ads a = new Ads(jsonArray.getJSONObject(i));
                        if (adsController.needToUpdate(a))
                            adsController.update(a);
                        else if (!adsController.isAvailable(a)) {
                            ads.add(a);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (ads.size() > 0)
                    insert(ItemsContract.Videos.CONTENT_URI, adsController.getValues(ads));
            }
        }
    });


    private void applyBatch(List<ContentValues> contentValues) {
        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        for (ContentValues cv : contentValues) {
            ops.add(ContentProviderOperation.newInsert(ItemsContract.Videos.CONTENT_URI)
                    .withValue(ItemsContract.Videos._AD_ID, cv.get(ItemsContract.Videos._AD_ID))
                    .withValue(ItemsContract.Videos._USER_ID, cv.get(ItemsContract.Videos._USER_ID))
                    .withValue(ItemsContract.Videos._AD_URL, cv.get(ItemsContract.Videos._AD_URL))
                    .withValue(ItemsContract.Videos._THUMBNAIL_URL, cv.get(ItemsContract.Videos._THUMBNAIL_URL))
                    .withValue(ItemsContract.Videos._AD_TITLE, cv.get(ItemsContract.Videos._AD_TITLE))
                    .withValue(ItemsContract.Videos._KEYWORD, cv.get(ItemsContract.Videos._KEYWORD))
                    .withValue(ItemsContract.Videos._NUMBER, cv.get(ItemsContract.Videos._NUMBER))
                    .withValue(ItemsContract.Videos._ADVERTISER_ID, cv.get(ItemsContract.Videos._ADVERTISER_ID))
                    .withValue(ItemsContract.Videos._ADVERTISER_NAME, cv.get(ItemsContract.Videos._ADVERTISER_NAME))
                    .withValue(ItemsContract.Videos._TAGLINE, cv.get(ItemsContract.Videos._TAGLINE))
                    .withValue(ItemsContract.Videos._TYPE, cv.get(ItemsContract.Videos._TYPE))
                    .withValue(ItemsContract.Videos._PRIORITY, cv.get(ItemsContract.Videos._PRIORITY))
                    .withValue(ItemsContract.Videos._LOCATION, locationModule.getCurrentCity())
                    .withValue(ItemsContract.Videos._COUNTRY, locationModule.getCurrentCountry())
                    .withValue(ItemsContract.Videos._STATE, locationModule.getCurrentState())
                    .withValue(ItemsContract.Videos._COUNTY, locationModule.getCurrentCounty())
                    .withValue(ItemsContract.Videos._ZIP_CODE, locationModule.getCurrentZipCode())
                    .withYieldAllowed(true)
                    .build());
        }
        try {
            this.getContentResolver().applyBatch(ItemsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
        } catch (OperationApplicationException e) {
        }
    }

    private void deleteAdsWhichAreNotAvailable(JSONArray jsonArray) {
        final List<Ads> ads = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                final Ads a = new Ads(jsonArray.getJSONObject(i));
                ads.add(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (ads.size() > 0)
            adsController.deleteAdsWhichAreNotAvailable(ads);
    }

    private void deleteOldData(String lastCity, String lastcountry, String laststate, String lastcounty, String lastzipcode) {
        adsController.deleteDataByCity(lastCity, lastcountry, laststate, lastcounty, lastzipcode);
    }

    private void insert(Uri uri, List<ContentValues> contentValues) {
        switch (TaxiContentProvider.URI_MATCHER.match(uri)) {
            case TaxiContentProvider.ITEM_LIST:
                applyBatch(contentValues);
                break;
            case TaxiContentProvider.ITEM_ID:
                break;
            case TaxiContentProvider.STATS_LIST:
                break;
            case TaxiContentProvider.STATS_ID:
                break;

        }
    }


}

