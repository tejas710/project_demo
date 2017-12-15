package com.gonext.callreminder.datalayers.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AdDataResponse implements Parcelable {

    private boolean isError = false;
    private List<AdData> data = null;
    private String privacyUrl = "";

    public String getPrivacyUrl() {
        return privacyUrl;
    }

    public void setPrivacyUrl(String privacyUrl) {
        this.privacyUrl = privacyUrl;
    }

    public boolean getIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public List<AdData> getData() {
        return data;
    }

    public void setData(List<AdData> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.data);
        dest.writeString(this.privacyUrl);
    }

    public AdDataResponse() {
    }

    protected AdDataResponse(Parcel in) {
        this.isError = in.readByte() != 0;
        this.data = in.createTypedArrayList(AdData.CREATOR);
        this.privacyUrl = in.readString();
    }

    public static final Parcelable.Creator<AdDataResponse> CREATOR = new Parcelable.Creator<AdDataResponse>() {
        @Override
        public AdDataResponse createFromParcel(Parcel source) {
            return new AdDataResponse(source);
        }

        @Override
        public AdDataResponse[] newArray(int size) {
            return new AdDataResponse[size];
        }
    };
}
