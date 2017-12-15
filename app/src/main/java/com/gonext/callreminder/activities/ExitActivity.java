package com.gonext.callreminder.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gonext.callreminder.R;
import com.gonext.callreminder.datalayers.model.AdDataResponse;
import com.gonext.callreminder.datalayers.model.AdsOfThisCategory;
import com.gonext.callreminder.datalayers.serverad.OnAdLoaded;
import com.gonext.callreminder.utils.FileUtils;
import com.gonext.callreminder.utils.view.CustomTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class ExitActivity extends BaseActivity implements OnAdLoaded {


    @BindView(R.id.rlContent)
    RelativeLayout rlContent;
    @BindView(R.id.rlExitActivityRoot)
    RelativeLayout rlExitActivityRoot;
    @BindView(R.id.rlPolicy)
    RelativeLayout rlpolicy;
    @BindView(R.id.ivAds1)
    ImageView ivAds1;
    @BindView(R.id.tvAds1)
    TextView tvAds1;
    @BindView(R.id.llAds1)
    LinearLayout llAds1;
    @BindView(R.id.ivAds2)
    ImageView ivAds2;
    @BindView(R.id.tvAds2)
    TextView tvAds2;
    @BindView(R.id.llAds2)
    LinearLayout llAds2;
    @BindView(R.id.llHorizontal1)
    LinearLayout llHorizontal1;
    @BindView(R.id.ivAds3)
    ImageView ivAds3;
    @BindView(R.id.tvAds3)
    TextView tvAds3;
    @BindView(R.id.llAds3)
    LinearLayout llAds3;
    @BindView(R.id.ivAds4)
    ImageView ivAds4;
    @BindView(R.id.tvAds4)
    TextView tvAds4;
    @BindView(R.id.llAds4)
    LinearLayout llAds4;
    @BindView(R.id.llHorizontal2)
    LinearLayout llHorizontal2;
    @BindView(R.id.rlAdLayout)
    RelativeLayout rlAdLayout;

    int positionForAds1, positionForAds2, positionForAds3, positionForAds4;
    Animation zoomAnimationIvAdd1, zoomAnimationIvAdd2, zoomAnimationIvAdd3, zoomAnimationIvAdd4;
    AdDataResponse adDataResponse;
    List<AdsOfThisCategory> lstAdsThisCategory = new ArrayList<>();
    @BindView(R.id.tvTitle)
    CustomTextView tvTitle;
    @BindView(R.id.tvMessage)
    CustomTextView tvMessage;
    @BindView(R.id.addPrivacy)
    ImageView addPrivacy;
    @BindView(R.id.rlServerAd)
    RelativeLayout rlServerAd;
    @BindView(R.id.tvYes)
    CustomTextView tvYes;
    @BindView(R.id.tvNo)
    CustomTextView tvNo;
    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_exit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        zoomAnimationIvAdd1 = AnimationUtils.loadAnimation(this, R.anim.ad_zoom);
        zoomAnimationIvAdd2 = AnimationUtils.loadAnimation(this, R.anim.ad_zoom);
        zoomAnimationIvAdd3 = AnimationUtils.loadAnimation(this, R.anim.ad_zoom);
        zoomAnimationIvAdd4 = AnimationUtils.loadAnimation(this, R.anim.ad_zoom);
        getServerData();


    }

    private void getServerData() {
        String data = FileUtils.readeDataFromFile(ExitActivity.this);
        if (TextUtils.isEmpty(data)) {
            requestForServerAd(this);
        } else {
            adDataResponse = new Gson().fromJson(data, AdDataResponse.class);
            lstAdsThisCategory = adDataResponse.getData().get(0).getAdsOfThisCategory();
        }
        displayAdds();
    }

    @OnClick({R.id.rlPolicy, R.id.llAds1, R.id.llAds2, R.id.llAds3, R.id.llAds4, R.id.tvYes, R.id.tvNo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlPolicy:
                sendIntentToWebView();
                break;
            case R.id.llAds1:
                if (lstAdsThisCategory.size() > 0)
                    openPlayStore(lstAdsThisCategory.get(positionForAds1).getPlayStoreUrl());
                break;
            case R.id.llAds2:
                if (lstAdsThisCategory.size() > 0)
                    openPlayStore(lstAdsThisCategory.get(positionForAds2).getPlayStoreUrl());
                break;
            case R.id.llAds3:
                if (lstAdsThisCategory.size() > 0)
                    openPlayStore(lstAdsThisCategory.get(positionForAds3).getPlayStoreUrl());
                break;
            case R.id.llAds4:
                if (lstAdsThisCategory.size() > 0)
                    openPlayStore(lstAdsThisCategory.get(positionForAds4).getPlayStoreUrl());
                break;
            case R.id.tvYes:
                finishAffinity();
                break;
            case R.id.tvNo:
                finish();
                break;
        }
    }

    @Override
    public void adLoad() {
        if (rlAdLayout != null) {
            getServerData();
        }
    }

    @Override
    protected void onResume() {
        getServerData();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void displayAdds() {
        rlAdLayout.setVisibility(View.VISIBLE);
        if (lstAdsThisCategory.size() >= 2) {
            positionForAds1 = getRandom();
            FileUtils.loadImageFromPath(ivAds1, lstAdsThisCategory.get(positionForAds1).getAppLogo(), lstAdsThisCategory.get(positionForAds1).getAppName(), tvAds1, this);
            positionForAds2 = getRandom();
            while (positionForAds1 == positionForAds2) {
                positionForAds2 = getRandom();
            }
            FileUtils.loadImageFromPath(ivAds2, lstAdsThisCategory.get(positionForAds2).getAppLogo(), lstAdsThisCategory.get(positionForAds2).getAppName(), tvAds2, this);
        }

        if (lstAdsThisCategory.size() > 3) {
            positionForAds3 = getRandom();
            while (positionForAds1 == positionForAds3 || positionForAds2 == positionForAds3) {
                positionForAds3 = getRandom();
            }
            FileUtils.loadImageFromPath(ivAds3, lstAdsThisCategory.get(positionForAds3).getAppLogo(), lstAdsThisCategory.get(positionForAds3).getAppName(), tvAds3, this);
            positionForAds4 = getRandom();
            while (positionForAds1 == positionForAds4 || positionForAds2 == positionForAds4 || positionForAds3 == positionForAds4) {
                positionForAds4 = getRandom();
            }
            FileUtils.loadImageFromPath(ivAds4, lstAdsThisCategory.get(positionForAds4).getAppLogo(), lstAdsThisCategory.get(positionForAds4).getAppName(), tvAds4, this);
        }
        if (lstAdsThisCategory.size() > 3) {
            llHorizontal1.setVisibility(View.VISIBLE);
            llHorizontal2.setVisibility(View.VISIBLE);
        } else if (lstAdsThisCategory.size() == 2) {
            llHorizontal1.setVisibility(View.VISIBLE);
            llHorizontal2.setVisibility(View.GONE);
        } else {
            rlAdLayout.setVisibility(View.GONE);
            llHorizontal1.setVisibility(View.VISIBLE);
            llHorizontal2.setVisibility(View.GONE);
        }
        ivAds1.startAnimation(zoomAnimationIvAdd1);
        ivAds4.startAnimation(zoomAnimationIvAdd4);
        ivAds2.startAnimation(zoomAnimationIvAdd2);
        ivAds3.startAnimation(zoomAnimationIvAdd3);
    }

    public int getRandom() {
        int min = 0;
        int max = lstAdsThisCategory.size() - 1;
        Random randoms = new Random();
        return randoms.nextInt((max - min) + 1) + min;
    }

    private void openPlayStore(String url) {
        if (!TextUtils.isEmpty(url)) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            } catch (Exception ignored) {

            }
        }
    }

    private void sendIntentToWebView() {
        Intent intent = new Intent(ExitActivity.this, PrivacyPolicyForAdActivity.class);
        intent.putExtra("url", adDataResponse.getPrivacyUrl());
        navigateToDifferentScreen(intent);
    }
}
