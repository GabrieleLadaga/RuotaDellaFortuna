package it.game.service.command;

import it.game.service.GameManager;

public class GetCurrentPlayerCommand implements GameCommand<String> {
    private final GameManager gameManager;

    public GetCurrentPlayerCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String execute() {
        return gameManager.getCurrentPlayer().getName();
    }

}
