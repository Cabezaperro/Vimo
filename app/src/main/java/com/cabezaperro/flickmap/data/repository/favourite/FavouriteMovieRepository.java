package com.cabezaperro.flickmap.data.repository.favourite;

import androidx.annotation.NonNull;

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

public class FavouriteMovieRepository
{
    private static FavouriteMovieRepository repository;
    private List<FavouriteMovie> list;
    private DatabaseReference favouriteMovies;

    static
    {
        repository = new FavouriteMovieRepository();
    }

    private FavouriteMovieRepository()
    {
        list = new ArrayList<>();
        favouriteMovies = FirebaseDatabase.getInstance().getReference("favouriteMovies");
    }

    public static FavouriteMovieRepository getInstance()
    {
        return repository;
    }

    public List<FavouriteMovie> getList()
    {
        Collator collator = Collator.getInstance(new Locale("es", "ES"));
        collator.setStrength(Collator.PRIMARY);

        list.sort(Comparator.comparing(FavouriteMovie::getMovie, collator));

        return list;
    }

    public void addFavouriteMovie(FavouriteMovie favouriteMovie)
    {
        if (!list.contains(favouriteMovie))
            list.add(favouriteMovie);

        favouriteMovies.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String favouriteMovieID = "e" + email.hashCode() + "m" + favouriteMovie.getMovie().hashCode();

                favouriteMovies.child(favouriteMovieID).setValue(favouriteMovie);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public void deleteFavouriteMovie(FavouriteMovie favouriteMovie)
    {
        if (list.contains(favouriteMovie))
            getList().remove(favouriteMovie);

        favouriteMovies.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String favouriteMovieID = "e" + email.hashCode() + "m" + favouriteMovie.getMovie().hashCode();

                favouriteMovies.child(favouriteMovieID).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }
}
