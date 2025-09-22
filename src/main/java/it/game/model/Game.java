package it.game.model;

import it.game.model.wheel.NormalWheel;

import java.util.Scanner;

public class Game {
    private Player[] players = new Player[3];
    private int[] jackpotPartial = new int[3];
    private final NormalWheel wheel;
    private final Board board;
    private int numRound;
    private static int turn = -1;

    public Game(String name1, String name2, String name3, int numRound) {
        players[0] = new Player("1", name1);
        players[1] = new Player("2", name2);
        players[2] = new Player("3", name3);
        wheel = new NormalWheel();
        board = new Board();
        this.numRound = numRound;
        this.numRound++;
    }

    public Player nextTurn() {
        turn = (turn + 1) % players.length;
        return players[turn];
    }

    public boolean nextRound() {
        if( numRound > 0 ) {
            numRound--;
            return true;
        }
        return false;
    }

    public void play() throws Exception {
        while (numRound > 0) {
            nextRound();
            playRound();
        }
        System.out.println("Partita finita!");
        Player winner = checkWinner();
        System.out.println("Ha vinto: " + winner.getName());
    }

    public void playRound() throws Exception {
        while( !board.isSolved() ) {
            nextTurn();
            playGame();
        }
        System.out.println("Complimenti " + players[turn].getName() + " hai vinto questo round!");
        players[turn].addScore(jackpotPartial[turn]);
        resetJackpotPartial();
    }

    public void playGame() throws Exception {
        String resultWheel = wheel.spin().value();
        switch (resultWheel) {
            case "Passa Turno":
                break;
            case "Bancarotta":
                players[turn].resetScore(); //Azzero montepremi
                jackpotPartial[turn] = 0; //Azzero montepremi parziale
                break;
            default: //Bonus
                char choice = players[turn].choiceConsonant();
                if(!board.alreadyCalledLetter(choice)) {
                    if(board.revealLetter(choice)) {
                        if(resultWheel.equals("Raddoppia")) {
                            jackpotPartial[turn] = jackpotPartial[turn] * 2;
                        } else {
                            int numOcc = board.numberOccurrences(choice);
                            jackpotPartial[turn] = jackpotPartial[turn] + (numOcc * Integer.parseInt(resultWheel));
                        }
                        System.out.println("Cosa vuoi fare? Girare la ruota o comprare una vocale?");
                        Scanner sc = new Scanner(System.in);
                        String answer = sc.nextLine();
                        if(answer.contains("vocale") && jackpotPartial[turn] >= 300) {
                            jackpotPartial[turn] -= 300; //costo della vocale
                            choice = players[turn].choiceVocal();
                            if (board.revealLetter(choice)) { //caso positivo vocale
                                playGame();
                            } else {
                                break; //Errore - vocale non presente -> Passa turno
                            }
                        } else {
                            throw new IllegalAccessException("No Vocal");
                        }
                    } else { //Errore - consonante non presente -> Passa Turno
                        break;
                    }
                } else {
                    break; //Errore - consonante già uscita -> Passa Turno
                }
        }
    }

    private Player checkWinner() {
        Player winner = null;
        int jackpotMax = -1;
        for(int i=0; i<players.length; i++) {
            if(players[i].getScore() > jackpotMax) {
                jackpotMax = players[i].getScore();
                winner = players[i];
            }
        }
        return winner;
    }

    private void resetJackpotPartial() {
        for(int i = 0; i < players.length; i++) {
            jackpotPartial[i] = 0;
        }
    }

}
