package com.gonext.callreminder.activities;

import android.os.Bundle;
import android.webkit.WebView;

import com.gonext.callreminder.R;

import butterknife.BindView;

public class PrivacyPolicyForAdActivity extends BaseActivity {

    @BindView(R.id.wvPrivacy)
    WebView wvPrivacy;

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_privacy_policy_for_ad;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wvPrivacy.clearCache(true);
        wvPrivacy.getSettings().setJavaScriptEnabled(true);
        wvPrivacy.getSettings().setBuiltInZoomControls(true);
        wvPrivacy.setInitialScale(1);
        wvPrivacy.getSettings().setLoadWithOverviewMode(true);
        wvPrivacy.getSettings().setUseWideViewPort(true);
        wvPrivacy.loadUrl(getIntent().getStringExtra("url"));

    }
}
