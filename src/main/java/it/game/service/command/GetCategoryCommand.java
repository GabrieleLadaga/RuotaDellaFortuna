package it.game.service.command;

import it.game.service.GameManager;

public class GetCategoryCommand implements GameCommand<String> {
    private final GameManager gameManager;

    public GetCategoryCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String execute() {
        return gameManager.getCategory();
    }

}
