package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class read extends Activity
{
    //string for JSONobject
    String myJSON;

    //strings to be stored from dialog
    String ingredient_nameD;
    String ingredient_categoryD;

    //LogCat tags
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "ingredient_id";
    private static final String TAG_NAME = "ingredient_name";
    private static final String TAG_CAT ="ingredient_category";

    //data being read in from the database
    JSONArray ingredientsArray;

    //used to store selected items for recipe search
    ArrayList<String> items1;

    //arraylist for the items read in from the database
    ArrayList<HashMap<String, String>> ingredientList;

    //arraylist of items to be passed to scraper for searching recipes
    ArrayList<HashMap<String, String>> searchList;

    //list of database items
    ListView ingredientsList;

    //list of search items
    ListView searchItems;

    //corresponds to textView txt1 in layout file
    TextView tx;

    //populate dropdown of dialog
    String[] s = { "Meats", "Vegetables", "Fruits", "Dairy" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        //initialise the above variables
        ingredientsList = (ListView) findViewById(R.id.listView);
        searchItems = (ListView) findViewById(R.id.listView1);
        ingredientList = new ArrayList<HashMap<String,String>>();
        searchList = new ArrayList<HashMap<String,String>>();
        items1 = new ArrayList<String>();

        //on creation of the activity, call the method to retrieve whats in the database
        getData();
    }

    public void onClick(View v)
    {
        //store the text of the button into a variable
        Button b = (Button)v;
        String buttonText = b.getText().toString();

        //if buttonText is equal to an existing list item, stop
        for(int i = 0;i<items1.size();i++)
        {
            if(items1.get(i).equals(buttonText))
            {
                return;
            }
        }

        //if the method hasn't been jumped out of, add clicked item to array
        items1.add(buttonText);

        RelativeLayout rl = (RelativeLayout)v.getParent();

        //store array items in hashmap
        HashMap<String, String> items = new HashMap<String,String>();

        //hashmap is used so the string and tag can be used
        items.put(TAG_NAME, buttonText);

        //add the hashmap to this array
        searchList.add(items);

        //this method handles entering the button onto the adjacent listView
        ListAdapter adapter = new SimpleAdapter
                (
                        read.this, searchList, R.layout.list_item, new String[]{TAG_NAME},
                        new int[]{R.id.name}
                );

        //populate the listView with the searchItems array
        searchItems.setAdapter(adapter);

    }

    public void showDialog(Activity activity)
    {
        //the spinner in the dialog comes with an array for item categories
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(read.this,
                android.R.layout.simple_spinner_item, s);

        //textView txt1 holds the dropdown for categories
        tx = (TextView)findViewById(R.id.txt1);
        final Spinner sp = new Spinner(read.this);
        sp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);

        //EditText is the placeholder for where the user enters the item that they
        //want to manually add to the database
        final EditText input = new EditText(read.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("Type item to be added");

        //with the need to add two views to the alert dialog,
        //layout had to be declared to hold two views
        LinearLayout layout = new LinearLayout(read.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(sp);
        layout.addView(input);

        //dark theme chosen for the dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }

        else
        {
            alertDialogBuilder = new AlertDialog.Builder(this);
        }

        //setting the dialog attributes
        alertDialogBuilder
                .setTitle("Select category of item")
                .setView(layout)
                .setCancelable(false)
                .setPositiveButton("Add to Pantry", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // Do something with parameter.
                        ingredient_nameD = input.getText().toString();
                        ingredient_categoryD = sp.getSelectedItem().toString();
                        //Toast.makeText(read.this, ingredient_name+ingredient_category, Toast.LENGTH_SHORT).show();
                        addItemToPantry();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();

                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void insertItem(View v)
    {
        //display dialog
        showDialog(read.this);
    }

    public void goScraping(View v)
    {
        //once the user wants to search for a recipe based on selected items,
        //the new activity is opened with the user's selections passed to that activity
        Intent openScraping = new Intent(getApplicationContext(), scrape.class);
        openScraping.putExtra("key", items1);
        startActivity(openScraping);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    //querys the ingredients database and shows the results
    protected void showList()
    {
        try
        {
            //holds the db results
            JSONObject jsonObj = new JSONObject(myJSON);
            ingredientsArray = jsonObj.getJSONArray(TAG_RESULTS);

            //all db results will be buttons in an array
            Button[] btn = new Button[ingredientsArray.length()];

            //for the amount of items, loop through and fill the arrays
            for(int i=0;i<ingredientsArray.length();i++)
            {
                JSONObject c = ingredientsArray.getJSONObject(i);
                String ingredient_id = c.getString(TAG_ID);
                String ingredient_name = c.getString(TAG_NAME);
                String ingredient_category = c.getString(TAG_CAT);

                HashMap<String, String> ingredients = new HashMap<String,String>();

                ingredients.put(TAG_NAME, ingredient_name);

                ingredientList.add(ingredients);
                btn[i] = new Button(getApplicationContext());
            }

            //add the db results to a listView
            ListAdapter adapter = new SimpleAdapter
                    (
                            read.this, ingredientList, R.layout.list_item, new String[]{TAG_NAME},
                            new int[]{R.id.name}
                    );

            ingredientsList.setAdapter(adapter);

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void addItemToPantry()
    {
        //to ensure that the correct variables were being captured
        Toast.makeText(read.this, ingredient_nameD+" "+ingredient_categoryD, Toast.LENGTH_SHORT).show();

        //method to write to db unsuccessful!!
    }

    //this method actually does the work of retrieving the db data
    //showList() just puts it on the screen!
    public void getData()
    {
        //make the network tasks happen off of the main thread
        class GetDataJSON extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                //set up the connection and call the relevant php file for the query
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://10.0.2.2/get-data.php");
                //HttpPost httppost = new HttpPost("http://172.30.8.19/get-data.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;

                //interpret the response and store the result to a string
                try
                {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;

                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }

                    result = sb.toString();

                }

                catch (Exception e)
                {
                    // Oops
                }

                finally
                {
                    try
                    {
                        if(inputStream != null)inputStream.close();
                    }

                    catch(Exception squish)
                    {

                    }
                }
                //query result!
                return result;
            }

            @Override
            protected void onPostExecute(String result)
            {
                myJSON=result;
                showList();
            }
        } //end of async task

        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read, menu);
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
