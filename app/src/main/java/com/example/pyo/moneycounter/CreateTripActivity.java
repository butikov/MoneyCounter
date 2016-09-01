package com.example.pyo.moneycounter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateTripActivity extends AppCompatActivity {

    public static String city;
    public static ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Добавить путешествие");
        setSupportActionBar(toolbar);
        final EditText menNumber = (EditText) findViewById(R.id.numberEdit);
        assert menNumber != null;
        menNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    LinearLayout layout = (LinearLayout) findViewById(R.id.menLayout);
                    assert layout != null;
                    layout.removeAllViews();
                    int count = Integer.parseInt(menNumber.getText().toString());
                    for (int i = 0; i < count; ++i) {
                        TextView nextManLabel = new TextView(getApplicationContext());
                        nextManLabel.setText("Введите имя:");
                        nextManLabel.setTextColor(Color.BLACK);
                        layout.addView(nextManLabel);
                        EditText nextMan = new EditText(getApplicationContext());
                        nextMan.setId(i);
                        layout.addView(nextMan);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void okClicked(View view) {
        EditText cityEdit = (EditText) findViewById(R.id.cityEdit);
        assert cityEdit != null;
        city = cityEdit.getText().toString();
        EditText numberEdit = (EditText) findViewById(R.id.numberEdit);
        assert numberEdit != null;
        int number = Integer.parseInt(numberEdit.getText().toString());
        LinearLayout layout = (LinearLayout) findViewById(R.id.menLayout);
        names = new ArrayList<>();
        for (int i = 0; i < number; ++i) {
            assert layout != null;
            EditText nameEdit = (EditText) layout.findViewById(i);
            String name = nameEdit.getText().toString();
            for (String prevname : names
                    ) {
                if (name == prevname) {
                    return;
                }
            }
            names.add(name);
        }
        if (city.isEmpty())
            return;
        Intent intent = new Intent();
        intent.putExtra("newCity", city);
        intent.putStringArrayListExtra("namesList", names);
        setResult(RESULT_OK, intent);
        finish();
    }
}
