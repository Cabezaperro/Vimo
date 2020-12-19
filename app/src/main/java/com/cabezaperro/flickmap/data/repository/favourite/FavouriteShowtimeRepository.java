package com.cabezaperro.flickmap.data.repository.favourite;

import androidx.annotation.NonNull;

import com.cabezaperro.flickmap.data.model.favourite.FavouriteMovie;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteShowtime;
import com.cabezaperro.flickmap.data.repository.ShowtimeRepository;
import com.cabezaperro.flickmap.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class FavouriteShowtimeRepository
{
    private static FavouriteShowtimeRepository repository;
    private ArrayList<FavouriteShowtime> list;
    private DatabaseReference favouriteShowtimes;

    static
    {
        repository = new FavouriteShowtimeRepository();
    }

    private FavouriteShowtimeRepository()
    {
        initialiseList();
    }

    private void initialiseList()
    {
        list = new ArrayList<>();
        favouriteShowtimes = FirebaseDatabase.getInstance().getReference("favouriteShowtimes");
    }

    public static FavouriteShowtimeRepository getInstance()
    {
        return repository;
    }

    public ArrayList<FavouriteShowtime> getList()
    {
        Collator collator = Collator.getInstance(new Locale("es", "ES"));
        collator.setStrength(Collator.PRIMARY);

        list.sort(Comparator.comparing(FavouriteShowtime::getShowtime, collator));

        return list;
    }

    public void addFavouriteShowtime(FavouriteShowtime favouriteShowtime)
    {
        if (!list.contains(favouriteShowtime))
            list.add(favouriteShowtime);

        favouriteShowtimes.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String favouriteShowtimeID = "e" + email.hashCode() + "s" + favouriteShowtime.getShowtime().hashCode();

                favouriteShowtimes.child(favouriteShowtimeID).setValue(favouriteShowtime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }
    
    public void deleteFavouriteShowtime(FavouriteShowtime favouriteShowtime)
    {
        if (list.contains(favouriteShowtime))
            getList().remove(favouriteShowtime);

        favouriteShowtimes.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String favouriteShowtimeID = "e" + email.hashCode() + "s" + favouriteShowtime.getShowtime().hashCode();

                favouriteShowtimes.child(favouriteShowtimeID).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }
}
