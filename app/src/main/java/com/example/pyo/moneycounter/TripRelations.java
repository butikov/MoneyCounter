package com.example.pyo.moneycounter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by pyo on 18.08.2016.
 */
public class TripRelations implements Parcelable {

    private String City;
    private ArrayList<String> Names;
    private int[] Debts;

    public TripRelations(String city, ArrayList<String> names) {
        City = city;
        Names = names;
        Debts = new int[names.size() * (names.size() - 1) / 2];
        for (Integer next : Debts
                ) {
            next = 0;
        }
    }

    protected TripRelations(Parcel in) {
        City = in.readString();
        Names = in.createStringArrayList();
    }

    public static final Creator<TripRelations> CREATOR = new Creator<TripRelations>() {
        @Override
        public TripRelations createFromParcel(Parcel in) {
            return new TripRelations(in);
        }

        @Override
        public TripRelations[] newArray(int size) {
            return new TripRelations[size];
        }
    };

    public String getCityName() {
        return City;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(City);
        dest.writeStringList(Names);
        dest.writeIntArray(Debts);
    }
}
