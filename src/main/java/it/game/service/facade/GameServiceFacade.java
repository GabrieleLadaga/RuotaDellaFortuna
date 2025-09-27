package it.game.service.facade;

public interface GameServiceFacade {

    void startGame(String name1, String name2, String name3, int numRounds);
    void startRound();
    String spinWheel();
    boolean guessLetter(char letter);
    boolean buyVowel(char vowel);
    boolean solvePuzzle(String phrase);
    void endRound();
    boolean nextRound();

    String displayBoard();
    String getCategory();
    String getCurrentPlayer();

}
