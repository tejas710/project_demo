package com.gonext.callreminder.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.gonext.callreminder.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Dinesh Hirani.
 */

public class PrivacyPolicyActivity extends BaseActivity {
    WebView wbPrivacy;
    String FOLDER_NAME = "privacy_policy";
    String PATH_APPENDER = "/";
    @BindView(R.id.llBack)
    LinearLayout llBack;


    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_privacy_policy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        wbPrivacy = (WebView) findViewById(R.id.wbPrivacy);
        wbPrivacy.clearCache(true);
        wbPrivacy.getSettings().setJavaScriptEnabled(true);
        wbPrivacy.getSettings().setBuiltInZoomControls(true);
        wbPrivacy.setInitialScale(1);
        wbPrivacy.getSettings().setLoadWithOverviewMode(true);
        wbPrivacy.getSettings().setUseWideViewPort(true);
        try {
            String[] url = getAssets().list(FOLDER_NAME);
            if (!TextUtils.isEmpty(url[0])) {
                wbPrivacy.loadUrl("file:///android_asset" + PATH_APPENDER + FOLDER_NAME + PATH_APPENDER + url[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.llBack)
    public void onClick() {
        finish();
    }
}
