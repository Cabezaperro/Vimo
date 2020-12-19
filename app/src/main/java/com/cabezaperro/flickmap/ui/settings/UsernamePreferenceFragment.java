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
import android.widget.Toast;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.repository.UserRepository;
import com.cabezaperro.flickmap.utils.ProfileTasks;
import com.cabezaperro.flickmap.utils.TextFieldUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsernamePreferenceFragment extends DialogFragment
{
    public interface OnUsernameChangedListener
    {
        void onUsernameChanged(String username);
    }

    public static final String TAG = "UsernamePreferenceFragment";
    private static OnUsernameChangedListener onUsernameChangedListener;

    private TextInputLayout tilUsernamePreferenceNewUsername;
    private TextInputEditText tieUsernamePreferenceNewUsername;

    private TextInputLayout tilUsernamePreferencePassword;
    private TextInputEditText tieUsernamePreferencePassword;

    private TextInputLayout tilUsernamePreferenceConfirmPassword;
    private TextInputEditText tieUsernamePreferenceConfirmPassword;

    private Button btnUsernamePreferenceConfirm;

    public static UsernamePreferenceFragment createNewInstance(OnUsernameChangedListener listener, Bundle bundle)
    {
        UsernamePreferenceFragment usernamePreferenceFragment = new UsernamePreferenceFragment();

        if (bundle != null)
            usernamePreferenceFragment.setArguments(bundle);

        usernamePreferenceFragment.setShowsDialog(true);

        onUsernameChangedListener = listener;

        return usernamePreferenceFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_username_preference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        tilUsernamePreferenceNewUsername = view.findViewById(R.id.tilUsernamePreferenceNewUsername);
        tieUsernamePreferenceNewUsername = view.findViewById(R.id.tieUsernamePreferenceNewUsername);

        tilUsernamePreferencePassword = view.findViewById(R.id.tilUsernamePreferencePassword);
        tieUsernamePreferencePassword = view.findViewById(R.id.tieUsernamePreferencePassword);

        tilUsernamePreferenceConfirmPassword = view.findViewById(R.id.tilUsernamePreferenceConfirmPassword);
        tieUsernamePreferenceConfirmPassword = view.findViewById(R.id.tieUsernamePreferenceConfirmPassword);

        btnUsernamePreferenceConfirm = view.findViewById(R.id.btnUsernamePreferenceConfirm);

        tieUsernamePreferenceNewUsername.addTextChangedListener(new UsernamePreferenceWatcher(tieUsernamePreferenceNewUsername));
        tieUsernamePreferencePassword.addTextChangedListener(new UsernamePreferenceWatcher(tieUsernamePreferencePassword));
        tieUsernamePreferenceConfirmPassword.addTextChangedListener(new UsernamePreferenceWatcher(tieUsernamePreferenceConfirmPassword));

        btnUsernamePreferenceConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateUsername();
            }
        });
    }

    private void updateUsername()
    {
        if (!validateFields())
            return;

        new ProfileTasks.ProfileUsernameUpdateTask(tieUsernamePreferenceNewUsername.getText().toString()).execute();
        onUsernameChangedListener.onUsernameChanged(tieUsernamePreferenceNewUsername.getText().toString());

        dismiss();
    }

    private boolean validateFields()
    {
        if (!validateUsername() || !validatePassword() || !validateConfirmPassword())
            return false;

        return true;
    }

    private boolean validateUsername()
    {
        if (!TextFieldUtils.validateUsername(tilUsernamePreferenceNewUsername, tieUsernamePreferenceNewUsername))
            return false;

        if (tieUsernamePreferenceNewUsername.getText().toString().equals(UserRepository.getInstance().getCurrentUser().getUsername()))
        {
            tilUsernamePreferenceNewUsername.setError(getString(R.string.errNewUsernameIsSameAsOldOne));
            TextFieldUtils.displaySoftKeyboard(tieUsernamePreferenceNewUsername);
            return false;
        }

        tilUsernamePreferenceNewUsername.setError(null);
        return true;
    }

    private boolean validatePassword()
    {
        if (tieUsernamePreferencePassword.getText() == null)
        {
            tilUsernamePreferencePassword.setError(tilUsernamePreferencePassword.getContext().getString(R.string.errPasswordNotCorrect));
            TextFieldUtils.displaySoftKeyboard(tieUsernamePreferencePassword);
            return false;
        }

        String password = tieUsernamePreferencePassword.getText().toString();

        if (!password.equals(UserRepository.getInstance().getCurrentUser().getPassword()))
        {
            tilUsernamePreferencePassword.setError(tilUsernamePreferencePassword.getContext().getString(R.string.errPasswordNotCorrect));
            TextFieldUtils.displaySoftKeyboard(tieUsernamePreferencePassword);
            return false;
        }

        tilUsernamePreferencePassword.setError(null);
        return true;
    }

    private boolean validateConfirmPassword()
    {
        if (tieUsernamePreferenceConfirmPassword.getText() == null)
        {
            tilUsernamePreferenceConfirmPassword.setError(tilUsernamePreferenceConfirmPassword.getContext().getString(R.string.errPasswordsDontMatch));
            TextFieldUtils.displaySoftKeyboard(tieUsernamePreferenceConfirmPassword);
            return false;
        }

        String password = tieUsernamePreferenceConfirmPassword.getText().toString();

        if (!password.equals(tieUsernamePreferencePassword.getText().toString()))
        {
            tilUsernamePreferenceConfirmPassword.setError(getString(R.string.errPasswordsDontMatch));
            TextFieldUtils.displaySoftKeyboard(tieUsernamePreferenceConfirmPassword);
            return false;
        }

        tilUsernamePreferenceConfirmPassword.setError(null);
        return true;
    }

    class UsernamePreferenceWatcher implements TextWatcher
    {
        private final TextInputEditText tie;

        UsernamePreferenceWatcher(TextInputEditText tie)
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
                case R.id.tieUsernamePreferenceNewUsername:
                    validateUsername();
                    break;
                case R.id.tieUsernamePreferencePassword:
                    if (tieUsernamePreferencePassword.getText() == null || TextUtils.isEmpty(tieUsernamePreferencePassword.getText()))
                        tilUsernamePreferencePassword.setError(getString(R.string.errPasswordEmpty));
                    else
                        tilUsernamePreferencePassword.setError(null);

                    if (!tieUsernamePreferencePassword.getText().toString().equals(tieUsernamePreferenceConfirmPassword.getText().toString()))
                        tilUsernamePreferenceConfirmPassword.setError(getString(R.string.errPasswordsDontMatch));
                    else
                        tilUsernamePreferenceConfirmPassword.setError(null);
                    break;
                case R.id.tieUsernamePreferenceConfirmPassword:
                    validateConfirmPassword();
                    break;
            }
        }
    }
}
