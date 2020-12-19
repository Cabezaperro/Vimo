package com.cabezaperro.flickmap.data.model;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cabezaperro.flickmap.R;

import java.util.Objects;

public class Movie implements Parcelable
{
    private String title;
    private String releaseDate;
    private String genre;
    private String rating;
    private int runtime;
    private String synopsis;
    private String director;
    private String mainCast;
    private String posterUri;
    private String posterUrl;

    public Movie()
    {
    }

    public Movie(String title, String releaseDate, String genre, int runtime, String synopsis, String director)
    {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.rating = Resources.getSystem().getString(R.string.unavailable);
        this.runtime = runtime;
        this.synopsis = synopsis;
        this.director = director;
        this.mainCast = Resources.getSystem().getString(R.string.unavailable);
        this.posterUri = Resources.getSystem().getString(R.string.no_image);
    }

    public Movie(String title, String releaseDate, String genre, String rating, int runtime, String synopsis, String director, String mainCast, String posterUri, String posterUrl)
    {
        this.title = title;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.rating = rating;
        this.runtime = runtime;
        this.synopsis = synopsis;
        this.director = director;
        this.mainCast = mainCast;
        this.posterUri = posterUri;
        this.posterUrl = posterUrl;

        if (this.rating == null)
            this.rating = Resources.getSystem().getString(R.string.unavailable);

        if (this.mainCast == null)
            this.mainCast = Resources.getSystem().getString(R.string.unavailable);

        if (this.posterUri == null)
            this.posterUri = Resources.getSystem().getString(R.string.no_image);
    }

    protected Movie(Parcel in)
    {
        title = in.readString();
        releaseDate = in.readString();
        genre = in.readString();
        rating = in.readString();
        runtime = in.readInt();
        synopsis = in.readString();
        director = in.readString();
        mainCast = in.readString();
        posterUri = in.readString();
        posterUrl = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public String getRating()
    {
        return rating;
    }

    public void setRating(String rating)
    {
        this.rating = rating;
    }

    public int getRuntime()
    {
        return runtime;
    }

    public void setRuntime(int runtime)
    {
        this.runtime = runtime;
    }

    public String getSynopsis()
    {
        return synopsis;
    }

    public void setSynopsis(String synopsis)
    {
        this.synopsis = synopsis;
    }

    public String getDirector()
    {
        return director;
    }

    public void setDirector(String director)
    {
        this.director = director;
    }

    public String getMainCast()
    {
        return mainCast;
    }

    public void setMainCast(String mainCast)
    {
        this.mainCast = mainCast;
    }

    public String getPosterUri()
    {
        return posterUri;
    }

    public void setPosterUri(String posterUri)
    {
        this.posterUri = posterUri;
    }

    public String getPosterUrl()
    {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl)
    {
        this.posterUrl = posterUrl;
    }

    @NonNull
    @Override
    public String toString()
    {
        return getTitle() + " (" + getReleaseDate() + ")";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(title, releaseDate);
    }

    @Override
    public int describeContents()
    {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(genre);
        dest.writeString(rating);
        dest.writeInt(runtime);
        dest.writeString(synopsis);
        dest.writeString(director);
        dest.writeString(mainCast);
        dest.writeString(posterUri);
        dest.writeString(posterUrl);
    }
}
