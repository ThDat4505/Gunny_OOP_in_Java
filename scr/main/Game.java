package main;

import java.awt.Component;

public class Game {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;

    public final static int TILE_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.75f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILE_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

    }

    public void windowFocusLost() {
    }
}
