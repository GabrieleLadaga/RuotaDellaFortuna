package it.game.service.command;

import it.game.service.GameManager;

public class BuyVowelCommand implements GameCommand<Boolean> {
    private final GameManager gameManager;
    private final char vowel;

    public BuyVowelCommand(GameManager gameManager, char vowel) {
        this.gameManager = gameManager;
        this.vowel = vowel;
    }

    @Override
    public Boolean execute() {
        if(gameManager.buyVowel()) {
            return gameManager.revealLetter(vowel);
        }
        return false;
    }

}
