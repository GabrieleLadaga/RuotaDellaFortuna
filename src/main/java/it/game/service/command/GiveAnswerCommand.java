package it.game.service.command;

import it.game.model.Player;

public class GiveAnswerCommand implements GameCommand<String> {
    private final Player player;

    public GiveAnswerCommand(Player player) {
        this.player = player;
    }

    @Override
    public String execute() {
        return player.giveAnswer();
    }

}
