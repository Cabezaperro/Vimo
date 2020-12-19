package com.cabezaperro.flickmap.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cabezaperro.flickmap.utils.ProfileTasks;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class User implements Parcelable
{
    private String username;
    private String password;
    private String email;
    private String imageUriString;
    private Bitmap image;

    public User()
    {
    }

    public User(FirebaseUser firebaseUser)
    {
        username = firebaseUser.getDisplayName();
        email = firebaseUser.getEmail();
        imageUriString = "";

        new ProfileTasks.ProfileImageBitmapCreationTask(this).execute();
    }

    protected User(Parcel in)
    {
        username = in.readString();
        password = in.readString();
        email = in.readString();
        imageUriString = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel in)
        {
            return new User(in);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getImageUriString()
    {
        return imageUriString;
    }

    public void setImageUriString(String imageUriString)
    {
        this.imageUriString = imageUriString;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString()
    {
        return getUsername();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(email);
    }

    @Override
    public int describeContents()
    {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(imageUriString);
    }
}
