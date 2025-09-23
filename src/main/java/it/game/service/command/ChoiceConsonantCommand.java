package it.game.service.command;

import it.game.model.Player;

public class ChoiceConsonantCommand implements GameCommand<Character> {
    private final Player player;

    public ChoiceConsonantCommand(Player player) {
        this.player = player;
    }

    @Override
    public Character execute() {
        return player.choiceConsonant();
    }

}
