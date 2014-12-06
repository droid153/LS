package com.example.matrix.livingsocial.util;

import android.os.Parcel;
import android.os.Parcelable;

public class MyParcelable implements Parcelable{
    public String id;
    public String name;
    public String grade;

    // Constructor
    public MyParcelable(String id, String name, String grade){
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    // Parcelling part
    public MyParcelable(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.grade = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.id,
                this.name,
                this.grade});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };
}