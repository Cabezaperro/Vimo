package com.cabezaperro.flickmap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteSessionRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SessionShowtimeAdapter extends RecyclerView.Adapter<SessionShowtimeAdapter.ViewHolder>
{
    public List<Session> sessionList;
    public List<String> showtimeList;
    DatabaseReference favouriteSessions;

    public SessionShowtimeAdapter(List<Session> sessionList)
    {
        this.sessionList = sessionList;
        showtimeList = new ArrayList<>();
        favouriteSessions = FirebaseDatabase.getInstance().getReference("favouriteSessions");

        for (Session session : sessionList)
            if (!showtimeList.contains(session.getShowtime()))
                showtimeList.add(session.getShowtime());
    }

    @NonNull
    @Override
    public SessionShowtimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);

        return new SessionShowtimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionShowtimeAdapter.ViewHolder holder, int position)
    {
        Session session = sessionList.get(position);
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FavouriteSession favouriteSession = new FavouriteSession(session.getMovie(), session.getTheatre(), session.getShowtime(), email);

        holder.txvSessionItemShowtime.setText(showtimeList.get(position));

        if (FavouriteSessionRepository.getInstance().getList().contains(favouriteSession))
        {
            holder.imvShowtimeItemFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
            holder.imvShowtimeItemFavourite.setTag("fav");
        }
        else
        {
            holder.imvShowtimeItemFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
            holder.imvShowtimeItemFavourite.setTag("notfav");
        }

        holder.imvShowtimeItemFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (holder.imvShowtimeItemFavourite.getTag().equals("notfav"))
                {
                    FavouriteSessionRepository.getInstance().addFavouriteSession(favouriteSession);

                    holder.imvShowtimeItemFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
                    holder.imvShowtimeItemFavourite.setTag("fav");
                }
                else
                {
                    FavouriteSessionRepository.getInstance().deleteFavouriteSession(favouriteSession);

                    holder.imvShowtimeItemFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
                    holder.imvShowtimeItemFavourite.setTag("notfav");
                }
            }
        });

        holder.bind(showtimeList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return showtimeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txvSessionItemShowtime;
        ImageView imvShowtimeItemFavourite;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txvSessionItemShowtime = itemView.findViewById(R.id.txvShowtimeItemShowtime);
            imvShowtimeItemFavourite = itemView.findViewById(R.id.imvShowtimeItemFavourite);
        }

        public void bind(final String showtime)
        {
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    return false;
                }
            });
        }
    }
}

