package com.appnucleus.loginandregisteruser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Menu_Activity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        //Buttons to correspond to layout buttons
        Button logoutButton = (Button) findViewById(R.id.logoutBtn);
        Button pantryButton = (Button)findViewById(R.id.pantry);
        Button browseButton = (Button)findViewById(R.id.browse);
        Button contactButton = (Button)findViewById(R.id.contact);
        Button aboutButton = (Button)findViewById(R.id.about);
        Button helpButton = (Button)findViewById(R.id.help);

        //log the user out
        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(i);
            }
        });

        //go to the read page
        pantryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), read.class);
                startActivity(i);
            }
        });

        //go to the browse recipes page
        browseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Browse.class);
                startActivity(i);
            }
        });

        //go to the contact us page
        contactButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), ContactUs.class);
                startActivity(i);
            }
        });

        //go to the about us page
        aboutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), About.class);
                startActivity(i);
            }
        });

        //go to the need help page
        helpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Help.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_, menu);
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
