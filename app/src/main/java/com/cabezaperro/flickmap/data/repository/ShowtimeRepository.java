package com.cabezaperro.flickmap.data.repository;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class ShowtimeRepository
{
    private static ShowtimeRepository repository;
    private ArrayList<String> list;

    static
    {
        repository = new ShowtimeRepository();
    }

    private ShowtimeRepository()
    {
        initialiseList();
    }

    private void initialiseList()
    {
        list = new ArrayList<>();
    }

    public static ShowtimeRepository getInstance()
    {
        return repository;
    }

    public ArrayList<String> getList()
    {
        Collator collator = Collator.getInstance(new Locale("es", "ES"));
        collator.setStrength(Collator.PRIMARY);

        Collections.sort(list);

        return list;
    }
}
