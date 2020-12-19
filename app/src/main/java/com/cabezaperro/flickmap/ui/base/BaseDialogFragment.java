package com.cabezaperro.flickmap.ui.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment
{
    public static final String TAG = "BaseDialogFragment";
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";

    public interface OnDialogFinishedListener
    {
        void onDialogFinishedOk();
        void onDialogFinishedCancel();
    }

    public static BaseDialogFragment createNewInstance(Bundle bundle)
    {
        BaseDialogFragment baseDialogFragment = new BaseDialogFragment();

        if (bundle != null)
            baseDialogFragment.setArguments(bundle);

        return baseDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                OnDialogFinishedListener listener = (OnDialogFinishedListener)getTargetFragment();

                if (listener != null)
                    listener.onDialogFinishedOk();
            }
        });

        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                OnDialogFinishedListener listener = (OnDialogFinishedListener)getTargetFragment();

                dialog.dismiss();

                if (listener != null)
                    listener.onDialogFinishedCancel();
            }
        });

        return builder.create();
    }
}
