package it.game.model;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Board {
    private final String phrase;
    private final List<Character> revealedLetters = new LinkedList<>();

    public Board(String phrase) {
        this.phrase = phrase;
    }

    public boolean revealLetter(char c) {
        if(phrase.contains(String.valueOf(c))) {
            revealedLetters.add(c);
            return true;
        }
        return false;
    }

    public boolean isSolved() {
        StringTokenizer st = new StringTokenizer(phrase, " ");
        while(st.hasMoreTokens()) {
            String word = st.nextToken();
            for(int i = 0; i < word.length(); i++) {
                if(!revealedLetters.contains(word.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public String display() { return phrase; }

}
