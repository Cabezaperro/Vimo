package com.cabezaperro.flickmap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.Movie;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class FavouriteSessionAdapter extends RecyclerView.Adapter<FavouriteSessionAdapter.ViewHolder>
{
    public interface OnLongClickFavouriteSessionListener
    {
        void onDeleteFavouriteSession(FavouriteSession favouriteSession);
    }

    private List<FavouriteSession> list;
    private OnLongClickFavouriteSessionListener listener;

    public FavouriteSessionAdapter(OnLongClickFavouriteSessionListener listener)
    {
        list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavouriteSessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite_session, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteSessionAdapter.ViewHolder holder, int position)
    {
        FavouriteSession favouriteSession = list.get(position);
        String moviePosterUrl = "";

        for (Movie m : MovieRepository.getInstance().getList())
            if (m.getTitle().equals(favouriteSession.getMovie()))
                moviePosterUrl = m.getPosterUrl();

        Glide.with(FlickmapApplication.currentContext)
                .load(moviePosterUrl)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.imvFavouriteSessionMoviePoster);

        if (favouriteSession.getMovie().length() > 24)
            holder.txvFavouriteSessionMovieTitle.setTextSize(18);
        else
            holder.txvFavouriteSessionMovieTitle.setTextSize(22);

        holder.txvFavouriteSessionMovieTitle.setText(favouriteSession.getMovie());
        holder.txvFavouriteSessionCinemaName.setText(favouriteSession.getCinema());
        holder.txvFavouriteSessionShowtime.setText(favouriteSession.getShowtime());

        holder.bind(favouriteSession, listener);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void clear()
    {
        list.clear();
    }

    public void load(List<FavouriteSession> favouriteSessionList)
    {
        list.addAll(favouriteSessionList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imvFavouriteSessionMoviePoster;
        private TextView txvFavouriteSessionMovieTitle;
        private TextView txvFavouriteSessionCinemaName;
        private TextView txvFavouriteSessionShowtime;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imvFavouriteSessionMoviePoster = itemView.findViewById(R.id.imvFavouriteSessionMoviePoster);
            txvFavouriteSessionMovieTitle = itemView.findViewById(R.id.txvFavouriteSessionMovieTitle);
            txvFavouriteSessionCinemaName = itemView.findViewById(R.id.txvFavouriteSessionCinemaName);
            txvFavouriteSessionShowtime = itemView.findViewById(R.id.txvFavouriteSessionShowtime);
        }

        public void bind(final FavouriteSession favouriteSession, final OnLongClickFavouriteSessionListener listener)
        {
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    listener.onDeleteFavouriteSession(favouriteSession);
                    return true;
                }
            });
        }
    }
}
