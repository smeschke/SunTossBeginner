package com.stephen.acer.suntossbeginner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class badge extends AppCompatActivity {

    //initialize variables for resources
    public String[] pattern_list;
    public int[] goals;
    public int[] levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);

        //hide the action bar
        getSupportActionBar().hide();

        //make toast to tell users to take screenshot and share
        Toast.makeText(this, "Press and hold\nvolume down + power\nto take a screenshot.",
                Toast.LENGTH_SHORT).show();

        //get resources from strings
        Resources res = getResources();
        pattern_list = res.getStringArray(R.array.patterns);
        goals = res.getIntArray(R.array.goals);
        levels = res.getIntArray(R.array.levels);
        //create int list for the names of the levels
        int[] level_icons = new int[]{R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five};
        //get data from shared preferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences("LOG", 0);
        String name = settings.getString("NAME", "Juggler\n(enter name in settings)");
        //int overall_level = settings.getInt("OVERALL_LEVEL", 123);
        float overall_level = settings.getFloat("OVERALL_LEVEL", 0);
        double level = Math.round(overall_level * 10) / 10.0;

        //get number for total catches
        int total_catches = settings.getInt("total_catches", 0);

        //declare text boxes and image box
        TextView juggler_name = (TextView) findViewById(R.id.jugglers_name);
        TextView juggler_level = (TextView) findViewById(R.id.levelId);
        ImageView levelIconView = (ImageView) findViewById(R.id.levelImage);
        TextView total_catches_text_view = (TextView) findViewById(R.id.totalCatchesTextView);

        //set the text
        juggler_name.setText(name);
        juggler_level.setText("Level: " + Double.toString(level) + " Juggler");
        levelIconView.setImageResource(level_icons[Math.round(overall_level) - 1]);
        total_catches_text_view.setText("Total Catches: " + total_catches);
    }
}

