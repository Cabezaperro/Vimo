package com.cabezaperro.flickmap.ui.favourite.movie;

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
import com.cabezaperro.flickmap.adapter.FavouriteMovieAdapter;
import com.cabezaperro.flickmap.data.model.Movie;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteMovie;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.repository.MovieRepository;
import com.cabezaperro.flickmap.data.repository.SessionRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteMovieRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteSessionRepository;
import com.cabezaperro.flickmap.ui.base.BaseDialogFragment;
import com.cabezaperro.flickmap.ui.movie.MovieInfoActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class FavouriteMovieListFragment extends Fragment implements BaseDialogFragment.OnDialogFinishedListener
{
    public static final String TAG = "FavouriteMovieListFragment";
    private static final int CODE_DELETE = 300;
    private static boolean VIEW_AS_GRID = false;
    private RecyclerView rcvFavouriteMovie;
    private FavouriteMovieAdapter adapter;
    private FavouriteMovieAdapter.OnManageFavouriteMovieListener listener;
    private FavouriteMovie deletedFavouriteMovie;
    private Menu optionsMenu;
    private boolean performSelect;

    public static Fragment createNewInstance(Bundle bundle)
    {
        FavouriteMovieListFragment fragment = new FavouriteMovieListFragment();

        if (bundle != null)
            fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Iterator<FavouriteMovie> iterator = FavouriteMovieRepository.getInstance().getList().iterator();

        while (iterator.hasNext())
        {
            FavouriteMovie favouriteMovie = iterator.next();
            boolean favouriteMovieIsOutdated = true;

            for (Movie movie : MovieRepository.getInstance().getList())
            {
                if (favouriteMovie.getMovie().equals(movie.getTitle()))
                {
                    favouriteMovieIsOutdated = false;
                    break;
                }
            }

            if (favouriteMovieIsOutdated)
                iterator.remove();
        }
        
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        reload();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favourite_movie_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        rcvFavouriteMovie = view.findViewById(R.id.rcvFavouriteMovieListFragmentFavouriteMovieList);
        initialiseRcvFavouriteMovie();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.fragment_movie_list, menu);

        menu.findItem(R.id.mniOrderByTitle).setVisible(false);
        menu.findItem(R.id.mniOrderByYear).setVisible(false);

        optionsMenu = menu;

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
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

        initialiseRcvFavouriteMovie();
    }

    private void initialiseRcvFavouriteMovie()
    {
        initialiseListener();
        adapter = new FavouriteMovieAdapter(listener, getContext(), VIEW_AS_GRID);

        RecyclerView.LayoutManager layoutManager;

        if (VIEW_AS_GRID)
            layoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
        else
            layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        rcvFavouriteMovie.setLayoutManager(layoutManager);
        rcvFavouriteMovie.setAdapter(adapter);
    }

    private void initialiseListener()
    {
        listener = new FavouriteMovieAdapter.OnManageFavouriteMovieListener()
        {
            @Override
            public void onSelectFavouriteMovie(FavouriteMovie favouriteMovie)
            {
                if (performSelect)
                {
                    performSelect = false;

                    Intent intent = new Intent(FlickmapApplication.currentContext, MovieInfoActivity.class);
                    Bundle movieBundle = new Bundle();
                    Bundle sessionBundle = new Bundle();
                    ArrayList<Session> selectedMovieSessionList = new ArrayList<>();
                    Movie movie = null;

                    for (Movie m : MovieRepository.getInstance().getList())
                        if (m.getTitle().equals(favouriteMovie.getMovie()))
                            movie = m;

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

            @Override
            public void onDeleteFavouriteMovie(FavouriteMovie favouriteMovie)
            {
                if (performSelect)
                {
                    performSelect = false;
                    showDeleteDialog(favouriteMovie);
                }
            }
        };
    }

    private void showDeleteDialog(FavouriteMovie favouriteMovie)
    {
        Bundle bundle = new Bundle();

        bundle.putString(BaseDialogFragment.TITLE, getString(R.string.remove_favourite_movie));
        bundle.putString(BaseDialogFragment.MESSAGE, getString(R.string.removeFavouriteMovie1) + "\"" + favouriteMovie.getMovie() + "\"" + getString(R.string.removeFavourite2));

        BaseDialogFragment baseDialogFragment = BaseDialogFragment.createNewInstance(bundle);

        baseDialogFragment.setTargetFragment(FavouriteMovieListFragment.this, CODE_DELETE);
        baseDialogFragment.show(getParentFragmentManager(), BaseDialogFragment.TAG);

        deletedFavouriteMovie = favouriteMovie;
    }

    @Override
    public void onDialogFinishedOk()
    {
        FavouriteMovieRepository.getInstance().deleteFavouriteMovie(deletedFavouriteMovie);
        deletedFavouriteMovie = null;
        reload();
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
        adapter.load(FavouriteMovieRepository.getInstance().getList());
        adapter.notifyDataSetChanged();
    }
}
