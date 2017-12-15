package com.gonext.callreminder.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gonext.callreminder.BuildConfig;
import com.gonext.callreminder.R;
import com.gonext.callreminder.application.BaseApplication;
import com.gonext.callreminder.datalayers.model.AdData;
import com.gonext.callreminder.datalayers.model.AdDataResponse;
import com.gonext.callreminder.datalayers.model.AdsOfThisCategory;
import com.gonext.callreminder.datalayers.retrofit.ApiInterface;
import com.gonext.callreminder.datalayers.retrofit.RetrofitProvider;
import com.gonext.callreminder.datalayers.serverad.OnAdLoaded;
import com.gonext.callreminder.utils.AdUtils;
import com.gonext.callreminder.utils.FileUtils;
import com.gonext.callreminder.utils.PopUtils;
import com.gonext.callreminder.utils.StaticUtils;
import com.gonext.callreminder.utils.logger.CustomLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dinesh Hirani
 */

public abstract class BaseActivity extends AppCompatActivity {

    String[] PERMISSIONS = {};
    int PERMISSION = 1210;
    protected abstract Integer getLayoutId();

    public Context context;
    Unbinder unbinder;


    //List of current running activities.
    //Do not forget to extend your activity to BaseActivity then only this logic will work.
    public static ArrayList<String> runningActivity = new ArrayList<>();
    public static final long BACKGROUND_CHECK_DELAY = 1000L;// Perform background/Foreground thread after 2 second

    public static Handler backgroundHandler = new Handler();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutId() == null) {
            return;
        }
        setContentView(getLayoutId());
        //we are importing unbinber and binder in base activity.
        // so no need to import it in child classes.
        unbinder = ButterKnife.bind(this);
    }

    public void shouldShowRequestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION);
    }


    @Override
    protected void onStart() {
        super.onStart();
        context = this;
        activityDidForeground();

    }



    public Runnable backgroundRunnable = new Runnable() {
        @Override
        public void run() {
            if (runningActivity.size() == 0) {
                BaseApplication.isAppWentToBg = true;
                //           Toast.makeText(context, "App is Going to Background", Toast.LENGTH_SHORT).show();
            } else if (BaseApplication.isAppWentToBg) {
                BaseApplication.isAppWentToBg = false;
                //       Toast.makeText(context, "App is came to Foreground", Toast.LENGTH_SHORT).show();

            }

        }
    };

    private void activityDidForeground() {
        runningActivity.add(this.getClass().getName());
        backgroundHandler.removeCallbacks(backgroundRunnable);
        backgroundHandler.postDelayed(backgroundRunnable, BACKGROUND_CHECK_DELAY);
    }

    private void activityDidBackground() {
        runningActivity.remove(this.getClass().getName());
        backgroundHandler.removeCallbacks(backgroundRunnable);
        backgroundHandler.postDelayed(backgroundRunnable, BACKGROUND_CHECK_DELAY);
    }


    public void requestForServerAd(final OnAdLoaded onAdLoaded) {

        if (StaticUtils.isConnectingToInternet(this)) {
            ApiInterface apiService = RetrofitProvider.createAdService(ApiInterface.class);
            Call<AdDataResponse> call = apiService.getServerAds("35");
            call.enqueue(new Callback<AdDataResponse>() {
                @Override
                public void onResponse(@NonNull Call<AdDataResponse> call, @NonNull Response<AdDataResponse> response) {
                    if (response.body() != null) {
                        try {
                            if (!response.body().getIsError()) {
                                AdDataResponse adDataResponse = response.body();

                                if (adDataResponse != null && adDataResponse.getData() != null) {
                                    AdData adData = adDataResponse.getData().get(0);
                                    List<AdsOfThisCategory> lstAdsOfThisCategory = adData.getAdsOfThisCategory();
                                    if (lstAdsOfThisCategory.size() > 0) {

                                        FileUtils.deleteDataFile(BaseActivity.this);
                                        Gson gson = new GsonBuilder().create();
                                        String responseString = gson.toJson(adDataResponse);
                                        FileUtils.writeJsonFileToStoreAdResponse(BaseActivity.this, responseString);
                                        if (onAdLoaded != null)
                                            onAdLoaded.adLoad();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AdDataResponse> call, @NonNull Throwable t) {
                    CustomLog.error("error",""+t.getMessage());
                }
            });
        }
    }


    /**
     * Method to show toast for short time duration with release build flag.
     * @param message message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     */
    public void showToastForShortTime(String message,boolean showInReleaseBuild){
        showToastForLongTime(message,showInReleaseBuild,Toast.LENGTH_SHORT);
    }

    /**
     * Method to show toast for short time duration.
     * @param message message which will be shown in toast.
     */
    public void showToastForShortTime(String message){
        showToastForLongTime(message,false,Toast.LENGTH_SHORT);
    }

    /**
     * Method to show toast for long time duration with release build flag.
     * @param message message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     */
    public void showToastForLongTime(String message,boolean showInReleaseBuild){
        showToastForLongTime(message,showInReleaseBuild,Toast.LENGTH_LONG);
    }

    /**
     * Method to show toast for long time duration.
     * @param message message which will be shown in toast.
     */
    public void showToastForLongTime(String message){
        showToastForLongTime(message,false,Toast.LENGTH_LONG);
    }

    /**
     * Method to show toast.
     * @param message message which will be shown in toast.
     * @param showInReleaseBuild set true if want to show toast in release build.
     * @param toastLength length of Toast duration. Toast.LENGTH_LONG/Toast.LENGTH_SHORT
     */
    public void showToastForLongTime(String message,boolean showInReleaseBuild,int toastLength){
        if (!showInReleaseBuild) {
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, message, toastLength).show();
            }
        }else{
            Toast.makeText(this, message, toastLength).show();
        }

    }


    public void navigateToDifferentScreen(Intent nextScreenIntent){
        navigateToDifferentScreen(nextScreenIntent,null,"",false,false,false,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,View view,String sharedElementName,boolean finishActivity,boolean isDisplayIntestial){
        navigateToDifferentScreen(nextScreenIntent,view,sharedElementName,false,finishActivity,isDisplayIntestial,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,View view,String sharedElementName){
        navigateToDifferentScreen(nextScreenIntent,view,sharedElementName,false,false,false,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,boolean finishActivity){
        navigateToDifferentScreen(nextScreenIntent,null,"",false,finishActivity,false,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,boolean finishActivity,boolean isDisplayIntestial){
        navigateToDifferentScreen(nextScreenIntent,null,"",false,finishActivity,isDisplayIntestial,0,0);

    }

    public void navigateToDifferentScreen(Intent nextScreenIntent,boolean finishActivity,boolean isDisplayIntestial,int startAnimation,int endAnimation){
        navigateToDifferentScreen(nextScreenIntent,null,"",true,finishActivity,isDisplayIntestial,startAnimation,endAnimation);

    }

    /**
     * Common method to navigate in different activity class
     * @param nextScreenIntent object of Intent
     * @param view view which will be animate
     * @param sharedElementName transition name
     * @param isAnimate boolean for screen animation
     * @param finishActivity boolean set true if want to finish current activity
     * @param isDisplayIntestial boolean set true if want to display intestial ad.
     * @param startAnimation this is start animation use full if isAnimate is true
     * @param endAnimation this is end animation use full if isAnimate is true
     */
    public void navigateToDifferentScreen(Intent nextScreenIntent, View view, String sharedElementName,
                                          boolean isAnimate, boolean finishActivity, boolean isDisplayIntestial,int startAnimation, int endAnimation){

        try {
            if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, view, sharedElementName);
                startActivity(nextScreenIntent, options.toBundle());
                if (finishActivity){
                    finish();
                }
            }
            else {
                startActivity(nextScreenIntent);
                if (isAnimate){
                    overridePendingTransition(startAnimation,endAnimation);
                }

                if (isDisplayIntestial){
                    AdUtils.displayIntestial();
                }
                if (finishActivity){
                    finish();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show snack bar message with one button for short time
     * @param view  main view group
     * @param message string which is to be displayed
     */
    public void showSnackBarForShortTime(View view,String message){
        showSnackBar(view,message,true);

    }

    /**
     * Method to show snack bar message with one button for long time
     * @param view  main view group
     * @param message string which is to be displayed
     */
    public void showSnackBarForLongTime(View view,String message){
        showSnackBar(view,message,false);
    }
    private void showSnackBar(View view,String message,boolean isShortTime){
        try {
            int timeDuration = 0;
            if (isShortTime){
                timeDuration = Snackbar.LENGTH_SHORT;
            }else{
                timeDuration = Snackbar.LENGTH_LONG;
            }
            Snackbar snackbar = Snackbar.make(view, message, timeDuration);
            snackbar.setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //snackbar.setActionTextColor(getResources().getColor(R.color.gray_medium));
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(3);
            //snackbarView.setBackgroundColor(getResources().getColor(R.color.black_transparent_70));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        activityDidBackground();

    }

    public void onBackPressFromActivity(Intent nextScreenIntent,boolean isHomeScreen){

        try {
            if (isHomeScreen){
                PopUtils.showCustomTwoButtonAlertDialog(BaseActivity.this,"", getString(R.string.title_exit_dialog),
                        getString(R.string.yes), getString(R.string.no), false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                BaseActivity.this.finishActivity();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }else{
                finishActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishActivity(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                supportFinishAfterTransition();
            }
            else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onBackPressFromActivity(){
        onBackPressFromActivity(null,false);
    }

    public void onBackPressFromActivity(boolean isHomeScreen){
        onBackPressFromActivity(null,isHomeScreen);
    }
    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

}
