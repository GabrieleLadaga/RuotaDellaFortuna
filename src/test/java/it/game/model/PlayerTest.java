package it.game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("1", "TestPlayer");
    }

    @Test
    void testPlayerCreation() {
        assertEquals("1", player.getId());
        assertEquals("TestPlayer", player.getName());
        assertEquals(0, player.getScore());
    }

    @Test
    void testAddScore() {
        player.addScore(100);
        assertEquals(100, player.getScore());

        player.addScore(50);
        assertEquals(150, player.getScore());
    }

    @Test
    void testResetScore() {
        player.addScore(100);
        player.resetScore();
        assertEquals(0, player.getScore());
    }

    @Test
    void testScoreAccumulation() {
        player.addScore(100);
        player.addScore(200);
        player.addScore(300);

        assertEquals(600, player.getScore());
    }

}
