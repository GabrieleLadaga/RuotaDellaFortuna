package it.game.service;

import it.game.model.Board;
import it.game.model.Player;
import it.game.service.llm.LLMProvider;
import it.game.service.utils.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameManagerTest {
    private GameManager gameManager;
    private Player[] players;
    private LLMProvider mockLLM;

    @BeforeEach
    void setup() throws Exception {
        gameManager = new GameManager();

        mockLLM = mock(LLMProvider.class);
        when(mockLLM.getPuzzle(anyString(), anyInt())).thenReturn("TEST PHRASE");

        Field llmFiled = GameManager.class.getDeclaredField("llm");
        llmFiled.setAccessible(true);
        llmFiled.set(gameManager, mockLLM);

        players = new Player[] {
                new Player("1", "Player1"),
                new Player("2", "Player2")
        };
        gameManager.setPlayers(players);
        gameManager.setNumRounds(3);
    }

    @Test
    void testStartRound() {
        gameManager.startRound();

        assertEquals(GameState.IN_PROGRESS, gameManager.getState());
        assertNotNull(gameManager.getCurrentPlayer());
        assertNotNull(gameManager.getCategory());
    }

    @Test
    void testSpinWheel() {
        gameManager.startRound();
        String result = gameManager.spinWheel();

        assertNotNull(result);
        assertTrue(gameManager.canInsertConsonant());
    }

    @Test
    void testRevealLetter() {
        gameManager.startRound();
        gameManager.spinWheel();

        Board board = getBoard();
        board.setPhrase("TEST");

        boolean found = gameManager.revealLetter('T');

        assertTrue(found);
        assertFalse(gameManager.canInsertConsonant());
    }

    @Test
    void testBuyVowel() {
        gameManager.startRound();

        setPartialJackpot(500);

        Board board = getBoard();
        board.setPhrase("AEIOU");

        boolean success = gameManager.buyVowel('A');

        assertTrue(success);
    }

    @Test
    void testSolvePuzzle() {
        gameManager.startRound();

        Board board = getBoard();
        board.setPhrase("SOLUTION");

        boolean correct = gameManager.solvePuzzle("SOLUTION");

        assertTrue(correct);
        assertEquals(GameState.GAME_OVER, gameManager.getState());
    }

    @Test
    void testNextTurn() {
        gameManager.startRound();
        int initialTurn = gameManager.getTurn();

        gameManager.nextTurn();

        assertEquals((initialTurn + 1) % players.length, gameManager.getTurn());
    }

    @Test
    void testEndRound() {
        gameManager.startRound();
        gameManager.endRound();

        assertEquals(GameState.ROUND_OVER, gameManager.getState());
    }

    // -- Metodi di Supporto --
    private Board getBoard() {
        try {
            Field boardField = GameManager.class.getDeclaredField("board");
            boardField.setAccessible(true);
            return (Board) boardField.get(gameManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPartialJackpot(int amount) {
        try {
            Field partialJackpotField = GameManager.class.getDeclaredField("partialJackpot");
            partialJackpotField.setAccessible(true);
            int[] partialJackpot = (int[]) partialJackpotField.get(gameManager);
            partialJackpot[gameManager.getTurn()] = amount;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
