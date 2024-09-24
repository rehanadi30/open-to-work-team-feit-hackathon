package com.hackafun.foodliner.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class GenerateFoodResponse implements Parcelable {
    private List<String> items;

    public GenerateFoodResponse(List<String> items) {
        this.items = items;
    }

    protected GenerateFoodResponse(Parcel in) {
        items = in.createStringArrayList();
    }

    public static final Creator<GenerateFoodResponse> CREATOR = new Creator<GenerateFoodResponse>() {
        @Override
        public GenerateFoodResponse createFromParcel(Parcel in) {
            return new GenerateFoodResponse(in);
        }

        @Override
        public GenerateFoodResponse[] newArray(int size) {
            return new GenerateFoodResponse[size];
        }
    };

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringList(items);
    }
}
