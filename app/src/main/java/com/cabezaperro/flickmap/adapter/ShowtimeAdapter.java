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
import com.cabezaperro.flickmap.data.model.favourite.FavouriteShowtime;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteShowtimeRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ViewHolder>
{
    public interface OnSelectShowtimeListener
    {
        void onSelectShowtime(String showtime);
    }

    private List<Session> sessionList;
    private List<String> showtimeList;
    private OnSelectShowtimeListener listener;

    public ShowtimeAdapter(List<Session> sessionList, List<String> showtimeList, OnSelectShowtimeListener listener)
    {
        this.sessionList = new ArrayList<>();
        this.showtimeList = new ArrayList<>();
        this.listener = listener;

        this.sessionList.addAll(sessionList);
        this.showtimeList.addAll(showtimeList);
    }

    @NonNull
    @Override
    public ShowtimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime_generic, parent, false);

        return new ShowtimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeAdapter.ViewHolder holder, int position)
    {
        String showtime = showtimeList.get(position);
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FavouriteShowtime favouriteShowtime = new FavouriteShowtime(showtime, email);

        holder.txvShowtimeItemShowtime.setText(showtime);

        if (FavouriteShowtimeRepository.getInstance().getList().contains(favouriteShowtime))
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
                    FavouriteShowtimeRepository.getInstance().addFavouriteShowtime(favouriteShowtime);

                    holder.imvShowtimeItemFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
                    holder.imvShowtimeItemFavourite.setTag("fav");
                }
                else
                {
                    FavouriteShowtimeRepository.getInstance().deleteFavouriteShowtime(favouriteShowtime);

                    holder.imvShowtimeItemFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
                    holder.imvShowtimeItemFavourite.setTag("notfav");
                }
            }
        });

        holder.bind(showtimeList.get(position), listener);
    }

    @Override
    public int getItemCount()
    {
        return showtimeList.size();
    }

    public void clear()
    {
        showtimeList.clear();
    }

    public void load(List<String> showtimeList)
    {
        this.showtimeList.addAll(showtimeList);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txvShowtimeItemShowtime;
        ImageView imvShowtimeItemFavourite;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txvShowtimeItemShowtime = itemView.findViewById(R.id.txvShowtimeItemGenericShowtime);
            imvShowtimeItemFavourite = itemView.findViewById(R.id.imvShowtimeItemGenericFavourite);
        }

        public void bind(final String showtime, OnSelectShowtimeListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    listener.onSelectShowtime(showtime);
                }
            });
        }
    }
}
