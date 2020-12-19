package com.cabezaperro.flickmap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.User;
import com.cabezaperro.flickmap.data.repository.UserRepository;
import com.cabezaperro.flickmap.ui.movie.MovieActivity;
import com.cabezaperro.flickmap.ui.settings.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignInActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private String updateProfileParameter;
    public static boolean showWelcomeActivity;

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText tieEmail;
    private TextInputEditText tiePassword;
    private Button btnSignIn;
    private Button btnSignUp;
    private CheckBox cbxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
            updateProfileParameter = bundle.getString("update_profile");

        firebaseAuth = FirebaseAuth.getInstance();
        showWelcomeActivity = false;

        tilEmail = findViewById(R.id.tilSignInEmail);
        tilPassword = findViewById(R.id.tilSignInPassword);
        tieEmail = findViewById(R.id.tieSignInEmail);
        tiePassword = findViewById(R.id.tieSignInPassword);
        btnSignIn = findViewById(R.id.btnSignInSignIn);
        btnSignUp = findViewById(R.id.btnSignInSignUp);
        cbxRememberMe = findViewById(R.id.cbxSignInRememberMe);

        tieEmail.addTextChangedListener(new SignInActivity.SignInWatcher(tieEmail));
        tiePassword.addTextChangedListener(new SignInActivity.SignInWatcher(tiePassword));

        cbxRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (cbxRememberMe.isChecked())
                    UserRepository.getInstance().setRememberUser(true);
                else
                    UserRepository.getInstance().setRememberUser(false);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showWelcomeActivity = true;
                signIn();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        FlickmapApplication.currentContext = SignInActivity.this;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
        boolean rememberCredentials = sharedPreferences.getBoolean("rememberCredentials", false);

        UserRepository.getInstance().setRememberUser(rememberCredentials);

        if (UserRepository.getInstance().getRememberUser())
        {
            tieEmail.setText(sharedPreferences.getString("email", null));
            tiePassword.setText(sharedPreferences.getString("password", null));
            cbxRememberMe.setChecked(true);
        }
        else
            cbxRememberMe.setChecked(false);

        if (firebaseAuth.getCurrentUser() == null)
            UserRepository.getInstance().setCurrentUser(null);
        else
        {
            User user = new User(firebaseAuth.getCurrentUser());
            user.setPassword(sharedPreferences.getString("password", null));

            UserRepository.getInstance().setCurrentUser(user);

            signInWithBypass();
        }
    }

    private void signIn()
    {
        String email = tieEmail.getText().toString();
        String password = tiePassword.getText().toString();

        if (email.isEmpty())
            tilEmail.setError(getString(R.string.errEmailEmpty));

        if (password.isEmpty())
            tilPassword.setError(getString(R.string.errPasswordEmpty));

        if (tilEmail.getError() != null || tilPassword.getError() != null)
            return;

        signInWithFirebase(email, password);
    }

    private void signInWithFirebase(String email, String password)
    {
        btnSignIn.setEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);

        ProgressBar progressBar = new ProgressBar(SignInActivity.this, null, R.style.Widget_AppCompat_ProgressBar_Horizontal);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        builder.setCancelable(false);
        builder.setView(R.layout.dialog_loading);
        builder.setTitle(R.string.signing_in);

        AlertDialog updateDialog = builder.create();
        updateDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    if (!FlickmapApplication.repositoriesInitialised && FirebaseAuth.getInstance().getCurrentUser() != null)
                    {
                        FlickmapApplication.initialiseMovieRepository();
                        FlickmapApplication.initialiseCinemaRepository();
                        FlickmapApplication.initialiseSessionRepository();

                        FlickmapApplication.initialiseFavouriteMovieRepository();
                        FlickmapApplication.initialiseFavouriteCinemaRepository();
                        FlickmapApplication.initialiseFavouriteSessionRepository();

                        FlickmapApplication.repositoriesInitialised = true;
                    }

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if (cbxRememberMe.isChecked())
                    {
                        UserRepository.getInstance().setRememberUser(true);
                        editor.putBoolean("rememberCredentials", true);
                        editor.putString("email", email);
                        editor.putString("password", password);
                    }
                    else
                        editor.putBoolean("rememberCredentials", false);

                    editor.apply();

                    if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    {
                        User user = new User(firebaseAuth.getCurrentUser());
                        user.setPassword(password);

                        UserRepository.getInstance().setCurrentUser(user);
                        UserRepository.getInstance().setLastUser(user);

                        if (updateProfileParameter != null && !updateProfileParameter.isEmpty())
                            signInWithParameter();
                        else
                            signInWithBypass();
                    }
                }
                else
                {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        Toast.makeText(SignInActivity.this, getString(R.string.errEmailAndOrPasswordAreNotCorrect), Toast.LENGTH_SHORT).show();
                    else if (task.getException() instanceof FirebaseAuthInvalidUserException)
                        Toast.makeText(SignInActivity.this, getString(R.string.errEmailAndOrPasswordAreNotCorrect), Toast.LENGTH_SHORT).show();

                    btnSignIn.setEnabled(true);
                }

                updateDialog.dismiss();
            }
        });
    }

    private void signInWithParameter()
    {
        switch (updateProfileParameter)
        {
            case "password":
                Intent intent = new Intent(SignInActivity.this, SettingsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("update_profile", "password");
                intent.putExtras(bundle);

                updateProfileParameter = null;
                startActivity(intent);
                finish();
                break;
        }
    }

    private void signInWithBypass()
    {
        Intent intent;

        if (showWelcomeActivity)
            intent = new Intent(SignInActivity.this, WelcomeActivity.class);
        else
            intent = new Intent(SignInActivity.this, MovieActivity.class);

        startActivity(intent);
        finish();
    }

    class SignInWatcher implements TextWatcher
    {
        private final View view;

        public SignInWatcher(View view)
        {
            this.view = view;
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
            switch (view.getId())
            {
                case R.id.tieSignInEmail:
                    if (tieEmail.getText() == null || TextUtils.isEmpty(tieEmail.getText()))
                        tilEmail.setError(getString(R.string.errEmailEmpty));
                    else
                        tilEmail.setError(null);
                    break;
                case R.id.tieSignInPassword:
                    if (tiePassword.getText() == null || TextUtils.isEmpty(tiePassword.getText()))
                        tilPassword.setError(getString(R.string.errPasswordEmpty));
                    else
                        tilPassword.setError(null);
                    break;
            }
        }
    }
}
