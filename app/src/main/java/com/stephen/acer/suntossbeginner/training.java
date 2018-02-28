package com.stephen.acer.suntossbeginner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class training extends AppCompatActivity {

    public int index;
    public int num_catches = 0;
    public String[] pattern_list;
    public int[] goals;
    public int[] levels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        //make the back button on the top left
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //get resources
        Resources res = getResources();
        pattern_list = res.getStringArray(R.array.patterns);
        goals = res.getIntArray(R.array.goals);
        levels = res.getIntArray(R.array.levels);

        //get extra
        Intent fromMainActivity = getIntent();
        index = fromMainActivity.getIntExtra("index", 0);

        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        NumberPicker np = (NumberPicker) findViewById(R.id.numberPickerOnes);
        NumberPicker npT = (NumberPicker) findViewById(R.id.numberPickerTens);
        np.setMinValue(0);
        np.setMaxValue(9);
        npT.setMinValue(0);
        npT.setMaxValue(40);

        //set up text views
        TextView trick_name_text_view = (TextView) findViewById(R.id.trick_name_text_view);
        TextView goal_catches_text_view = (TextView) findViewById(R.id.goal_catches_text_view);
        TextView personalRecordTextView = (TextView) findViewById(R.id.personal_record_text_view);

        //start playing gif
        Integer[] gif_trick_list = {R.raw.five_five_five_one_four,//workaround
                R.raw.one_ball, R.raw.five_zero_zero_zero_zero, R.raw.four_two_zero,
                R.raw.four_four_zero_zero, R.raw.two_balls, R.raw.two_balls_right,
                R.raw.two_balls_left, R.raw.four_one_one_two_two, R.raw.four_two_three,
                R.raw.four_four_one, R.raw.three_balls, R.raw.three_four_zero_one_two,
                R.raw.four_ball_fountain, R.raw.five_two_two, R.raw.five_zero_one,
                R.raw.five_two_five_one_two, R.raw.five_three_one, R.raw.five_five_two,
                R.raw.five_ball_cascade, R.raw.five_five_five_one_four, R.raw.one_two_three_four_five};
        VideoView videoView = (VideoView) findViewById(R.id.trick_image);
        //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.five_ball_cascade);
        String s = "android.resource://" + getPackageName() + "/" + gif_trick_list[index];
        Uri uri = Uri.parse(s);



        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });


        //--------------------------------------------------------------
        // set number of catches view to show users previous personal best
        SharedPreferences settings = getApplicationContext().getSharedPreferences("LOG", 0);
        //set the text views
        trick_name_text_view.setText(pattern_list[index]);
        goal_catches_text_view.setText("Goal: " + goals[index]);
        personalRecordTextView.setText(getPR(index) + "c.");
    }

    //poorly named - should be submit personal record
    public void increment_method(View view) {

        //write the result to shared preferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences("LOG", 0);
        SharedPreferences.Editor editor = settings.edit();
        //try to get the origional value for comparison
        int previous_best = settings.getInt(Integer.toString(index), 0);

        //get previous total number of catches
        int previous_total = settings.getInt("total_catches", 0);

        //listen to number picker
        NumberPicker npO = (NumberPicker) findViewById(R.id.numberPickerOnes);
        NumberPicker npT = (NumberPicker) findViewById(R.id.numberPickerTens);
        num_catches = npO.getValue() + npT.getValue() * 10;

        //calculate the new number of catches
        int new_total = previous_total + num_catches;

        //if the number entered is over the PR, record it, if not don't
        if (num_catches > previous_best) {

            String key = Integer.toString(index);
            editor.putInt(key, num_catches);
            Toast.makeText(this, "Record Updated\nNew Record: "
                    + Integer.toString(num_catches)+
                    "\nGreat Job!" +
                    "\nTotal number of catches: "+new_total, Toast.LENGTH_SHORT).show();
            //update the text view
            TextView personalRecordTextView = (TextView) findViewById(R.id.personal_record_text_view);
            personalRecordTextView.setText(Integer.toString(num_catches) + "c.");
        } else {
            Toast.makeText(this, "# of catches is below record."+
                    "\nRecord not updated."+
                    "\nTotal number of catches: "+new_total, Toast.LENGTH_SHORT).show();
        }

        //increment total number of catches
        editor.putInt("total_catches", new_total);
        //commit all applicable changes
        editor.commit();
    }

    public int getPR(int idx) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("LOG", 0);
        //get the personal record
        int personalRecord = settings.getInt(Integer.toString(idx), 0);
        return personalRecord;
    }

    //go on to next patter in the list
    public void nextTrick(View view) {
        //go on to the next trick in the list
        if (index < pattern_list.length - 1) {
            Intent toTraining = new Intent(training.this, training.class);
            toTraining.putExtra("index", index + 1);
            startActivity(toTraining);
        }
        //user has reached the end of the list and needs to go back to the home screen
        else {
            Intent backHome = new Intent(training.this, MainActivity.class);
            startActivity(backHome);
        }
    }

    //override the back button so that a new activity is created
    //this absolute hack takes care of updating the list, by completely redrawing it
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent backHome = new Intent(training.this, MainActivity.class);
                startActivity(backHome);
                break;
        }
        return true;
    }
    //and do the same for the back button on the bottom of the phone
    @Override
    public void onBackPressed(){
        Intent backHome = new Intent(training.this, MainActivity.class);
        startActivity(backHome);
    }
}