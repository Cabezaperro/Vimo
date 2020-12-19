package com.cabezaperro.flickmap.data.repository;

import android.graphics.Bitmap;

import com.cabezaperro.flickmap.data.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository
{
    private static UserRepository repository;
    private User currentUser;
    private User lastUser;
    private boolean rememberUser;
    private Bitmap profileImage;

    static
    {
        repository = new UserRepository();
    }

    private UserRepository()
    {
        rememberUser = false;
    }

    public static UserRepository getInstance()
    {
        return repository;
    }

    public List<User> getList()
    {
        List<User> list = new ArrayList<>();

        return list;
    }

    public void update(User user)
    {

    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(User currentUser)
    {
        this.currentUser = currentUser;
    }

    public User getLastUser()
    {
        return lastUser;
    }

    public void setLastUser(User lastUser)
    {
        this.lastUser = lastUser;
    }

    public boolean getRememberUser()
    {
        return rememberUser;
    }

    public void setRememberUser(boolean rememberUser)
    {
        this.rememberUser = rememberUser;
    }

    public Bitmap getProfileImage()
    {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage)
    {
        this.profileImage = profileImage;
    }
}
