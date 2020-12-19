package com.cabezaperro.flickmap.ui.favourite.showtime;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.adapter.FavouriteShowtimeAdapter;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteShowtime;
import com.cabezaperro.flickmap.data.repository.SessionRepository;
import com.cabezaperro.flickmap.data.repository.ShowtimeRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteSessionRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteShowtimeRepository;
import com.cabezaperro.flickmap.ui.base.BaseDialogFragment;
import com.cabezaperro.flickmap.ui.showtime.ShowtimeInfoActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class FavouriteShowtimeListFragment extends Fragment implements BaseDialogFragment.OnDialogFinishedListener
{
    private static final int CODE_DELETE = 500;
    private FavouriteShowtimeAdapter adapter;
    private FavouriteShowtimeAdapter.OnManageFavouriteShowtimeListener listener;
    private RecyclerView rcvShowtimeList;
    private FavouriteShowtime deletedFavouriteShowtime;
    private boolean performSelect;

    public static FavouriteShowtimeListFragment createNewInstance(Bundle bundle)
    {
        FavouriteShowtimeListFragment fragment = new FavouriteShowtimeListFragment();

        if (bundle != null)
            fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Iterator<FavouriteShowtime> iterator = FavouriteShowtimeRepository.getInstance().getList().iterator();

        while (iterator.hasNext())
        {
            FavouriteShowtime favouriteShowtime = iterator.next();
            boolean favouriteShowtimeIsOutdated = true;

            for (String showtime : ShowtimeRepository.getInstance().getList())
            {
                if (favouriteShowtime.getShowtime().equals(showtime))
                {
                    favouriteShowtimeIsOutdated = false;
                    break;
                }
            }

            if (favouriteShowtimeIsOutdated)
                iterator.remove();
        }

        reload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_favourite_showtime_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        rcvShowtimeList = view.findViewById(R.id.rcvFavouriteShowtimeListFragmentFavouriteShowtimeList);
        initialiseRcvShowtimeList();
    }

    private void initialiseRcvShowtimeList()
    {
        initialiseListener();
        adapter = new FavouriteShowtimeAdapter(SessionRepository.getInstance().getList(), FavouriteShowtimeRepository.getInstance().getList(), listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rcvShowtimeList.setLayoutManager(linearLayoutManager);
        rcvShowtimeList.setAdapter(adapter);
    }

    private void initialiseListener()
    {
        listener = new FavouriteShowtimeAdapter.OnManageFavouriteShowtimeListener() {
            @Override
            public void onSelectFavouriteShowtime(FavouriteShowtime favouriteShowtime)
            {
                if (performSelect)
                {
                    performSelect = false;

                    Intent intent = new Intent(FlickmapApplication.currentContext, ShowtimeInfoActivity.class);
                    Bundle showtimeBundle = new Bundle();
                    Bundle sessionBundle = new Bundle();
                    ArrayList<Session> selectedShowtimeSessionList = new ArrayList<>();
                    String showtime = null;

                    for (String s : ShowtimeRepository.getInstance().getList())
                        if (s.equals(favouriteShowtime.getShowtime()))
                            showtime = s;

                    for (Session session : SessionRepository.getInstance().getList())
                    {
                        String timeStamp = session.getShowtime();

                        if (timeStamp.length() > 5)
                            timeStamp = timeStamp.substring(0, 5);

                        if (timeStamp.equals(showtime))
                            selectedShowtimeSessionList.add(session);
                    }

                    showtimeBundle.putString("showtime", showtime);

                    sessionBundle.setClassLoader(Session.class.getClassLoader());
                    sessionBundle.putParcelableArrayList("sessionList", selectedShowtimeSessionList);

                    intent.putExtras(showtimeBundle);
                    intent.putExtras(sessionBundle);

                    startActivity(intent);
                }
            }

            @Override
            public void onDeleteFavouriteShowtime(FavouriteShowtime favouriteShowtime)
            {
                if (performSelect)
                {
                    performSelect = false;
                    showDeleteDialog(favouriteShowtime);
                }
            }
        };
    }

    private void showDeleteDialog(FavouriteShowtime favouriteShowtime)
    {
        Bundle bundle = new Bundle();

        bundle.putString(BaseDialogFragment.TITLE, getString(R.string.remove_favourite_showtime));
        bundle.putString(BaseDialogFragment.MESSAGE, getString(R.string.removeFavouriteShowtime1) + "\"" + favouriteShowtime.getShowtime() + "\"" + getString(R.string.removeFavourite2));

        BaseDialogFragment baseDialogFragment = BaseDialogFragment.createNewInstance(bundle);

        baseDialogFragment.setTargetFragment(FavouriteShowtimeListFragment.this, CODE_DELETE);
        baseDialogFragment.show(getParentFragmentManager(), BaseDialogFragment.TAG);

        deletedFavouriteShowtime = favouriteShowtime;
    }

    @Override
    public void onDialogFinishedOk()
    {
        FavouriteShowtimeRepository.getInstance().deleteFavouriteShowtime(deletedFavouriteShowtime);
        deletedFavouriteShowtime = null;
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
        adapter.load(FavouriteShowtimeRepository.getInstance().getList());
        adapter.notifyDataSetChanged();
    }
}
