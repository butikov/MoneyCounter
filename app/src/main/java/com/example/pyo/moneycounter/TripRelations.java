package com.example.pyo.moneycounter;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pyo on 18.08.2016.
 */
public class TripRelations implements Parcelable {

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
    public String City;
    public ArrayList<String> Names;
    public int[] Debts;
    public Integer peopleNumber;

    public TripRelations(String city, ArrayList<String> names, int[] debts) {
        City = city;
        Names = new ArrayList<>();
        for (String name : names
                ) {
            Names.add(name);
        }
        peopleNumber = names.size();
        Debts = new int[names.size() * names.size()];
        if (debts == null) {
            for (int i = 0; i < peopleNumber * peopleNumber; ++i) {
                Debts[i] = 0;
            }
        } else {
            for (int i = 0; i < peopleNumber * peopleNumber; ++i) {
                Debts[i] = debts[i];
            }
        }
    }

    public TripRelations(String city, ArrayList<String> names) {
        new TripRelations(city, names, null);
    }

    protected TripRelations(Parcel in) {
        City = in.readString();
        Names = in.createStringArrayList();
        Debts = in.createIntArray();
        peopleNumber = in.readInt();
    }

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
        dest.writeInt(peopleNumber);
    }

    public void saveCurrentState(BufferedWriter outputStreamWriter) {
        try {
            outputStreamWriter.write(peopleNumber.toString());
            for (int i = 0; i < peopleNumber; ++i) {
                outputStreamWriter.write(Names.get(i));
            }
            for (int i = 0; i < peopleNumber * peopleNumber; ++i) {
                outputStreamWriter.write(Integer.toString(Debts[i]));
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
