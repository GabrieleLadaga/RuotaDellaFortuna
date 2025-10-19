package it.game.service;

import it.game.model.Board;
import it.game.model.Player;
import it.game.model.Sector;
import it.game.model.wheel.AbstractWheel;
import it.game.model.wheel.NormalWheel;
import it.game.service.llm.LLMProvider;
import it.game.service.llm.OllamaModelCreator;
import it.game.service.observer.GameSubject;
import it.game.service.singleton.HistoryManager;
import it.game.service.utils.GameState;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class GameManager extends GameSubject {
    private final AbstractWheel wheel;
    private final Board board;
    private final LLMProvider llm;
    private final HistoryManager historyManager;

    private Player[] players;
    private int[] partialJackpot;

    private int turn = 0;
    private int numRounds;
    private static int currentWheelValue = 0;
    private String lastSpinType = "";

    private GameState state = GameState.WAITING;
    private boolean roundStarted = false;
    private boolean canInsertConsonant = false;

    public GameManager() {
        this.wheel = new NormalWheel();
        this.board = new Board();
        this.llm = new OllamaModelCreator();
        this.historyManager = HistoryManager.getInstance();
    }

    // -- Gestione Rounds e Turni --
    public void startRound() {
        if(roundStarted) return;

        turn = 0;
        resetPartialJackpot();
        choicePhrase();
        state = GameState.IN_PROGRESS;
        roundStarted = true;
        notifyObservers();
    }

    public void endRound() {
        addScore(partialJackpot[turn]);
        resetPartialJackpot();
        state = GameState.ROUND_OVER;
        roundStarted = false;
        board.clear();
    }

    public void nextTurn() {
        turn = (turn + 1) % players.length;
        notifyObservers();
    }

    public boolean nextRound() {
        if(--numRounds > 0) {
            startRound();
            return true;
        }
        state = GameState.GAME_OVER;
        updateDBMS();
        notifyObservers();
        return false;
    }

    private void updateDBMS() {
        LocalDate today = LocalDate.now();
        String todayFormat = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        String player1 = players.length > 0 ? players[0].getName() : null;
        String player2 = players.length > 1 ? players[1].getName() : null;
        String player3 = players.length > 2 ? players[2].getName() : null;
        String player4 = players.length > 3 ? players[3].getName() : null;
        String player5 = players.length > 4 ? players[4].getName() : null;

        String winner = null;
        int winnerPrize = - 1;
        for(Player p: players) {
            if(p.getScore() > winnerPrize) {
                winner = p.getName();
                winnerPrize = p.getScore();
            }
        }

        try {
            historyManager.addMatch(todayFormat, player1, player2, player3, player4, player5, winner, winnerPrize);
        } catch (SQLException e) {
            System.err.println("Errore nel salvataggio dei dati: " + e.getMessage());
        }
    }

    // -- Gestione della Ruota --
    public String spinWheel() {
        String result = wheel.spin().value();
        lastSpinType = result.toLowerCase();

        switch (lastSpinType) {
            case "bancarotta" -> {
                partialJackpot[turn] = 0;
                resetScoreCurrentPlayer();
                nextTurn();
            }
            case "passa" -> nextTurn();
            case "raddoppia" -> {
                //Il raddoppia viene applicato solo se la lettera del concorrente è corretta
                currentWheelValue = -2;
            }
            default -> {
                try {
                    String numeric = result.replaceAll("[^0-9]", "");
                    currentWheelValue = numeric.isEmpty() ? -1 : Integer.parseInt(numeric);
                } catch (NumberFormatException e) {
                    currentWheelValue = -1;
                }
            }
        }

        canInsertConsonant = true;

        notifyObservers();
        return result;
    }

    // -- Gestione Lettere --
    public boolean revealLetter(char c) {
        if(!canInsertConsonant) return false;

        boolean found = board.revealLetter(c);
        if(found) {
            if("raddoppia".equalsIgnoreCase(lastSpinType)) {
                partialJackpot[turn] *= 2;
            } else if(currentWheelValue > 0) {
                int occurrences = board.numberOccurrences(c);
                partialJackpot[turn] += occurrences * currentWheelValue;
            }
            notifyObservers();
        } else {
            nextTurn();
        }

        canInsertConsonant = false;

        return found;
    }

    public boolean buyVowel(char vowel) {
        if(partialJackpot[turn] < 300) return false;

        partialJackpot[turn] -= 300;

        boolean found = board.revealLetter(vowel);
        if(!found) {
            nextTurn();
            notifyObservers();
            return false;
        }

        notifyObservers();
        return true;
    }

    public boolean solvePuzzle(String guess) {
        boolean correct = board.checkPhrase(guess);
        if(correct) {
            players[turn].addScore(partialJackpot[turn]);
            resetPartialJackpot();
            state = GameState.GAME_OVER;
        } else {
            nextTurn();
        }
        notifyObservers();
        return correct;
    }

    public boolean checkIfSolvedAfterGuess() {
        if(board.isSolved()) {
            players[turn].addScore(partialJackpot[turn]);
            resetPartialJackpot();
            state = GameState.ROUND_OVER;
            notifyObservers();
            return true;
        }
        return false;
    }

    // -- Metodi di Supporto --
    public void resetPartialJackpot() { Arrays.fill(partialJackpot, 0); }

    public void resetScoreCurrentPlayer() { players[turn].resetScore(); }

    public void addScore(int jackpot) { players[turn].addScore(jackpot); }

    public List<Integer> getAllPartialJackpot() { return Arrays.stream(partialJackpot).boxed().toList(); }

    public Player getCurrentPlayer() { return players[turn]; }

    public int getTurn() { return turn; }

    public List<String> getMarkedTokens() { return board.getDisplayTokens(); }

    public GameState getState() { return state; }

    public void setNumRounds(int numRounds) { this.numRounds = numRounds; }

    public void setPlayers(Player[] players) {
        this.players = Arrays.copyOf(players, players.length);
        this.partialJackpot = new int[players.length];
    }

    public List<Sector> getWheel() { return wheel.getResult(); }

    public String getCurrentPhrase() { return board.getPhrase(); }

    public int getCurrentWheelValue() { return currentWheelValue; }

    public List<Player> getPlayers() { return List.of(players); }

    public boolean canInsertConsonant() { return canInsertConsonant; }

    public void setCanInsertConsonant(boolean canInsertConsonant) { this.canInsertConsonant = canInsertConsonant; }

    public boolean alreadyCalledLetter(char letter) { return board.alreadyCalledLetter(letter); }

    public List<String> getAllNamePlayers() {
        List<String> playerNames = new ArrayList<>();
        for(Player player : players) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public List<Integer> getAllScorePlayers() {
        List<Integer> scorePlayers = new ArrayList<>();
        for(Player player : players) {
            scorePlayers.add(player.getScore());
        }
        return scorePlayers;
    }

    // -- Frase e Categoria --
    public void choicePhrase() {
        String category = takeCategory();
        int maxLength = board.getSize();
        String phrase;
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

    public boolean checkPhrase(String phrase) { return board.checkPhrase(phrase); }

    public boolean isSolved() { return board.isSolved(); }

    public boolean checkFinishConsonants() {
        return board.checkFinishConsonants();
    }

    public boolean checkFinishVowels() {
        return board.checkFinishVowels();
    }

}
