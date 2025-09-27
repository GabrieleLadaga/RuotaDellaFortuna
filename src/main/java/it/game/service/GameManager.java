package it.game.service;

import it.game.model.Board;
import it.game.model.Player;
import it.game.model.wheel.AbstractWheel;
import it.game.model.wheel.NormalWheel;
import it.game.service.llm.LLMProvider;
import it.game.service.llm.OllamaModelCreator;
import it.game.service.observer.GameSubject;
import it.game.service.utils.GameState;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameManager extends GameSubject {
    private final AbstractWheel wheel;
    private final Board board;
    private final LLMProvider llm;
    private final Player[] players = new Player[3];
    private final int[] partialJackpot = new int[3];
    private int turn = 0;
    private int numRounds;
    private GameState state = GameState.WAITING;

    public GameManager() {
        this.wheel = new NormalWheel();
        this.board = new Board();
        this.llm = new OllamaModelCreator();
    }

    public void setPlayers(Player[] players) {
        System.arraycopy(players, 0, this.players, 0, players.length);
    }

    public void setNumRounds(int numRounds) { this.numRounds = numRounds; }

    public String spinWheel() {
        return wheel.spin().value();
    }

    public boolean revealLetter(char letter) {
        return board.revealLetter(letter);
    }

    public boolean isSolved() {
        return board.isSolved();
    }

    public String display() {
        return board.display();
    }

    public boolean checkPhrase(String phrase) {
        return board.checkPhrase(phrase);
    }

    public void updatePartialJackpot(char c, String value) {
        partialJackpot[turn] += board.numberOccurrences(c) * Integer.parseInt(value);
        notifyObservers();
    }

    public boolean buyVowel() {
        if(partialJackpot[turn] >= 500) {
            partialJackpot[turn] -= 500;
            notifyObservers();
            return true;
        }
        return false;
    }

    public void choicePhrase() {
        String phrase;
        String category = takeCategory();
        int maxLength = board.getSize();
        do {
            phrase = llm.getPuzzle(category, maxLength);
        } while (phrase == null || phrase.length() > maxLength);
        board.setPhrase(phrase);
    }

    private String takeCategory() {
        try {
            Path path = Path.of(getClass().getClassLoader().getResource("data/categories.txt").toURI());
            List<String> categories = Files.readAllLines(path);
            if(categories.isEmpty()) {
                throw new IllegalStateException("Nessuna categoria trovata nel file!");
            }
            Random rand = new Random();
            String category = categories.get(rand.nextInt(categories.size())).trim();
            board.setCategory(category);
            return category;
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la lettura del file!");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Errore nel Path!");
        }
    }

    public String getCategory() { return board.getCategory(); }

    public List<String> getAllNamePlayers() {
        List<String> playerNames = new ArrayList<>();
        for(Player player : players) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public List<Integer> getAllPartialJackpot() {
        List<Integer> partialJackpots = new ArrayList<>();
        for(int jackpot : partialJackpot) {
            partialJackpots.add(jackpot);
        }
        return partialJackpots;
    }

    public List<Integer> getAllScorePlayers() {
        List<Integer> scorePlayers = new ArrayList<>();
        for(Player player : players) {
            scorePlayers.add(player.getScore());
        }
        return scorePlayers;
    }

    public void addScore(int jackpot) {
        players[turn].addScore(jackpot);
    }

    public void resetScore() {
        players[turn].resetScore();
    }

    public char choiceConsonant() {
        return players[turn].choiceConsonant();
    }

    public char choiceVocal() {
        return players[turn].choiceVocal();
    }

    public String giveAnswer() {
        return players[turn].giveAnswer();
    }

    public void nextTurn() {
        turn = (turn + 1) % players.length;
    }

    public Player getCurrentPlayer() {
        return players[turn];
    }

    public boolean nextRound() {
        if(numRounds > 0) {
            --numRounds;
            startRound();
            return true;
        }
        state = GameState.GAME_OVER;
        return false;
    }

    public void resetPartialJackpot() {
        Arrays.fill(partialJackpot, 0);
    }

    public void startRound() {
        turn = 0;
        resetPartialJackpot();
        choicePhrase();
        state = GameState.IN_PROGRESS;
        notifyObservers();
    }

    public void endRound() {
        addScore(partialJackpot[turn]);
        resetPartialJackpot();
        state = GameState.ROUND_OVER;
        board.clear();
    }

}
