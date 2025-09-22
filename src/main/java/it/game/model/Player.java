package it.game.model;

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

}
