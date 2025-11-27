package ui;

import main.Game;
import gamestates.Time;
import gamestates.Timer;


import java.awt.*;

public class Text {
    private String text;
    private Timer timer;

    private int x;
    private int y;

    private int fontSize;
    private int fontStyle;
    private String fontFamily;
    private Color color;

    private boolean dropShadow;
    private int dropShadowOffset;
    private Color shadowColor;

    private Font font;

    public Text(String text, Timer timer, int x, int y) {
        this.text = text;
        this.timer = timer;
        this.x = x;
        this.y = y;

        this.fontSize = (int) (20 * Game.SCALE);
        this.fontStyle = Font.BOLD;
        this.fontFamily = "ARCADECLASSIC";
        this.color = Color.LIGHT_GRAY;

        this.dropShadow = true;
        this.dropShadowOffset = (int) (1 * Game.SCALE);
        this.shadowColor = new Color(140,140, 140);
    }

    public Text(String text, Time time, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;

        this.fontSize = (int) (20 * Game.SCALE);
        this.fontStyle = Font.BOLD;
        this.fontFamily = "ARCADECLASSIC";
        this.color = Color.LIGHT_GRAY;

        this.dropShadow = true;
        this.dropShadowOffset = (int) (1 * Game.SCALE);
        this.shadowColor = new Color(140,140, 140);
    }

    public Text(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;

        this.fontSize = (int) (20 * Game.SCALE);
        this.fontStyle = Font.BOLD;
        this.fontFamily = "ARCADECLASSIC";
        this.color = Color.LIGHT_GRAY;

        this.dropShadow = true;
        this.dropShadowOffset = (int) (1 * Game.SCALE);
        this.shadowColor = new Color(140,140, 140);
    }

    public Text(String text, int x, int y, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;

        this.fontSize = (int) (20 * Game.SCALE);
        this.fontStyle = Font.BOLD;
        this.fontFamily = "ARCADECLASSIC";
        this.color = color;

        this.dropShadow = true;
        this.dropShadowOffset = (int) (1 * Game.SCALE);
        this.shadowColor = new Color(140,140, 140);
    }


    public void draw(Graphics g) {
        font = new Font(fontFamily, fontStyle, fontSize);
        g.setFont(font);
        g.setColor(shadowColor);
        g.drawString(text, x + dropShadowOffset, y + dropShadowOffset);

        g.setColor(color);
        g.drawString(text, x, y);
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }



}
