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
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.repository.MovieRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteSessionRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ShowtimeSessionAdapter extends RecyclerView.Adapter<ShowtimeSessionAdapter.ViewHolder>
{
    List<Session> sessionList;

    public ShowtimeSessionAdapter(List<Session> sessionList)
    {
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public ShowtimeSessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime_session, parent, false);

        return new ShowtimeSessionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeSessionAdapter.ViewHolder holder, int position)
    {
        Session session = sessionList.get(position);
        String posterUrl = null;

        for (Movie movie : MovieRepository.getInstance().getList())
            if (movie.getTitle().equals(session.getMovie()))
                posterUrl = movie.getPosterUrl();

        Glide.with(FlickmapApplication.currentContext)
                .load(posterUrl)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.imvPoster);

        if (session.getMovie().length() > 18)
            holder.txvMovie.setTextSize(18);
        else
            holder.txvMovie.setTextSize(22);

        holder.txvMovie.setText(session.getMovie());
        holder.txvCinema.setText(session.getTheatre());

        FavouriteSession favouriteSession = new FavouriteSession(session.getMovie(), session.getTheatre(), session.getShowtime(), FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if (FavouriteSessionRepository.getInstance().getList().contains(favouriteSession))
        {
            holder.imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
            holder.imvFavourite.setTag("fav");
        }
        else
        {
            holder.imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
            holder.imvFavourite.setTag("notfav");
        }

        holder.imvFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (holder.imvFavourite.getTag().equals("notfav"))
                {
                    FavouriteSessionRepository.getInstance().addFavouriteSession(favouriteSession);

                    holder.imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full_yellow));
                    holder.imvFavourite.setTag("fav");
                }
                else
                {
                    FavouriteSessionRepository.getInstance().deleteFavouriteSession(favouriteSession);

                    holder.imvFavourite.setImageDrawable(FlickmapApplication.currentContext.getDrawable(R.drawable.star_full));
                    holder.imvFavourite.setTag("notfav");
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return sessionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imvPoster;
        TextView txvMovie;
        TextView txvCinema;
        ImageView imvFavourite;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imvPoster = itemView.findViewById(R.id.imvShowtimeSessionItemPoster);
            txvMovie = itemView.findViewById(R.id.txvShowtimeSessionItemMovie);
            txvCinema = itemView.findViewById(R.id.txvShowtimeSessionItemCinema);
            imvFavourite = itemView.findViewById(R.id.imvShowtimeSessionItemFavourite);
        }
    }
}
