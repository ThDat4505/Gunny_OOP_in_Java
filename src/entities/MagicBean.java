package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.HelpMethods.IsEntityOnFloor;

public class MagicBean extends Entity {
    private BufferedImage img;
    private int[][] lvlData;
    private float xDrawOffset = 7 * Game.SCALE;
    private float yDrawOffset = 3 * Game.SCALE;

    private int miniHealthWidth;
//    private boolean isActive = false;

    private Playing playing;

    private int tileY = 0;

    public MagicBean(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.maxHealth = 100;
        this.currentHealth = 40;
        loadImg();
        initHitbox(25, 35);
        tileY = (int) (hitbox.y / Game.TILES_SIZE);
    }

    private void loadImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.MAGIC_BEAN);
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    public void update() {
        updateHealthbar();

        if(currentHealth <= 0) {
            playing.setPlayerDying(true);
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            playing.setGameOver(true);
            playing.getGame().getAudioPlayer().stopSong();
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
        }

        return;
    }

    private void updateHealthbar() {
        miniHealthWidth = (int) ((currentHealth / (float) maxHealth) * (int) (width - 2 * xDrawOffset));
    }

    public void draw(Graphics g, int lvlOffset) {
        g.drawImage(img, (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
        drawHitbox(g, lvlOffset);
        drawUI(g, lvlOffset);
    }

    private void drawUI(Graphics g, int lvlOffset) {
        g.setColor(Color.green);
        g.fillRect((int) hitbox.x - lvlOffset, (int) (hitbox.y - 5 * Game.SCALE), miniHealthWidth, (int) (2 * Game.SCALE));
        g.drawRect((int) hitbox.x - lvlOffset, (int) (hitbox.y - 5 * Game.SCALE), (int) (width - 2 * xDrawOffset), (int) (2 * Game.SCALE));
    }

    public void changeHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0)
            currentHealth = 0;
        else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetAll() {
        inAir = false;
        airSpeed = 0f;
        currentHealth = 40;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

}
