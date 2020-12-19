package com.cabezaperro.flickmap.data.model.favourite;

import java.util.Objects;

public class FavouriteShowtime
{
    private String showtime;
    private String userEmail;

    public FavouriteShowtime()
    {
    }

    public FavouriteShowtime(String showtime, String userEmail)
    {
        this.showtime = showtime;
        this.userEmail = userEmail;
    }

    public String getShowtime()
    {
        return showtime;
    }

    public void setShowtime(String showtime)
    {
        this.showtime = showtime;
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
        FavouriteShowtime that = (FavouriteShowtime) o;
        return Objects.equals(showtime, that.showtime) &&
                Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(showtime, userEmail);
    }
}
