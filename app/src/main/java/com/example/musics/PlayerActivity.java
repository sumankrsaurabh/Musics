package com.example.musics;

import static com.example.musics.AlbumDetails.albumSongs;
import static com.example.musics.MusicAdapter.mFiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity {
    TextView song_name,artist_name,duration_played,duration_total,status_txt;
    ImageView cover_art,nextBtn,playBtn,prevBtn,shuffleBtn,repeatBtn;
    SeekBar seekBar;
    int position = -1;
    ConstraintLayout grediant;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;

    static Boolean shuffle=false,repeat=false;
    Thread updateSeek;
    Thread updateTime;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        getIntentMethod();
        metaData(uri);
        /*statusUpdater();*/


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null&&fromUser)
                {
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateSeek = new Thread(){
            @Override
            public void run() {
                int currentPosition;
                try {
                    while (true) {
                        currentPosition = mediaPlayer.getCurrentPosition()/1000;

                        seekBar.setProgress(currentPosition);
                        int mContext = currentPosition;
                        duration_played.setText(formattedTime(mContext));

                        //noinspection BusyWait
                        sleep(1000);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        updateTime= new Thread(){
            @Override
            public void run() {
                try {
                    while (true) {

                        int mContext = mediaPlayer.getCurrentPosition()/1000;
                        duration_played.setText(formattedTime(mContext));

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateTime.start();

        playBtn.setOnClickListener(v -> {
            if(mediaPlayer.isPlaying()){
                playBtn.setImageResource(R.drawable.baseline_play_arrow_24);
                mediaPlayer.pause();
            }
            else{
                playBtn.setImageResource(R.drawable.baseline_pause_24);
                mediaPlayer.start();
            }

        });

        nextBtn.setOnClickListener(v -> {
           if (mediaPlayer.isPlaying()){
               mediaPlayer.stop();
               mediaPlayer.release();
               if (shuffle && !repeat){
                   position = getRandom(listSongs.size()-1);
               } else if (!shuffle && !repeat) {
                   position = ((position+1)%listSongs.size());
               }


               uri = Uri.parse(listSongs.get(position).getPath());
               mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
               metaData(uri);
               song_name.setText(listSongs.get(position).getTitle());
               artist_name.setText(listSongs.get(position).getArtist());
               seekBar.setMax(mediaPlayer.getDuration()/1000);
               playBtn.setImageResource(R.drawable.baseline_pause_24);
           }
           else{
               if (shuffle && !repeat){
                   position = getRandom(listSongs.size()-1);
               } else if (!shuffle && !repeat) {
                   position = ((position + 1) % listSongs.size());
               }
               uri = Uri.parse(listSongs.get(position).getPath());
               mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
               metaData(uri);
               song_name.setText(listSongs.get(position).getTitle());
               artist_name.setText(listSongs.get(position).getArtist());
               seekBar.setMax(mediaPlayer.getDuration()/1000);
               playBtn.setImageResource(R.drawable.baseline_play_arrow_24);
            }
            mediaPlayer.start();

        });

        prevBtn.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
                if (shuffle && !repeat){
                    position = getRandom(listSongs.size()-1);
                } else if (!shuffle && !repeat) {
                    position = ((position-1)<0 ? (listSongs.size() - 1):(position-1));
                }

                uri = Uri.parse(listSongs.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                metaData(uri);
                song_name.setText(listSongs.get(position).getTitle());
                artist_name.setText(listSongs.get(position).getArtist());
                seekBar.setMax(mediaPlayer.getDuration()/1000);
                playBtn.setImageResource(R.drawable.baseline_pause_24);
            }
            else{
                mediaPlayer.stop();
                mediaPlayer.release();
                if (shuffle && !repeat){
                    position = getRandom(listSongs.size()-1);
                } else if (!shuffle && !repeat) {
                    position = ((position-1)<0 ? (listSongs.size() - 1):(position-1));
                }

                uri = Uri.parse(listSongs.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                metaData(uri);
                song_name.setText(listSongs.get(position).getTitle());
                artist_name.setText(listSongs.get(position).getArtist());
                seekBar.setMax(mediaPlayer.getDuration()/1000);
                playBtn.setImageResource(R.drawable.baseline_play_arrow_24);
            }
            mediaPlayer.start();
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            nextBtn.performClick();
            playBtn.setImageResource(R.drawable.baseline_pause_24);
        });

        shuffleBtn.setOnClickListener(v -> {
            if (shuffle){
                shuffle = false;
                shuffleBtn.setImageResource(R.drawable.baseline_shuffle_24);
            }
            else {
                shuffle = true;
                shuffleBtn.setImageResource(R.drawable.baseline_shuffle_on);
            }
        });

        repeatBtn.setOnClickListener(v -> {
            if (repeat)
            {
                repeat = false;
                repeatBtn.setImageResource(R.drawable.baseline_repeat_24);
            }
            else {
                repeat = true;
                repeatBtn.setImageResource(R.drawable.baseline_repeat_on);
            }
        });
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
    }


    /*private void statusUpdater() {
        if (mediaPlayer.isPlaying()){
            status_txt.setText(R.string.status);
        }
        else {
            status_txt.setText(R.string.app_name);
        }
    }*/

    private String formattedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
        String second = String.valueOf(mCurrentPosition %60);
        String minute = String.valueOf(mCurrentPosition /60);
        totalOut = minute+":"+second;
        totalNew = minute+":"+"0"+second;
        if (second.length()==1)
            return totalNew;
        else return totalOut;
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position",-1);
        String sender = getIntent().getStringExtra("albumName");

        if (sender != null && sender.equals("albumDetails")){
            listSongs = albumSongs;
        }
        else {
            listSongs = mFiles;
        }
        if (listSongs!=null) {
            playBtn.setImageResource(R.drawable.baseline_pause_24);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer!= null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();

        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        duration_total.setText(formattedTime(mediaPlayer.getDuration()/1000));
    }

    private void initView() {
        /*status_txt = findViewById(R.id.status_txt);*/
        song_name = findViewById(R.id.Song_name);
        artist_name = findViewById(R.id.artist_name);
        duration_played = findViewById(R.id.current_time);
        duration_total = findViewById(R.id.total_time);
        cover_art = findViewById(R.id.album_image);
        nextBtn = findViewById(R.id.next_btn);
        playBtn = findViewById(R.id.play_pause_btn);
        prevBtn = findViewById(R.id.previous_btn);
        shuffleBtn = findViewById(R.id.shuffle_btn);
        repeatBtn = findViewById(R.id.repeat_btn);
        seekBar = findViewById(R.id.seekBar);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    private void metaData (Uri uri) {
        //noinspection resource
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        String title = listSongs.get(position).getTitle();
        String artist = listSongs.get(position).getArtist();
        song_name.setText(title);
        artist_name.setText(artist);
        retriever.setDataSource(uri.toString());
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art != null) {

            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            ImageAnimation(this,cover_art,bitmap);
            Palette.from(bitmap).generate(palette -> {
                Palette.Swatch swatch = palette != null ? palette.getDominantSwatch() : null;
                grediant = findViewById(R.id.background);
                if (swatch != null) {
                    // Set the background color of a layout based on the vibrant color

                    grediant.setBackgroundColor(swatch.getRgb());
                    song_name.setTextColor(swatch.getTitleTextColor());
                    artist_name.setTextColor(swatch.getTitleTextColor());
                    duration_total.setTextColor(swatch.getBodyTextColor());
                    duration_played.setTextColor(swatch.getBodyTextColor());
                } else {
                    // Set the background color of a layout based on the vibrant color
                    grediant.setBackgroundColor(R.drawable.background_gradiant);
                    song_name.setTextColor(R.color.white);
                    artist_name.setTextColor(R.color.white);
                    duration_total.setTextColor(R.color.white);
                    duration_played.setTextColor(R.color.white);
                }
            });
            //noinspection deprecation
            cover_art.setBackgroundDrawable(getApplicationContext()
                    .getDrawable(R.drawable.background_gradiant));
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.logo)
                    .into(cover_art);
            //noinspection deprecation
            cover_art.setBackgroundDrawable(getApplicationContext()
                    .getDrawable(R.drawable.background_gradiant));

            ConstraintLayout grediant = findViewById(R.id.background);
            // Set the background color of a layout based on the vibrant color
            grediant.setBackgroundColor(R.drawable.background_gradiant);
            song_name.setTextColor(R.color.white);
            artist_name.setTextColor(R.color.white);
            duration_total.setTextColor(R.color.white);
            duration_played.setTextColor(R.color.white);
        }

    }
    public void ImageAnimation(final Context context, ImageView imageView, Bitmap bitmap){
    Animation AnimOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
    Animation AnimIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
    AnimOut.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Glide.with(context).load(bitmap).into(imageView);
            AnimIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {}

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            imageView.startAnimation(AnimIn);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    });
    imageView.startAnimation(AnimOut);
}
    }
