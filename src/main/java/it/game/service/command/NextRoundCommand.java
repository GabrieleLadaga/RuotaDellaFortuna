package it.game.service.command;

import it.game.service.GameManager;

public class NextRoundCommand implements GameCommand<Boolean> {
    private final GameManager gameManager;

    public NextRoundCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public Boolean execute() {
        return gameManager.nextRound();
    }
}
