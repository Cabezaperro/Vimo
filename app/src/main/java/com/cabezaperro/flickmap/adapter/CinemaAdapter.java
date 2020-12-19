package com.cabezaperro.flickmap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.repository.CinemaRepository;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.ViewHolder>
{
    public interface OnSelectCinemaListener
    {
        void onSelectCinema(Cinema cinema);
    }

    private List<Cinema> list;
    private OnSelectCinemaListener listener;

    public CinemaAdapter(OnSelectCinemaListener listener)
    {
        list = new ArrayList<>();
        list.addAll(CinemaRepository.getInstance().getList());
        this.listener = listener;
    }

    public void clear()
    {
        list.clear();
    }

    public void load(List<Cinema> cinemaList)
    {
        list.addAll(cinemaList);
    }

    @NonNull
    @Override
    public CinemaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cinema, parent, false);

        return new CinemaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaAdapter.ViewHolder holder, int position)
    {
        holder.mliCinemaItemIcon.setLetter(list.get(position).getName().substring(5));

        if (list.get(position).getName().length() > 24)
            holder.txvCinemaItemTitle.setTextSize(18);
        else
            holder.txvCinemaItemTitle.setTextSize(22);

        holder.txvCinemaItemTitle.setText(list.get(position).getName());
        holder.txvCinemaItemAddress.setText(list.get(position).getAddress());
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        MaterialLetterIcon mliCinemaItemIcon;
        TextView txvCinemaItemTitle;
        TextView txvCinemaItemAddress;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mliCinemaItemIcon = itemView.findViewById(R.id.mliCinemaItemIcon);
            txvCinemaItemTitle = itemView.findViewById(R.id.txvCinemaItemName);
            txvCinemaItemAddress = itemView.findViewById(R.id.txvCinemaItemAddress);
        }

        public void bind(final Cinema cinema, final CinemaAdapter.OnSelectCinemaListener listener)
        {
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onSelectCinema(cinema);
                }
            });
        }
    }
}
