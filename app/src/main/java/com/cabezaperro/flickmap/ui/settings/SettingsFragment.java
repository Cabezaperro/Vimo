package com.cabezaperro.flickmap.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.repository.UserRepository;
import com.cabezaperro.flickmap.utils.ProfileTasks;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends PreferenceFragmentCompat implements UsernamePreferenceFragment.OnUsernameChangedListener, EmailPreferenceFragment.OnEmailChangedListener
{
    public static final String TAG = "SettingsFragment";
    private static final int CHOOSE_IMAGE = 101;
    private static String updateProfileParameter;

    Preference prfUsername;
    Preference prfPassword;
    Preference prfEmail;
    Preference prfProfileImage;
    ListPreference lprLanguage;
    SwitchPreferenceCompat spcNotifications;
    ListPreference lprUpdates;

    public static Fragment createNewInstance(Bundle bundle)
    {
        SettingsFragment settingsFragment = new SettingsFragment();

        if (bundle != null)
        {
            settingsFragment.setArguments(bundle);
            updateProfileParameter = bundle.getString("update_profile");
        }

        return settingsFragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.settings, rootKey);

        prfUsername = findPreference("prfUsername");
        prfPassword = findPreference("prfPassword");
        prfEmail = findPreference("prfEmail");
        prfProfileImage = findPreference("prfProfileImage");
        lprLanguage = findPreference("lprLanguage");
        spcNotifications = findPreference("spcNotifications");
        lprUpdates = findPreference("lprUpdates");

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            prfUsername.setSummary(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            prfEmail.setSummary(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        lprLanguage.setSummary(getString(R.string.language_determined_by_operating_system));
        spcNotifications.setChecked(true);
        lprUpdates.setSummary(FlickmapApplication.UPDATE_OPTION);

        lprLanguage.setValue(FlickmapApplication.APP_LANGUAGE);
        lprUpdates.setValue(FlickmapApplication.UPDATE_OPTION);

        prfUsername.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                loadUsernamePreferenceFragment();
                return true;
            }
        });

        prfPassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                loadPasswordPreferenceFragment();
                return true;
            }
        });

        prfEmail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                loadEmailPreferenceFragment();
                return true;
            }
        });

        prfProfileImage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                showImagePickerDialog();
                return true;
            }
        });

        lprLanguage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                FlickmapApplication.APP_LANGUAGE = newValue.toString();

                return true;
            }
        });

        lprUpdates.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                FlickmapApplication.UPDATE_OPTION = newValue.toString();
                preference.setSummary(newValue.toString());

                return true;
            }
        });

        if (updateProfileParameter != null && !updateProfileParameter.isEmpty())
        {
            loadFragmentByUpdateProfileParameter();
            updateProfileParameter = null;
        }
    }

    private void loadFragmentByUpdateProfileParameter()
    {
        switch (updateProfileParameter)
        {
            case "email":
                loadEmailPreferenceFragment();
                break;
            case "password":
                loadPasswordPreferenceFragment();
                break;
        }
    }

    private void loadUsernamePreferenceFragment()
    {
        FragmentManager fragmentManager = getParentFragmentManager();

        UsernamePreferenceFragment usernamePreferenceFragment = (UsernamePreferenceFragment) fragmentManager.findFragmentByTag(UsernamePreferenceFragment.TAG);

        if (usernamePreferenceFragment == null)
            usernamePreferenceFragment = UsernamePreferenceFragment.createNewInstance(this, null);

        usernamePreferenceFragment.show(fragmentManager, UsernamePreferenceFragment.TAG);
    }

    public void loadPasswordPreferenceFragment()
    {
        FragmentManager fragmentManager = getParentFragmentManager();

        PasswordPreferenceFragment passwordPreferenceFragment = (PasswordPreferenceFragment) fragmentManager.findFragmentByTag(PasswordPreferenceFragment.TAG);

        if (passwordPreferenceFragment == null)
            passwordPreferenceFragment = PasswordPreferenceFragment.createNewInstance(null);

        passwordPreferenceFragment.show(fragmentManager, PasswordPreferenceFragment.TAG);
    }

    private void loadEmailPreferenceFragment()
    {
        FragmentManager fragmentManager = getParentFragmentManager();

        EmailPreferenceFragment emailPreferenceFragment = (EmailPreferenceFragment) fragmentManager.findFragmentByTag(EmailPreferenceFragment.TAG);

        if (emailPreferenceFragment == null)
            emailPreferenceFragment = EmailPreferenceFragment.createNewInstance(this, null);

        emailPreferenceFragment.show(fragmentManager, EmailPreferenceFragment.TAG);
    }

    private void showImagePickerDialog()
    {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select new profile image"), CHOOSE_IMAGE);
    }

    @Override
    public void onUsernameChanged(String username)
    {
        prfUsername.setSummary(username);
    }

    @Override
    public void OnEmailChanged(String email)
    {
        prfEmail.setSummary(email);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null)
        {
            new ProfileTasks.ProfileImageUpdateTask(data.getData()).execute();
        }
    }
}
