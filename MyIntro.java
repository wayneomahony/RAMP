package com.appnucleus.loginandregisteruser;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;

import java.util.HashMap;

import helper.SQLiteHandler;

public class MyIntro extends AppIntro
{
    //android local database
    private SQLiteHandler db;

    @Override
    public void init(Bundle savedInstanceState)
    {
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");

        Toast temp = Toast.makeText(MyIntro.this, "Welcome\u0020" +name, Toast.LENGTH_SHORT);
        temp.show();

        // AppIntro will automatically generate the dots indicator and buttons.
        setFlowAnimation();
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));

        setSeparatorColor(Color.parseColor("#002196F3"));

        // show Skip and Done buttons
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed()
    {
        //go to menu when skip button is pressed
        Intent i = new Intent(getApplicationContext(),
                Menu_Activity.class);
        startActivity(i);
    }

    @Override
    public void onDonePressed()
    {
        //go to menu when done button is pressed
        Intent i = new Intent(getApplicationContext(),
                Menu_Activity.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged()
    {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed()
    {
        // Do something when users tap on Next button.
    }

}
