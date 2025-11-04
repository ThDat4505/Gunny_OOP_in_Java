package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static main.Game.*;

public class GamePanel extends JPanel {
    private Game game;

    public GamePanel(Game game) {
        this.game = game;

        setPanelSize();
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
