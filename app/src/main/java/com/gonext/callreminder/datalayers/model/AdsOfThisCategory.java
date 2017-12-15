package com.gonext.callreminder.datalayers.model;

import android.os.Parcel;
import android.os.Parcelable;


public class AdsOfThisCategory implements Parcelable {

        private String appName;
        private String playStoreUrl;
        private String clickUrl;
        private String appLogo;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getPlayStoreUrl() {
            return playStoreUrl;
        }

        public void setPlayStoreUrl(String playStoreUrl) {
            this.playStoreUrl = playStoreUrl;
        }


        public String getClickUrl() {
            return clickUrl;
        }

        public void setClickUrl(String clickUrl) {
            this.clickUrl = clickUrl;
        }

        public String getAppLogo() {
            return appLogo;
        }

        public void setAppLogo(String appLogo) {
            this.appLogo = appLogo;

        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appName);
        dest.writeString(this.playStoreUrl);
        dest.writeString(this.clickUrl);
        dest.writeString(this.appLogo);
    }

    public AdsOfThisCategory() {
    }

    protected AdsOfThisCategory(Parcel in) {
        this.appName = in.readString();
        this.playStoreUrl = in.readString();
        this.clickUrl = in.readString();
        this.appLogo = in.readString();
    }

    public static final Parcelable.Creator<AdsOfThisCategory> CREATOR = new Parcelable.Creator<AdsOfThisCategory>() {
        @Override
        public AdsOfThisCategory createFromParcel(Parcel source) {
            return new AdsOfThisCategory(source);
        }

        @Override
        public AdsOfThisCategory[] newArray(int size) {
            return new AdsOfThisCategory[size];
        }
    };
}
