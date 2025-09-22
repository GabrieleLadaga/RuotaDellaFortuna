package it.game.model;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Board {
    private String phrase;
    private final List<Character> revealedLetters = new LinkedList<>();

    public void setPhrase(String phrase) { this.phrase = phrase; }
    public int getSize() { return 48; } //Dimensione massima del tabellone (12*4)

    public boolean revealLetter(char c) {
        if(phrase.contains(String.valueOf(c))) {
            revealedLetters.add(c);
            return true;
        }
        return false;
    }

    public int numberOccurrences(char c) {
        StringTokenizer st = new StringTokenizer(phrase, " ");
        int count = 0;
        while(st.hasMoreTokens()) {
            String word = st.nextToken();
            if(word.contains(String.valueOf(c))) {
                ++count;
            }
        }
        return count;
    }

    public boolean alreadyCalledLetter(char c) {
        return revealedLetters.contains(c);
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

    public boolean checkPhrase(String phrase) {
        if(phrase.length() != this.phrase.length()) return false;
        StringTokenizer st1 = new StringTokenizer(this.phrase, " ");
        StringTokenizer st2 = new StringTokenizer(phrase, " ");
        while(st1.hasMoreTokens() && st2.hasMoreTokens()) {
            String word1 = st1.nextToken();
            String word2 = st2.nextToken();
            if(!word1.equals(word2)) return false;
        }
        return true;
    }

}
