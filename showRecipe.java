package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class showRecipe extends Activity
{
    //used to retrieve specific recipe details
    String recipe, url;

    //user feedback
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        //variables passed from scrape.java
        Bundle extras = getIntent().getExtras();

        if(extras!=null)
        {
            recipe = extras.getString("button_name");
        }

        url = "http://www.afamilyfeast.com/" + recipe;
        new Scraping().execute();
    }

    //happening off of main thread
    private class Scraping extends AsyncTask<Void, Void, Void>
    {
        //headings
        String serving_time, ingredient_list, instruction_list;

        //arrays to hold the data scraped from a recipe
        String[] serving_times = new String[4];
        String[] ingredients_list = new String[30];
        String[] instructions_list = new String[30];

        //logcat tag
        String TAG = "Description";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //user feedback
            mProgressDialog = new ProgressDialog(showRecipe.this);
            mProgressDialog.setTitle("Finding Recipe Details");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try{
                //Connect to the website
                Document document = Jsoup.connect(url).get();

                //Using Elements to get the Meta data
/**************************** Serving Time ******************************************/
                Elements serve_time = document.select("div.ERSTopRight");
                Elements serves = serve_time.select("div.ERSRHSItem");

                int count = 0;

                //populate the first heading
                for(Element serve : serves)
                {
                    serving_time = serve.text();
                    serving_times[count] = serving_time;
                    count++;
                }

                serving_time = "";

                for(int i = 0; i < serving_times.length; i++ )
                {
                    serving_time = serving_time + serving_times[i] + "\n";
                }

/**************************** Ingredients ******************************************/
                Elements ingredient_div = document.select("div.ERSIngredients ul");
                Elements ingredients = ingredient_div.select("li.ingredient");

                count = 0;

                //populate the second heading
                for(Element ingredient : ingredients)
                {
                    ingredient_list = ingredient.text();
                    ingredients_list[count] = ingredient_list;
                    count++;
                }

                ingredient_list = "";

                for(int i = 0; i < count; i++ )
                {
                    ingredient_list = ingredient_list + "\u2022  " + ingredients_list[i] + "\n";
                }

/**************************** Instructions ******************************************/
                Elements instruction_diw = document.select("div.ERSInstructions ol");
                Elements instructions = instruction_diw.select("li.instruction");

                count = 0;

                //populate the third heading
                for(Element instruction : instructions)
                {
                    instruction_list = instruction.text();
                    instructions_list[count] = instruction_list;
                    count++;
                }

                instruction_list = "";

                for(int i = 0; i < count; i++ )
                {
                    instruction_list = instruction_list + "Step " + (i + 1) + ": " + instructions_list[i] + "\n\n";
                }

            }

            catch (IOException e)
            {
                Log.e(TAG, "Problem connecting to the Website.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            //Set description into text view
            TextView servetxt = (TextView) findViewById(R.id.serveText);
            servetxt.setText(serving_time);

            TextView ingedienttxt = (TextView) findViewById(R.id.ingredientText);
            ingedienttxt.setText(ingredient_list);

            TextView instructiontxt = (TextView) findViewById(R.id.instructionText);
            instructiontxt.setText(instruction_list);

            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_recipe, menu);
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
