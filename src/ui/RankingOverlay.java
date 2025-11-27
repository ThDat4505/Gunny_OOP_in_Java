package ui;

import gamestates.Levels;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

public class RankingOverlay {
    private Levels levels;
    private UrmButton returnButton;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    public RankingOverlay(Levels levels) {
        this.levels = levels;
        initImg();
        initButton();

    }

    private void  initButton() {
        int returnX = (int) (Game.GAME_WIDTH/2 - URM_SIZE/2);
        int returnY = (int) (Game.GAME_HEIGHT/2 + 300 * Game.SCALE / 2);
        returnButton = new UrmButton(returnX, returnY, URM_SIZE, URM_SIZE, 1);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.RANKING_FRAME);
        bgW = (int) (400 * Game.SCALE);
        bgH = (int) (300 * Game.SCALE);
        bgX = (int) (Game.GAME_WIDTH / 2 - 400 * Game.SCALE / 2);
        bgY = (int) (Game.GAME_HEIGHT / 2 - 300 * Game.SCALE / 2);
    }

    public void update() {
        returnButton.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        returnButton.draw(g);
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
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(returnButton, e))
            returnButton.setMousePressed(true);
    }

    public void resetBools() {
        returnButton.resetBools();
    }
}
