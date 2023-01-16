package com.example.musics;

import android.annotation.SuppressLint;
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
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {
    private final Context mContext;
    static ArrayList<MusicFiles> albumFiles;
    View view;

    public AlbumAdapter(Context mContext, ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public AlbumAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.album_items,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.MyHolder holder,int position) {
        holder.album_name.setText(albumFiles.get(position).getAlbum());
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());
        Bitmap bitmap;
        if (image != null)
        {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.album_image);
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            Palette.from(bitmap).generate(palette -> {
                // Get the "vibrant" color swatch based on the bitmap
                Palette.Swatch vibrant = Objects.requireNonNull(palette).getVibrantSwatch();
                if (vibrant != null) {
                    // Set the background color of a layout based on the vibrant color
                    holder.relativeLayout.setBackgroundColor(vibrant.getRgb());
                    // Update the title TextView with the proper text color
                    holder.album_name.setTextColor(vibrant.getTitleTextColor());
                }
            });
        }
        else {
            Glide.with(mContext)
                    .load(R.drawable.logo)
                    .into(holder.album_image);

        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,AlbumDetails.class);
            intent.putExtra("albumName",albumFiles.get(position).getAlbum());
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView album_image;
        TextView album_name;
        RelativeLayout relativeLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.album_image);
            album_name = itemView.findViewById(R.id.album_name);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    private byte[] getAlbumArt(String uri)
    {
        @SuppressWarnings("resource") MediaMetadataRetriever retriever = new MediaMetadataRetriever();
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
        albumFiles = new ArrayList<>();
        albumFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
    }
}
