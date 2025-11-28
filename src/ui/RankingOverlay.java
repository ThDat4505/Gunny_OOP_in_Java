package ui;

import database.RankingEntry;
import main.Game;
import gamestates.Levels;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import static database.JDBCRanking.getGlobalLeaderboard;
import static utilz.Constants.UI.URMButtons.URM_SIZE;

public class RankingOverlay {
    private Levels levels;
    private UrmButton returnButton;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;
    private List<RankingEntry> leaderboard;
    private int currentLevel = 1;
    private final int MAX_DISPLAY = 5;

    public RankingOverlay(Levels levels) {
        this.levels = levels;
        initImg();
        initButton();
        loadLeaderboard();

    }

    private void loadLeaderboard() {
        leaderboard = getGlobalLeaderboard(MAX_DISPLAY);
    }

    private void  initButton() {
        int returnX = (int) (Game.GAME_WIDTH/2 - URM_SIZE/2);
        int returnY = (int) (Game.GAME_HEIGHT/2 + 300 * Game.SCALE / 2);
        returnButton = new UrmButton(returnX, returnY, URM_SIZE, URM_SIZE, 1);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.RANKING_FRAME);
        bgW = (int) (700 * Game.SCALE);
        bgH = (int) (300 * Game.SCALE);
        bgX = (int) (Game.GAME_WIDTH / 2 - 700 * Game.SCALE / 2);
        bgY = (int) (Game.GAME_HEIGHT / 2 - 300 * Game.SCALE / 2);
    }

    public void update() {
        returnButton.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, bgX, bgY, bgW, bgH, null);

        if (leaderboard == null || leaderboard.isEmpty()) {
            g.setColor(new Color(255, 215, 0));
            drawCenteredString(g, "No records yet!", (int) (bgY + 140 * Game.SCALE));
            returnButton.draw(g);
            return;
        }

        g.setColor(new Color(255, 215, 0));
        g.setFont(new Font("Joystix MonoSpace", Font.BOLD, (int) (20 * Game.SCALE)));
        drawCenteredString(g, "GLOBAL LEADERBOARD", (int) (bgY + 42 * Game.SCALE));

        g.setFont(new Font("Joystix MonoSpace", Font.BOLD, (int) (11.5 * Game.SCALE)));
        int startY = (int) (bgY + 123 * Game.SCALE);
        int lineHeight = (int) (29 * Game.SCALE);

        for (int i = 0; i < leaderboard.size(); i++) {
            RankingEntry e = leaderboard.get(i);
            String categories = new String(" Rank  Level        Point      #Turn         Time      ");
            String line = String.format(" #%d   Level %d %,7d pts   %,3d turns   %s",
                    i + 1, e.getLevel(), e.getScore(), e.getTurn(), e.getTime());

            g.setColor(new Color(255, 215, 0));
            drawCenteredString(g, categories, startY - lineHeight);
            drawCenteredString(g, line, startY + i * lineHeight);
        }

        returnButton.draw(g);
    }

    private void drawCenteredString(Graphics g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = bgX + (bgW - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        returnButton.setMouseOver(false);
        if (isIn(returnButton, e))
            returnButton.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(returnButton, e)) {
            if (returnButton.isMousePressed()) {
                levels.setRanking(!levels.getRanking());
            }
        }
        returnButton.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(returnButton, e))
            returnButton.setMousePressed(true);
    }

    public void resetBools() {
        returnButton.resetBools();
    }
}
