package it.game.service.singleton;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static final String DATABASE_URL = "jdbc:sqlite:database/history.db";
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
                    CREATE TABLE IF NOT EXIST history (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        date TEXT NOT NULL,
                        player1 TEXT NOT NULL,
                        player2 TEXT NOT NULL,
                        player3 TEXT NOT NULL,
                        winner TEXT NOT NULL,
                        prize INTEGER NOT NULL
                    )
                    """;
        try(Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void addMatch(String date, String player1, String player2, String player3, String winner, int prize) throws SQLException {
        String sql = "INSERT INT history(date, player1, player2, player3, winner, prize) VALUES(?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, date);
            statement.setString(2, player1);
            statement.setString(3, player2);
            statement.setString(4, player3);
            statement.setString(5, winner);
            statement.setInt(6, prize);
            statement.executeUpdate();
        }
    }

    public List<String> getAllMatch() throws SQLException {
        List<String> matches = new ArrayList<>();
        String sql = "SELECT * FROM history ORDER BY id DESC";

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String match = String.format(
                        "Partita %d (%s): %s vs %s vs %s - Winner: %s - Premio: %d€",
                        resultSet.getInt("id"),
                        resultSet.getString("date"),
                        resultSet.getString("player1"),
                        resultSet.getString("player2"),
                        resultSet.getString("player3"),
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

}
