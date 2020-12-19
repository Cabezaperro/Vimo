package com.cabezaperro.flickmap.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cinema implements Parcelable, Comparable<Cinema>
{
    private String name;
    private String address;

    public Cinema()
    {
    }

    public Cinema(String name, String address)
    {
        this.name = name;
        this.address = address;
    }

    protected Cinema(Parcel in)
    {
        name = in.readString();
        address = in.readString();
    }

    public static final Creator<Cinema> CREATOR = new Creator<Cinema>()
    {
        @Override
        public Cinema createFromParcel(Parcel in)
        {
            return new Cinema(in);
        }

        @Override
        public Cinema[] newArray(int size)
        {
            return new Cinema[size];
        }
    };

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Override
    public int describeContents()
    {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(address);
    }

    @Override
    public int compareTo(Cinema cinema)
    {
        return getName().compareToIgnoreCase(cinema.getName());
    }
}
