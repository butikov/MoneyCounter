package com.example.pyo.moneycounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
        menNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    int newValue;
                    try {
                        newValue = Integer.parseInt(s.toString());
                    } catch (Exception e) {
                        newValue = 0;
                    }
                    LinearLayout layout = (LinearLayout) findViewById(R.id.menLayout);
                    assert layout != null;
                    layout.removeAllViews();
                    for (int i = 0; i < newValue; ++i) {
                        TextView nextManLabel = new TextView(getApplicationContext());
                        nextManLabel.setText("Введите имя:");
                        nextManLabel.setTextColor(Color.BLACK);
                        layout.addView(nextManLabel);
                        EditText nextMan = new EditText(getApplicationContext());
                        nextMan.setId(i);
                        nextMan.setTextColor(Color.BLACK);
                        layout.addView(nextMan);
                    }
                }
            }
        });
    }

    public void okClicked(View view) {
        EditText cityEdit = (EditText) findViewById(R.id.cityEdit);
        assert cityEdit != null;
        city = cityEdit.getText().toString().trim();
        EditText numberEdit = (EditText) findViewById(R.id.numberEdit);
        assert numberEdit != null;
        int number = 0;
        try {
            number = Integer.parseInt(numberEdit.getText().toString());
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Не задано количество участников!");
            builder.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return;
        }
        LinearLayout layout = (LinearLayout) findViewById(R.id.menLayout);
        names = new ArrayList<>();
        for (Integer i = 0; i < number; ++i) {
            assert layout != null;
            EditText nameEdit = (EditText) layout.findViewById(i);
            String name = nameEdit.getText().toString().trim();
            if (name.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Не задано имя " + i.toString() + " участника").setPositiveButton("Понятно",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
                return;
            }
            for (Integer j = 0; j < names.size(); ++j) {
                if (name.equals(names.get(j))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Имя " + i.toString() + " участника совпадает с именем " +
                            j.toString() + " участника").setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                    return;
                }
            }
            names.add(name);
        }
        if (city.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Не задано навзвание!").setPositiveButton("Понятно",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("newCity", city);
        intent.putStringArrayListExtra("namesList", names);
        setResult(RESULT_OK, intent);
        finish();
    }
}
