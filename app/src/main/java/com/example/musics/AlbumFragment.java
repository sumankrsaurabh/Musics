package com.example.musics;

import static com.example.musics.MainActivity.albums;
import static com.example.musics.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    static AlbumAdapter albumAdapter;
    public AlbumFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerSongView);
        recyclerView.setHasFixedSize(true);
        if (albums.size()!=0)
        {
            albumAdapter = new AlbumAdapter(getContext(),albums);

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            recyclerView.setAdapter(albumAdapter);
        }
        return view;
    }


}