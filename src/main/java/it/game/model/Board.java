package it.game.model;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private String phrase;
    private String category;
    private final List<Character> revealedLetters = new LinkedList<>();
    private final List<Character> allAttemptedLetters = new LinkedList<>();

    public void setPhrase(String phrase) { this.phrase = phrase; }
    public String getPhrase() { return this.phrase; }
    public void setCategory(String category) { this.category = category; }
    public String getCategory() { return this.category; }
    public int getSize() { return 36; } //Dimensione massima del tabellone (12*4)

    public boolean revealLetter(char c) {
        char normalizedChar = normalizeChar(c);
        allAttemptedLetters.add(normalizedChar);

        String normalizedPhrase = normalizedString(phrase);

        if(normalizedPhrase.contains(String.valueOf(normalizedChar))) {
            revealedLetters.add(normalizedChar);
            return true;
        }
        return false;
    }

    public int numberOccurrences(char c) {
        int count = 0;
        char normalizedChar = normalizeChar(c);
        String normalizedPhrase = normalizedString(phrase);

        for(char ch: normalizedPhrase.toCharArray())
            if(ch == normalizedChar) ++count;
        return count;
    }

    public boolean alreadyCalledLetter(char c) {
        char normalizedChar = normalizeChar(c);
        return revealedLetters.contains(normalizedChar) || allAttemptedLetters.contains(normalizedChar);
    }

    public boolean isSolved() {
        String normalizedPhrase = normalizedString(phrase);
        for(char c: normalizedPhrase.toCharArray())
            if(Character.isLetter(c))
                if(!revealedLetters.contains(c)) return false;
        return true;
    }

    public boolean checkPhrase(String phrase) {
        if( phrase == null ) return false;
        return normalizedString(this.phrase).equals(normalizedString(phrase));
    }

    private char normalizeChar(char c) {
        String normalized = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
        return normalized.charAt(0);
    }

    private String normalizedString(String input) {
        if(input == null) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("\\s+", " ")
                .trim();
    }

    public void clear() {
        phrase = null;
        category = null;
        revealedLetters.clear();
        allAttemptedLetters.clear();
    }

    public List<String> getDisplayTokens() {
        List<String> tokens = new LinkedList<>();
        if(phrase == null || phrase.isEmpty()) return tokens;

        for(char c: phrase.toCharArray()) {
            if(c == ' ') {
                tokens.add(" "); //spazio come token
            } else if(!Character.isLetter(c)) {
                tokens.add(String.valueOf(c)); //punteggiatura
            } else {
                char normalized = normalizeChar(c);
                if(revealedLetters.contains(normalized)) {
                    tokens.add(String.valueOf(c)); //lettera rivelata
                } else {
                    tokens.add("_"); //lettera nascosta
                }
            }
        }
        return tokens;
    }

    public boolean checkFinishConsonants() {
        String normalizedPhrase = normalizedString(phrase);
        for(char c: normalizedPhrase.toCharArray()) {
            if(Character.isLetter(c) && "aeiou".indexOf(c) == -1) {
                if(!revealedLetters.contains(c)) return false;
            }
        }
        return true;
    }

    public boolean checkFinishVowels() {
        String normalizedPhrase = normalizedString(phrase);
        for(char c: normalizedPhrase.toCharArray()) {
            if(Character.isLetter(c) && "aeiou".indexOf(c) != -1) {
                if(!revealedLetters.contains(c)) return false;
            }
        }
        return true;
    }

}
