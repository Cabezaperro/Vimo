package com.cabezaperro.flickmap.data.model.favourite;

import java.util.Objects;

public class FavouriteCinema
{
    public static final String TAG = "FavouriteCinema";

    private String cinema;
    private String userEmail;

    public FavouriteCinema()
    {
    }

    public FavouriteCinema(String cinema, String userEmail)
    {
        this.cinema = cinema;
        this.userEmail = userEmail;
    }

    public String getCinema()
    {
        return cinema;
    }

    public void setCinema(String cinema)
    {
        this.cinema = cinema;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavouriteCinema that = (FavouriteCinema) o;
        return Objects.equals(cinema, that.cinema) &&
                Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(cinema, userEmail);
    }
}
