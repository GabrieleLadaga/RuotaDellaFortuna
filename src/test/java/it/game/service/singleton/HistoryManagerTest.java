package it.game.service.singleton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = HistoryManager.getInstance();
    }

    @Test
    void testSingletonInstance() {
        HistoryManager instance1 =  HistoryManager.getInstance();
        HistoryManager instance2 = HistoryManager.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    void testAddRetrieveMatch() throws SQLException {
        historyManager.clearHistory();

        historyManager.addMatch("19/10/2025", "Player1", "Player2", null, null, null, "Player1", 1000);

        List<HistoryManager.MatchHistory> matches = historyManager.getAllMatch();
        assertFalse(matches.isEmpty());

        HistoryManager.MatchHistory lastMatch = matches.get(0);

        assertEquals("Player1", lastMatch.getPlayer1());
        assertEquals("Player2", lastMatch.getPlayer2());
        assertEquals("Player1", lastMatch.getWinner());
        assertEquals(1000, lastMatch.getPrize());
    }

    @Test
    void testMatchHistoryObject() {
        HistoryManager.MatchHistory match = new HistoryManager.MatchHistory(1, "19/10/2025", "P1", "P2", "P3", null, null, "P1", 1500);

        assertEquals(1, match.getId());
        assertEquals("19/10/2025", match.getDate());
        assertEquals("P1", match.getPlayer1());
        assertEquals("P2", match.getPlayer2());
        assertEquals("P3", match.getPlayer3());
        assertEquals("P1", match.getWinner());
        assertEquals(1500, match.getPrize());
    }

    @AfterEach
    void tearDown() throws SQLException {
        historyManager.clearHistory();
    }

}
