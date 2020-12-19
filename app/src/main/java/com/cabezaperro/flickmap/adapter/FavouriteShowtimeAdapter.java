package com.cabezaperro.flickmap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteShowtime;

import java.util.ArrayList;
import java.util.List;

public class FavouriteShowtimeAdapter extends RecyclerView.Adapter<FavouriteShowtimeAdapter.ViewHolder>
{
    public interface OnManageFavouriteShowtimeListener
    {
        void onSelectFavouriteShowtime(FavouriteShowtime favouriteShowtime);
        void onDeleteFavouriteShowtime(FavouriteShowtime favouriteShowtime);
    }

    public List<Session> sessionList;
    public List<FavouriteShowtime> favouriteShowtimeList;
    OnManageFavouriteShowtimeListener listener;

    public FavouriteShowtimeAdapter(List<Session> sessionList, List<FavouriteShowtime> favouriteShowtimeList, OnManageFavouriteShowtimeListener listener)
    {
        this.sessionList = new ArrayList<>();
        this.favouriteShowtimeList = new ArrayList<>();
        this.listener = listener;

        this.sessionList.addAll(sessionList);
        this.favouriteShowtimeList.addAll(favouriteShowtimeList);
    }

    @NonNull
    @Override
    public FavouriteShowtimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime_generic, parent, false);

        return new FavouriteShowtimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteShowtimeAdapter.ViewHolder holder, int position)
    {
        FavouriteShowtime favouriteShowtime = favouriteShowtimeList.get(position);

        holder.txvShowtimeItemShowtime.setText(favouriteShowtime.getShowtime());

        holder.bind(favouriteShowtime, listener);
    }

    @Override
    public int getItemCount()
    {
        return favouriteShowtimeList.size();
    }

    public void clear()
    {
        favouriteShowtimeList.clear();
    }

    public void load(List<FavouriteShowtime> showtimeList)
    {
        this.favouriteShowtimeList.addAll(showtimeList);
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

            imvShowtimeItemFavourite.setVisibility(View.GONE);
        }

        public void bind(final FavouriteShowtime favouriteShowtime, OnManageFavouriteShowtimeListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    listener.onSelectFavouriteShowtime(favouriteShowtime);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)
                {
                    listener.onDeleteFavouriteShowtime(favouriteShowtime);
                    return true;
                }
            });
        }
    }
}
