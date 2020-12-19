package com.cabezaperro.flickmap.ui.favourite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.ui.favourite.cinema.FavouriteCinemaListFragment;
import com.cabezaperro.flickmap.ui.favourite.movie.FavouriteMovieListFragment;
import com.cabezaperro.flickmap.ui.favourite.session.FavouriteSessionListFragment;
import com.cabezaperro.flickmap.ui.favourite.showtime.FavouriteShowtimeListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavouriteFragment extends Fragment
{
    public static final String TAG = "FavouriteFragment";

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener listener;

    private FavouriteMovieListFragment favouriteMovieListFragment;
    private FavouriteCinemaListFragment favouriteCinemaListFragment;
    private FavouriteSessionListFragment favouriteSessionListFragment;

    public static FavouriteFragment createNewInstance(Bundle bundle)
    {
        FavouriteFragment favouriteFragment = new FavouriteFragment();

        if (bundle != null)
            favouriteFragment.setArguments(bundle);

        return favouriteFragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        bottomNavigationView = view.findViewById(R.id.bnvBottomNavigationView);
        initialiseNavigation();

        loadFavouriteMovieListFragment();

        return view;
    }

    private void initialiseNavigation()
    {
        listener = new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                Fragment fragment = null;

                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                switch (menuItem.getItemId())
                {
                    case R.id.mniFavouriteMovies:
                        loadFavouriteMovieListFragment();
                        break;
                    case R.id.mniFavouriteCinemas:
                        loadFavouriteCinemaListFragment();
                        break;
                    case R.id.mniFavouriteShowtimes:
                        fragment = FavouriteShowtimeListFragment.createNewInstance(null);
                        loadSimpleFragment(fragment);
                        break;
                    case R.id.mniFavouriteSessions:
                        fragment = FavouriteSessionListFragment.createNewInstance(null);
                        loadFavouriteSessionListFragment();
                        break;
                }
                
                return true;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
    }

    private boolean loadSimpleFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fmlFavouriteFragmentContent, fragment).commit();
            return true;
        }

        return false;
    }

    private void loadFavouriteMovieListFragment()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        favouriteMovieListFragment = (FavouriteMovieListFragment) fragmentManager.findFragmentByTag(FavouriteMovieListFragment.TAG);

        if (favouriteMovieListFragment == null)
            favouriteMovieListFragment = (FavouriteMovieListFragment) FavouriteMovieListFragment.createNewInstance(null);

        fragmentManager.beginTransaction().replace(R.id.fmlFavouriteFragmentContent, favouriteMovieListFragment, FavouriteMovieListFragment.TAG).commitAllowingStateLoss();
    }

    private void loadFavouriteCinemaListFragment()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        favouriteCinemaListFragment = (FavouriteCinemaListFragment)fragmentManager.findFragmentByTag(FavouriteCinemaListFragment.TAG);

        if (favouriteCinemaListFragment != null)
            fragmentManager.beginTransaction().remove(favouriteCinemaListFragment);

        favouriteCinemaListFragment = FavouriteCinemaListFragment.createNewInstance(null);

        fragmentManager.beginTransaction().replace(R.id.fmlFavouriteFragmentContent, favouriteCinemaListFragment, FavouriteCinemaListFragment.TAG).commitAllowingStateLoss();
    }

    private void loadFavouriteSessionListFragment()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        favouriteSessionListFragment = (FavouriteSessionListFragment) fragmentManager.findFragmentByTag(FavouriteSessionListFragment.TAG);

        if (favouriteSessionListFragment == null)
            favouriteSessionListFragment = FavouriteSessionListFragment.createNewInstance(null);

        fragmentManager.beginTransaction().replace(R.id.fmlFavouriteFragmentContent, favouriteSessionListFragment, FavouriteSessionListFragment.TAG).commitAllowingStateLoss();
    }
}
