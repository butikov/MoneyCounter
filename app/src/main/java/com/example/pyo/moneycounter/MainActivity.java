package com.example.pyo.moneycounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayList<String> tripNames;
    private List<TripRelations> Trips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Trips = new ArrayList<>();
        tripNames = new ArrayList<>();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView lv = (ListView) findViewById(R.id.tripsList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tripNames);
        assert lv != null;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TripActivity.class);
                intent.putExtra("currentTrip", Trips.get(position));
                startActivity(intent);
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
        String city = data.getStringExtra("newCity");
        ArrayList<String> names = data.getStringArrayListExtra("namesList");
        Trips.add(new TripRelations(city, names));
        tripNames.add(city);
        adapter.notifyDataSetChanged();
    }

    public void addTripClicked(View view) {
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivityForResult(intent, 1);
    }
}
