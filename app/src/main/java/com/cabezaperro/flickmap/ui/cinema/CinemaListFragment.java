package com.cabezaperro.flickmap.ui.cinema;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.adapter.CinemaAdapter;
import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.repository.CinemaRepository;
import com.cabezaperro.flickmap.data.repository.SessionRepository;

import java.util.ArrayList;

public class CinemaListFragment extends Fragment
{
    public static final String TAG = "CinemaListFragment";

    private CinemaAdapter adapter;
    private CinemaAdapter.OnSelectCinemaListener listener;
    private RecyclerView rcvCinemaList;
    private boolean performSelect;

    public static Fragment createNewInstance(Bundle bundle)
    {
        CinemaListFragment cinemaListFragment = new CinemaListFragment();

        if (bundle != null)
            cinemaListFragment.setArguments(bundle);

        return cinemaListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        reload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cinema_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        rcvCinemaList = view.findViewById(R.id.rcvCinemaListFragmentCinemaList);
        initialiseRcvCinemaList();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.fragment_cinema_list, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initialiseRcvCinemaList()
    {
        initialiseListener();
        adapter = new CinemaAdapter(listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        rcvCinemaList.setLayoutManager(linearLayoutManager);
        rcvCinemaList.setAdapter(adapter);
    }

    private void initialiseListener()
    {
        listener = new CinemaAdapter.OnSelectCinemaListener()
        {
            @Override
            public void onSelectCinema(Cinema cinema)
            {
                if (performSelect)
                {
                    performSelect = false;

                    Intent intent = new Intent(FlickmapApplication.currentContext, CinemaInfoActivity.class);
                    Bundle cinemaBundle = new Bundle();
                    Bundle sessionBundle = new Bundle();
                    ArrayList<Session> selectedCinemaSessionList = new ArrayList<>();

                    for (Session session : SessionRepository.getInstance().getList())
                        if (session.getTheatre().equals(cinema.getName()))
                            selectedCinemaSessionList.add(session);

                    cinemaBundle.setClassLoader(Cinema.class.getClassLoader());
                    cinemaBundle.putParcelable("cinema", cinema);

                    sessionBundle.setClassLoader(Session.class.getClassLoader());
                    sessionBundle.putParcelableArrayList("sessionList", selectedCinemaSessionList);

                    intent.putExtras(cinemaBundle);
                    intent.putExtras(sessionBundle);

                    startActivity(intent);
                }
            }
        };
    }

    private void reload()
    {
        performSelect = true;
        adapter.clear();
        adapter.load(CinemaRepository.getInstance().getList());
        adapter.notifyDataSetChanged();
    }
}
