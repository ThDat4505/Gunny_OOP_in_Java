package database;

import java.sql.Connection;
import java.sql.Statement;

public class JDBCInitializer {

    public static void init() {
        String[] statements = {
                "CREATE DATABASE IF NOT EXISTS gunny2",
                "USE gunny2",

                """
            CREATE TABLE IF NOT EXISTS Level_Info (
                Level       INT          PRIMARY KEY,
                Title       VARCHAR(50)  NOT NULL,
                Information VARCHAR(500) NOT NULL
            ) ENGINE=InnoDB
            """,

                """
            CREATE TABLE IF NOT EXISTS Ranking (
                Level   INT      NOT NULL,
                Score   INT      NOT NULL,
                Time    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                Win     BOOLEAN  NOT NULL DEFAULT FALSE,
                Turn    INT      NOT NULL DEFAULT 0,
                
                PRIMARY KEY (Time),
                FOREIGN KEY (Level) REFERENCES Level_Info(Level)
                    ON DELETE CASCADE ON UPDATE CASCADE,
                
                INDEX idx_level_win (Level, Win),
                INDEX idx_level_time (Level, Time DESC)
            ) ENGINE=InnoDB
            """,

                """
            INSERT IGNORE INTO Level_Info (Level, Title, Information) VALUES
            (1, 'Welcome to the Beach', 'Protect the Magic Bean by defeating the Crabbies'),
            (2, 'Heavenly Magic Bean',  'Heal the Magic Bean to restore its power.')
            """
        };

        try (Connection c = JDBCConnection.getConnection();
             Statement s = c.createStatement()) {

            for (String sql : statements) {
                s.execute(sql);
            }

            System.out.println("Database 'gunny' initialized successfully!");

        } catch (Exception e) {
            System.err.println("JDBC init failed:");
            e.printStackTrace();
        }
    }
}