package com.example.bagrutapp;

public class GameSummary {
    private String player1, player2, winner, duration, moves;

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMoves() {
        return moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    public GameSummary(String player1, String player2, String winner, String duration, String moves) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
        this.duration = duration;
        this.moves = moves;
    }
}
