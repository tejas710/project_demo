package com.gonext.callreminder.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.gonext.callreminder.BuildConfig;
import com.gonext.callreminder.R;
import com.gonext.callreminder.utils.PermissionUtils;
import com.gonext.callreminder.utils.view.CustomTextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.BindView;

/**
 * Created by Dinesh Hirani.
 */

public class SplashActivity extends BaseActivity {

    CountDownTimer waitTimer;
    InterstitialAd mInterstitialAd;
    boolean isIntentStart = false;


    @BindView(R.id.tvAppVersion)
    CustomTextView tvAppVersion;
    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_splash;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init() {
        showAppVersion();
        mInterstitialAd = new InterstitialAd(SplashActivity.this);
        mInterstitialAd.setAdUnitId(BuildConfig.INTERSTITIAL_ID);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                sendIntentToHomeActivity();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                cancelTimer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancelTimer();
                        checkUserPermission(false);
                    }
                }, 3 * 1000); // wait for 3 seconds
            }

            @Override
            public void onAdLoaded() {
                // Change the button text and enable the button.
                cancelTimer();
                checkUserPermission(true);
            }

        });
        requestNewInterstitial();
        int SPLASH_TIME_OUT = 15000;
        waitTimer = new CountDownTimer(SPLASH_TIME_OUT, 1000) {
            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
            }
            public void onFinish() {
                checkUserPermission(false);
            }
        }.start();

    }
    private void sendIntentToHomeActivity() {
        if (!isIntentStart) {
            isIntentStart = true;
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(getString(R.string.splash),7);
            navigateToDifferentScreen(intent,true);
        }
    }
    private void cancelTimer() {
        if (waitTimer != null) {
            waitTimer.cancel();
            waitTimer = null;
        }
    }
    private void showAppVersion() {
        if (BuildConfig.DEBUG) {
            tvAppVersion.setText(getString(R.string.app_version).concat(BuildConfig.VERSION_NAME).concat(BuildConfig.BUILD_TYPE));
        }else{
            tvAppVersion.setText(getString(R.string.app_version).concat(BuildConfig.VERSION_NAME));
        }
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void checkUserPermission(boolean isAddLoaded){
        if (PERMISSIONS.length>0) {
            if (PermissionUtils.hasPermissions(SplashActivity.this, PERMISSIONS)) {
                showAds(isAddLoaded);
            } else {
                shouldShowRequestPermissions();
            }
        }else{
            showAds(isAddLoaded);
        }
    }

    private void showAds(boolean isAddLoaded){
        if (!isIntentStart) {
            if (isAddLoaded) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            } else {
                sendIntentToHomeActivity();
            }
        }
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    sendIntentToHomeActivity();
                }
            }
        }
    }
}
