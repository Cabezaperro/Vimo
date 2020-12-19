package com.cabezaperro.flickmap;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.model.Movie;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteCinema;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteMovie;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteShowtime;
import com.cabezaperro.flickmap.data.repository.CinemaRepository;
import com.cabezaperro.flickmap.data.repository.MovieRepository;
import com.cabezaperro.flickmap.data.repository.SessionRepository;
import com.cabezaperro.flickmap.data.repository.ShowtimeRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteCinemaRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteMovieRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteSessionRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteShowtimeRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class FlickmapApplication extends Application
{
    public static String APP_LANGUAGE = "English (UK)";
    public static String UPDATE_OPTION = "Download and install automatically";
    public static Context currentContext;
    public static FirebaseDatabase firebaseDatabase;
    public static boolean repositoriesInitialised = false;

    @Override
    public void onCreate()
    {
        super.onCreate();

        firebaseDatabase = FirebaseDatabase.getInstance();
    }
    
    public static void initialiseRepositories()
    {
        FlickmapApplication.initialiseMovieRepository();
        FlickmapApplication.initialiseCinemaRepository();
        FlickmapApplication.initialiseSessionRepository();

        FlickmapApplication.initialiseFavouriteMovieRepository();
        FlickmapApplication.initialiseFavouriteCinemaRepository();
        FlickmapApplication.initialiseFavouriteSessionRepository();
    }

    public static void initialiseMovieRepository()
    {
        DatabaseReference movies = FlickmapApplication.firebaseDatabase.getReference("movies");

        MovieRepository.getInstance().getList().clear();

        movies.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    MovieRepository.getInstance().getList().add(movie);
                }

                Collator collator = Collator.getInstance(new Locale("es", "ES"));
                collator.setStrength(Collator.PRIMARY);

                MovieRepository.getInstance().getList().sort(Comparator.comparing(Movie::getTitle, collator));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public static void initialiseCinemaRepository()
    {
        DatabaseReference cinemas = FlickmapApplication.firebaseDatabase.getReference("theatres");

        CinemaRepository.getInstance().getList().clear();

        cinemas.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Cinema cinema = dataSnapshot.getValue(Cinema.class);
                    CinemaRepository.getInstance().getList().add(cinema);
                }

                Collator collator = Collator.getInstance(new Locale("es", "ES"));
                collator.setStrength(Collator.PRIMARY);

                CinemaRepository.getInstance().getList().sort(Comparator.comparing(Cinema::getName, collator));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public static void initialiseSessionRepository()
    {
        DatabaseReference sessions = FlickmapApplication.firebaseDatabase.getReference("sessions");

        SessionRepository.getInstance().getList().clear();

        sessions.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Session session = dataSnapshot.getValue(Session.class);
                    SessionRepository.getInstance().getList().add(session);
                }

                Collator collator = Collator.getInstance(new Locale("es", "ES"));
                collator.setStrength(Collator.PRIMARY);

                SessionRepository.getInstance().getList().sort(Comparator.comparing(Session::getMovie, collator));

                FlickmapApplication.initialiseShowtimeRepository();
                FlickmapApplication.initialiseFavouriteShowtimeRepository();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public static void initialiseShowtimeRepository()
    {
        ShowtimeRepository.getInstance().getList().clear();

        for (Session session : SessionRepository.getInstance().getList())
        {
            String showtime = session.getShowtime();

            if (showtime.length() > 5)
                showtime = showtime.substring(0, 5);

            if (!ShowtimeRepository.getInstance().getList().contains(showtime))
                ShowtimeRepository.getInstance().getList().add(showtime);
        }
    }

    public static void initialiseFavouriteMovieRepository()
    {
        DatabaseReference favouriteMovies = FlickmapApplication.firebaseDatabase.getReference("favouriteMovies");

        FavouriteMovieRepository.getInstance().getList().clear();

        favouriteMovies.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    FavouriteMovie favouriteMovie = dataSnapshot.getValue(FavouriteMovie.class);

                    if (favouriteMovie.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        FavouriteMovieRepository.getInstance().addFavouriteMovie(favouriteMovie);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public static void initialiseFavouriteCinemaRepository()
    {
        DatabaseReference favouriteCinemas = FlickmapApplication.firebaseDatabase.getReference("favouriteCinemas");

        FavouriteCinemaRepository.getInstance().getList().clear();

        favouriteCinemas.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    FavouriteCinema favouriteCinema = dataSnapshot.getValue(FavouriteCinema.class);

                    if (favouriteCinema.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        FavouriteCinemaRepository.getInstance().addFavouriteCinema(favouriteCinema);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public static void initialiseFavouriteSessionRepository()
    {
        DatabaseReference favouriteSessions = FlickmapApplication.firebaseDatabase.getReference("favouriteSessions");

        FavouriteSessionRepository.getInstance().getList().clear();

        favouriteSessions.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    FavouriteSession favouriteSession = dataSnapshot.getValue(FavouriteSession.class);

                    if (favouriteSession.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        FavouriteSessionRepository.getInstance().addFavouriteSession(favouriteSession);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }

    public static void initialiseFavouriteShowtimeRepository()
    {
        DatabaseReference favouriteShowtimes = FlickmapApplication.firebaseDatabase.getReference("favouriteShowtimes");

        FavouriteShowtimeRepository.getInstance().getList().clear();

        favouriteShowtimes.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    FavouriteShowtime favouriteShowtime = dataSnapshot.getValue(FavouriteShowtime.class);

                    if (favouriteShowtime.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        FavouriteShowtimeRepository.getInstance().addFavouriteShowtime(favouriteShowtime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                error.toException().printStackTrace();
            }
        });
    }
}
