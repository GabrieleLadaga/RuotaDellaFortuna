package it.game.service.memento;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BoardMementoTest {

    @Test
    void testBoardMementoCreation() {
        String[][] texts = {{"A", "B"}, {"C", "D"}};
        String[][] colors = {{"red", "blue"}, {"green", "yellow"}};

        BoardMemento memento = new BoardMemento(texts, colors);

        assertArrayEquals(texts, memento.getTexts());
        assertArrayEquals(colors, memento.getColors());
    }

    @Test
    void testDeepCopy() {
        String[][] originalTexts = {{"A", "B"}, {"C", "D"}};
        String[][] originalColors = {{"red", "blue"}, {"green", "yellow"}};

        BoardMemento memento = new BoardMemento(originalTexts, originalColors);

        originalTexts[0][0] = "MODIFIED";
        originalColors[0][0] = "MODIFIED";

        assertNotEquals("MODIFIED", memento.getTexts()[0][0]);
        assertNotEquals("MODIFIED", memento.getColors()[0][0]);
    }

}
