package com.example.marezina.rabysk.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.marezina.rabysk.R;

public class ClubActivity extends ActionBarActivity {

    private TextView club_id;
    private TextView club_name;
    private TextView club_uuid;
    private TextView club_url;
    private TextView club_created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        TextView club_id = (TextView) findViewById(R.id.id);
        TextView club_name = (TextView) findViewById(R.id.name);
        TextView club_uuid = (TextView) findViewById(R.id.uuid);
        TextView club_url = (TextView) findViewById(R.id.url);
        TextView club_created_at = (TextView) findViewById(R.id.created_at);

        Intent myIntent = getIntent(); // gets the previously created intent
        club_id.setText(myIntent.getStringExtra("club_id"));
        club_name.setText(myIntent.getStringExtra("club_name"));
        club_uuid.setText(myIntent.getStringExtra("club_uuid"));
        club_url.setText(myIntent.getStringExtra("club_url"));
        club_created_at.setText(myIntent.getStringExtra("club_created_at"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_club, menu);
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
}
