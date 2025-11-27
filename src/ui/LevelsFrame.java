package ui;

import gamestates.Gamestate;
import gamestates.Levels;
import levels.LevelManager;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.Buttons.*;
import static utilz.Constants.UI.Buttons.B_HEIGHT_DEFAULT;
import static utilz.Constants.UI.Buttons.B_WIDTH_DEFAULT;
import static utilz.Constants.UI.LFrames.FRAME_SIZE;
import static utilz.Constants.UI.LFrames.FRAME_SIZE_DEFAULT;

public class LevelsFrame {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private Boolean mouseOver = false, mousePressed = false;
    private Rectangle bounds;
    private LevelManager levelManager;
    private Levels levels;

    public LevelsFrame(int xPos, int yPos, int rowIndex, Gamestate state, LevelManager levelManager, Levels levels) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        this.levelManager = levelManager;
        this.levels = levels;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos, yPos, FRAME_SIZE, FRAME_SIZE);
    }

    private void loadImgs() {
        imgs = new BufferedImage[2];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_FRAMES);
        for(int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * FRAME_SIZE_DEFAULT, rowIndex * FRAME_SIZE_DEFAULT, FRAME_SIZE_DEFAULT, FRAME_SIZE_DEFAULT);
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos, yPos, FRAME_SIZE, FRAME_SIZE, null);
    }

    public void update() {
        index = 0;
        if(mouseOver)
            index = 1;
    }



    public Boolean isMouseOver() {
        return mouseOver;
    }

    public Boolean isMousePressed() {
        if(rowIndex == 2)
            levels.setRanking(!levels.getRanking());
        return mousePressed;
    }

    public void setMousePressed(Boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void setMouseOver(Boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void applyGamestate() {
        Gamestate.state = state;
    }

    public void resetBools() {
        mouseOver = false;
    }

    public Gamestate getState() {
        return state;
    }

    public int getRowIndex() {
        return rowIndex;
    }

}
