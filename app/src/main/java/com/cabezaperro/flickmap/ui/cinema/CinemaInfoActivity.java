package com.cabezaperro.flickmap.ui.cinema;

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
import com.cabezaperro.flickmap.adapter.MovieSessionAdapter;
import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteCinema;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteCinemaRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class CinemaInfoActivity extends AppCompatActivity
{
    private List<Session> sessionList;

    private Toolbar tlbToolbar;
    private TextView txvCinemaName;
    private TextView txvCinemaAddress;
    private ImageView imvFavourite;
    private TextView txvNoSessionsAvailable;
    private RecyclerView rcvSessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_info);

        Cinema cinema = getIntent().getParcelableExtra("cinema");
        sessionList = getIntent().getParcelableArrayListExtra("sessionList");

        tlbToolbar = findViewById(R.id.tlbCinemaInfoToolbar);

        setSupportActionBar(tlbToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        txvCinemaName = findViewById(R.id.txvCinemaInfoCinemaName);
        txvCinemaAddress = findViewById(R.id.txvCinemaInfoCinemaAddress);
        imvFavourite = findViewById(R.id.imvCinemaInfoFavourite);
        txvNoSessionsAvailable = findViewById(R.id.txvCinemaInfoNoSessionsAvailable);

        if (sessionList.isEmpty())
            txvNoSessionsAvailable.setVisibility(View.VISIBLE);
        else
            txvNoSessionsAvailable.setVisibility(View.GONE);
        
        if (cinema != null)
        {
            txvCinemaName.setText(cinema.getName());
            txvCinemaAddress.setText(cinema.getAddress());

            FavouriteCinema favouriteCinema = new FavouriteCinema(cinema.getName(), FirebaseAuth.getInstance().getCurrentUser().getEmail());

            if (FavouriteCinemaRepository.getInstance().getList().contains(favouriteCinema))
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
                        FavouriteCinemaRepository.getInstance().addFavouriteCinema(favouriteCinema);

                        imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
                        imvFavourite.setTag("fav");
                    }
                    else
                    {
                        FavouriteCinemaRepository.getInstance().deleteFavouriteCinema(favouriteCinema);

                        imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
                        imvFavourite.setTag("notfav");
                    }
                }
            });
        }

        rcvSessionList = findViewById(R.id.rcvCinemaInfoSessionList);
        initialiseRcvSessionList();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FlickmapApplication.currentContext = CinemaInfoActivity.this;
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
        CinemaSessionAdapter adapter = new CinemaSessionAdapter(sessionList);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(CinemaInfoActivity.this, RecyclerView.VERTICAL, false);

        rcvSessionList.setLayoutManager(layoutManager);
        rcvSessionList.setAdapter(adapter);
    }
}