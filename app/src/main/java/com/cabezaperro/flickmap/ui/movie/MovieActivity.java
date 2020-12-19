package com.cabezaperro.flickmap.ui.movie;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.repository.UserRepository;
import com.cabezaperro.flickmap.ui.base.BaseActivity;

public class MovieActivity extends BaseActivity
{
    MovieListFragment movieListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BaseActivity.itemChecked = 0;

        showMovieListFragment();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = MovieActivity.this;
    }

    private void showMovieListFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        movieListFragment = (MovieListFragment) fragmentManager.findFragmentByTag(MovieListFragment.TAG);

        if (movieListFragment == null)
            movieListFragment = (MovieListFragment) MovieListFragment.createNewInstance(null);

        fragmentManager.beginTransaction().add(R.id.fmlList, movieListFragment, MovieListFragment.TAG).commit();
    }
}
