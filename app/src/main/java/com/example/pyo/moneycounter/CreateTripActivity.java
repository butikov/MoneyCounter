package com.example.pyo.moneycounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CreateTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton okButton = (FloatingActionButton) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final EditText menNumber = (EditText) findViewById(R.id.numberEdit);
        assert menNumber != null;
        menNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    LinearLayout layout = (LinearLayout) findViewById(R.id.menLayout);
                    int count = Integer.parseInt(menNumber.getText().toString());
                    for (int i = 0; i < count; ++i) {
                        TextView nextManLabel = new TextView(getApplicationContext());
                        nextManLabel.setText("Введите имя:");
                        layout.addView(nextManLabel);
                        EditText nextMan = new EditText(getApplicationContext());
                        layout.addView(nextMan);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void cancelButtonClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void okClicked(View view) {
        EditText cityEdit = (EditText) findViewById(R.id.cityEdit);
        String city = cityEdit.getText().toString();
        EditText numberEdit = (EditText) findViewById(R.id.numberEdit);
        int number = Integer.parseInt(numberEdit.getText().toString());
        List<String> names;
        for (int i = 0; i < number; ++i) {

        }
    }
}
