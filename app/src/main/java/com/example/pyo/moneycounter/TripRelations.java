package com.example.pyo.moneycounter;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pyo on 18.08.2016.
 */

//TODO: Replace int[] with graph library
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
    private String City;
    private ArrayList<String> Names;
    private int[] Debts;
    private Integer peopleNumber;

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
            System.arraycopy(debts, 0, Debts, 0, peopleNumber * peopleNumber);
        }
    }

    public TripRelations(String city, ArrayList<String> names) {
        this(city, names, null);
    }

    protected TripRelations(Parcel in) {
        City = in.readString();
        Names = in.createStringArrayList();
        Debts = in.createIntArray();
        peopleNumber = in.readInt();
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public ArrayList<String> getNamesList() {
        return Names;
    }

    public String getName(int i) {
        return Names.get(i);
    }

    public Integer getDebt(int i) {
        return Debts[i];
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
            outputStreamWriter.newLine();
            for (int i = 0; i < peopleNumber; ++i) {
                outputStreamWriter.write(Names.get(i));
                outputStreamWriter.newLine();
            }
            for (int i = 0; i < peopleNumber * peopleNumber; ++i) {
                outputStreamWriter.write(Integer.toString(Debts[i]));
                outputStreamWriter.newLine();
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simplify() {
        for (int k = 0; k < peopleNumber; ++k) {
            for (int i = 0; i < peopleNumber * peopleNumber; ++i) {
                if (Debts[i] > 0) {
                    int second = i % peopleNumber;
                    for (int third = 0; third < peopleNumber; ++third) {
                        if (Debts[second * peopleNumber + third] > 0) {
                            int newDebt = Math.min(Debts[i], Debts[second * peopleNumber + third]);
                            int first = i / peopleNumber;

                            Debts[i] -= newDebt;
                            Debts[second * peopleNumber + first] += newDebt;

                            Debts[second * peopleNumber + third] -= newDebt;
                            Debts[third * peopleNumber + second] += newDebt;

                            Debts[first * peopleNumber + third] += newDebt;
                            Debts[third * peopleNumber + first] -= newDebt;

                        }
                    }
                }
            }
        }
    }

    public void addOperation(int[] operations) {
        for (int i = 0; i < peopleNumber; ++i) {
            if (operations[i] > 0)
                for (int j = 0; j < peopleNumber; ++j) {
                    if (operations[j + peopleNumber] > 0 && Debts[i * peopleNumber + j] != 0) {
                        int debt = Math.min(operations[i], operations[j + peopleNumber]);
                        Debts[i * peopleNumber + j] += debt;
                        Debts[j * peopleNumber + i] -= debt;
                        operations[i] -= debt;
                        operations[j + peopleNumber] -= debt;
                        if (operations[i] == 0)
                            break;
                    }
                }
        }
        for (int i = 0; i < peopleNumber; ++i) {
            if (operations[i + peopleNumber] > 0)
                for (int j = 0; j < peopleNumber; ++j) {
                    if (operations[j] > 0) {
                        int debt = Math.min(operations[i + peopleNumber], operations[j]);
                        Debts[j * peopleNumber + i] += debt;
                        Debts[i * peopleNumber + j] -= debt;
                        operations[i + peopleNumber] -= debt;
                        operations[j] -= debt;
                        if (operations[i + peopleNumber] == 0)
                            break;
                    }
                }
        }
    }
}
