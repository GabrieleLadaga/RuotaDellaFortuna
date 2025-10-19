package it.game.service.facade;

import com.vaadin.flow.component.html.Div;
import it.game.model.Player;
import it.game.model.Sector;
import it.game.service.GameManager;
import it.game.service.command.*;
import it.game.service.memento.BoardMemento;
import it.game.service.memento.BoardOriginator;

import java.util.List;

public class OfflineGameServiceFacade implements GameServiceFacade {
    private final GameManager gameManager;
    private final GameCommandInvoker invoker;
    private final BoardOriginator originator;

    public OfflineGameServiceFacade(GameManager gameManager, GameCommandInvoker invoker, BoardOriginator originator) {
        this.gameManager = gameManager; this.invoker = invoker; this.originator = originator;
    }

    @Override
    public void startGame(List<String> names, int numRounds) {
        Player[] players = new Player[names.size()];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("" + i, names.get(i));
        }

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
        return gameManager.revealLetter(letter);
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
    public List<String> getMarkedTokens() {
        return gameManager.getMarkedTokens();
    }

    @Override
    public boolean checkFinishConsonants() {
        return gameManager.checkFinishConsonants();
    }

    @Override
    public boolean checkFinishVowels() {
        return gameManager.checkFinishVowels();
    }

    @Override
    public boolean checkIfSolvedAfterGuess() { return  gameManager.checkIfSolvedAfterGuess(); }

    @Override
    public boolean alreadyCalledLetter(char letter) { return gameManager.alreadyCalledLetter(letter); }

    @Override
    public String getCategory() {
        return gameManager.getCategory();
    }

    @Override
    public Player getCurrentPlayer() {
        return gameManager.getCurrentPlayer();
    }

    @Override
    public List<Player> getPlayers() {
        return gameManager.getPlayers();
    }

    @Override
    public List<Sector> getWheel() {
        GameCommand<List<Sector>> command = new GetWheelCommand(gameManager);
        return invoker.executeCommand(command);
    }

    @Override
    public int getTurn() {
        return gameManager.getTurn();
    }

    @Override
    public List<Integer> getAllPartialJackpot() {
        return gameManager.getAllPartialJackpot();
    }

    @Override
    public String getCurrentPhrase() { return gameManager.getCurrentPhrase(); }

    @Override
    public int getCurrentWheelValue() { return gameManager.getCurrentWheelValue(); }

    @Override
    public boolean canInsertConsonant() { return gameManager.canInsertConsonant(); }

    @Override
    public BoardMemento saveBoardState(Div[][] cells) {
        return originator.saveState(cells);
    }

    @Override
    public void restoreBoardState(BoardMemento memento, Div[][] cells) {
        originator.restoreToBoard(memento, cells);
    }

}
