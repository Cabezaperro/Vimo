package com.cabezaperro.flickmap.ui.favourite;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.ui.base.BaseActivity;

public class FavouriteActivity extends BaseActivity
{
    FavouriteFragment favouriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BaseActivity.itemChecked = 3;

        showFavouriteFragment();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = FavouriteActivity.this;
    }

    private void showFavouriteFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        favouriteFragment = (FavouriteFragment) fragmentManager.findFragmentByTag(FavouriteFragment.TAG);

        if (favouriteFragment == null)
            favouriteFragment = FavouriteFragment.createNewInstance(null);

        fragmentManager.beginTransaction().add(R.id.fmlList, favouriteFragment, FavouriteFragment.TAG).commitAllowingStateLoss();
    }
}
