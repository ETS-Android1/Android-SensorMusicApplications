package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import data_classes.Music;

public class HomePageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView no_songs_TextView;
    ArrayList<File> music_files;
    ArrayList<Music> music_list;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        IntentFilter intentFilter = new IntentFilter("com.example.myBroadcastMessage");
        SensorReceiver myReceiver = new SensorReceiver();
        registerReceiver(myReceiver, intentFilter);


        recyclerView = findViewById(R.id.rvMusicList);
        no_songs_TextView = findViewById(R.id.tvNoSongsText);

        if(!check_permission()) {
            request_permission();
            return;
        }

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        music_files = findSongs(folder);
        music_list = get_music_objects(music_files);

        if(music_list.size() == 0) {
            no_songs_TextView.setVisibility(View.VISIBLE);
        }
        else {
            // recycler view
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new SongAdapter(music_files, music_list, getApplicationContext()));
        }


    }



    boolean check_permission() {

        int result = ContextCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    void request_permission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomePageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(HomePageActivity.this, "READ PERMISSION IS REQUIRED !", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(HomePageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<File> findSongs(File file) {
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
//        File first = arrayList.get(0);
//        System.out.println(first.toString());
        return arrayList;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Music> get_music_objects(ArrayList<File> music_files) {

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
//        Music first = all_music_objects.get(0);
//        System.out.println(first.getMusic_name());
        return all_music_objects;
    }





}