package aoop.asteroids.model;

import javax.swing.*;
import java.sql.*;

/**
 * Class for the creation and managing of a database
 */
public class DataBase {

    /**
     * The database URL to connect with the database
     */
    private final String DB_URL = "jdbc:sqlite:highscores.db";

    /**
     * Constructor, will create a database instance and call create table to create a table in case it does not exist
     */
    public DataBase() {
        this.createTable();
    }

    /**
     * Try to establish a Connection with the database, return the connection
     * @return the Connection to the database
     */
    private Connection connectToDB() {
        Connection conn = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection to the database could not be established");
        }
        return conn;
    }

    /**
     * Create a table in the database with an integer as primary key that autoincrements on each insert, a player name and a score. It is allowed to have duplicate player names.
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `highscores` (\n" +
                "  `index` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  `player_name` TEXT NOT NULL,\n" +
                "  `score` INTEGER NOT NULL\n" +
                ")";
        try (Connection conn = this.connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL query could not be executed");
        }
    }

    /**
     * Insert an entry into the database, taking the player name and the high score
     * @param pname the player name to be inserted
     * @param highscore the score of the player
     */
    public void insert(String pname, int highscore) {
        String sql = "INSERT INTO highscores(player_name, score) VALUES(?,?)";
        try (Connection conn = this.connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pname);
            pstmt.setInt(2, highscore);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL query could not be executed");
        }
    }

    /**
     * Function to select the Top 10 entries, since we only wanted to show the 10 best scores on the scoreboard
     * @return A resultset containing the entries of the top 10 best scores
     */
    public ResultSet selectTop() {
        try {
            Statement stmt = this.connectToDB().createStatement();
            String sql = "SELECT player_name, score FROM highscores ORDER BY score DESC LIMIT 10";
            return stmt.executeQuery(sql);
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL query could not be executed");
        }
        return null;
    }

    /**
     * Will clear all the entries of the table, so the scoreboard will be cleared
     */
    public void deleteScores() {
        String sql = "DELETE FROM highscores";
        try (Connection conn = this.connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL query could not be executed");
        }
    }
}
