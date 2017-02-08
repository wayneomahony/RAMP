package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class About extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        //Tag for debugging with LogCat
        String DEBUG_TAG = "About";

        // Read raw files into stringd and populate the empty TextViews

        // Read raw file into string and populate textview of the General about
        InputStream iFile = getResources().openRawResource(R.raw.aboutus);

        try
        {
            TextView helpText = (TextView) findViewById(R.id.textAbout);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        }

        catch(Exception e)
        {
            Log.e(DEBUG_TAG, "InputStreamToString failure", e);
        }

        // Read the raw file for the information on Wayne
        iFile = getResources().openRawResource(R.raw.wayne);
        try
        {
            TextView helpText = (TextView) findViewById(R.id.wayneAbout);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        }

        catch(Exception e)
        {
            Log.e(DEBUG_TAG, "InputStreamToString failure", e);
        }

        //Read the raw file for the information on Dave
        iFile = getResources().openRawResource(R.raw.dave);

        try
        {
            TextView helpText = (TextView) findViewById(R.id.daveAbout);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        }

        catch(Exception e)
        {
            Log.e(DEBUG_TAG, "InputStreamToString failure", e);
        }

        // Read the raw file for the information on Ciaran
        iFile = getResources().openRawResource(R.raw.ciaran);

        try
        {
            TextView helpText = (TextView) findViewById(R.id.ciaranAbout);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        }

        catch(Exception e)
        {
            Log.e(DEBUG_TAG, "InputStreamToString failure", e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    public String inputStreamToString(InputStream is) throws IOException
    {
        //StringBuffer is used to represent characters that can be modified
        StringBuffer sBuffer = new StringBuffer();

        //BufferedReader is a wrapper for inputstreamreader which buffers the info
        BufferedReader dataIO = new BufferedReader(new InputStreamReader(is));

        //Stores what is being read in from an external text file
        String strLine = null;

        //while the file still has lines to read in - append the text onto the StringBuffer
        while((strLine = dataIO.readLine()) !=null )
        {
            sBuffer.append(strLine + "\n");
        }

        //close the stream
        dataIO.close();
        is.close();

        //convert the results of the stringBuffer to a string
        //contents of external text files appear as one string
        return sBuffer.toString();
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
