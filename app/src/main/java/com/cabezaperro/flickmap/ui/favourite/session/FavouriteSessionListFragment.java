package com.cabezaperro.flickmap.ui.favourite.session;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.adapter.FavouriteSessionAdapter;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.model.favourite.FavouriteSession;
import com.cabezaperro.flickmap.data.repository.SessionRepository;
import com.cabezaperro.flickmap.data.repository.favourite.FavouriteSessionRepository;
import com.cabezaperro.flickmap.ui.base.BaseDialogFragment;

import java.util.Iterator;

public class FavouriteSessionListFragment extends Fragment implements BaseDialogFragment.OnDialogFinishedListener
{
    public static final String TAG = "FavouriteSessionListFragment";
    private static final int CODE_DELETE = 400;
    private RecyclerView rcvFavouriteSessionListFragmentFavouriteSessionList;
    private FavouriteSessionAdapter adapter;
    private FavouriteSessionAdapter.OnLongClickFavouriteSessionListener listener;
    private FavouriteSession deletedFavouriteSession;
    private boolean performSelect;

    public static FavouriteSessionListFragment createNewInstance(Bundle bundle)
    {
        FavouriteSessionListFragment fragment = new FavouriteSessionListFragment();

        if (bundle != null)
            fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Iterator<FavouriteSession> iterator = FavouriteSessionRepository.getInstance().getList().iterator();

        while (iterator.hasNext())
        {
            FavouriteSession favouriteSession = iterator.next();
            boolean favouriteSessionIsOutdated = true;

            for (Session session : SessionRepository.getInstance().getList())
            {
                if (favouriteSession.getMovie().equals(session.getMovie()) && favouriteSession.getCinema().equals(session.getTheatre()) && favouriteSession.getShowtime().equals(session.getShowtime()))
                {
                    favouriteSessionIsOutdated = false;
                    break;
                }
            }

            if (favouriteSessionIsOutdated)
                iterator.remove();
        }

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
        return inflater.inflate(R.layout.fragment_favourite_session_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        rcvFavouriteSessionListFragmentFavouriteSessionList = view.findViewById(R.id.rcvFavouriteSessionListFragmentFavouriteSessionList);
        initialiseRcvFavouriteSessionListFragmentFavouriteSessionList();
    }

    private void initialiseRcvFavouriteSessionListFragmentFavouriteSessionList()
    {
        initialiseListener();
        adapter = new FavouriteSessionAdapter(listener);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        rcvFavouriteSessionListFragmentFavouriteSessionList.setLayoutManager(layoutManager);
        rcvFavouriteSessionListFragmentFavouriteSessionList.setAdapter(adapter);
    }

    private void initialiseListener()
    {
        listener = new FavouriteSessionAdapter.OnLongClickFavouriteSessionListener()
        {
            @Override
            public void onDeleteFavouriteSession(FavouriteSession favouriteSession)
            {
                if (performSelect)
                {
                    performSelect = false;
                    showDeleteDialog(favouriteSession);
                }
            }
        };
    }

    private void showDeleteDialog(FavouriteSession favouriteSession)
    {
        Bundle bundle = new Bundle();

        bundle.putString(BaseDialogFragment.TITLE, getString(R.string.remove_favourite_session));
        bundle.putString(BaseDialogFragment.MESSAGE, getString(R.string.removeFavouriteSession));

        BaseDialogFragment baseDialogFragment = BaseDialogFragment.createNewInstance(bundle);

        baseDialogFragment.setTargetFragment(FavouriteSessionListFragment.this, CODE_DELETE);
        baseDialogFragment.show(getParentFragmentManager(), BaseDialogFragment.TAG);

        deletedFavouriteSession = favouriteSession;
    }

    @Override
    public void onDialogFinishedOk()
    {
        FavouriteSessionRepository.getInstance().deleteFavouriteSession(deletedFavouriteSession);
        deletedFavouriteSession = null;
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
        adapter.load(FavouriteSessionRepository.getInstance().getList());
        adapter.notifyDataSetChanged();
    }
}
