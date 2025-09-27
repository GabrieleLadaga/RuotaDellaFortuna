package it.game.model;

import java.util.Scanner;

public class Player {
    private final String id;
    private final String name;
    private int score;
    private final static Scanner sc = new Scanner(System.in);

    public Player(String id, String name) {
        this.id = id; this.name = name; this.score = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getScore() { return score; }

    public void addScore(int points) { this.score += points; }
    public void resetScore() { this.score = 0; }

    public char choiceConsonant() {
        char choice;
        do {
            choice = sc.next().charAt(0);
        } while (!String.valueOf(choice).matches("(?i)^[b-df-hj-np-tv-z]$"));
        return choice;
    }

    public char choiceVocal() {
        char choice;
        do {
            choice = sc.next().charAt(0);
        } while (String.valueOf(choice).matches("(?i)^[b-df-hj-np-tv-z]$"));
        return choice;
    }

    public String giveAnswer() {
        String answer = sc.next();
        return answer;
    }

}
