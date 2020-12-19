package com.cabezaperro.flickmap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteCinema;
import com.cabezaperro.flickmap.data.repository.CinemaRepository;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

public class FavouriteCinemaAdapter extends RecyclerView.Adapter<FavouriteCinemaAdapter.ViewHolder>
{
    public interface OnManageFavouriteCinemaListener
    {
        void onSelectFavouriteCinema(FavouriteCinema favouriteCinema);
        void onDeleteFavouriteCinema(FavouriteCinema favouriteCinema);
    }

    private List<FavouriteCinema> list;
    private OnManageFavouriteCinemaListener listener;

    public FavouriteCinemaAdapter()
    {
        list = new ArrayList<>();
    }

    public FavouriteCinemaAdapter(OnManageFavouriteCinemaListener listener)
    {
        list = new ArrayList<>();
        this.listener = listener;
    }

    public void clear()
    {
        list.clear();
    }

    public void load(List<FavouriteCinema> favouriteCinemaList)
    {
        list.addAll(favouriteCinemaList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cinema, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        FavouriteCinema favouriteCinema = list.get(position);
        Cinema cinema = null;

        for (Cinema c : CinemaRepository.getInstance().getList())
            if (c.getName().equals(favouriteCinema.getCinema()))
                cinema = c;
        
        holder.mliIcon.setLetter(cinema.getName().substring(5));

        if (cinema.getName().length() > 24)
            holder.txvName.setTextSize(18);
        else
            holder.txvName.setTextSize(22);

        holder.txvName.setText(cinema.getName());
        holder.txvAddress.setText(cinema.getAddress());

        holder.bind(favouriteCinema, listener);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        MaterialLetterIcon mliIcon;
        TextView txvName;
        TextView txvAddress;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mliIcon = itemView.findViewById(R.id.mliCinemaItemIcon);
            txvName = itemView.findViewById(R.id.txvCinemaItemName);
            txvAddress = itemView.findViewById(R.id.txvCinemaItemAddress);
        }

        void bind(final FavouriteCinema favouriteCinema, final OnManageFavouriteCinemaListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onSelectFavouriteCinema(favouriteCinema);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    listener.onDeleteFavouriteCinema(favouriteCinema);
                    return true;
                }
            });
        }
    }
}
