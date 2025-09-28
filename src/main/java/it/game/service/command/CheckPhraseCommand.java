package it.game.service.command;

import it.game.service.GameManager;

public class CheckPhraseCommand implements GameCommand<Boolean> {
    private final GameManager gameManager;
    private final String phrase;

    public CheckPhraseCommand(GameManager gameManager, String phrase) {
        this.gameManager = gameManager;
        this.phrase = phrase;
    }

    @Override
    public Boolean execute() {
        return gameManager.checkPhrase(phrase);
    }
}
