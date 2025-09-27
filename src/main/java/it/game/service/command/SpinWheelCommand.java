package it.game.service.command;

import it.game.service.GameManager;

public class SpinWheelCommand implements GameCommand<String> {
    private final GameManager gameManager;

    public SpinWheelCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String execute() {
        return gameManager.spinWheel();
    }
}
