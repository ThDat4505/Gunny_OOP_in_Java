package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCLevelInfo {

    public static String getDescription(int level) {
        String sql = "SELECT Title FROM Level_Info WHERE Level = ?";
        try (Connection c = JDBCConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, level);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No description";
    }

    public static String getInstruction(int level) {
        String sql = "SELECT Information FROM Level_Info WHERE Level = ?";
        try (Connection c = JDBCConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, level);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No Information";
    }
}