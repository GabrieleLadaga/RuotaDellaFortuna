package it.game.service.command;

import it.game.model.Player;
import it.game.service.GameManager;

public class ChoiceConsonantCommand implements GameCommand<Character> {
    private final GameManager gameManager;

    public ChoiceConsonantCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public Character execute() {
        return gameManager.choiceConsonant();
    }

}
