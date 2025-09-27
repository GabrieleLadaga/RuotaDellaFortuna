package it.game.service.command;

import it.game.service.GameManager;

public class GiveAnswerCommand implements GameCommand<String> {
    private final GameManager gameManager;

    public GiveAnswerCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String execute() {
        return gameManager.giveAnswer();
    }

}
