package com.example.pyo.moneycounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    ArrayAdapter<String> adapter;
    ArrayList<String> tripNames;
    Context context;
    ListView tripsList;
    private ArrayList<TripRelations> Trips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        Trips = new ArrayList<>();
        tripNames = new ArrayList<>();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("Список путешествий");
        setSupportActionBar(toolbar);
        tripsList = (ListView) findViewById(R.id.tripsList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tripNames);
        File trips = new File(context.getFilesDir(), "trips");
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
        assert tripsList != null;
        tripsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TripActivity.class);
                intent.putExtra("currentTrip", Trips.get(position));
                startActivityForResult(intent, 2);
            }
        });
        tripsList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE, 0, Menu.NONE, "Открыть");
                menu.add(Menu.NONE, 1, Menu.NONE, "Редактировать");
                menu.add(Menu.NONE, 2, Menu.NONE, "Удалить");
            }

        });
        tripsList.setAdapter(adapter);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(MainActivity.this, TripActivity.class);
                intent.putExtra("currentTrip", Trips.get((int) info.id));
                startActivityForResult(intent, 2);
                return true;
            case 1:
                return true;
            case 2:
                File trips = new File(context.getFilesDir(), "trips");
                File thisTrip = new File(trips, tripNames.get((int) info.id));
                thisTrip.delete();
                tripNames.remove((int) info.id);
                adapter.notifyDataSetChanged();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
            TripRelations newTip;
            newTip = new TripRelations(city, names);
            Trips.add(newTip);
            File trips = new File(context.getFilesDir(), "trips");
            File file = new File(trips, city);
            try {
                BufferedWriter outputStreamWriter = new BufferedWriter(new FileWriter(file));
                newTip.saveCurrentState(outputStreamWriter);
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
