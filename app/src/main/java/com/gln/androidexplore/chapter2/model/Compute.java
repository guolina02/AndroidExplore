package com.gln.androidexplore.chapter2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guolina on 2017/6/23.
 */
public class Compute implements Parcelable {

    private String name;
    private float price;
    private String cpu;
    private String interStorage;

    public Compute(String name, float price, String cpu, String interStorage) {
        this.name = name;
        this.price = price;
        this.cpu = cpu;
        this.interStorage = interStorage;
    }

    private Compute(Parcel in) {
        this.name = in.readString();
        this.price = in.readFloat();
        this.cpu = in.readString();
        this.interStorage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeFloat(this.price);
        dest.writeString(this.cpu);
        dest.writeString(this.interStorage);
    }

    public static final Parcelable.Creator<Compute> CREATOR = new Parcelable.Creator<Compute>() {
        @Override
        public Compute createFromParcel(Parcel source) {
            return new Compute(source);
        }

        @Override
        public Compute[] newArray(int size) {
            return new Compute[size];
        }
    };
}
