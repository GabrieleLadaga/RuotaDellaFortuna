package it.game.service.command;

import it.game.model.Player;

public class ChoiceVocalCommand implements GameCommand<Character> {
    private final Player player;

    public ChoiceVocalCommand(Player player) {
        this.player = player;
    }

    @Override
    public Character execute() {
        return player.choiceVocal();
    }

}
