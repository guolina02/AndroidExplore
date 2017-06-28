package com.gln.androidexplore.chapter2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by guolina on 2017/6/22.
 */
public class Book implements Parcelable, Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private float price;
    private String writer;
    private String publisher;

    public Book(String title, float price) {
        this.title = title;
        this.price = price;
    }

    public Book(String title, float price, String writer, String publisher) {
        this.title = title;
        this.price = price;
        this.writer = writer;
        this.publisher = publisher;
    }

    private Book(Parcel in) {
        this.title = in.readString();
        this.price = in.readFloat();
        this.writer = in.readString();
        this.publisher = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeFloat(this.price);
        dest.writeString(this.writer);
        dest.writeString(this.publisher);
    }

    public void readFromParcel(Parcel in) {
        this.title = in.readString();
        this.price = in.readFloat();
        this.writer = in.readString();
        this.publisher = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString())
                .append("[title=")
                .append(TextUtils.isEmpty(title) ? " " : title)
                .append(", price=")
                .append(price)
                .append(", writer=")
                .append(TextUtils.isEmpty(writer) ? " " : writer)
                .append(", publisher=")
                .append(TextUtils.isEmpty(publisher) ? " " : publisher)
                .append("]");
        return builder.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
