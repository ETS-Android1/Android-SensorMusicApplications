package data_classes;

import androidx.annotation.NonNull;

//import java.util.ArrayList;
import java.io.Serializable;
import java.util.Arrays;

public class Music implements Serializable {

    private String music_name;
    private String artist;
    private String album;
    private String genre;
    private String track;
    private String duration;
    private int duration_int;
    private byte[] image_array;

//    public static ArrayList<Music> all_music = new ArrayList<Music>();


    public Music() {

    }

    public Music(String music_name, String artist, String album, String genre, String track, String duration, int duration_int, byte[] image_array) {
        this.music_name = music_name;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.track = track;
        this.duration = duration;
        this.duration_int = duration_int;
        this.image_array = image_array;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getDuration_int() {
        return duration_int;
    }

    public void setDuration_int(int duration_int) {
        this.duration_int = duration_int;
    }

    public byte[] getImage_array() {
        return image_array;
    }

    public void setImage_array(byte[] image_array) {
        this.image_array = image_array;
    }

    public String get_title() {
        return artist + " - " + music_name;
    }

    public String get_song_duration() {
        return createTimeLabel(duration_int);
    }

    @NonNull
    @Override
    public String toString() {
        return "Music{" +
                "music_name='" + music_name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", track='" + track + '\'' +
                ", duration='" + duration + '\'' +
                ", image_array=" + Arrays.toString(image_array) +
                '}';
    }

    private String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timeLabel += min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;


    }
}
