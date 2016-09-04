package com.example.pyo.moneycounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TripActivity extends AppCompatActivity {

    private TripRelations currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        currentTrip = intent.getParcelableExtra("currentTrip");
        currentTrip.peopleNumber = currentTrip.Names.size();
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(currentTrip.City);
        setSupportActionBar(toolbar);
        updateRelationsView();
    }

    public void newOperationButtonClicked(View view) {
        Intent intent = new Intent(this, NewOperationActivity.class);
        intent.putStringArrayListExtra("names", currentTrip.Names);
        startActivityForResult(intent, 1);
    }

    private void updateRelationsView() {
        LinearLayout relations = (LinearLayout) findViewById(R.id.relationsLayout);
        assert relations != null;
        relations.removeAllViews();
        for (int i = 0; i < currentTrip.Debts.length; ++i)
            if (currentTrip.Debts[i] > 0) {
                int first = i / currentTrip.peopleNumber;
                int second = i % currentTrip.peopleNumber;
                TextView newDebt = new TextView(this);
                newDebt.setText(currentTrip.Names.get(first) + " должен " + currentTrip.Debts[i] +
                        moneyEnd(currentTrip.Debts[i] % 100) + accusative(currentTrip.Names.get(second)));
                relations.addView(newDebt);
            }
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {
        if (data == null || ResultCode != RESULT_OK)
            return;
        int[] operations = data.getIntArrayExtra("pays");
        for (int i = 0; i < currentTrip.peopleNumber; ++i) {
            if (operations[i] > 0)
                for (int j = 0; j < currentTrip.peopleNumber; ++j) {
                    if (operations[j + currentTrip.peopleNumber] > 0 &&
                            currentTrip.Debts[i * currentTrip.peopleNumber + j] != 0) {
                        int debt = Math.min(operations[i], operations[j + currentTrip.peopleNumber]);
                        currentTrip.Debts[i * currentTrip.peopleNumber + j] += debt;
                        currentTrip.Debts[j * currentTrip.peopleNumber + i] -= debt;
                        operations[i] -= debt;
                        operations[j + currentTrip.peopleNumber] -= debt;
                        if (operations[i] == 0)
                            break;
                    }
                }
        }
        for (int i = 0; i < currentTrip.peopleNumber; ++i) {
            if (operations[i] > 0)
                for (int j = 0; j < currentTrip.peopleNumber; ++j) {
                    if (operations[j + currentTrip.peopleNumber] > 0) {
                        int debt = Math.min(operations[i], operations[j + currentTrip.peopleNumber]);
                        currentTrip.Debts[i * currentTrip.peopleNumber + j] += debt;
                        currentTrip.Debts[j * currentTrip.peopleNumber + i] -= debt;
                        operations[i] -= debt;
                        operations[j + currentTrip.peopleNumber] -= debt;
                        if (operations[i] == 0)
                            break;
                    }
                }
        }

        simplify();
        updateRelationsView();
        File trips = new File(getApplicationContext().getFilesDir(), "trips");
        trips.mkdirs();
        File file = new File(trips, currentTrip.City);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            currentTrip.saveCurrentState(bw);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void simplify() {
        for (int i = 0; i < currentTrip.peopleNumber * currentTrip.peopleNumber; ++i) {
            if (currentTrip.Debts[i] > 0) {
                int second = i % currentTrip.peopleNumber;
                for (int third = 0; third < currentTrip.peopleNumber; ++third) {
                    if (currentTrip.Debts[second * currentTrip.peopleNumber + third] > 0) {
                        int newDebt = Math.min(currentTrip.Debts[i],
                                currentTrip.Debts[second * currentTrip.peopleNumber + third]);
                        int first = i / currentTrip.peopleNumber;

                        currentTrip.Debts[i] -= newDebt;
                        currentTrip.Debts[second * currentTrip.peopleNumber + first] += newDebt;

                        currentTrip.Debts[second * currentTrip.peopleNumber + third] -= newDebt;
                        currentTrip.Debts[third * currentTrip.peopleNumber + second] += newDebt;

                        currentTrip.Debts[first * currentTrip.peopleNumber + third] += newDebt;
                        currentTrip.Debts[third * currentTrip.peopleNumber + first] -= newDebt;
                    }
                }
            }
        }
    }

    private String accusative(String name) {
        String result = "";
        String[] nameParts = name.split(" ");
        for (String namePart : nameParts) {
            if (namePart.endsWith("ец") || namePart.endsWith("ац") || namePart.endsWith("о") ||
                    namePart.endsWith("и") || namePart.endsWith("ы") || namePart.endsWith("е") ||
                    namePart.endsWith("э") || namePart.endsWith("ё")) {
                result += namePart + " ";
                continue;
            }
            if (namePart.endsWith("й")) {
                result += namePart.substring(0, name.length() - 1) + "ю ";
                continue;
            }
            if (namePart.endsWith("а") || namePart.endsWith("я")) {
                result += namePart.substring(0, name.length() - 1) + "е ";
                continue;
            }
            result += namePart + "у ";
        }
        return result;
    }

    private String moneyEnd(int money) {
        if (money != 11 && money % 10 == 1)
            return " рубль ";
        if (((money < 5) || (money > 20)) && (money % 10) > 1 && (money % 10) < 5)
            return " рубля ";
        return " рублей ";
    }
}
