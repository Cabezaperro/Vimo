package com.cabezaperro.flickmap.data.repository;

import com.cabezaperro.flickmap.data.model.Session;

import java.util.ArrayList;

public class SessionRepository
{
    private static SessionRepository repository;
    private ArrayList<Session> list;

    static
    {
        repository = new SessionRepository();
    }

    private SessionRepository()
    {
        initialiseList();
    }

    private void initialiseList()
    {
        list = new ArrayList<>();
    }

    public static SessionRepository getInstance()
    {
        return repository;
    }

    public ArrayList<Session> getList()
    {
        return list;
    }
}
