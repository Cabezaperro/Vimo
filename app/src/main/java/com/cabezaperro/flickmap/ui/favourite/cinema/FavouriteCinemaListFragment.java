package com.cabezaperro.flickmap.ui.favourite.cinema;

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
import com.cabezaperro.flickmap.adapter.FavouriteCinemaAdapter;
import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteCinema;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.repository.CinemaRepository;
import com.cabezaperro.flickmap.data.repository.SessionRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteCinemaRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteSessionRepository;
import com.cabezaperro.flickmap.ui.base.BaseDialogFragment;
import com.cabezaperro.flickmap.ui.cinema.CinemaInfoActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class FavouriteCinemaListFragment extends Fragment implements BaseDialogFragment.OnDialogFinishedListener
{
    public static final String TAG = "FavouriteCinemaListFragment";
    private static final int CODE_DELETE = 400;

    private RecyclerView rcvFavouriteCinema;
    private FavouriteCinemaAdapter adapter;
    private FavouriteCinemaAdapter.OnManageFavouriteCinemaListener listener;
    private FavouriteCinema deletedFavouriteCinema;
    private boolean performSelect;

    public static FavouriteCinemaListFragment createNewInstance(Bundle bundle)
    {
        FavouriteCinemaListFragment fragment = new FavouriteCinemaListFragment();

        if (bundle != null)
            fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Iterator<FavouriteCinema> iterator = FavouriteCinemaRepository.getInstance().getList().iterator();

        while (iterator.hasNext())
        {
            FavouriteCinema favouriteCinema = iterator.next();
            boolean favouriteCinemaIsOutdated = true;

            for (Cinema cinema : CinemaRepository.getInstance().getList())
            {
                if (favouriteCinema.getCinema().equals(cinema.getName()))
                {
                    favouriteCinemaIsOutdated = false;
                    break;
                }
            }

            if (favouriteCinemaIsOutdated)
                iterator.remove();
        }

        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_favourite_cinema_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        initialiseListener();

        rcvFavouriteCinema = view.findViewById(R.id.rcvFavouriteCinemaListFragmentFavouriteCinemaList);
        initialiseRcvFavouriteCinema();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        reload();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.fragment_cinema_list, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initialiseRcvFavouriteCinema()
    {
        adapter = new FavouriteCinemaAdapter(listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rcvFavouriteCinema.setLayoutManager(linearLayoutManager);
        rcvFavouriteCinema.setAdapter(adapter);
    }

    private void initialiseListener()
    {
        listener = new FavouriteCinemaAdapter.OnManageFavouriteCinemaListener()
        {
            @Override
            public void onSelectFavouriteCinema(FavouriteCinema favouriteCinema)
            {
                if (performSelect)
                {
                    performSelect = false;

                    Intent intent = new Intent(FlickmapApplication.currentContext, CinemaInfoActivity.class);
                    Bundle cinemaBundle = new Bundle();
                    Bundle sessionBundle = new Bundle();
                    ArrayList<Session> selectedCinemaSessionList = new ArrayList<>();
                    Cinema cinema = null;

                    for (Cinema c : CinemaRepository.getInstance().getList())
                        if (c.getName().equals(favouriteCinema.getCinema()))
                            cinema = c;

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

            @Override
            public void onDeleteFavouriteCinema(FavouriteCinema favouriteCinema)
            {
                if (performSelect)
                {
                    performSelect = false;
                    showDeleteDialog(favouriteCinema);
                }
            }
        };
    }

    private void showDeleteDialog(FavouriteCinema favouriteCinema)
    {
        Bundle bundle = new Bundle();

        bundle.putString(BaseDialogFragment.TITLE, getString(R.string.removeFavouriteCinema));
        bundle.putString(BaseDialogFragment.MESSAGE, getString(R.string.removeFavouriteCinema1) + "\"" + favouriteCinema.getCinema() + "\"" + getString(R.string.removeFavourite2));

        BaseDialogFragment baseDialogFragment = BaseDialogFragment.createNewInstance(bundle);

        baseDialogFragment.setTargetFragment(FavouriteCinemaListFragment.this, CODE_DELETE);
        baseDialogFragment.show(getParentFragmentManager(), BaseDialogFragment.TAG);

        deletedFavouriteCinema = favouriteCinema;
    }

    @Override
    public void onDialogFinishedOk()
    {
        FavouriteCinemaRepository.getInstance().deleteFavouriteCinema(deletedFavouriteCinema);
        reload();
        deletedFavouriteCinema = null;
    }

    @Override
    public void onDialogFinishedCancel()
    {
        performSelect = true;
    }

    private void reload()
    {
        performSelect = true;
        adapter.clear();
        adapter.load(FavouriteCinemaRepository.getInstance().getList());
        adapter.notifyDataSetChanged();
    }
}
