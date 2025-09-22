package it.game.model;

import java.util.Scanner;

public class Player {
    private final String id;
    private final String name;
    private int score;

    Player(String id, String name) {
        this.id = id; this.name = name; this.score = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getScore() { return score; }

    public void addScore(int points) { this.score += points; }
    public void resetScore() { this.score = 0; }

//    public char choiceLetter() {
//        Scanner sc = new Scanner(System.in);
//        char choice = sc.next().charAt(0);
//        sc.close();
//        return choice;
//    }

    public char choiceConsonant() {
        Scanner sc = new Scanner(System.in);
        char choice;
        do {
            choice = sc.next().charAt(0);
        } while (!String.valueOf(choice).matches("(?i)^[b-df-hj-np-tv-z]$"));
        sc.close();
        return choice;
    }

    public char choiceVocal() {
        Scanner sc = new Scanner(System.in);
        char choice;
        do {
            choice = sc.next().charAt(0);
        } while (String.valueOf(choice).matches("(?i)^[b-df-hj-np-tv-z]$"));
        sc.close();
        return choice;
    }

    public String giveAnswer() {
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        sc.close();
        return answer;
    }

}
