package it.game.service.memento;

public class BoardMemento {

    private String[][] texts;
    private String[][] colors;

    public BoardMemento(String[][] texts, String[][] colors) {
        this.texts = deepCopy(texts); this.colors = deepCopy(colors);
    }

    public String[][] getTexts() { return this.texts; }
    public String[][] getColors() { return this.colors; }

    private String[][] deepCopy(String[][] toCopy) {
        String[][] copy = new String[toCopy.length][toCopy[0].length];
        for (int i = 0; i < toCopy.length; i++) System.arraycopy(toCopy[i], 0, copy[i], 0, toCopy[i].length);
        return copy;
    }

}
