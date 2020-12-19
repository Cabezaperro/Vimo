package com.cabezaperro.flickmap.ui.showtime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.ui.base.BaseActivity;

public class ShowtimeActivity extends BaseActivity
{
    ShowtimeListFragment showtimeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BaseActivity.itemChecked = 2;

        showShowtimeListFragment();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = ShowtimeActivity.this;
    }

    private void showShowtimeListFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        showtimeListFragment = (ShowtimeListFragment) fragmentManager.findFragmentByTag(ShowtimeListFragment.TAG);

        if (showtimeListFragment == null)
            showtimeListFragment = (ShowtimeListFragment) ShowtimeListFragment.createNewInstance(null);

        fragmentManager.beginTransaction().add(R.id.fmlList, showtimeListFragment, ShowtimeListFragment.TAG).commit();
    }
}
