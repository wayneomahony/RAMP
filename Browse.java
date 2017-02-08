package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class Browse extends Activity
{
    //arrayList to store text entered into search box
    ArrayList<String> items1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        //store what is entered in search box
        items1 = new ArrayList<String>();
    }

    public void goScraping(View v)
    {
        //searchText = what's in the search view
        final EditText search = (EditText) findViewById(R.id.searchEditText);

        //convert to string
        String one = search.getText().toString();

        //add to arrayList
        items1.add(one);

        //go scraping for the recipe
        Intent openScraping = new Intent(getApplicationContext(), scrape.class);
        openScraping.putExtra("key", items1);
        startActivity(openScraping);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
