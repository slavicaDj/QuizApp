package com.example.hpkorisnik.quizapp;

import android.support.annotation.NonNull;

public class Player implements  Comparable<Player>{

    private String nickname;
    private int points;

    public Player() {

    }

    public Player (String nickname, int points) {
        this.nickname = nickname;
        this.points = points;
    }

    public void incrementPoints() {
        this.points++;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return nickname + "#" + points;
    }

    @Override
    public int compareTo(Player player) {
        return this.points - player.getPoints();
    }
}
