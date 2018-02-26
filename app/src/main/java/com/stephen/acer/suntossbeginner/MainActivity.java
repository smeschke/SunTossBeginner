package com.stephen.acer.suntossbeginner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        mAdapter.mAdapterOnClickHandler {

    //initialize variables for resources from strings.xml
    public ArrayList<String> output_list = new ArrayList<>();
    public int[] goals;
    public int[] levels;
    public String[] pattern_list;
    //initialize an adapter and recyclerView
    public mAdapter mAdapter;
    public RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get resources from strings.xml
        Resources res = getResources();
        pattern_list = res.getStringArray(R.array.patterns);
        goals = res.getIntArray(R.array.goals);
        levels = res.getIntArray(R.array.levels);

        //get user's progress data form SharedPreferences
        SharedPreferences settings =
                getApplicationContext().getSharedPreferences("LOG", 0);

        //compare the user's records to the goals
        //create output string, and bind to mAdapter
        int num_catches = 0;
        int level_totals = 0;
        //iterate through each pattern in the list
        for (int index = 0; index < pattern_list.length; index++) {
            String key = Integer.toString(index);
            num_catches = settings.getInt(key, 0);
            //determine which level the user is
            //levels start at "1"
            int current_level = 1;
            for (int level : levels) {
                if (num_catches > goals[index] / level) {
                    current_level += 1;
                }
            }
            //create output string
            String output = pattern_list[index] + "," +
                    num_catches + "," +
                    Integer.toString(goals[index]) + "," +
                    Integer.toString(current_level);

            output_list.add(output);
            level_totals+=current_level;
        }
        //the overall_level is the average of the individual levels
        //-1's are to get around a work-around, a get-around-a-work-around
        float overall_level = (float) (level_totals-1)/ (float) (pattern_list.length-1);


        //write the overall level to shared preferences
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("OVERALL_LEVEL", overall_level);
        editor.commit();

        //code for recycler view
        mList = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(layoutManager);
        mList.setHasFixedSize(true);
        mAdapter = new mAdapter(this, this, output_list);
        mList.setAdapter(mAdapter);
    }

    //on user clicks, send to training
    @Override
    public void onClick(int index) {
        if (index == 0) {
            Intent toBadge = new Intent(MainActivity.this, badge.class);
            toBadge.putExtra("user_data", output_list);
            startActivity(toBadge);
        } else {
            Intent toTraining = new Intent(MainActivity.this, training.class);
            toTraining.putExtra("index", index);
            startActivity(toTraining);
        }
    }

    //---------------------------------------------------------
    //code for the menu bar settings button (copied from T02.02
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        //go to the settings activity
        if (itemThatWasClickedId == R.id.action_search) {
            Intent toSettings = new Intent(MainActivity.this, settings.class);
            startActivity(toSettings);
            return true;
        }
        if (itemThatWasClickedId == R.id.action_about) {
            Intent toSettings = new Intent(MainActivity.this, about.class);
            startActivity(toSettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
