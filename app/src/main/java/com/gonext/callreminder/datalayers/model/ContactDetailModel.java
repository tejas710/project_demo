package com.gonext.callreminder.datalayers.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sellnews on 9/28/17.
 */

public class ContactDetailModel implements Parcelable {
    String id,contactName,contactNumber,contactImage,contactEmail;

    public ContactDetailModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.contactName);
        dest.writeString(this.contactNumber);
        dest.writeString(this.contactImage);
        dest.writeString(this.contactEmail);
    }

    protected ContactDetailModel(Parcel in) {
        this.id = in.readString();
        this.contactName = in.readString();
        this.contactNumber = in.readString();
        this.contactImage = in.readString();
        this.contactEmail = in.readString();
    }

    public static final Parcelable.Creator<ContactDetailModel> CREATOR = new Parcelable.Creator<ContactDetailModel>() {
        @Override
        public ContactDetailModel createFromParcel(Parcel source) {
            return new ContactDetailModel(source);
        }

        @Override
        public ContactDetailModel[] newArray(int size) {
            return new ContactDetailModel[size];
        }
    };
}
