package it.game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    void setupBoard() {
        board = new Board();
    }

    @Test
    void testSetAndGetPhrase() {
        board.setPhrase("TEST PHRASE");
        assertEquals("TEST PHRASE", board.getPhrase());
    }

    @Test
    void testSetAndGetCategory() {
        board.setCategory("TEST CATEGORY");
        assertEquals("TEST CATEGORY", board.getCategory());
    }

    @Test
    void testRevealLetter() {
        board.setPhrase("TEST PHRASE");

        assertTrue(board.revealLetter('T'));
        assertTrue(board.revealLetter('P'));
        assertFalse(board.revealLetter('X'));
    }

    @Test
    void testNumberOccurrences() {
        board.setPhrase("TEST PHRASE");

        assertEquals(2, board.numberOccurrences('T'));
        assertEquals(1, board.numberOccurrences('P'));
        assertEquals(0, board.numberOccurrences('X'));
    }

    @Test
    void testAlreadyCalledLetter() {
        board.setPhrase("TEST");
        board.revealLetter('T');

        assertTrue(board.alreadyCalledLetter('T'));
        assertFalse(board.alreadyCalledLetter('P'));
    }

    @Test
    void testIsSolved() {
        board.setPhrase("CAT");

        board.revealLetter('C');
        board.revealLetter('A');
        board.revealLetter('T');

        assertTrue(board.isSolved());
    }

    @Test
    void testCheckPhrase() {
        board.setPhrase("TEST PHRASE");

        assertTrue(board.checkPhrase("TEST PHRASE"));
        assertTrue(board.checkPhrase("test phrase"));
        assertFalse(board.checkPhrase("WRANG PHRASE"));
    }

    @Test
    void testGetDisplayTokens() {
        board.setPhrase("TEST");
        board.revealLetter('T');

        List<String> tokens = board.getDisplayTokens();
        assertEquals("T", tokens.get(0));
        assertEquals("_", tokens.get(1));
    }

    @Test
    void testClear() {
        board.setPhrase("TEST");
        board.setCategory("CATEGORY");
        board.revealLetter('T');

        board.clear();

        assertNull(board.getPhrase());
        assertNull(board.getCategory());
        assertFalse(board.alreadyCalledLetter('T'));
    }

    @Test
    void testNormalization() {
        board.setPhrase("Café");
        assertTrue(board.revealLetter('e'));
    }

}
