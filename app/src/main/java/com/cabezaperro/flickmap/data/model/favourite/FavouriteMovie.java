package com.cabezaperro.flickmap.data.model.favourite;

import java.util.Objects;

public class FavouriteMovie
{
    public static final String TAG = "FavouriteMovie";
    private String movie;
    private String userEmail;

    public FavouriteMovie()
    {
    }

    public FavouriteMovie(String movie, String userEmail)
    {
        this.movie = movie;
        this.userEmail = userEmail;
    }

    public String getMovie()
    {
        return movie;
    }

    public void setMovie(String movie)
    {
        this.movie = movie;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FavouriteMovie that = (FavouriteMovie) o;
        return Objects.equals(movie, that.movie) &&
                Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(movie, userEmail);
    }
}
