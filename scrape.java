package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class scrape extends Activity
{
    //LogCat tag
    private static final String TAG_NAME = "recipe_name";
    //Intent i = getIntent();

    //handles the search items passed from various pages
    ArrayList<String> array;

    //array to store links
    String[] recipe_links;

    //the website that we are scraping results from
    String pySearch = "http://www.afamilyfeast.com/?s=";
    //String pySearch = "http://www.afamilyfeast.com/?s=chicken+carrots+"; --> Hardcoded test

    //gives the user some feedback as to what's happening
    ProgressDialog mProgressDialog;

    //listView to store the list of recipes
    ListView recipesList;

    //array containing all the recipes
    ArrayList<HashMap<String, String>> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrape);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        //initialised to match corresponding layout objects
        recipesList = (ListView) findViewById(R.id.recipeView);
        recipeList = new ArrayList<HashMap<String,String>>();
        Bundle i = getIntent().getExtras();

        //key is what has been passed from other activities to here (browse and pantry pass the key variable)
        array = i.getStringArrayList("key");

        //this loops appends the search terms onto a url
        for(int n = 0; n<array.size(); n++)
        {
            pySearch = pySearch + array.get(n) + "+";
            //Toast.makeText(scrape.this, pySearch, Toast.LENGTH_SHORT).show();
        }

        new Title().execute();
    }

    //work to take place off of main thread
    private class Title extends AsyncTask<Void, Void, Void>
    {
        String recipe_link = "";
        String[] recipe_links = new String[20];
        String TAG = "title";

        @Override
        protected void onPreExecute()
        {
            //give the user some feedback as to whats happening
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(scrape.this);
            mProgressDialog.setTitle("Finding Recipes");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                //search for the relevant tags and capture the contents of the tags
                Document document = Jsoup.connect(pySearch).get();
                Elements title = document.select("div.entry-content");
                Elements links = title.select("a[href]");
                recipe_link = links.attr("href");

                int count = 0;

                //populates the recipe_links array
                for(Element link : links)
                {
                    recipe_links[count] = link.attr("href") + "\n";
                    count++;
                }
            }

            catch(IOException e)
            {
                Log.e(TAG, "Problem connecting to website");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {

            //show in list of buttons
            Button[] btn = new Button[recipe_links.length];

            for(int n=0;n<recipe_links.length;n++)
            {
                //alter what actually gets put into the button text
                recipe_links[n] = recipe_links[n].replace("http://www.afamilyfeast.com/", "");
                recipe_links[n] = recipe_links[n].replace("/", "");
                //code to improve menu output
                recipe_links[n] = recipe_links[n].replace("-"," ");

                String recipe_name = "recipe";

                HashMap<String, String> recipes = new HashMap<String,String>();

                //done for the logcat
                recipes.put(TAG_NAME, recipe_links[n]);

                recipeList.add(recipes);
                btn[n] = new Button(getApplicationContext());
            }

            //populate listView
            ListAdapter adapter = new SimpleAdapter
                    (
                            scrape.this, recipeList, R.layout.recipe_item, new String[]{TAG_NAME},
                            new int[]{R.id.recipeName}
                    );

            recipesList.setAdapter(adapter);

            //close dialog
            mProgressDialog.dismiss();
        }
    }

    public void recipeToast(View v)
    {
        //grab button text
        Button b = (Button)v;
        String buttonText = b.getText().toString();

        //replace spaces with dashes
        buttonText = buttonText.replace(" ","-");

        //this button scrapes the recipe details on displays them
        Intent i = new Intent(getApplicationContext(), showRecipe.class);
        i.putExtra("button_name", buttonText);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrape, menu);
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
