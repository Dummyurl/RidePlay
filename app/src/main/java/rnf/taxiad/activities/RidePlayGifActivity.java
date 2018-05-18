package rnf.taxiad.activities;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import rnf.taxiad.R;
import rnf.taxiad.views.GifImageView;

/**
 * Created by rnf-new on 6/3/17.
 */

public class RidePlayGifActivity extends BaseActivity implements View.OnClickListener {

//    WebView gifDraweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gif_layout);
        findViewById(R.id.tvContinue).setOnClickListener(this);
        setGifView();
    }


    private void setGifView() {
//        gifDraweeView = (WebView) findViewById(R.id.gifDraweeView);
//        gifDraweeView.loadUrl("file:///android_asset/welcome.gif");
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (anim != null) {
                    // app-specific logic to enable animation starting
                    anim.start();
                }
            }
        };

        SimpleDraweeView imageView = (SimpleDraweeView) findViewById(R.id.imageView);
        Uri uri = Uri.parse("asset:///welcome.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder().
                setUri(uri).
                setControllerListener(controllerListener).
                setAutoPlayAnimations(true).build();
        imageView.setController(controller);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvContinue) {
            startActivityClearAll(LoginActivity.class);
        }
    }
}
