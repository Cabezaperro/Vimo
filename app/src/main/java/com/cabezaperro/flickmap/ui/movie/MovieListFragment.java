package com.cabezaperro.flickmap.ui.movie;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.adapter.MovieAdapter;
import com.cabezaperro.flickmap.data.model.Movie;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.repository.MovieRepository;
import com.cabezaperro.flickmap.data.repository.SessionRepository;

import java.util.ArrayList;

public class MovieListFragment extends Fragment
{
    public static final String TAG = "MovieListFragment";
    private static Boolean VIEW_AS_GRID = false;

    private MovieAdapter adapter;
    private MovieAdapter.OnSelectMovieListener listener;
    private RecyclerView rcvMovieList;
    private Menu optionsMenu;
    private boolean performSelect;

    public static Fragment createNewInstance(Bundle bundle)
    {
        MovieListFragment movieListFragment = new MovieListFragment();

        if (bundle != null)
            movieListFragment.setArguments(bundle);

        return movieListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
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
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        rcvMovieList = view.findViewById(R.id.rcvFavouriteMovieListFragmentFavouriteMovieList);
        initialiseRcvMovieList();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.fragment_movie_list, menu);

        menu.findItem(R.id.mniOrderByTitle).setVisible(false);
        menu.findItem(R.id.mniOrderByYear).setVisible(false);

        optionsMenu = menu;

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mniChangeViewMode:
                changeViewMode();
                break;
        }

        reload();

        return super.onOptionsItemSelected(item);
    }

    private void changeViewMode()
    {
        VIEW_AS_GRID = !VIEW_AS_GRID;

        if (VIEW_AS_GRID)
        {
            optionsMenu.findItem(R.id.mniChangeViewMode).setIcon(R.drawable.ic_action_show_as_grid);
        }
        else
        {
            optionsMenu.findItem(R.id.mniChangeViewMode).setIcon(R.drawable.ic_action_show_as_list);
        }

        initialiseRcvMovieList();
    }

    private void initialiseRcvMovieList()
    {
        initialiseListener();
        adapter = new MovieAdapter(listener, VIEW_AS_GRID);

        RecyclerView.LayoutManager layoutManager;

        if (VIEW_AS_GRID)
            layoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
        else
            layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        rcvMovieList.setLayoutManager(layoutManager);
        rcvMovieList.setAdapter(adapter);
    }

    private void initialiseListener()
    {
        listener = new MovieAdapter.OnSelectMovieListener()
        {
            @Override
            public void onSelectMovie(Movie movie)
            {
                if (performSelect)
                {
                    performSelect = false;

                    Intent intent = new Intent(FlickmapApplication.currentContext, MovieInfoActivity.class);
                    Bundle movieBundle = new Bundle();
                    Bundle sessionBundle = new Bundle();
                    ArrayList<Session> selectedMovieSessionList = new ArrayList<>();

                    for (Session session : SessionRepository.getInstance().getList())
                        if (session.getMovie().equals(movie.getTitle()))
                            selectedMovieSessionList.add(session);

                    movieBundle.setClassLoader(Movie.class.getClassLoader());
                    movieBundle.putParcelable("movie", movie);

                    sessionBundle.setClassLoader(Session.class.getClassLoader());
                    sessionBundle.putParcelableArrayList("sessionList", selectedMovieSessionList);

                    intent.putExtras(movieBundle);
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
        adapter.load(MovieRepository.getInstance().getList());
        adapter.notifyDataSetChanged();
    }
}
