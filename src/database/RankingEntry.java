package database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class RankingEntry {
    int level, score, turn, enemyKill;
    LocalDateTime time;
    boolean win;
    String levelInfo = "";

    public RankingEntry(int level, int score, LocalDateTime time, boolean win, int turn) {
        this.level = level;
        this.score = score;
        this.time = time;
        this.win = win;
        this.turn = turn;
    }

    public void setLevelInfo(String info) {
        this.levelInfo = info;
    }

    public String getTime() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("Lvl %d | %,d pts | %,d turns | %,d kills | %s%s",
                level, score, turn, enemyKill, getTime(),
                levelInfo.isEmpty() ? "" : " – " + levelInfo);
    }
}
