package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton{

    private BufferedImage[][] soundImgs;
    private Boolean mouseOver = false, mousePressed = false;
    private Boolean muted = false;
    private int rowIndex, colIndex;


    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTON);
        soundImgs = new BufferedImage[2][3];
        for(int j = 0; j < soundImgs.length; j++)
            for(int i = 0; i < soundImgs[j].length; i++)
                soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT , SOUND_SIZE_DEFAULT );
    }

    public void update() {
        if(muted)
            rowIndex = 1;
        else
            rowIndex = 0;

        colIndex = 0;
        if(mouseOver)
            colIndex = 1;
        if(mousePressed)
            colIndex = 2;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public void draw(Graphics g) {
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
    }

    public Boolean isMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public Boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(Boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(Boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
}
