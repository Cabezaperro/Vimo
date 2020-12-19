package com.cabezaperro.flickmap.data.model.favourite;

import java.util.Objects;

public class FavouriteSession
{
    private String movie;
    private String cinema;
    private String showtime;
    private String userEmail;

    public FavouriteSession()
    {
    }

    public FavouriteSession(String movie, String cinema, String showtime, String userEmail)
    {
        this.movie = movie;
        this.cinema = cinema;
        this.showtime = showtime;
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

    public String getCinema()
    {
        return cinema;
    }

    public void setCinema(String cinema)
    {
        this.cinema = cinema;
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FavouriteSession that = (FavouriteSession) o;
        return Objects.equals(movie, that.movie) &&
                Objects.equals(cinema, that.cinema) &&
                Objects.equals(showtime, that.showtime) &&
                Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(movie, cinema, showtime, userEmail);
    }
}
