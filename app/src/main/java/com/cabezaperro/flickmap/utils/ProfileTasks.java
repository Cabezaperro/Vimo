package com.cabezaperro.flickmap.utils;

import android.app.*;
import android.content.Intent;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;

import com.cabezaperro.flickmap.*;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.User;
import com.cabezaperro.flickmap.data.repository.*;
import com.cabezaperro.flickmap.ui.SignInActivity;
import com.cabezaperro.flickmap.ui.base.BaseActivity;
import com.cabezaperro.flickmap.ui.settings.PasswordPreferenceFragment;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

import java.net.*;

public final class ProfileTasks
{
    public static class ProfileUsernameUpdateTask extends AsyncTask<Void, Void, String>
    {
        private final String newUsername;
        private AlertDialog.Builder builder;
        private AlertDialog updateDialog;

        public ProfileUsernameUpdateTask(String newUsername)
        {
            this.newUsername = newUsername;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            builder = new AlertDialog.Builder(FlickmapApplication.currentContext);

            ProgressBar progressBar = new ProgressBar(FlickmapApplication.currentContext, null, R.style.Widget_AppCompat_ProgressBar_Horizontal);
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            builder.setCancelable(false);
            builder.setTitle(R.string.updating_username);
            builder.setView(R.layout.dialog_loading);

            updateDialog = builder.create();
            updateDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            UserProfileChangeRequest profileUsernameUpdateRequest = new UserProfileChangeRequest.Builder().setDisplayName(newUsername).build();

            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUsernameUpdateRequest).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                        Toast.makeText(FlickmapApplication.currentContext, FlickmapApplication.currentContext.getString(R.string.username_updated), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(FlickmapApplication.currentContext, FlickmapApplication.currentContext.getString(R.string.error_updating_username), Toast.LENGTH_SHORT).show();

                    updateDialog.dismiss();
                    builder = null;
                }
            });

            return newUsername;
        }

        @Override
        protected void onPostExecute(final String username)
        {
            super.onPostExecute(username);

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        UserRepository.getInstance().getCurrentUser().setUsername(username);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static class ProfilePasswordUpdateTask extends AsyncTask<Void, Void, String>
    {
        private final PasswordPreferenceFragment fragment;
        private String newPassword;
        private AlertDialog.Builder builder;
        private AlertDialog updateDialog;

        public ProfilePasswordUpdateTask(PasswordPreferenceFragment fragment, String newPassword)
        {
            this.fragment = fragment;
            this.newPassword = newPassword;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            builder = new AlertDialog.Builder(FlickmapApplication.currentContext);

            ProgressBar progressBar = new ProgressBar(FlickmapApplication.currentContext, null, R.style.Widget_AppCompat_ProgressBar_Horizontal);
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            builder.setCancelable(false);
            builder.setTitle(R.string.updating_password);
            builder.setView(R.layout.dialog_loading);

            updateDialog = builder.create();
            updateDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Intent intent = new Intent(FlickmapApplication.currentContext, SignInActivity.class);

                        FlickmapApplication.currentContext.startActivity(intent);
                        BaseActivity.itemChecked = -1;

                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(FlickmapApplication.currentContext, FlickmapApplication.currentContext.getString(R.string.authenticate_to_change_password), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(FlickmapApplication.currentContext, FlickmapApplication.currentContext.getString(R.string.error_updating_password), Toast.LENGTH_SHORT).show();
                    }

                    fragment.dismiss();

                    updateDialog.dismiss();
                    builder = null;
                }
            });

            return newPassword;
        }

        @Override
        protected void onPostExecute(final String password)
        {
            super.onPostExecute(password);

            if (newPassword == null)
                return;

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        UserRepository.getInstance().getCurrentUser().setPassword(password);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static class ProfileImageBitmapCreationTask extends AsyncTask<Void, Void, Bitmap>
    {
        private final User user;

        public ProfileImageBitmapCreationTask(User user)
        {
            this.user = user;
        }

        @Override
        protected Bitmap doInBackground(Void... voids)
        {
            try
            {
                URL profileImageUrl = new URL(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());

                return BitmapFactory.decodeStream(profileImageUrl.openConnection().getInputStream());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);

            user.setImage(bitmap);
        }
    }

    public static class ProfileImageUpdateTask extends AsyncTask<Void, Void, Uri>
    {
        Uri imageUploadUri;
        Uri imageDownloadUri;
        AlertDialog.Builder builder;
        AlertDialog updateDialog;

        public ProfileImageUpdateTask(Uri imageUri)
        {
            this.imageUploadUri = imageUri;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            builder = new AlertDialog.Builder(FlickmapApplication.currentContext);

            ProgressBar progressBar = new ProgressBar(FlickmapApplication.currentContext, null, R.style.Widget_AppCompat_ProgressBar_Horizontal);
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            builder.setCancelable(false);
            builder.setTitle(R.string.updating_profile_image);
            builder.setView(R.layout.dialog_loading);

            updateDialog = builder.create();
            updateDialog.show();
        }

        @Override
        protected Uri doInBackground(Void... voids)
        {
            updateProfileImage();

            return imageDownloadUri;
        }

        @Override
        protected void onPostExecute(final Uri uri)
        {
            super.onPostExecute(uri);

            final URL[] profileImageUrl = new URL[1];

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        profileImageUrl[0] = new URL(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());

                        UserRepository.getInstance().getCurrentUser().setImageUriString(uri.toString());
                        UserRepository.getInstance().setProfileImage(BitmapFactory.decodeStream(profileImageUrl[0].openConnection().getInputStream()));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        private void updateProfileImage()
        {
            StorageReference profileImageReference = FirebaseStorage.getInstance().getReference("profile_images/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + ".jpg");

            if (imageUploadUri != null)
            {
                UploadTask uploadTask = profileImageReference.putFile(imageUploadUri);

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (task.isSuccessful())
                            return profileImageReference.getDownloadUrl();
                        else
                            throw task.getException();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        imageDownloadUri = uri;
                        updateFirebaseUserProfileImage();
                    }
                });
            }
        }

        private void updateFirebaseUserProfileImage()
        {
            UserProfileChangeRequest profileImageUpdateRequest = new UserProfileChangeRequest.Builder().setPhotoUri(imageDownloadUri).build();

            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileImageUpdateRequest).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        if (imageDownloadUri == null)
                            Toast.makeText(FlickmapApplication.currentContext, "imageDownloadUri is NULL", Toast.LENGTH_SHORT).show();

                        Toast.makeText(FlickmapApplication.currentContext, FlickmapApplication.currentContext.getString(R.string.profile_image_updated), Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(FlickmapApplication.currentContext, FlickmapApplication.currentContext.getString(R.string.error_updating_profile_image), Toast.LENGTH_SHORT).show();

                    updateDialog.dismiss();
                    builder = null;
                }
            });
        }
    }
}
