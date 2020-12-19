package com.cabezaperro.flickmap.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cabezaperro.flickmap.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public final class TextFieldUtils
{
    public static boolean validateUsername(TextInputLayout til, TextInputEditText tie)
    {
        if (tie.getText() == null)
        {
            til.setError(til.getContext().getString(R.string.errUsernameEmpty));
            displaySoftKeyboard(tie);
            return false;
        }

        String username = tie.getText().toString();

        if (TextUtils.isEmpty(username.trim()))
        {
            til.setError(til.getContext().getString(R.string.errUsernameEmpty));
            displaySoftKeyboard(tie);
            return false;
        }

        if (username.length() > 70)
        {
            til.setError(til.getContext().getString(R.string.errUsernameTooLong));
            displaySoftKeyboard(tie);
            return false;
        }

        til.setError(null);
        return true;
    }

    public static boolean validatePassword(TextInputLayout til, TextInputEditText tie)
    {
        if (tie.getText() == null)
        {
            til.setError(til.getContext().getString(R.string.errPasswordEmpty));
            displaySoftKeyboard(tie);
            return false;
        }

        String password = tie.getText().toString();

        if (TextUtils.isEmpty(password))
        {
            til.setError(til.getContext().getString(R.string.errPasswordEmpty));
            displaySoftKeyboard(tie);
            return false;
        }

        if (!validatePasswordPattern(password))
        {
            til.setError(til.getContext().getString(R.string.errPasswordNotValid));
            displaySoftKeyboard(tie);
            return false;
        }

        til.setError(null);
        return true;
    }

    public static boolean validateEmail(TextInputLayout til, TextInputEditText tie)
    {
        if (tie.getText() == null)
        {
            til.setError(til.getContext().getString(R.string.errEmailEmpty));
            displaySoftKeyboard(tie);
            return false;
        }
        
        String email = tie.getText().toString();
        
        if (TextUtils.isEmpty(email))
        {
            til.setError(til.getContext().getString(R.string.errEmailEmpty));
            displaySoftKeyboard(tie);
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            til.setError(til.getContext().getString(R.string.errEmailNotValid));
            displaySoftKeyboard(tie);
            return false;
        }

        til.setError(null);
        return true;
    }

    public static void displaySoftKeyboard(View view)
    {
        if (view.requestFocus())
            ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
    }

    private static boolean validatePasswordPattern(String password)
    {
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.\\S+$).{8,12}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        return pattern.matcher(password).matches();
    }
}
