package com.cabezaperro.flickmap.ui.cinema;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.ui.base.BaseActivity;

public class CinemaActivity extends BaseActivity
{
    CinemaListFragment cinemaListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BaseActivity.itemChecked = 1;

        showCinemaListFragment();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = CinemaActivity.this;
    }

    private void showCinemaListFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        cinemaListFragment = (CinemaListFragment) fragmentManager.findFragmentByTag(CinemaListFragment.TAG);

        if (cinemaListFragment == null)
            cinemaListFragment = (CinemaListFragment) CinemaListFragment.createNewInstance(null);

        fragmentManager.beginTransaction().add(R.id.fmlList, cinemaListFragment, CinemaListFragment.TAG).commit();
    }
}
