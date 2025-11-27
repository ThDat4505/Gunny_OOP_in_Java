package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCRanking {

    public static void saveResult(int level, int score, boolean win, int turns) {
        String sql = "INSERT INTO Ranking (Level, Score, Win, Turn) VALUES (?, ?, ?, ?)";
        try (Connection c = JDBCConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, level);
            ps.setInt(2, score);
            ps.setBoolean(3, win);
            ps.setInt(4, turns);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<RankingEntry> getLeaderboard(int level, int limit) {
        List<RankingEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM Ranking WHERE Level = ? AND Win = TRUE ORDER BY Time ASC LIMIT ?";

        try (Connection c = JDBCConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, level);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new RankingEntry(
                        rs.getInt("Level"),
                        rs.getInt("Score"),
                        rs.getTimestamp("Time").toLocalDateTime(),
                        rs.getBoolean("Win"),
                        rs.getInt("Turn")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<RankingEntry> getGlobalLeaderboard(int limit) {
        List<RankingEntry> list = new ArrayList<>();
        String sql = """
        SELECT Level, Score, Time, Win, Turn
        FROM Ranking
        WHERE Win = TRUE
        ORDER BY Score DESC
        LIMIT ?""";

        try (Connection c = JDBCConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new RankingEntry(
                        rs.getInt("Level"),
                        rs.getInt("Score"),
                        rs.getTimestamp("Time").toLocalDateTime(),
                        rs.getBoolean("Win"),
                        rs.getInt("Turn")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

