package com.example.golf_apk.dto;

import java.util.List;

public class PracticeGame {

    private Long id;

    private String playDate;

    private Account createdBy;

    private List<Account> players;

    private List<PracticeRound> rounds;

    private int holeCount;

    private String status;

    private String[] names;

    private int[] totalHits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }

    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }

    public List<Account> getPlayers() {
        return players;
    }

    public void setPlayers(List<Account> players) {
        this.players = players;
    }

    public List<PracticeRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<PracticeRound> rounds) {
        this.rounds = rounds;
    }

    public int getHoleCount() {
        return holeCount;
    }

    public void setHoleCount(int holeCount) {
        this.holeCount = holeCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public int[] getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int[] totalHits) {
        this.totalHits = totalHits;
    }
}
