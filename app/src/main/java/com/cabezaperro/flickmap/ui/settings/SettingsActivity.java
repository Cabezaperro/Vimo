package com.cabezaperro.flickmap.ui.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;

public class SettingsActivity extends AppCompatActivity
{
    public static SettingsActivity instance;
    SettingsFragment settingsFragment;
    private Toolbar tlbToolbar;
    private String updateProfileParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
            updateProfileParameter = bundle.getString("update_profile");

        instance = SettingsActivity.this;

        tlbToolbar = findViewById(R.id.tlbSettingsToolbar);

        setSupportActionBar(tlbToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        loadSettingsFragment();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = SettingsActivity.this;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadSettingsFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        settingsFragment = (SettingsFragment)(fragmentManager.findFragmentByTag(SettingsFragment.TAG));

        if (settingsFragment == null)
        {
            if (updateProfileParameter != null && !updateProfileParameter.isEmpty())
            {
                Bundle bundle = new Bundle();
                bundle.putString("update_profile", updateProfileParameter);

                settingsFragment = (SettingsFragment) (SettingsFragment.createNewInstance(bundle));
                updateProfileParameter = null;
                finish();
            }
            else
                settingsFragment = (SettingsFragment) (SettingsFragment.createNewInstance(null));
        }

        fragmentManager.beginTransaction().add(R.id.fmlSettingsLayout, settingsFragment, SettingsFragment.TAG).commit();
    }
}
