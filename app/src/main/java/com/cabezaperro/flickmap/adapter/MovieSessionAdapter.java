package com.cabezaperro.flickmap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.Session;

import java.util.ArrayList;
import java.util.List;

public class MovieSessionAdapter extends RecyclerView.Adapter<MovieSessionAdapter.ViewHolder>
{
    List<Session> sessionList;
    List<String> cinemaList;
    private RecyclerView rcvShowtimeList;

    public MovieSessionAdapter(List<Session> sessionList)
    {
        this.sessionList = sessionList;
        cinemaList = new ArrayList<>();

        for (Session session : sessionList)
            if (!cinemaList.contains(session.getTheatre()))
                cinemaList.add(session.getTheatre());
    }

    @NonNull
    @Override
    public MovieSessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);

        return new MovieSessionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieSessionAdapter.ViewHolder holder, int position)
    {
        holder.txvMovieInfoCinemaItemName.setText(cinemaList.get(position));

        holder.bind(sessionList.get(position));

        initialiseRcvShowtimeList(cinemaList.get(position));
    }

    private void initialiseRcvShowtimeList(String cinema)
    {
        List<Session> cinemaSessionList = new ArrayList<>();

        for (Session session : sessionList)
            if (session.getTheatre().equals(cinema))
                cinemaSessionList.add(session);

        SessionShowtimeAdapter adapter = new SessionShowtimeAdapter(cinemaSessionList);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(rcvShowtimeList.getContext(), RecyclerView.VERTICAL, false);

        rcvShowtimeList.setLayoutManager(layoutManager);
        rcvShowtimeList.setAdapter(adapter);
    }

    @Override
    public int getItemCount()
    {
        return cinemaList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txvMovieInfoCinemaItemName;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txvMovieInfoCinemaItemName = itemView.findViewById(R.id.txvMovieInfoCinemaItemName);
            rcvShowtimeList = itemView.findViewById(R.id.rcvShowtimeList);
        }

        public void bind(final Session session)
        {

        }
    }
}

