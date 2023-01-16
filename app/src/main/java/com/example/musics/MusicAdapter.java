package com.example.musics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.myViewHolder> {
    private final Context mContext;
    static ArrayList<MusicFiles> mFiles;

    public MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles) {
        this.mContext = mContext;
        this.mFiles = mFiles;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.myViewHolder holder, int position) {
        holder.songName.setText(mFiles.get(position).getTitle());
        byte[] image = getAlbumArt(mFiles.get(position).getPath());
        if (image != null)
        {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .centerCrop()
                    .into(holder.albumArt);
        }
        else {
            Glide.with(mContext)
                    .load(R.drawable.logo)
                    .into(holder.albumArt);

        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,PlayerActivity.class);
            intent.putExtra("position",position);
            mContext.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        ImageView albumArt;
        public myViewHolder (@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.song_name);
            albumArt = itemView.findViewById(R.id.musicImage);
        }
    }
    @SuppressWarnings("resource")
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

    @SuppressLint("NotifyDataSetChanged")
    void updateList(ArrayList<MusicFiles> musicFilesArrayList){
        mFiles = new ArrayList<>();
        mFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
    }
}
