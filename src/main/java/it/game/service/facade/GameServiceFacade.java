package it.game.service.facade;

import com.vaadin.flow.component.html.Div;
import it.game.model.Player;
import it.game.model.Sector;
import it.game.service.memento.BoardMemento;

import java.util.List;

public interface GameServiceFacade {

    void startGame(List<String> names, int numRounds);
    void startRound();
    String spinWheel();
    boolean guessLetter(char letter);
    boolean buyVowel(char vowel);
    boolean solvePuzzle(String phrase);
    void endRound();
    boolean nextRound();

    List<String> getMarkedTokens();
    boolean checkFinishConsonants();
    boolean checkFinishVowels();
    boolean checkIfSolvedAfterGuess();
    boolean alreadyCalledLetter(char letter);
    String getCategory();
    Player getCurrentPlayer();
    List<Player> getPlayers();

    List<Sector> getWheel();
    int getTurn();
    List<Integer> getAllPartialJackpot();
    String getCurrentPhrase();
    int getCurrentWheelValue();
    boolean canInsertConsonant();

    BoardMemento saveBoardState(Div[][] cells);
    void restoreBoardState(BoardMemento memento, Div[][] cells);
}
