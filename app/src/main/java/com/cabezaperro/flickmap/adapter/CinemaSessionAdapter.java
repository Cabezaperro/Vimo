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

public class CinemaSessionAdapter extends RecyclerView.Adapter<CinemaSessionAdapter.ViewHolder>
{
    List<Session> sessionList;
    List<String> movieList;
    private RecyclerView rcvShowtimeList;

    public CinemaSessionAdapter(List<Session> sessionList)
    {
        this.sessionList = sessionList;
        movieList = new ArrayList<>();

        for (Session session : sessionList)
            if (!movieList.contains(session.getMovie()))
                movieList.add(session.getMovie());
    }

    @NonNull
    @Override
    public CinemaSessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);

        return new CinemaSessionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaSessionAdapter.ViewHolder holder, int position)
    {
        holder.txvCinemaInfoMovieItemTitle.setText(movieList.get(position));

        holder.bind(sessionList.get(position));

        initialiseRcvShowtimeList(movieList.get(position));
    }

    private void initialiseRcvShowtimeList(String movie)
    {
        List<Session> movieSessionList = new ArrayList<>();

        for (Session session : sessionList)
            if (session.getMovie().equals(movie))
                movieSessionList.add(session);

        SessionShowtimeAdapter adapter = new SessionShowtimeAdapter(movieSessionList);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(rcvShowtimeList.getContext(), RecyclerView.VERTICAL, false);

        rcvShowtimeList.setLayoutManager(layoutManager);
        rcvShowtimeList.setAdapter(adapter);
    }

    @Override
    public int getItemCount()
    {
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txvCinemaInfoMovieItemTitle;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txvCinemaInfoMovieItemTitle = itemView.findViewById(R.id.txvMovieInfoCinemaItemName);
            rcvShowtimeList = itemView.findViewById(R.id.rcvShowtimeList);
        }

        public void bind(final Session session)
        {

        }
    }
}
