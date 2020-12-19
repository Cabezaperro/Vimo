package com.cabezaperro.flickmap.ui.showtime;

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

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.adapter.CinemaSessionAdapter;
import com.cabezaperro.flickmap.adapter.ShowtimeSessionAdapter;
import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteCinema;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteShowtime;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteCinemaRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteShowtimeRepository;
import com.cabezaperro.flickmap.ui.cinema.CinemaInfoActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ShowtimeInfoActivity extends AppCompatActivity
{
    private Toolbar tlbToolbar;
    private TextView txvTimestamp;
    private ImageView imvFavourite;
    private RecyclerView rcvSessionList;
    
    private List<Session> sessionList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_info);

        String showtime = getIntent().getStringExtra("showtime");
        sessionList = getIntent().getParcelableArrayListExtra("sessionList");

        tlbToolbar = findViewById(R.id.tlbShowtimeInfoToolbar);

        setSupportActionBar(tlbToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        
        txvTimestamp = findViewById(R.id.txvShowtimeInfoShowtimeTimestamp);
        imvFavourite = findViewById(R.id.imvShowtimeInfoFavourite);

        if (showtime != null)
        {
            txvTimestamp.setText(showtime);
            FavouriteShowtime favouriteShowtime = new FavouriteShowtime(showtime, FirebaseAuth.getInstance().getCurrentUser().getEmail());

            if (FavouriteShowtimeRepository.getInstance().getList().contains(favouriteShowtime))
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
                        FavouriteShowtimeRepository.getInstance().addFavouriteShowtime(favouriteShowtime);

                        imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
                        imvFavourite.setTag("fav");
                    }
                    else
                    {
                        FavouriteShowtimeRepository.getInstance().deleteFavouriteShowtime(favouriteShowtime);

                        imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
                        imvFavourite.setTag("notfav");
                    }
                }
            });
        }

        rcvSessionList = findViewById(R.id.rcvShowtimeInfoSessionList);
        initialiseRcvSessionList();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = ShowtimeInfoActivity.this;
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
        ShowtimeSessionAdapter adapter = new ShowtimeSessionAdapter(sessionList);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(ShowtimeInfoActivity.this, RecyclerView.VERTICAL, false);

        rcvSessionList.setLayoutManager(layoutManager);
        rcvSessionList.setAdapter(adapter);
    }
}