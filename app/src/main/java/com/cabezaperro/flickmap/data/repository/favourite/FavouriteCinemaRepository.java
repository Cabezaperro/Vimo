package com.cabezaperro.flickmap.data.repository.favourite;

import androidx.annotation.NonNull;

import com.cabezaperro.flickmap.data.model.favourite.FavouriteCinema;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteMovie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class FavouriteCinemaRepository
{
    private static FavouriteCinemaRepository repository;
    private List<FavouriteCinema> list;
    private DatabaseReference favouriteCinemas;

    static
    {
        repository = new FavouriteCinemaRepository();
    }

    private FavouriteCinemaRepository()
    {
        list = new ArrayList<>();
        favouriteCinemas = FirebaseDatabase.getInstance().getReference("favouriteCinemas");
    }

    public static FavouriteCinemaRepository getInstance()
    {
        return repository;
    }

    public List<FavouriteCinema> getList()
    {
        Collator collator = Collator.getInstance(new Locale("es", "ES"));
        collator.setStrength(Collator.PRIMARY);

        list.sort(Comparator.comparing(FavouriteCinema::getCinema, collator));

        return list;
    }

    public void addFavouriteCinema(FavouriteCinema favouriteCinema)
    {
        if (!list.contains(favouriteCinema))
            list.add(favouriteCinema);

        favouriteCinemas.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String favouriteCinemaID = "e" + email.hashCode() + "c" + favouriteCinema.getCinema().hashCode();

                favouriteCinemas.child(favouriteCinemaID).setValue(favouriteCinema);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public void deleteFavouriteCinema(FavouriteCinema favouriteCinema)
    {
        if (list.contains(favouriteCinema))
            getList().remove(favouriteCinema);

        favouriteCinemas.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String favouriteCinemaID = "e" + email.hashCode() + "c" + favouriteCinema.getCinema().hashCode();

                favouriteCinemas.child(favouriteCinemaID).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }
}
