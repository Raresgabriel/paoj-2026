package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {

    private String name;
    private Song[] songs = new Song[0];

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addSong(Song song) {
        Song[] newSongs = new Song[songs.length + 1];
        System.arraycopy(songs, 0, newSongs, 0, songs.length);
        newSongs[songs.length] = song;
        songs = newSongs;
    }

    public void printSortedByTitle() {
        Song[] copy = songs.clone();
        Arrays.sort(copy);
        for (Song s : copy) {
            System.out.println(s);
        }
    }

    public void printSortedByDuration() {
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator());
        for (Song s : copy) {
            System.out.println(s);
        }
    }

    public int getTotalDuration() {
        int total = 0;
        for (Song s : songs) {
            total += s.durationSeconds();
        }
        return total;
    }
}
