package it.game.service.command;

import it.game.service.GameManager;

public class DisplayBoardCommand implements GameCommand<String> {
    private final GameManager gameManager;

    public DisplayBoardCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String execute() {
        return gameManager.display();
    }
}
