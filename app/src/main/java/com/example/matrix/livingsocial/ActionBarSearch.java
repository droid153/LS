package com.example.matrix.livingsocial;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;


public class ActionBarSearch extends Activity {

    EditText tvZipcode;
    Button buttonSearch;
    String zipCode;
    long zip;

    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar_search);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvZipcode = (EditText) findViewById(R.id.idEditTextZipcode);
        tvZipcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvZipcode.setText("");
                tvZipcode.setCursorVisible(true);
                tvZipcode.setTextColor(Color.parseColor("#000000"));
            }
        });

        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Edit URL
                zipCode = tvZipcode.getText().toString();
                zip = Long.parseLong(tvZipcode.getText().toString());   //Log.d("ZIP",Long.toString(zip));


                // Move to MyActivity
                Intent intent = new Intent(ActionBarSearch.this, MyActivity.class);
                intent.putExtra("ZIPCODE",zipCode);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
