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
        if(phrase.toLowerCase().contains(String.valueOf(Character.toLowerCase(c)))) {
            revealedLetters.add(c);
            return true;
        }
        return false;
    }

    public int numberOccurrences(char c) {
        int count = 0;
        char character = Character.toLowerCase(c);
        for(char ch: phrase.toCharArray())
            if(Character.toLowerCase(ch) == character) ++count;
        return count;
    }

    public boolean alreadyCalledLetter(char c) {
        return revealedLetters.contains(c);
    }

    public boolean isSolved() {
        for(char c: phrase.toCharArray())
            if(Character.isLetter(c))
                if(!revealedLetters.contains(Character.toLowerCase(c)))
                    return false;
        return true;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        for(char c: phrase.toCharArray()) {
            if(c == ' ')
                sb.append(' ');
            else if (String.valueOf(c).matches("[^a-zA-Z]"))
                sb.append(c).append(' ');
            else if (revealedLetters.contains(Character.toLowerCase(c)))
                sb.append(c).append(' ');
            else
                sb.append("_ ");
        }
        return sb.toString();
    }

    public boolean checkPhrase(String phrase) {
        if( phrase == null ) return false;
        return normalize(this.phrase).equals(normalize(phrase));
    }

    private String normalize(String input) {
        return input
                .toLowerCase()
                .replaceAll("[^a-zàèéìòù ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

}
