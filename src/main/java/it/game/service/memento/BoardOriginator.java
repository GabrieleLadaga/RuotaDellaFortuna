package it.game.service.memento;

import com.vaadin.flow.component.html.Div;

public class BoardOriginator {

    public BoardMemento saveState(Div[][] cells) {
        int rows = cells.length;
        int cols = cells[0].length;

        String[][] texts = new String[rows][cols];
        String[][] colors = new String[rows][cols];

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                texts[i][j] = cells[i][j].getText();
                colors[i][j] = cells[i][j].getStyle().get("background-color");
            }
        }
        return new BoardMemento(texts, colors);
    }

    public void restoreToBoard(BoardMemento memento, Div[][] cells) {
        String[][] texts = memento.getTexts();
        String[][] colors = memento.getColors();

        for(int i = 0; i < texts.length; i++) {
            for(int j = 0; j < colors.length; j++) {
                cells[i][j].setText(texts[i][j]);
                cells[i][j].getStyle().set("background-color", colors[i][j]);
            }
        }
    }
}
