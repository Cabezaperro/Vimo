package com.cabezaperro.flickmap.ui.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.repository.UserRepository;
import com.cabezaperro.flickmap.utils.ProfileTasks;
import com.cabezaperro.flickmap.utils.TextFieldUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PasswordPreferenceFragment extends DialogFragment
{
    public static final String TAG = "PasswordPreferenceFragment";

    private TextInputLayout tilPasswordPreferenceOldPassword;
    private TextInputEditText tiePasswordPreferenceOldPassword;

    private TextInputLayout tilPasswordPreferenceNewPassword;
    private TextInputEditText tiePasswordPreferenceNewPassword;

    private TextInputLayout tilPasswordPreferenceConfirmNewPassword;
    private TextInputEditText tiePasswordPreferenceConfirmNewPassword;

    private Button btnPasswordPreferenceConfirm;

    public static PasswordPreferenceFragment createNewInstance(Bundle bundle)
    {
        PasswordPreferenceFragment passwordPreferenceFragment = new PasswordPreferenceFragment();

        if (bundle != null)
            passwordPreferenceFragment.setArguments(bundle);

        passwordPreferenceFragment.setShowsDialog(true);

        return passwordPreferenceFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_password_preference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        tilPasswordPreferenceOldPassword = view.findViewById(R.id.tilPasswordPreferenceOldPassword);
        tiePasswordPreferenceOldPassword = view.findViewById(R.id.tiePasswordPreferenceOldPassword);

        tilPasswordPreferenceNewPassword = view.findViewById(R.id.tilPasswordPreferenceNewPassword);
        tiePasswordPreferenceNewPassword = view.findViewById(R.id.tiePasswordPreferenceNewPassword);

        tilPasswordPreferenceConfirmNewPassword = view.findViewById(R.id.tilPasswordPreferenceConfirmNewPassword);
        tiePasswordPreferenceConfirmNewPassword = view.findViewById(R.id.tiePasswordPreferenceConfirmNewPassword);

        btnPasswordPreferenceConfirm = view.findViewById(R.id.btnPasswordPreferenceConfirm);

        tiePasswordPreferenceOldPassword.addTextChangedListener(new PasswordPreferenceWatcher(tiePasswordPreferenceOldPassword));
        tiePasswordPreferenceNewPassword.addTextChangedListener(new PasswordPreferenceWatcher(tiePasswordPreferenceNewPassword));
        tiePasswordPreferenceConfirmNewPassword.addTextChangedListener(new PasswordPreferenceWatcher(tiePasswordPreferenceConfirmNewPassword));

        btnPasswordPreferenceConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updatePassword();
            }
        });
    }

    private void updatePassword()
    {
        if (!validateFields())
            return;

        new ProfileTasks.ProfilePasswordUpdateTask(PasswordPreferenceFragment.this, tiePasswordPreferenceNewPassword.getText().toString()).execute();

        dismiss();
    }

    private boolean validateFields()
    {
        if (!validateOldPassword() || !validateNewPassword() || !validateConfirmNewPassword())
            return false;

        return true;
    }

    private boolean validateOldPassword()
    {
        if (tiePasswordPreferenceOldPassword.getText() == null)
        {
            tilPasswordPreferenceOldPassword.setError(tilPasswordPreferenceOldPassword.getContext().getString(R.string.errPasswordNotCorrect));
            TextFieldUtils.displaySoftKeyboard(tiePasswordPreferenceOldPassword);
            return false;
        }

        String password = tiePasswordPreferenceOldPassword.getText().toString();

        if (!password.equals(UserRepository.getInstance().getCurrentUser().getPassword()))
        {
            tilPasswordPreferenceOldPassword.setError(tilPasswordPreferenceOldPassword.getContext().getString(R.string.errPasswordNotCorrect));
            TextFieldUtils.displaySoftKeyboard(tiePasswordPreferenceOldPassword);
            return false;
        }

        tilPasswordPreferenceOldPassword.setError(null);
        return true;
    }

    private boolean validateNewPassword()
    {
        if (!TextFieldUtils.validatePassword(tilPasswordPreferenceNewPassword, tiePasswordPreferenceNewPassword))
            return false;

        String password = tiePasswordPreferenceNewPassword.getText().toString();

        if (password.equals(UserRepository.getInstance().getCurrentUser().getPassword()))
        {
            tilPasswordPreferenceNewPassword.setError(getString(R.string.errNewPasswordIsSameAsOldOne));
            TextFieldUtils.displaySoftKeyboard(tiePasswordPreferenceNewPassword);
            return false;
        }

        tilPasswordPreferenceNewPassword.setError(null);
        return true;
    }

    private boolean validateConfirmNewPassword()
    {
        if (tiePasswordPreferenceConfirmNewPassword.getText() == null)
        {
            tilPasswordPreferenceConfirmNewPassword.setError(getString(R.string.errPasswordsDontMatch));
            TextFieldUtils.displaySoftKeyboard(tiePasswordPreferenceConfirmNewPassword);
            return false;
        }

        String password = tiePasswordPreferenceConfirmNewPassword.getText().toString();

        if (!password.equals(tiePasswordPreferenceNewPassword.getText().toString()))
        {
            tilPasswordPreferenceConfirmNewPassword.setError(getString(R.string.errPasswordsDontMatch));
            TextFieldUtils.displaySoftKeyboard(tiePasswordPreferenceConfirmNewPassword);
            return false;
        }

        tilPasswordPreferenceConfirmNewPassword.setError(null);
        return true;
    }

    class PasswordPreferenceWatcher implements TextWatcher
    {
        private final TextInputEditText tie;

        PasswordPreferenceWatcher(TextInputEditText tie)
        {
            this.tie = tie;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable s)
        {
            switch (tie.getId())
            {
                case R.id.tiePasswordPreferenceOldPassword:
                    if (tiePasswordPreferenceOldPassword.getText() == null || TextUtils.isEmpty(tiePasswordPreferenceOldPassword.getText()))
                        tilPasswordPreferenceOldPassword.setError(getString(R.string.errPasswordEmpty));
                    else
                        tilPasswordPreferenceOldPassword.setError(null);

                    if (!tiePasswordPreferenceOldPassword.getText().toString().equals(tiePasswordPreferenceOldPassword.getText().toString()))
                        tilPasswordPreferenceOldPassword.setError(getString(R.string.errPasswordsDontMatch));
                    else
                        tilPasswordPreferenceOldPassword.setError(null);
                    break;
                case R.id.tiePasswordPreferenceNewPassword:
                    validateNewPassword();

                    if (!tiePasswordPreferenceNewPassword.getText().toString().equals(tiePasswordPreferenceConfirmNewPassword.getText().toString()))
                        tilPasswordPreferenceConfirmNewPassword.setError(getString(R.string.errPasswordsDontMatch));
                    else
                        tilPasswordPreferenceConfirmNewPassword.setError(null);
                    break;
                case R.id.tiePasswordPreferenceConfirmNewPassword:
                    validateConfirmNewPassword();
                    break;
            }
        }
    }
}
