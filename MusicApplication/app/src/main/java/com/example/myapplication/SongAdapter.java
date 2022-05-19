package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import data_classes.Music;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    ArrayList<File> songFiles;
    ArrayList<Music> songList;
    Context context;

    public SongAdapter(ArrayList<File> songFiles,ArrayList<Music> songList, Context context) {
        this.songFiles = songFiles;
        this.songList = songList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SongAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setTag(position);
        Music music = songList.get(position);
        holder.music_title.setText(music.get_title());
        holder.music_duration.setText(music.get_song_duration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicActivity.position = position;
                Intent intent = new Intent(context, MusicActivity.class);
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void send_music(File music) {

        String music_path = music.getAbsolutePath();
        Uri uri = Uri.parse(music_path);


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("audio/mp3");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, "Send Music ..."));
    }

    public void delete_music(File music) {
        boolean delete = music.delete();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView music_title;
        TextView music_duration;
        Button send_button;
        Button delete_button;
        ImageView music_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            music_title = itemView.findViewById(R.id.tvMusicTitle);
            music_title.setSelected(true);
            music_duration = itemView.findViewById(R.id.tvMusicDuration);
            send_button = itemView.findViewById(R.id.btnSendMusic);
            delete_button = itemView.findViewById(R.id.btnDeleteMusic);
            music_icon = itemView.findViewById(R.id.iwSongIcon);

            send_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) itemView.getTag();
                    System.out.println(pos);
                    send_music(songFiles.get(pos));
                }
            });

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) itemView.getTag();
                    delete_music(songFiles.get(pos));
                    notifyItemRemoved(pos);
//                    songFiles.remove(pos);
                }
            });

        }
    }

}
