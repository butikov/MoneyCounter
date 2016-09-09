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
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(currentTrip.getCityName());
        setSupportActionBar(toolbar);
        updateRelationsView();
    }

    public void newOperationButtonClicked(View view) {
        Intent intent = new Intent(this, NewOperationActivity.class);
        intent.putStringArrayListExtra("names", currentTrip.getNamesList());
        startActivityForResult(intent, 1);
    }

    private void updateRelationsView() {
        LinearLayout relations = (LinearLayout) findViewById(R.id.relationsLayout);
        assert relations != null;
        relations.removeAllViews();
        for (int i = 0; i < currentTrip.getPeopleNumber() * currentTrip.getPeopleNumber(); ++i)
            if (currentTrip.getDebt(i) > 0) {
                int first = i / currentTrip.getPeopleNumber();
                int second = i % currentTrip.getPeopleNumber();
                TextView newDebt = new TextView(this);
                newDebt.setText(currentTrip.getName(first) + " должен " + currentTrip.getDebt(i) +
                        moneyEnd(currentTrip.getDebt(i) % 100) + accusative(currentTrip.getName(second)));
                relations.addView(newDebt);
            }
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {
        if (data == null || ResultCode != RESULT_OK)
            return;
        int[] operations = data.getIntArrayExtra("pays");
        currentTrip.addOperation(operations);

        currentTrip.simplify();
        updateRelationsView();
        File trips = new File(getApplicationContext().getFilesDir(), "trips");
        File file = new File(trips, currentTrip.getCityName());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            currentTrip.saveCurrentState(bw);
        } catch (IOException e) {
            e.printStackTrace();
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
