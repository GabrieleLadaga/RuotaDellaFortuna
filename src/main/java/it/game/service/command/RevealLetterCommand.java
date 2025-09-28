package it.game.service.command;

import it.game.service.GameManager;

public class RevealLetterCommand implements GameCommand<Boolean> {
    private final GameManager gameManager;
    private final char letter;

    public RevealLetterCommand(GameManager gameManager, char letter) {
        this.gameManager = gameManager;
        this.letter = letter;
    }

    @Override
    public Boolean execute() {
        return gameManager.revealLetter(letter);
    }

}
