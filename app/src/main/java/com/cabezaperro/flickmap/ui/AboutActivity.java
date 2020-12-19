package com.cabezaperro.flickmap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;

public class AboutActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = AboutActivity.this;
    }
}
