package it.game.service.facade;

import it.game.model.Player;
import it.game.service.GameManager;
import it.game.service.command.*;

public class OfflineGameServiceFacade implements GameServiceFacade {
    private final GameManager gameManager;
    private final GameCommandInvoker invoker;

    public OfflineGameServiceFacade(GameManager gameManager, GameCommandInvoker invoker) {
        this.gameManager = gameManager;
        this.invoker = invoker;
    }

    @Override
    public void startGame(String name1, String name2, String name3, int numRounds) {
        Player[] players = {
                new Player("1", name1),
                new Player("2", name2),
                new Player("3", name3),
        };
        gameManager.setPlayers(players);
        gameManager.setNumRounds(numRounds);
    }

    @Override
    public void startRound() {
        GameCommand<Void> command = new StartRoundCommand(gameManager);
        invoker.executeCommand(command);
    }

    @Override
    public String spinWheel() {
        GameCommand<String> command = new SpinWheelCommand(gameManager);
        return invoker.executeCommand(command);
    }

    @Override
    public boolean guessLetter(char letter) {
        GameCommand<Boolean> command = new RevealLetterCommand(gameManager, letter);
        return invoker.executeCommand(command);
    }

    @Override
    public boolean buyVowel(char vowel) {
        GameCommand<Boolean> command = new BuyVowelCommand(gameManager, vowel);
        return invoker.executeCommand(command);
    }

    @Override
    public boolean solvePuzzle(String phrase) {
        GameCommand<Boolean> command = new CheckPhraseCommand(gameManager, phrase);
        return invoker.executeCommand(command);
    }

    @Override
    public void endRound() {
        GameCommand<Void> command = new EndRoundCommand(gameManager);
        invoker.executeCommand(command);
    }

    @Override
    public boolean nextRound() {
        GameCommand<Boolean> command = new NextRoundCommand(gameManager);
        return invoker.executeCommand(command);
    }

    @Override
    public String displayBoard() {
        GameCommand<String> command = new DisplayBoardCommand(gameManager);
        return invoker.executeCommand(command);
    }

    @Override
    public String getCategory() {
        GameCommand<String> command = new GetCategoryCommand(gameManager);
        return invoker.executeCommand(command);
    }

    @Override
    public String getCurrentPlayer() {
        GameCommand<String> command = new GetCurrentPlayerCommand(gameManager);
        return invoker.executeCommand(command);
    }

}
