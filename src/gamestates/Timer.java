package gamestates;

import main.Game;
import physic.Turn;
import ui.Text;

import java.awt.*;

public class Timer extends Time {

    private Playing playing;
    private double timer;
    private Text gameTimer;

    public Timer(double seconds, Playing playing) {
        currentUpdates = getUpdateFromSeconds(seconds);
        gameTimer = new Text ("", this, Game.GAME_WIDTH /2, 2 * Game.TILES_SIZE);
        this.playing = playing;
        timer = seconds;
    }

    @Override
    public void update() {
        gameTimer.setColor(Color.LIGHT_GRAY);
        if(playing.getTurn() == Turn.PLAYER) {
            if (currentUpdates / Game.UPS_SET <= 5)
                gameTimer.setColor(Color.YELLOW);
            if (currentUpdates / Game.UPS_SET < 3)
                gameTimer.setColor(Color.RED);

            if (currentUpdates > 0) {
                currentUpdates--;

                if (playing.getTurn() == Turn.THROWING)
                    currentUpdates = 0;

                if (currentUpdates == 0) {
                    playing.setTurn(Turn.ENEMY);
                    playing.getPlayer().resetDirBooleans();
                    resetCurrentUpdates();
                }
            }
        }
        gameTimer.setText(getFormattedTime());
    }

    @Override
    public void draw(Graphics g) {
        if(playing.getTurn() == Turn.PLAYER)
            gameTimer.draw(g);
    }

    public void resetCurrentUpdates() {
        currentUpdates = getUpdateFromSeconds(timer);
    }

}
