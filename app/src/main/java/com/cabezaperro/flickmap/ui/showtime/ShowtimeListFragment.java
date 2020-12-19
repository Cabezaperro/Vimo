package com.cabezaperro.flickmap.ui.showtime;

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
import com.cabezaperro.flickmap.adapter.SessionShowtimeAdapter;
import com.cabezaperro.flickmap.adapter.ShowtimeAdapter;
import com.cabezaperro.flickmap.data.model.Cinema;
import com.cabezaperro.flickmap.data.model.Session;
import com.cabezaperro.flickmap.data.repository.SessionRepository;
import com.cabezaperro.flickmap.data.repository.ShowtimeRepository;
import com.cabezaperro.flickmap.ui.cinema.CinemaInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class ShowtimeListFragment extends Fragment
{
    public static final String TAG = "ShowtimeListFragment";
    private ShowtimeAdapter.OnSelectShowtimeListener listener;
    private RecyclerView rcvShowtimeList;
    private ShowtimeAdapter adapter;
    private boolean performSelect;

    public static Fragment createNewInstance(Bundle bundle)
    {
        ShowtimeListFragment ShowtimeListFragment = new ShowtimeListFragment();

        if (bundle != null)
            ShowtimeListFragment.setArguments(bundle);

        return ShowtimeListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
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
        View view = inflater.inflate(R.layout.fragment_showtime_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        rcvShowtimeList = view.findViewById(R.id.rcvShowtimeListFragmentShowtimeList);
        initialiseRcvShowtimeList();
    }

    private void initialiseRcvShowtimeList()
    {
        initialiseListener();
        adapter = new ShowtimeAdapter(SessionRepository.getInstance().getList(), ShowtimeRepository.getInstance().getList(), listener);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(rcvShowtimeList.getContext(), RecyclerView.VERTICAL, false);

        rcvShowtimeList.setLayoutManager(layoutManager);
        rcvShowtimeList.setAdapter(adapter);
    }

    private void initialiseListener()
    {
        listener = new ShowtimeAdapter.OnSelectShowtimeListener() {
            @Override
            public void onSelectShowtime(String showtime)
            {
                if (performSelect)
                {
                    performSelect = false;

                    Intent intent = new Intent(FlickmapApplication.currentContext, ShowtimeInfoActivity.class);
                    Bundle showtimeBundle = new Bundle();
                    Bundle sessionBundle = new Bundle();
                    ArrayList<Session> selectedShowtimeSessionList = new ArrayList<>();

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
        };
    }

    private void reload()
    {
        performSelect = true;
        adapter.clear();
        adapter.load(ShowtimeRepository.getInstance().getList());
        adapter.notifyDataSetChanged();
    }

    /*private void setTimePickerInterval()
    {
        try
        {
            @SuppressLint("PrivateApi") Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field field = classForid.getField("minute");

            minutePicker = tpcShowtime.findViewById(field.getInt(null));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(11);

            minutePickerValues = new ArrayList<>();

            for (int i = 0; i < 60; i++)
                if (i % TIME_PICKER_INTERVAL == 0)
                    minutePickerValues.add(String.format("%02d", i));

            minutePicker.setDisplayedValues(minutePickerValues.toArray(new String[0]));
            minutePicker.setWrapSelectorWheel(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
}
