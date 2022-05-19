package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import data_classes.Music;


public class MusicActivity extends AppCompatActivity {

    public static MediaPlayer mediaPlayer;
    private Button play_button;
    private Button stop_button;
    private Button next_music_button;
    private Button prev_music_button;
    private Button back_page_button;
    private boolean isPlaying = false;
    private Drawable pause_image;
    private Drawable play_image;
    private ImageView album_image;



    SeekBar seek_bar;
    private TextView song_title;
    public static int position = -2;
    private TextView current_time;
    private TextView total_time;
    Intent playerData;
    Bundle bundle;
    ArrayList<File> all_song_files;
    ArrayList<Music> all_music_objects;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);


        play_button = (Button) findViewById(R.id.btnPlay);
        stop_button = (Button) findViewById(R.id.btnStop);
        next_music_button = (Button) findViewById(R.id.btnNextMusic);
        prev_music_button = (Button) findViewById(R.id.btnPrevMusic);
        back_page_button = (Button) findViewById(R.id.btnBackHomePage);

        song_title = (TextView) findViewById(R.id.tvSongDetail);
        seek_bar = (SeekBar) findViewById(R.id.sbMusicSeekBar);
        current_time = (TextView) findViewById(R.id.tvCurrentTimeMusic);
        total_time = (TextView) findViewById(R.id.tvTotalTimeMusic);
        album_image = (ImageView) findViewById(R.id.iwMusicImage);

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        all_song_files = findSongs(folder);
        all_music_objects = get_music_objects(all_song_files);

        if (position == -2) {
            position = 0;
        }
//        position = 0;
        initPlayer(position);

        play_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                if(!isPlaying) {
                    pause_image = getResources().getDrawable(android.R.drawable.ic_media_pause);
                    play_button.setBackground(pause_image);
                    isPlaying = true;
                    music_play();
                }
                else {
                    play_image = getResources().getDrawable(android.R.drawable.ic_media_play);
                    play_button.setBackground(play_image);
                    isPlaying = false;
                    music_pause();
                }
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                play_image = getResources().getDrawable(android.R.drawable.ic_media_play);
                play_button.setBackground(play_image);
                isPlaying = false;
                music_stop();
            }
        });



        next_music_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                if (position < all_song_files.size() - 1) {
                    position++;
                } else {
                    position = 0;

                }

                pause_image = getResources().getDrawable(android.R.drawable.ic_media_pause);
                play_button.setBackground(pause_image);
                isPlaying = true;

                initPlayer(position);

            }
        });


        prev_music_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                if (position <= 0) {
                    position = all_song_files.size() - 1;
                } else {
                    position--;
                }

                pause_image = getResources().getDrawable(android.R.drawable.ic_media_pause);
                play_button.setBackground(pause_image);
                isPlaying = true;

                initPlayer(position);

            }
        });

        back_page_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });


    }


    public void openHomePage() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    private void initPlayer(final int position) {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }

//        String sname = all_song_files.get(position).getName().replace(".mp3", "").replace(".m4a", "").replace(".wav", "").replace(".m4b", "");
        // set song name with artist name
        String song_name = all_music_objects.get(position).getMusic_name();
        String artist_name = all_music_objects.get(position).getArtist();
        song_title.setText(artist_name + " - " + song_name);

        System.out.println(artist_name + " - " + song_name);

        // set image view
        byte[] image_array = all_music_objects.get(position).getImage_array();
        if (image_array != null) {
            Bitmap image = BitmapFactory.decodeByteArray(image_array, 0, image_array.length);
            album_image.setImageBitmap(image);
        }
        else {
            @SuppressLint("UseCompatLoadingForDrawables")
            Drawable default_image = getResources().getDrawable(R.drawable.music3);
            album_image.setImageDrawable(default_image);
//            album_image.setBackground(default_image);
        }


        Uri songResourceUri = Uri.parse(all_song_files.get(position).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), songResourceUri); // create and load mediaplayer with song resources
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                String totalTime = createTimeLabel(mediaPlayer.getDuration());
                total_time.setText(totalTime);
                seek_bar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
//                playIcon.setImageResource(R.drawable.ic_pause_black_24dp);

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int curSongPoition = position;
                // code to repeat songs until the
                if (curSongPoition < all_song_files.size() - 1) {
                    curSongPoition++;
                    initPlayer(curSongPoition);
                } else {
                    curSongPoition = 0;
                    initPlayer(curSongPoition);
                }

                //playIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp);

            }
        });


        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seek_bar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
//                        Log.i("Thread ", "Thread Called");
                        // create new message to send to handler
                        if (mediaPlayer.isPlaying()) {
                            Message msg = new Message();
                            msg.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Log.i("handler ", "handler called");
            int current_position = msg.what;
            seek_bar.setProgress(current_position);
            String cTime = createTimeLabel(current_position);
            current_time.setText(cTime);
        }
    };


    public String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timeLabel += min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;


    }


    public void music_play() {
        mediaPlayer.start();
    }

    public void music_pause() {
        mediaPlayer.pause();
    }

    public void music_stop() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
//        mediaPlayer.stop();
//        mediaPlayer = MediaPlayer.create(this, R.raw.joinme);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<File> findSongs(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        if (files != null) {
            for (File singlefile : files) {
                if (singlefile.isDirectory() && !singlefile.isHidden()) {
                    arrayList.addAll(findSongs(singlefile));
                } else {
                    if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {
                        arrayList.add(singlefile);
                    }
                }
            }
        }
        else {
            System.out.println("BOSBOSBOS");
        }

        Collections.sort(arrayList, Comparator.comparing(File::getName));
        return arrayList;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Music> get_music_objects(ArrayList<File> music_files) {

        ArrayList<Music> all_music_objects = new ArrayList<>();

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        for(File music_file : music_files) {

            Uri uri = Uri.fromFile(music_file);
            mediaMetadataRetriever.setDataSource(this, uri);

            String songName = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String genre = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            String track = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS);
            String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int duration_int = Integer.parseInt(duration);
            byte[] image_array = mediaMetadataRetriever.getEmbeddedPicture();
//            Bitmap image = BitmapFactory.decodeByteArray(image_array, 0, image_array.length);
//            imageView.setImageBitmap(bitmap);

            Music fetched_music = new Music(songName, artist, album, genre, track, duration, duration_int, image_array);
            all_music_objects.add(fetched_music);
        }

//        Collections.sort(all_music_objects, Comparator.comparing(Music::getMusic_name));
        return all_music_objects;
    }


}