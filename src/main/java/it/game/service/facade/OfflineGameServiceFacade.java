package it.game.service.facade;

import it.game.model.Player;
import it.game.service.GameManager;

public class OfflineGameServiceFacade implements GameServiceFacade {
    private final GameManager gameManager;

    public OfflineGameServiceFacade(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void startGame(String name1, String name2, String name3, int numRounds) {
        Player[] players = {
                new Player("1", name1),
                new Player("2", name2),
                new Player("3", name3),
        };
        gameManager.setPlayers(players);
        gameManager.setNumRounds(numRounds);
    }

    @Override
    public void startRound() {
        gameManager.startRound();
    }

    @Override
    public String spinWheel() {
        return gameManager.spinWheel();
    }

    @Override
    public boolean guessLetter(char letter) {
        return gameManager.revealLetter(letter);
    }

    @Override
    public boolean buyVowel(char vowel) {
        if(gameManager.buyVowel()) {
            return gameManager.revealLetter(vowel);
        }
        return false;
    }

    @Override
    public boolean solvePuzzle(String phrase) {
        return gameManager.checkPhrase(phrase);
    }

    @Override
    public void endRound() {
        gameManager.endRound();
    }

    @Override
    public boolean nextRound() {
        return gameManager.nextRound();
    }

    @Override
    public String displayBoard() {
        return gameManager.display();
    }

    @Override
    public String getCategory() {
        return gameManager.getCategory();
    }

    @Override
    public String getCurrentPlayer() {
        return gameManager.getCurrentPlayer().getName();
    }

}
