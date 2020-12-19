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
import com.cabezaperro.flickmap.data.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>
{
    public interface OnSelectMovieListener
    {
        void onSelectMovie(Movie movie);
    }

    private List<Movie> list;
    private OnSelectMovieListener listener;
    private Boolean viewAsGrid;

    public MovieAdapter(OnSelectMovieListener listener, Boolean viewAsGrid)
    {
        list = new ArrayList<>();
        list.addAll(MovieRepository.getInstance().getList());
        this.listener = listener;
        this.viewAsGrid = viewAsGrid;
    }

    public void clear()
    {
        list.clear();
    }

    public void load(List<Movie> movieList)
    {
        list.addAll(movieList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;

        if (viewAsGrid)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_linear, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Glide.with(FlickmapApplication.currentContext)
                .load(list.get(position).getPosterUrl())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.imvMovieItemPoster);

        if (list.get(position).getTitle().length() > 24)
            holder.txvMovieItemTitle.setTextSize(18);
        else
            holder.txvMovieItemTitle.setTextSize(22);

        holder.txvMovieItemTitle.setText(list.get(position).getTitle());
        holder.txvMovieItemGenre.setText(list.get(position).getGenre());
        holder.txvMovieItemRuntime.setText(String.format("%d %s", list.get(position).getRuntime(), FlickmapApplication.currentContext.getString(R.string.minutes)));
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imvMovieItemPoster;
        TextView txvMovieItemTitle;
        TextView txvMovieItemGenre;
        TextView txvMovieItemRuntime;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imvMovieItemPoster = itemView.findViewById(R.id.imvMovieItemPoster);
            txvMovieItemTitle = itemView.findViewById(R.id.txvMovieItemTitle);
            txvMovieItemGenre = itemView.findViewById(R.id.txvMovieItemGenre);
            txvMovieItemRuntime = itemView.findViewById(R.id.txvMovieItemRuntime);
        }

        public void bind(final Movie movie, final OnSelectMovieListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onSelectMovie(movie);
                }
            });
        }
    }
}
