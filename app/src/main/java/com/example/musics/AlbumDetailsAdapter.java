package com.example.musics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.AlbumDataHolder> {
    private final Context mContext;
    private final ArrayList<MusicFiles> albumFiles;
    View view;


    public AlbumDetailsAdapter(Context mContext, ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public AlbumDetailsAdapter.AlbumDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new AlbumDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdapter.AlbumDataHolder holder, int position) {
        holder.album_name.setText(albumFiles.get(position).getTitle());
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());
        if (image != null)
        {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.album_image);
        }
        else {
            Glide.with(mContext)
                    .load(R.drawable.logo)
                    .into(holder.album_image);

        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,PlayerActivity.class);
            intent.putExtra("albumName","albumDetails");
            intent.putExtra("position",position);
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public static class AlbumDataHolder extends RecyclerView.ViewHolder {
        ImageView album_image;
        TextView album_name;

        public AlbumDataHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.musicImage);
            album_name = itemView.findViewById(R.id.song_name);

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
}
