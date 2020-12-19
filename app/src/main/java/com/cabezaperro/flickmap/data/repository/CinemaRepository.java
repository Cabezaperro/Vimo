package com.cabezaperro.flickmap.data.repository;

import com.cabezaperro.flickmap.data.model.Cinema;

import java.util.ArrayList;
import java.util.List;

public class CinemaRepository
{
    private static CinemaRepository repository;
    private List<Cinema> list;

    static
    {
        repository = new CinemaRepository();
    }

    private CinemaRepository()
    {
        list = new ArrayList<>();
    }

    public static CinemaRepository getInstance()
    {
        return repository;
    }

    public List<Cinema> getList()
    {
        return list;
    }
}
