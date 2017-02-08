package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Help extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        String DEBUG_TAG = "Help";

        // Read raw file into string and populate textview
        InputStream iFile = getResources().openRawResource(R.raw.use);

        try
        {
            TextView helpText = (TextView) findViewById(R.id.howToUse1);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        }

        catch(Exception e)
        {
            Log.e(DEBUG_TAG, "InputStreamToString failure", e);
        }

        // Read the raw file for the information on Wayne
        iFile = getResources().openRawResource(R.raw.expect);

        try
        {
            TextView helpText = (TextView) findViewById(R.id.whatToExpect1);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        }

        catch(Exception e)
        {
            Log.e(DEBUG_TAG, "InputStreamToString failure", e);
        }

        //Read the raw file for the information on Dave
        iFile = getResources().openRawResource(R.raw.furtherhelp);

        try
        {
            TextView helpText = (TextView) findViewById(R.id.furtherHelp1);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        }

        catch(Exception e)
        {
            Log.e(DEBUG_TAG, "InputStreamToString failure", e);
        }

        // Button click listener to compose an email
        Button startBtn = (Button) findViewById(R.id.sendEmail);
        startBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                sendEmail(); // Starting the send email method
            }
        });
    }

    protected void sendEmail()
    {
        Log.i("Send email", ""); // Writing to the log when methods start
        String[] TO = {"helpramp@gmail.com"}; // Hard coding the email that will recieve the users message
        String[] CC = {""};
        // Setting up a new intent with action send to launch an email clienrt
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:")); // Specifying mailto: as URI to be able to send an email
        emailIntent.setType("text/plain"); // Setting the type
        //Attaching TO, CC, SUBJECT, and TEXT to the intent before sending the intent to the target email client
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        // Try block to start the emailIntent
        try
        {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", ""); // Writing to the logcat when the email is sent
        }
        // Catch block to be executed if there is no email client on the android device
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(Help.this, "There is no email client installed.", Toast.LENGTH_SHORT).show(); // Toast to tell the user that they need an email client
        }
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
