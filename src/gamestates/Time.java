package gamestates;

import main.Game;
import ui.Text;

import java.awt.*;

public class Time {

    protected int currentUpdates;
    private Text gameTime;

    public Time() {
        this.currentUpdates = 0;
        this.gameTime = new Text("", this, Game.GAME_WIDTH /2, 2 * Game.TILES_SIZE);
    }

    public int getUpdateFromSeconds(double seconds) {
        return (int) Math.round(seconds * Game.UPS_SET);
    }

    public void update() {
        currentUpdates++;
        gameTime.setText(getFormattedTime());
    }

    public void draw(Graphics g) {
        gameTime.draw(g);
    }

    public String getFormattedTime() {
        StringBuilder stringBuilder = new StringBuilder();
        int minutes = currentUpdates / Game.UPS_SET / 60;
        int seconds = currentUpdates / Game.UPS_SET % 60;

        if(minutes < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(minutes);
        stringBuilder.append(":");

        if(seconds < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(seconds);
        return stringBuilder.toString();
    }

    public int getCurrentUpdate() {
        return currentUpdates;
    }
}
