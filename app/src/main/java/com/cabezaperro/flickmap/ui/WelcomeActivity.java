package com.cabezaperro.flickmap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteMovie;
import com.cabezaperro.flickmap.ui.movie.MovieActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity
{
    private final int DELAY_TIME = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = WelcomeActivity.this;
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (!FlickmapApplication.repositoriesInitialised && FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            FlickmapApplication.initialiseRepositories();
            FlickmapApplication.repositoriesInitialised = true;
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(WelcomeActivity.this, MovieActivity.class);

                startActivity(intent);
                finish();
            }
        };

        handler.postDelayed(runnable, DELAY_TIME);
    }
}