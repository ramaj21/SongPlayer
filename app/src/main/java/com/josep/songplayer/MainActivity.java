package com.josep.songplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Song[] songs = {new Song("Vasja Prelijte Rakijom!", R.drawable.romanyjampic, R.raw.romanyjams), new Song("Slow Vibing", R.drawable.justvibingpic, R.raw.justvibing), new Song("It's Your Birthday!", R.drawable.bdaypic, R.raw.happybday)};
    private ImageView albumImage;
    private TextView songTitle;
    private ImageView pauseplay;
    private MediaPlayer newPlayer;
    private SeekBar seekBar;
    private Handler mSeekbarUpdateHandler = new Handler();
    private int songNum = 0;
    private AudioManager myManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumImage = findViewById(R.id.songImage);
        songTitle = findViewById(R.id.title);
        pauseplay = findViewById(R.id.pauseplay);
        seekBar = findViewById(R.id.seekBar);

//        myManager = new AudioManager();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && newPlayer!= null){
                    newPlayer.seekTo(progress);
                    if(progress == seekBar.getMax()) {
                        newPlayer.pause();
                        pauseplay.setImageResource(R.drawable.play);
                        newPlayer.seekTo(0);
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    public void skipTime(View v){
        if(newPlayer != null && newPlayer.getCurrentPosition()>0) {
            if (v.getId() == R.id.skipBack)
                newPlayer.seekTo(newPlayer.getCurrentPosition() - 10000);
            else if (v.getId() == R.id.skipforward)
                if(newPlayer.getCurrentPosition()+10000 >= newPlayer.getDuration()){
                    newPlayer.seekTo(0);
                    newPlayer.pause();
                    pauseplay.setImageResource(R.drawable.play);
                }
                else
                    newPlayer.seekTo(newPlayer.getCurrentPosition() + 10000);
            seekBar.setProgress(newPlayer.getCurrentPosition());
        }
    }
    public void skipSong(View v){
        if(v.getId() == R.id.backASong) {
            if (songNum == 0) {
                songNum=2;
            }
            else
                songNum--;
            setUpNewSong();
        }
        else if(v.getId() == R.id.skipASong){
            if(songNum == 2)
                songNum=0;
            else
                songNum++;
            setUpNewSong();
        }
    }
    public void pausePlay(View v) {
        if (newPlayer != null) {
            if (newPlayer.isPlaying()) {
                pauseplay.setImageResource(R.drawable.play);
                newPlayer.pause();
                mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
            } else {
                pauseplay.setImageResource(R.drawable.pause);
                startSong();
            }
        }
        else {
            setUpNewSong();
            pauseplay.setImageResource(R.drawable.pause);
            startSong();
        }
    }

    public void setUpNewSong(){
        releaseMediaPlayer();
        pauseplay.setImageResource(R.drawable.pause);
        newPlayer = MediaPlayer.create(this, songs[songNum].getAudio());
        albumImage.setImageResource(songs[songNum].getImage());
        songTitle.setText(songs[songNum].getTitle());
        seekBar.setMax(newPlayer.getDuration());
        startSong();
    }

    public void startSong(){
        newPlayer.start();
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        newPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayer();
            }
        });
    }

    private void releaseMediaPlayer(){
        if(newPlayer != null){
            newPlayer.release();
            newPlayer = null;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        releaseMediaPlayer();
    }
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            if(newPlayer!= null) {
                seekBar.setProgress(newPlayer.getCurrentPosition());
                mSeekbarUpdateHandler.postDelayed(this, 50);
            }
        }
    };

}
