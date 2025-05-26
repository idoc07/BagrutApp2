package com.example.bagrutapp;

import java.util.List;

public class GameLogEntry {
    private String title;
    private List<String> moves;
    private int duration;

    public GameLogEntry(String title, List<String> moves, int duration) {
        this.title = title;
        this.moves = moves;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getMoves() {
        return moves;
    }

    public int getDuration() {
        return duration;
    }
}
