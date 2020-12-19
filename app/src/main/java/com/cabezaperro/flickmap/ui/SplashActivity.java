package com.cabezaperro.flickmap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.model.Movie;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteCinema;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteMovie;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.repository.CinemaRepository;
import com.cabezaperro.flickmap.data.repository.MovieRepository;
import com.cabezaperro.flickmap.data.repository.SessionRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteCinemaRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteMovieRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteSessionRepository;
import com.cabezaperro.flickmap.ui.movie.MovieListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity
{
    private final int DELAY_TIME = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FlickmapApplication.currentContext = SplashActivity.this;

        FlickmapApplication.APP_LANGUAGE = getResources().getStringArray(R.array.languages)[0];
        FlickmapApplication.UPDATE_OPTION = getResources().getStringArray(R.array.update_options)[0];
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = SplashActivity.this;
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
                showSignInActivity();
            }
        };

        handler.postDelayed(runnable, DELAY_TIME);
    }

    private void showSignInActivity()
    {
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);

        startActivity(intent);
        finish();
    }
}
