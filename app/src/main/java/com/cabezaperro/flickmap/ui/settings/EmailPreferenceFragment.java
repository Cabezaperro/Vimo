package com.cabezaperro.flickmap.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.repository.UserRepository;
import com.cabezaperro.flickmap.utils.TextFieldUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EmailPreferenceFragment extends DialogFragment
{
    public interface OnEmailChangedListener
    {
        void OnEmailChanged(String email);
    }

    public static final String TAG = "EmailPreferenceFragment";
    private static OnEmailChangedListener onEmailChangedListener;

    private TextInputLayout tilEmailPreferenceNewEmail;
    private TextInputEditText tieEmailPreferenceNewEmail;

    private TextInputLayout tilEmailPreferencePassword;
    private TextInputEditText tieEmailPreferencePassword;

    private TextInputLayout tilEmailPreferenceConfirmPassword;
    private TextInputEditText tieEmailPreferenceConfirmPassword;

    private Button btnEmailPreferenceConfirm;

    public static EmailPreferenceFragment createNewInstance(OnEmailChangedListener listener, Bundle bundle)
    {
        EmailPreferenceFragment emailPreferenceFragment = new EmailPreferenceFragment();

        if (bundle != null)
            emailPreferenceFragment.setArguments(bundle);

        emailPreferenceFragment.setShowsDialog(true);

        onEmailChangedListener = listener;

        return emailPreferenceFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_email_preference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        tilEmailPreferenceNewEmail = view.findViewById(R.id.tilEmailPreferenceNewEmail);
        tieEmailPreferenceNewEmail = view.findViewById(R.id.tieEmailPreferenceNewEmail);

        tilEmailPreferencePassword = view.findViewById(R.id.tilEmailPreferencePassword);
        tieEmailPreferencePassword = view.findViewById(R.id.tieEmailPreferencePassword);

        tilEmailPreferenceConfirmPassword = view.findViewById(R.id.tilEmailPreferenceConfirmPassword);
        tieEmailPreferenceConfirmPassword = view.findViewById(R.id.tieEmailPreferenceConfirmPassword);

        btnEmailPreferenceConfirm = view.findViewById(R.id.btnEmailPreferenceConfirm);

        tieEmailPreferenceNewEmail.addTextChangedListener(new EmailPreferenceWatcher(tieEmailPreferenceNewEmail));
        tieEmailPreferencePassword.addTextChangedListener(new EmailPreferenceWatcher(tieEmailPreferencePassword));
        tieEmailPreferenceConfirmPassword.addTextChangedListener(new EmailPreferenceWatcher(tieEmailPreferenceConfirmPassword));

        btnEmailPreferenceConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateEmail();
            }
        });
    }

    private void updateEmail()
    {
        if (!validateFields())
            return;

        UserRepository.getInstance().getCurrentUser().setEmail(tieEmailPreferenceNewEmail.getText().toString());
        UserRepository.getInstance().update(UserRepository.getInstance().getCurrentUser());

        onEmailChangedListener.OnEmailChanged(UserRepository.getInstance().getCurrentUser().getEmail());
        dismiss();
    }

    private boolean validateFields()
    {
        if (!validateEmail() || !validatePassword() || !validateConfirmPassword())
            return false;

        return true;
    }

    private boolean validateEmail()
    {
        if (!TextFieldUtils.validateEmail(tilEmailPreferenceNewEmail, tieEmailPreferenceNewEmail))
            return false;

        String email = tieEmailPreferenceNewEmail.getText().toString();

        if (email.equals(UserRepository.getInstance().getCurrentUser().getEmail()))
        {
            tilEmailPreferenceNewEmail.setError(getString(R.string.errNewEmailIsSameAsOldOne));
            TextFieldUtils.displaySoftKeyboard(tieEmailPreferenceNewEmail);
            return false;
        }

        tilEmailPreferenceNewEmail.setError(null);
        return true;
    }

    private boolean validatePassword()
    {
        if (tieEmailPreferencePassword.getText() == null)
        {
            tilEmailPreferencePassword.setError(tilEmailPreferencePassword.getContext().getString(R.string.errPasswordNotCorrect));
            TextFieldUtils.displaySoftKeyboard(tieEmailPreferencePassword);
            return false;
        }

        String password = tieEmailPreferencePassword.getText().toString();

        if (!password.equals(UserRepository.getInstance().getCurrentUser().getPassword()))
        {
            tilEmailPreferencePassword.setError(tilEmailPreferencePassword.getContext().getString(R.string.errPasswordNotCorrect));
            TextFieldUtils.displaySoftKeyboard(tieEmailPreferencePassword);
            return false;
        }

        tilEmailPreferencePassword.setError(null);
        return true;
    }

    private boolean validateConfirmPassword()
    {
        if (tieEmailPreferenceConfirmPassword.getText() == null)
        {
            tilEmailPreferenceConfirmPassword.setError(tilEmailPreferenceConfirmPassword.getContext().getString(R.string.errPasswordsDontMatch));
            TextFieldUtils.displaySoftKeyboard(tieEmailPreferenceConfirmPassword);
            return false;
        }

        String password = tieEmailPreferenceConfirmPassword.getText().toString();

        if (!password.equals(tieEmailPreferencePassword.getText().toString()))
        {
            tilEmailPreferenceConfirmPassword.setError(getString(R.string.errPasswordsDontMatch));
            TextFieldUtils.displaySoftKeyboard(tieEmailPreferenceConfirmPassword);
            return false;
        }

        tilEmailPreferenceConfirmPassword.setError(null);
        return true;
    }

    class EmailPreferenceWatcher implements TextWatcher
    {
        private TextInputEditText tie;

        EmailPreferenceWatcher(TextInputEditText tie)
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

        @Override
        public void afterTextChanged(Editable s)
        {
            switch (tie.getId())
            {
                case R.id.tieEmailPreferenceNewEmail:
                    validateEmail();
                    break;
                case R.id.tieEmailPreferencePassword:
                    validatePassword();
                    break;
                case R.id.tieEmailPreferenceConfirmPassword:
                    validateConfirmPassword();
                    break;
            }
        }
    }
}
