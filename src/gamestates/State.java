package gamestates;

import main.Game;
import audio.AudioPlayer;
import ui.LevelsFrame;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;
    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public boolean isIn(MouseEvent e, LevelsFrame f) {
        return f.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }

    public void setGameState(Gamestate state) {
        switch(state) {
            case MENU, LEVELS -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
        }

        Gamestate.state = state;
    }
}
