package com.gonext.callreminder.datalayers.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class AdData implements Parcelable {

    private int categoryId;
    private String categoryName;
    private int categoryCode;
    private String label;
    private List<AdsOfThisCategory> AdsOfThisCategory = null;

    public int getCategoryId() {
        return categoryId;
    }


    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public String getCategoryName() {
        return categoryName;
    }


    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<AdsOfThisCategory> getAdsOfThisCategory() {
        return AdsOfThisCategory;
    }

    public void setAdsOfThisCategory(List<AdsOfThisCategory> adsOfThisCategory) {
        this.AdsOfThisCategory = adsOfThisCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeInt(this.categoryCode);
        dest.writeString(this.label);
        dest.writeTypedList(this.AdsOfThisCategory);
    }

    public AdData() {
    }

    protected AdData(Parcel in) {
        this.categoryId = in.readInt();
        this.categoryName = in.readString();
        this.categoryCode = in.readInt();
        this.label = in.readString();
        this.AdsOfThisCategory = in.createTypedArrayList(com.gonext.callreminder.datalayers.model.AdsOfThisCategory.CREATOR);
    }

    public static final Parcelable.Creator<AdData> CREATOR = new Parcelable.Creator<AdData>() {
        @Override
        public AdData createFromParcel(Parcel source) {
            return new AdData(source);
        }

        @Override
        public AdData[] newArray(int size) {
            return new AdData[size];
        }
    };
}
