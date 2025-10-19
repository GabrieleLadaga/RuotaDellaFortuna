package it.game.service.singleton;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private final String DATABASE_URL = "jdbc:sqlite:database/history.db";
    private static HistoryManager instance;
    private static Connection connection;

    private HistoryManager() {
        try {
            Class.forName("org.sqlite.JDBC");

            new File("database").mkdirs();

            connection = DriverManager.getConnection(DATABASE_URL);
            createTable();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore di connessione al database: " + e.getMessage());
        }
    }

    public synchronized static HistoryManager getInstance() {
        if(instance == null) {
            instance = new HistoryManager();
        }
        return instance;
    }

    private void createTable() throws SQLException {
        String sql = """
                    CREATE TABLE IF NOT EXISTS history (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        date TEXT NOT NULL,
                        player1 TEXT NOT NULL,
                        player2 TEXT NOT NULL,
                        player3 TEXT,
                        player4 TEXT,
                        player5 TEXT,
                        winner TEXT NOT NULL,
                        prize INTEGER NOT NULL
                    )
                    """;
        try(Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void addMatch(String date, String player1, String player2, String player3, String player4, String player5, String winner, int prize) throws SQLException {
        String sql = "INSERT INTO history(date, player1, player2, player3, player4, player5, winner, prize) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, date);
            statement.setString(2, player1);
            statement.setString(3, player2);
            statement.setString(4, player3);
            statement.setString(5, player4);
            statement.setString(6, player5);
            statement.setString(7, winner);
            statement.setInt(8, prize);
            statement.executeUpdate();
        }
    }

    public List<MatchHistory> getAllMatch() throws SQLException {
        List<MatchHistory> matches = new ArrayList<>();
        String sql = "SELECT * FROM history ORDER BY id DESC";

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                MatchHistory match = new MatchHistory(
                        resultSet.getInt("id"),
                        resultSet.getString("date"),
                        resultSet.getString("player1"),
                        resultSet.getString("player2"),
                        resultSet.getString("player3"),
                        resultSet.getString("player4"),
                        resultSet.getString("player5"),
                        resultSet.getString("winner"),
                        resultSet.getInt("prize")
                );
                matches.add(match);
            }
        }
        return matches;
    }

    public void clearHistory() throws SQLException {
        String sql = "DELETE FROM history";
        try(Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public static class MatchHistory {
        private int id;
        private String date;
        private String player1;
        private String player2;
        private String player3;
        private String player4;
        private String player5;
        private String winner;
        private int prize;

        public MatchHistory(int id, String date, String player1, String player2, String player3, String player4, String player5, String winner, int prize) {
            this.id = id; this.date = date; this.player1 = player1; this.player2 = player2;
            this.player3 = player3; this.player4 = player4; this.player5 = player5;
            this.winner = winner; this.prize = prize;
        }

        public int getId() { return id; }
        public String getDate() { return date; }
        public String getPlayer1() { return player1; }
        public String getPlayer2() { return player2; }
        public String getPlayer3() { return player3; }
        public String getPlayer4() { return player4; }
        public String getPlayer5() { return player5; }
        public String getWinner() { return winner; }
        public int getPrize() { return prize; }
    }
}
