package it.game.service.command;

public class GameCommandInvoker {

    public <E> E executeCommand(GameCommand<E> command) {
        return command.execute();
    }

}
