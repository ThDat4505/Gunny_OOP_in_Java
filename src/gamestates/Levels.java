package gamestates;

import main.Game;
import ui.LevelsFrame;
import ui.RankingOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Levels extends State implements Statemethods {

    private LevelsFrame[] frames = new LevelsFrame[3];
    private BufferedImage levelFrame, getBackgroundImg;
    private int level1X, level2X, level3X;
    private int levelY;
    private Playing playing;
    private boolean ranking = false;
    private RankingOverlay rankingOverlay;

    public Levels(Game game, Playing playing) {
        super(game);
        this.playing = playing;
        rankingOverlay = new RankingOverlay(this);
        loadFrames();
        getBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadFrames() {
        level1X = (int) (16 * Game.SCALE);
        level2X = (int) (288 * Game.SCALE);
        level3X = (int) (560 * Game.SCALE);
        levelY = (int) (96 * Game.SCALE);
        frames[0] = new LevelsFrame(level1X, levelY, 0, Gamestate.PLAYING, playing.getLevelManager(), this);
        frames[1] = new LevelsFrame(level2X, levelY, 1, Gamestate.PLAYING, playing.getLevelManager(), this);
        frames[2] = new LevelsFrame(level3X, levelY, 2, Gamestate.LEVELS, playing.getLevelManager(), this);
    }

    @Override
    public void update() {
        for(LevelsFrame f : frames)
            f.update();
        if(ranking)
            rankingOverlay.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getBackgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        for (LevelsFrame f : frames)
            f.draw(g);

        if(ranking)
            rankingOverlay.draw(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(ranking)
            rankingOverlay.mousePressed(e);
        else {
            for (LevelsFrame f : frames) {
                if (isIn(e, f)) {
                    f.setMousePressed(true);
                    break;
                }
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(ranking)
            rankingOverlay.mouseReleased(e);
        else {
            for (LevelsFrame f : frames) {
                if (isIn(e, f)) {
                    if (f.isMousePressed())
                        f.applyGamestate();
                    if (f.getState() == Gamestate.PLAYING)
                        game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
                    break;
                }
            }
            resetButtons();
        }
    }

    private void resetButtons() {
        if (ranking)
            rankingOverlay.resetBools();
        else {
            for (LevelsFrame f : frames)
                f.resetBools();
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if(ranking)
            rankingOverlay.mouseMoved(e);
        else {
            for (LevelsFrame f : frames)
                f.setMouseOver(false);

            for (LevelsFrame f : frames)
                if (isIn(e, f)) {
                    f.setMouseOver(true);
                    break;
                }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                setGameState(Gamestate.MENU);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean getRanking() {
        return ranking;
    }

    public void setRanking (boolean ranking) {
        this.ranking = ranking;
    }
}
