package com.example.pyo.moneycounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;

public class TripActivity extends AppCompatActivity {

    private TripRelations currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        currentTrip = intent.getParcelableExtra("currentTrip");
        currentTrip.peopleNumber = currentTrip.Names.size();
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
                int second = i / currentTrip.peopleNumber + 1;
                TextView newDebt = new TextView(this);
                newDebt.setText(currentTrip.Names.get(second) + " должен " + currentTrip.Debts[i] +
                        " рублей " + currentTrip.Names.get(first));
                relations.addView(newDebt);
            }
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {
        if (data == null || ResultCode != RESULT_OK)
            return;
        int[] operations = data.getIntArrayExtra("pays");
        updateRelationsView();
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(currentTrip.City, MODE_PRIVATE)));
            currentTrip.saveCurrentState(bw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
