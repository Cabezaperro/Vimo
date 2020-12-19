package com.cabezaperro.flickmap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.utils.ProfileTasks;
import com.cabezaperro.flickmap.utils.TextFieldUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;

    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextInputLayout tilEmail;

    private TextInputEditText tieUsername;
    private TextInputEditText tiePassword;
    private TextInputEditText tieEmail;

    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        tilUsername = findViewById(R.id.tilSignUpUsername);
        tilPassword = findViewById(R.id.tilSignUpPassword);
        tilEmail = findViewById(R.id.tilSignUpEmail);

        tieUsername = findViewById(R.id.tieSignUpUsername);
        tiePassword = findViewById(R.id.tieSignUpPassword);
        tieEmail = findViewById(R.id.tieSignUpEmail);

        tieUsername.addTextChangedListener(new SignUpWatcher(tieUsername));
        tiePassword.addTextChangedListener(new SignUpWatcher(tiePassword));
        tieEmail.addTextChangedListener(new SignUpWatcher(tieEmail));

        btnSignUp = findViewById(R.id.btnSignUpSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createNewUserAccount();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = SignUpActivity.this;
    }

    private void createNewUserAccount()
    {
        String username = tieUsername.getText().toString();
        String password = tiePassword.getText().toString();
        String email = tieEmail.getText().toString();

        if (TextFieldUtils.validateUsername(tilUsername, tieUsername) && TextFieldUtils.validatePassword(tilPassword, tiePassword) && TextFieldUtils.validateEmail(tilEmail, tieEmail))
        {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Uri profileImageUri = Uri.parse("https://www.nailseatowncouncil.gov.uk/wp-content/uploads/blank-profile-picture-973460_1280.jpg");

                        new ProfileTasks.ProfileImageUpdateTask(profileImageUri).execute();

                        UserProfileChangeRequest profileUsernameUpdateRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        user.updateProfile(profileUsernameUpdateRequest);

                        Toast.makeText(SignUpActivity.this, getString(R.string.user_created_successfully), Toast.LENGTH_SHORT).show();
                        SignInActivity.showWelcomeActivity = true;
                        finish();
                    }
                    else if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(SignUpActivity.this, getString(R.string.account_already_exists), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, getString(R.string.error_creating_user), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    class SignUpWatcher implements TextWatcher
    {
        private final View view;

        public SignUpWatcher(View view)
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
                case R.id.tieSignUpUsername:
                    TextFieldUtils.validateUsername(tilUsername, tieUsername);
                    break;
                case R.id.tieSignUpPassword:
                    TextFieldUtils.validatePassword(tilPassword, tiePassword);
                    break;
                case R.id.tieSignUpEmail:
                    TextFieldUtils.validateEmail(tilEmail, tieEmail);
                    break;
            }
        }
    }
}
