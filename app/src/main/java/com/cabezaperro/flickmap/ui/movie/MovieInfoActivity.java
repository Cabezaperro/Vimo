package com.cabezaperro.flickmap.ui.movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.adapter.MovieSessionAdapter;
import com.cabezaperro.flickmap.data.model.Movie;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteMovie;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteMovieRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MovieInfoActivity extends AppCompatActivity
{
    private List<Session> sessionList;

    private Toolbar tlbToolbar;
    ImageView imvMoviePoster;
    ImageView imvFavourite;
    TextView txvMovieTitle;
    TextView txvMovieReleaseDate;
    TextView txvMovieGenre;
    TextView txvMovieRuntime;
    TextView txvMovieRating;
    TextView txvMovieDirector;
    TextView txvMovieMainCast;
    TextView txvMovieSynopsis;
    private RecyclerView rcvSessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        Movie movie = getIntent().getParcelableExtra("movie");
        sessionList = getIntent().getParcelableArrayListExtra("sessionList");

        tlbToolbar = findViewById(R.id.tlbMovieInfoToolbar);

        setSupportActionBar(tlbToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        imvMoviePoster = findViewById(R.id.imvMovieInfoMoviePoster);
        imvFavourite = findViewById(R.id.imvMovieInfoFavourite);
        txvMovieTitle = findViewById(R.id.txvMovieInfoMovieTitle);
        txvMovieReleaseDate = findViewById(R.id.txvMovieInfoMovieReleaseDate);
        txvMovieGenre = findViewById(R.id.txvMovieInfoMovieGenre);
        txvMovieRuntime = findViewById(R.id.txvMovieInfoMovieRuntime);
        txvMovieRating = findViewById(R.id.txvMovieInfoMovieRating);
        txvMovieDirector = findViewById(R.id.txvMovieInfoMovieDirector);
        txvMovieMainCast = findViewById(R.id.txvMovieInfoMovieMainCast);
        txvMovieSynopsis = findViewById(R.id.txvMovieInfoMovieSynopsis);

        rcvSessionList = findViewById(R.id.rcvSessionList);

        if (movie != null)
        {
            Glide.with(FlickmapApplication.currentContext)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(imvMoviePoster);

            txvMovieTitle.setText(movie.getTitle());
            txvMovieReleaseDate.setText(movie.getReleaseDate());
            txvMovieGenre.setText(movie.getGenre());
            txvMovieRuntime.setText(String.format("%d %s", movie.getRuntime(), getString(R.string.minutes)));
            txvMovieRating.setText(movie.getRating());
            txvMovieDirector.setText(movie.getDirector());
            txvMovieMainCast.setText(movie.getMainCast());
            txvMovieSynopsis.setText(movie.getSynopsis());

            FavouriteMovie favouriteMovie = new FavouriteMovie(movie.getTitle(), FirebaseAuth.getInstance().getCurrentUser().getEmail());

            if (FavouriteMovieRepository.getInstance().getList().contains(favouriteMovie))
            {
                imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
                imvFavourite.setTag("fav");
            }
            else
            {
                imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
                imvFavourite.setTag("notfav");
            }

            imvFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (imvFavourite.getTag().equals("notfav"))
                    {
                        FavouriteMovieRepository.getInstance().addFavouriteMovie(favouriteMovie);

                        imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
                        imvFavourite.setTag("fav");
                    }
                    else
                    {
                        FavouriteMovieRepository.getInstance().deleteFavouriteMovie(favouriteMovie);

                        imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
                        imvFavourite.setTag("notfav");
                    }
                }
            });
        }

        initialiseRcvSessionList();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = MovieInfoActivity.this;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialiseRcvSessionList()
    {
        MovieSessionAdapter adapter = new MovieSessionAdapter(sessionList);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(MovieInfoActivity.this, RecyclerView.VERTICAL, false);

        rcvSessionList.setLayoutManager(layoutManager);
        rcvSessionList.setAdapter(adapter);
    }
}