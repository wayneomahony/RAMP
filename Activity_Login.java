package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import helper.SessionManager;
import volley.AppController;
import volley.Config_URL;

public class Activity_Login extends Activity
{
    // LogCat tag
    private static final String TAG = Activity_Register.class.getSimpleName();

    //Buttons to correspond to layout buttons
    private Button btnLogin;
    private Button btnLinkToRegister;

    //Buttons to correspond to layout EditTexts
    private EditText inputEmail;
    private EditText inputPassword;

    //To give the user some feedback while network tasks are happening
    private ProgressDialog pDialog;

    //To save the users session
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        //assign relevant variables to the corresponding id's
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Activity_Register.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        //let the user know what's happening
        pDialog.setMessage("Logging in ...");
        showDialog();

        //Creates a new GET request
        StringRequest strReq = new StringRequest(Method.POST,
                Config_URL.URL_REGISTER, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try
                {
                    //Used for data interchanging - JSON is a lightweight format
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error)
                    {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Launch main activity
                        Intent intent = new Intent(Activity_Login.this,
                                Menu_Activity.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                }

                catch (JSONException e)
                {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Login Error: " + error.getMessage());

                //Toast to show error message
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog()
    {
        if (!pDialog.isShowing())
        {
            pDialog.show();
        }
    }

    private void hideDialog()
    {
        if (pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }
}