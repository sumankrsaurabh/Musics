package com.example.musics;

import static com.example.musics.MainActivity.musicFiles;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumDetails extends AppCompatActivity {
   RecyclerView recyclerView;
    ImageView albumPhoto;
    String albumName;
    AlbumDetailsAdapter adapter;
    static ArrayList<MusicFiles> albumSongs = new ArrayList<>();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView = findViewById(R.id.recyclerView);
        albumPhoto = findViewById(R.id.albumPhoto);


        albumName = getIntent().getStringExtra("albumName");
        int j = 0;
        albumSongs.clear();
        for (int i = 0;i<musicFiles.size();i++){
            if (albumName.equals(musicFiles.get(i).getAlbum())){

                albumSongs.add(j,musicFiles.get(i));
            }
        }
        byte[] image = getAlbumArt(albumSongs.get(0).getPath());
        if (image!=null){
            Glide.with(this)
                    .load(image)
                    .centerCrop()
                    .into(albumPhoto);
        }
        else {
            Glide.with(this)
                    .load(R.drawable.logo)
                    .into(albumPhoto);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size()<1)){

            adapter = new AlbumDetailsAdapter(this,albumSongs);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,
                    RecyclerView.VERTICAL,false));



        }
    }

    private byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return art;
    }
}