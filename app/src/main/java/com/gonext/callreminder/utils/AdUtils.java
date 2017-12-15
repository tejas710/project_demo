package com.gonext.callreminder.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.gonext.callreminder.BuildConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Lenovo.
 */

public class AdUtils {

    private static InterstitialAd mInterstitialAd;

    /**
     * Method to load intestial Advertisement
     * @param context Instance of Activity/fragment
     */
    public static  void loadIntestial(Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(BuildConfig.INTERSTITIAL_ID);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

            }
        });
        requestNewInterstitial();
    }

    /**
     * Method to request for Advertisement
     */
    private static  void requestNewInterstitial() {
        if (mInterstitialAd != null) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    public static void displayIntestial() {
        if (mInterstitialAd != null) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    }

    /**
     * Method to display Banner Advertisement
     *
     * @param adsLayout it can be LinearLayout or RelativeLayout
     */
    public static void displayBanner(final ViewGroup adsLayout, Context context) {
        AdView avAds = new AdView(context);

        avAds.setAdSize(AdSize.SMART_BANNER);
        avAds.setAdUnitId(BuildConfig.BANNER_SMART_ID);
        adsLayout.addView(avAds);
        AdRequest adRequest = new AdRequest.Builder().build();

        avAds.loadAd(adRequest);

        avAds.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (adsLayout != null) {
                    adsLayout.setVisibility(View.VISIBLE);
                }
                super.onAdLoaded();

            }
        });
    }

}
