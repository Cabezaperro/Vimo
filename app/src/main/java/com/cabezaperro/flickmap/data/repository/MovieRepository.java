package com.cabezaperro.flickmap.data.repository;

import com.cabezaperro.flickmap.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository
{
    private static MovieRepository repository;
    private List<Movie> list;

    static
    {
        repository = new MovieRepository();
    }

    private MovieRepository()
    {
        list = new ArrayList<>();
    }

    public static MovieRepository getInstance()
    {
        return repository;
    }

    public List<Movie> getList()
    {
        return list;
    }
}
