package com.example.pyo.moneycounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    ArrayAdapter<String> adapter;
    ArrayList<String> tripNames;
    Context context;
    private ArrayList<TripRelations> Trips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        File trips = new File("/Trips");
        trips.mkdirs();
        
        for (File f : trips.listFiles()
                ) {
            try {
                if (f.exists() && f.isFile()) {
                    Log.d(LOG_TAG, f.getName());
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                    String current;
                    current = bufferedReader.readLine();
                    if (current == null)
                        continue;
                    int num = Integer.parseInt(current);
                    ArrayList<String> names = new ArrayList<>();
                    for (int i = 0; i < num; ++i) {
                        current = bufferedReader.readLine();
                        names.add(current);
                    }
                    int[] debts = new int[num * num];
                    for (int i = 0; i < num * num; ++i) {
                        current = bufferedReader.readLine();
                        int nextrel = Integer.parseInt(current);
                        debts[i] = nextrel;
                    }
                    Trips.add(new TripRelations(f.getName(), names, debts));
                    tripNames.add(f.getName());
                    adapter.notifyDataSetChanged();
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Trips = new ArrayList<>();
        tripNames = new ArrayList<>();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView lv = (ListView) findViewById(R.id.tripsList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tripNames);
        assert lv != null;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TripActivity.class);
                intent.putExtra("currentTrip", Trips.get(position));
                startActivityForResult(intent, 2);
            }
        });
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {
        if (data == null || ResultCode != RESULT_OK)
            return;
        if (RequestCode == 1) {
            String city = data.getStringExtra("newCity");
            ArrayList<String> names = data.getStringArrayListExtra("namesList");
            TripRelations newTip = new TripRelations(city, names);
            Trips.add(newTip);
            File trips = new File("/Trips");
            trips.mkdirs();
            File file = new File(trips, city);
            try {
                BufferedWriter outputStreamWriter = new BufferedWriter(new FileWriter(file));
                outputStreamWriter.write(Integer.toString(newTip.peopleNumber));
                for (int i = 0; i < newTip.peopleNumber; ++i) {
                    outputStreamWriter.write(newTip.Names.get(i));
                }
                for (int i = 0; i < newTip.peopleNumber * newTip.peopleNumber; ++i) {
                    outputStreamWriter.write(Integer.toString(newTip.Debts[i]));
                }
                outputStreamWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tripNames.add(city);
            adapter.notifyDataSetChanged();
        }
    }

    public void addTripClicked(View view) {
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivityForResult(intent, 1);
    }
}
