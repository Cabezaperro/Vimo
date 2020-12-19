package com.cabezaperro.flickmap.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Session implements Parcelable
{
    private String movie;
    private String theatre;
    private String showtime;

    public Session()
    {
    }

    public Session(String movie, String theatre, String showtime)
    {
        this.movie = movie;
        this.theatre = theatre;
        this.showtime = showtime;
    }

    protected Session(Parcel in)
    {
        movie = in.readString();
        theatre = in.readString();
        showtime = in.readString();
    }

    public static final Creator<Session> CREATOR = new Creator<Session>()
    {
        @Override
        public Session createFromParcel(Parcel in)
        {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size)
        {
            return new Session[size];
        }
    };

    public String getMovie()
    {
        return movie;
    }

    public void setMovie(String movie)
    {
        this.movie = movie;
    }

    public String getTheatre()
    {
        return theatre;
    }

    public void setTheatre(String theatre)
    {
        this.theatre = theatre;
    }

    public String getShowtime()
    {
        return showtime;
    }

    public void setShowtime(String showtime)
    {
        this.showtime = showtime;
    }

    @Override
    public int describeContents()
    {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(movie);
        dest.writeString(theatre);
        dest.writeString(showtime);
    }
}
