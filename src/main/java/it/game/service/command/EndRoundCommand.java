package it.game.service.command;

import it.game.service.GameManager;

public class EndRoundCommand implements GameCommand<Void> {
    private final GameManager gameManager;

    public EndRoundCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public Void execute() {
        gameManager.endRound();
        return null;
    }

}
