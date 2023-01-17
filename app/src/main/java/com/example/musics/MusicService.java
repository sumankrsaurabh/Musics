package com.example.musics;

import static com.example.musics.PlayerActivity.listSongs;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    MyBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    ActionPlaying actionPlaying;
    Uri uri;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind","Method");
        return mBinder;
    }

    void pause(){
        mediaPlayer.pause();
    }
    void start(){
        mediaPlayer.start();
    }
    boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    void stop(){
        mediaPlayer.stop();
    }
    void release(){
        mediaPlayer.release();
    }
    int getDuration(){
        return mediaPlayer.getDuration();
    }
    void seekTo(int position){
        mediaPlayer.seekTo(position);
    }
    void createMediaPlayer(int pPosition){
        uri = Uri.parse(musicFiles.get(pPosition).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(),uri);
    }

    int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }


    public class MyBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition",-1);
        if (myPosition!=-1){
            playMedia(myPosition);
        }

        return START_STICKY;
    }

    private void playMedia(int startPosition) {
        musicFiles = listSongs;
        if (mediaPlayer!= null){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles!=null){
                createMediaPlayer(startPosition);
                mediaPlayer.start();
            }
        }
        else {
            if (musicFiles!=null){
                createMediaPlayer(startPosition);
                mediaPlayer.start();
            }
        }
    }

    void setMediaPlayer(){
        mediaPlayer = MediaPlayer.create(getBaseContext(),uri);
    }

 void onCompleted(){
    mediaPlayer.setOnCompletionListener(this);
 }
}


