package it.game.service.command;

import it.game.model.Sector;
import it.game.service.GameManager;

import java.util.List;

public class GetWheelCommand implements GameCommand<List<Sector>> {
    private final GameManager gameManager;

    public GetWheelCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public List<Sector> execute() {
        return gameManager.getWheel();
    }
}
