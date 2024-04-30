package com.example.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class LocationItem implements Parcelable {
    private String locationUrl;
    private String locationName;

    // Constructor, getters, setters
    public LocationItem() {

    }
    // Parcelable implementation
    protected LocationItem(Parcel in) {
        locationUrl = in.readString();
        locationName = in.readString();
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public String getLocationName() {
        return locationName;
    }
    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationUrl);
        dest.writeString(locationName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationItem> CREATOR = new Creator<LocationItem>() {
        @Override
        public LocationItem createFromParcel(Parcel in) {
            return new LocationItem(in);
        }

        @Override
        public LocationItem[] newArray(int size) {
            return new LocationItem[size];
        }
    };
}
