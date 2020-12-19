package com.cabezaperro.flickmap.data.repository.favourite;

import androidx.annotation.NonNull;

import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.repository.SessionRepository;
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

public class FavouriteSessionRepository
{
    private static FavouriteSessionRepository repository;
    private ArrayList<FavouriteSession> list;
    private DatabaseReference favouriteSessions;

    static
    {
        repository = new FavouriteSessionRepository();
    }

    private FavouriteSessionRepository()
    {
        list = new ArrayList<>();
        favouriteSessions = FirebaseDatabase.getInstance().getReference("favouriteSessions");
    }

    public static FavouriteSessionRepository getInstance()
    {
        return repository;
    }

    public ArrayList<FavouriteSession> getList()
    {
        Collator collator = Collator.getInstance(new Locale("es", "ES"));
        collator.setStrength(Collator.PRIMARY);

        list.sort(Comparator.comparing(FavouriteSession::getMovie, collator));

        return list;
    }

    public void addFavouriteSession(FavouriteSession favouriteSession)
    {
        if (!list.contains(favouriteSession))
            list.add(favouriteSession);

        favouriteSessions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String favouriteSessionID = "e" + email.hashCode() + "m" + favouriteSession.getMovie().hashCode() + "c" + favouriteSession.getCinema().hashCode() + "s" + favouriteSession.getShowtime().hashCode();

                favouriteSessions.child(favouriteSessionID).setValue(favouriteSession);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public void deleteFavouriteSession(FavouriteSession favouriteSession)
    {
        if (list.contains(favouriteSession))
            getList().remove(favouriteSession);

        favouriteSessions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String favouriteSessionID = "e" + email.hashCode() + "m" + favouriteSession.getMovie().hashCode() + "c" + favouriteSession.getCinema().hashCode() + "s" + favouriteSession.getShowtime().hashCode();

                favouriteSessions.child(favouriteSessionID).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }
}
