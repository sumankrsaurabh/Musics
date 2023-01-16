package com.example.musics;

import static com.example.musics.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SongFragment extends Fragment {

    RecyclerView recyclerView;
    static MusicAdapter musicAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_song, container, false);
        recyclerView = view.findViewById(R.id.recyclerSongView);
        recyclerView.setHasFixedSize(true);
        if (musicFiles.size()!=0)
        {
            musicAdapter = new MusicAdapter(getContext(),musicFiles);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(musicAdapter);
        }
        return view;
    }
}