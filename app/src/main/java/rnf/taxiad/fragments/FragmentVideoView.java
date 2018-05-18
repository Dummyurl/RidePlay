package rnf.taxiad.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.File;

import rnf.taxiad.R;
import rnf.taxiad.activities.ClickInfoActivity;
import rnf.taxiad.activities.HomeActivity;
import rnf.taxiad.activities.TextToFeatureActivity;
import rnf.taxiad.controllers.FileUtils;
import rnf.taxiad.providers.ItemsContract;
import rnf.taxiad.videoplayer.EasyVideoCallback;
import rnf.taxiad.videoplayer.EasyVideoPlayer;

/**
 * Created by Rahil on 11/1/16.
 */
public class FragmentVideoView extends Fragment
        implements View.OnClickListener, EasyVideoCallback {

    private EasyVideoPlayer player;
    private ImageButton btnPlay;
    private Uri video;
    private HomeActivity parentActivity;
    private int position = 0;
    private long startTime = 0L, endTime = 0L, time = 0L;
    private boolean isRunning = false;

    TextView textFeature;

    boolean buttonclick = false, pause = false, play = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof HomeActivity)
            parentActivity = (HomeActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        position = getArguments().getInt("position", 0);
        try {
            btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
            btnPlay.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        player = (EasyVideoPlayer) view.findViewById(R.id.videoPlayer);

        player.setCallback(this);
        player.disableControls();
        if (isVideo()) {
            setVideoUri();
            if (getUserVisibleHint()) {
                player.setAutoPlay(true);
                if (player.isPrepared())
                    player.start();
            }
        } else {
            setImageUri();
        }
        if (getUserVisibleHint()) {
           /* enterStats(0, 1, 0, 0);*/
        }
        textFeature = (TextView) view.findViewById(R.id.tvTextFeature);
        textFeature.setOnClickListener(getTwilioListener());

        setTwilioText();
    }

    public boolean isVideo() {
        final String type = getArguments().getString(ItemsContract.Videos._TYPE, "video");

        if (!type.equalsIgnoreCase("video"))
            return false;
        return true;
    }

    private void setVideoUri() {
        if (getView() == null)
            return;
        try {
            getView().findViewById(R.id.videoViewLayout).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.imageView).setVisibility(View.GONE);
            getView().findViewById(R.id.btnPlay).setVisibility(View.VISIBLE);
            final String url = getArguments().getString(ItemsContract.Videos._AD_URL, null);
            final String path = FileUtils.getVideoFolderPath(getContext()) + File.separator +
                    FileUtils.getFileName(url);
            video = Uri.parse(path);
            player.setSource(video);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setImageUri() {
        if (getView() == null)
            return;
        try {
            getView().findViewById(R.id.videoViewLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.imageView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.btnPlay).setVisibility(View.GONE);
            final String url = getArguments().getString(ItemsContract.Videos._AD_URL, null);
//            final String path = FileUtils.getVideoFolderPath(getContext()) + File.separator +
//                    FileUtils.getFileName(url);
            final SimpleDraweeView imageView = (SimpleDraweeView) getView().findViewById(R.id.imageView);
            imageView.setImageURI(Uri.parse(url));
            if (parentActivity != null && getUserVisibleHint()) {
                parentActivity.setScrollTime(15000, position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else
            onHide();
    }

    private void onVisible() {
//        error = false;
        startTime = System.currentTimeMillis();
        checkAndDownload();
        setTwilioText();
      /*  enterStats(0, 1, 0, 0);*/
        if (isVideo()) {
            if (timer != null) {
                timer.start();
                isRunning = true;
            }
            if (player != null) {
                player.setAutoPlay(true);
                if (!player.isPlaying())
                    player.start();
                else
                    setVideoUri();
            }
        } else
            setImageUri();

    }

    private void onHide() {
//        error = false;
        endTime = System.currentTimeMillis();
        time = endTime - startTime;
       /* enterStats(0, 0, 0, (int) time);*/
        if (!isVideo()) {
            return;
        }
        if (timer != null)
            timer.cancel();
        isRunning = false;
        if (player != null) {
            player.setAutoPlay(false);
            player.seekTo(0);
            player.pause();
        }
    }

    private void checkAndDownload() {
        final String url = getArguments().getString(ItemsContract.Videos._AD_URL, null);
        final String path = FileUtils.getVideoFolderPath(getContext()) + File.separator +
                FileUtils.getFileName(url);
        if (getActivity() instanceof HomeActivity)
            ((HomeActivity) getActivity()).checkIfNeedsToDownload(path);
    }

    private void setTwilioText() {
        if (getView() == null)
            return;
        try {
            final String tagline = getArguments().getString(ItemsContract.Videos._TAGLINE, null);
            final String twilioText = getArguments().getString(ItemsContract.Videos._KEYWORD, null);
            String twilioNumber = getArguments().getString(ItemsContract.Videos._NUMBER, null);
            final String title = getArguments().getString(ItemsContract.Videos._AD_TITLE, null);
            final String adsId = getArguments().getString(ItemsContract.Videos._AD_ID, null);
            final TextView tvTwilio = (TextView) getView().findViewById(R.id.tvTwilio);

            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            if (twilioNumber != null && twilioNumber.trim().length() > 0) {
                Phonenumber.PhoneNumber pn = pnu.parse(twilioNumber, "US");
                twilioNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            }

           /* String twilioString = String.format("%1s text %2s to %3s",
                    tagline == null || tagline.trim().length() == 0 ? "" : tagline + ",",
                    twilioText == null ? "" : twilioText,
                    twilioNumber == null ? "" : twilioNumber);*/

            String twilioString = String.format("%1s",
                    tagline == null || tagline.trim().length() == 0 ? "" : tagline + "");

            if ((tagline == null || tagline.trim().length() == 0)
                    && (twilioText == null || twilioText.trim().length() == 0)
                    && (twilioNumber == null || twilioNumber.trim().length() == 0))
                tvTwilio.setVisibility(View.GONE);
            else
                tvTwilio.setVisibility(View.VISIBLE);

            twilioString = twilioString.replace("+", "");
            //tvTwilio.setOnClickListener(getTwilioListener());

            tvTwilio.setText(twilioString);
            tvTwilio.setSelected(true);
            TextView tvHeader = (TextView) getView().findViewById(R.id.tvHeader);
            tvHeader.setText(title == null ? "" : title);
            tvHeader.setSelected(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (player.isPlaying()) {
            pause = true;
            player.pause();

        } else {
            play = true;
            player.start();
        }

        /* enterStats(0, 0, 1, 0);*/
        buttonclick = true;
    }


    public void refresh() {
        if (getView() == null)
            return;
        if (isVideo()) {
            if (player != null) {
                setVideoUri();
                player.start();
                player.setAutoPlay(true);
//                if (!player.isPlaying()) {
//                } else
            }
        }
        setTwilioText();
    }

    @Override
    public void onStarted(EasyVideoPlayer easyVideoPlayer) {
        int duration = easyVideoPlayer.getDuration();
        if (duration > 0 && timer != null)
            timer.cancel();
        if (parentActivity != null)
            parentActivity.setScrollTime(duration == 0 ? 15000 : duration, position);
        setButton(true);
        /*enterStats(1, 0, 0, 0);*/

        startTime = System.currentTimeMillis();
    }

    @Override
    public void onPaused(EasyVideoPlayer easyVideoPlayer) {
        setButton(false);

        endTime = System.currentTimeMillis();
        time = endTime - startTime;
        if (buttonclick) {
            if (play) {
                enterStats(0, 0, 1, (int) time);
            } else if (pause) {
                enterStats(0, 0, 1, (int) time);
            }
        }


       /* enterStats(0, 0, 1, 0);*/
        int scrollTime = easyVideoPlayer.getDuration() - easyVideoPlayer.getCurrentPosition();
        if (parentActivity != null && getUserVisibleHint())
            parentActivity.setScrollTime(scrollTime, position);

    }

    @Override
    public void onPreparing(EasyVideoPlayer easyVideoPlayer) {
    }

    @Override
    public void onPrepared(EasyVideoPlayer easyVideoPlayer) {
        if (!getUserVisibleHint())
            return;
        if (parentActivity != null)
            parentActivity.setScrollTime(15000, position);
        easyVideoPlayer.start();
    }

    @Override
    public void onBuffering(int i) {

    }

    @Override
    public void onError(EasyVideoPlayer easyVideoPlayer, Exception e) {
//        error = true;
//        if (!getUserVisibleHint())
//            return;
//        if (parentActivity != null)
//            parentActivity.setScrollTime(15000, position);
//        easyVideoPlayer.start();
    }

    @Override
    public void onCompletion(EasyVideoPlayer easyVideoPlayer) {
        setButton(false);

        endTime = System.currentTimeMillis();
        time = endTime - startTime;
        if (buttonclick) {
            enterStats(1, 1, 1, (int) time);
        } else {
            enterStats(1, 1, 0, (int) time);
        }

        if (getActivity() instanceof HomeActivity && !isRunning) {
            if (!((HomeActivity) getActivity()).scroll() && getUserVisibleHint()) {
                easyVideoPlayer.start();

            } else {
                //easyVideoPlayer.release();
            }

        }
//        error = false;
    }

    @Override
    public void onRetry(EasyVideoPlayer easyVideoPlayer, Uri uri) {
    }

    @Override
    public void onSubmit(EasyVideoPlayer easyVideoPlayer, Uri uri) {
    }

    public void statsForImage() {
        /*enterStats(0, 1, 0, 15000);*/

    }

//    private class StatsRunnable implements Runnable {
//
//        private int played, viewed, taped, duration;
//
//        public StatsRunnable(int played, int viewed, int taped, int duration) {
//            this.played = played;
//            this.viewed = viewed;
//            this.taped = taped;
//            this.duration = duration;
//        }
//
//        @Override
//        public void run() {
//            enterStats(played, viewed, taped, duration);
//        }
//    }

    public synchronized void enterStats(int played, int viewed, int taped, int duration) {
        final String adId = getArguments().getString(ItemsContract.Videos._AD_ID, null);
        final String advertiserId = getArguments().getString(ItemsContract.Videos._ADVERTISER_ID, null);
        if (adId == null)
            return;
        if (!(getActivity() instanceof HomeActivity))
            return;
        final HomeActivity parentActivity = (HomeActivity) getActivity();
        parentActivity.enterStats(adId, played, viewed, taped, duration, advertiserId);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null && player.isPlaying())
            player.pause();
    }

    private void setButton(boolean isSelected) {
        try {
            if (btnPlay != null)
                btnPlay.setSelected(isSelected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseVideo() {
        //if (player !=null)
    }

//    @Override
//    public void onDestroyView() {
//        if(pla)
//        super.onDestroyView();
//    }

    private CountDownTimer timer = new CountDownTimer(15000, 1) {
        @Override
        public void onTick(long millisUntilFinished) {
            isRunning = true;
        }

        @Override
        public void onFinish() {
            isRunning = false;
            if (!(getActivity() instanceof HomeActivity))
                return;
            if (!player.isPlaying() && getUserVisibleHint()) {
                ((HomeActivity) getActivity()).scroll();
               /* enterStats(0, 0, 0, 15000);*/
            }
        }
    };


    private View.OnClickListener getTwilioListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TextToFeatureActivity.class);
                i.putExtra("adsId", parentActivity.getCurrentAdsId());
                startActivity(i);
                textFeature.getWidth();

            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
