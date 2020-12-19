package com.cabezaperro.flickmap.adapter;

import android.content.Context;
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
import com.cabezaperro.flickmap.data.model.favourite.FavouriteMovie;
import com.cabezaperro.flickmap.data.repository.MovieRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteMovieRepository;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMovieAdapter extends RecyclerView.Adapter<FavouriteMovieAdapter.ViewHolder>
{
    private ArrayList<FavouriteMovie> list;
    private OnManageFavouriteMovieListener listener;
    private boolean viewAsGrid;

    public interface OnManageFavouriteMovieListener
    {
        void onSelectFavouriteMovie(FavouriteMovie favouriteMovie);
        void onDeleteFavouriteMovie(FavouriteMovie favouriteMovie);
    }

    public FavouriteMovieAdapter(OnManageFavouriteMovieListener listener, Context context, boolean viewAsGrid)
    {
        list = new ArrayList<>();

        this.listener = listener;
        this.viewAsGrid = viewAsGrid;
    }

    @NonNull
    @Override
    public FavouriteMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;

        if (viewAsGrid)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_linear, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMovieAdapter.ViewHolder holder, int position)
    {
        FavouriteMovie favouriteMovie = list.get(position);
        Movie movie = null;

        for (Movie m : MovieRepository.getInstance().getList())
            if (m.getTitle().equals(favouriteMovie.getMovie()))
                movie = m;

        if (movie == null)
        {
            FavouriteMovieRepository.getInstance().deleteFavouriteMovie(favouriteMovie);
            return;
        }

        Glide.with(FlickmapApplication.currentContext)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.imvPoster);

        if (movie.getTitle().length() > 24)
            holder.txvTitle.setTextSize(18);
        else
            holder.txvTitle.setTextSize(22);

        holder.txvTitle.setText(movie.getTitle());
        holder.txvGenre.setText(movie.getGenre());
        holder.txvRuntime.setText(String.format("%d %s", movie.getRuntime(), FlickmapApplication.currentContext.getString(R.string.minutes)));

        holder.bind(favouriteMovie, listener);
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

    public void load(List<FavouriteMovie> favouriteMovieList)
    {
        list.addAll(favouriteMovieList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imvPoster;
        TextView txvTitle;
        TextView txvGenre;
        TextView txvRuntime;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imvPoster = itemView.findViewById(R.id.imvMovieItemPoster);
            txvTitle = itemView.findViewById(R.id.txvMovieItemTitle);
            txvGenre = itemView.findViewById(R.id.txvMovieItemGenre);
            txvRuntime = itemView.findViewById(R.id.txvMovieItemRuntime);
        }

        public void bind(final FavouriteMovie favouriteMovie, final OnManageFavouriteMovieListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onSelectFavouriteMovie(favouriteMovie);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    listener.onDeleteFavouriteMovie(favouriteMovie);
                    return true;
                }
            });
        }
    }
}
