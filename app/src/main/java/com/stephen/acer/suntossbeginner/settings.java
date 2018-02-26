package com.stephen.acer.suntossbeginner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //make the back button on the top left
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //try to set the jugglers name
        SharedPreferences settings = getApplicationContext().getSharedPreferences("LOG", 0);
        String jugglers_name = settings.getString("NAME", "--none entered--");
        TextView nameTextView = (TextView) findViewById(R.id.jugglers_name);
        nameTextView.setText(jugglers_name);

    }

    public void deleteData(View view) {
        //show alert dialog to confirm delete
        AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
        builder.setCancelable(true);
        builder.setMessage("Delete all log data?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Data Cleared", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getSharedPreferences("LOG", 0);
                pref.edit().clear().commit();
                TextView nameTextView = (TextView) findViewById(R.id.jugglers_name);
                nameTextView.setText("Enter name here");
            }
        }).show();
    }

    public void enterName(View view) {
        //add new activity button
        //update shared preferences
        String m_Text = "";
        //new dialog to build name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Name:");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                SharedPreferences settings = getApplicationContext().getSharedPreferences("LOG", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("NAME", m_Text);
                editor.commit();
                String toast_string = "Name Updated\nThanks " + m_Text;
                Toast.makeText(getApplicationContext(), toast_string, Toast.LENGTH_SHORT).show();
                TextView nameTextView = (TextView) findViewById(R.id.jugglers_name);
                nameTextView.setText(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //override the back button so that a new activity is created
    //this absolute hack takes care of updating the list, by completely redrawing it
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent backHome = new Intent(settings.this, MainActivity.class);
                startActivity(backHome);
                break;
        }
        return true;
    }
    //and do the same for the back button on the bottom of the phone
    @Override
    public void onBackPressed(){
        Intent backHome = new Intent(settings.this, MainActivity.class);
        startActivity(backHome);
    }
}
