package it.game.service.command;

import it.game.service.GameManager;

public class StartRoundCommand implements GameCommand<Void> {
    private final GameManager gameManager;

    public StartRoundCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public Void execute() {
        gameManager.startRound();
        return null;
    }

}
