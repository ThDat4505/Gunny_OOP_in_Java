package database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RankingEntry {
    int level, score, turn;
    LocalDateTime time;
    boolean win;

    public RankingEntry(int level, int score, LocalDateTime time, boolean win, int turn) {
        this.level = level;
        this.score = score;
        this.time = time;
        this.win = win;
        this.turn = turn;
    }

    public String getTime() {
        return time.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"));
    }

    public int getLevel() { return level; }
    public int getScore() { return score; }
    public int getTurn() { return turn; }
    public boolean isWin() { return win; }
}