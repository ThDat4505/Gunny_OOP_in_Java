package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.MagicBean;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import objects.Weapon;
import physic.Turn;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import ui.Text;
import utilz.LoadSave;

import static database.JDBCLevelInfo.getInstruction;
import static database.JDBCRanking.saveResult;
import static physic.Turn.*;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods {
    private Time time;
    private Timer timer;
    private Text gameTurnCount;
    private Text gamePower;
    private Text gameAngle;
    private Text gameScore;
    private Text gameInstruction;
    private Player player;
    private MagicBean magicBean;
    private Weapon weapon;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private Turn turn = PLAYER;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;
    private int turnCount;

    private int score = 0;

    private int angle;
    private int maxAngle = 90;
    private int rotateX;
    private int rotateY;
    private int lengthLine = (int) (20 * Game.SCALE);


    private int power;
    private int maxPower = 100;
    private int powerDir = 1;

    private int powerBarWidth = 10 * Game.TILES_SIZE;
    private int powerBar = 0;
    private int powerBarHeight = (Game.TILES_SIZE / 2);


    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();

    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean playerDying;


    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {

        levelManager.loadNextLevel();
        gameInstruction = new Text("Instruction: "+ getInstruction(levelManager.getLvlIndex() + 1), 8 * Game.TILES_SIZE, 1 * Game.TILES_SIZE, new Color(255, 215, 0), 10);
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        magicBean.setSpawn(levelManager.getCurrentLevel().getMagicBeanSpawn());
        resetAll();
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        time = new Time();
        timer = new Timer(10, this);
        gamePower = new Text("", (int) (Game.GAME_WIDTH/2 - 1.5 * Game.TILES_SIZE), (int) (11.5 * Game.TILES_SIZE));
        gameAngle = new Text("", (int) (Game.GAME_WIDTH/2 + 1.5 * Game.TILES_SIZE), (int) (11.5 * Game.TILES_SIZE));
        gameScore = new Text("", (int) (Game.GAME_WIDTH/2 + 5 * Game.TILES_SIZE), (int) (11.5 * Game.TILES_SIZE));
        gameTurnCount = new Text("", (int) (Game.GAME_WIDTH/2 + 9 * Game.TILES_SIZE), (int) (11.5 * Game.TILES_SIZE));
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        magicBean = new MagicBean(600, 200, (int) (40 * Game.SCALE), (int) (40 * Game.SCALE), this);
        magicBean.loadLvlData((levelManager.getCurrentLevel().getLevelData()));
        magicBean.setSpawn(levelManager.getCurrentLevel().getMagicBeanSpawn());

        weapon = new Weapon(player);

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);

        gameInstruction = new Text("Instruction: "+ getInstruction(levelManager.getLvlIndex() + 1), 8 * Game.TILES_SIZE, 1 * Game.TILES_SIZE, new Color(255, 215, 0), 10);

        angle = 30;
        power = 0;
        turnCount = 1;
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else {
            gameAngle.setText("Angle: " + Integer.toString(angle));
            updateRotateCoordinate();
            gamePower.setText("Power: " + Integer.toString(power));
            updatePowerBar();
            gameScore.setText("Score: " + Integer.toString(score));
            gameTurnCount.setText("Turn: " + Integer.toString(turnCount));
            time.update();
            timer.update();
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player, levelManager.getCurrentLevel().getCrabs(), angle, power, magicBean, this);
            player.update();
            magicBean.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player, magicBean);
            checkCloseToBorder();
        }
    }

    private void updateRotateCoordinate() {
        rotateX = (int) (player.getHitbox().x + 14 * Game.SCALE + lengthLine * Math.cos(Math.toRadians(angle)));
        rotateY = (int) (player.getHitbox().y + 14 * Game.SCALE - lengthLine * Math.sin(Math.toRadians(angle)));
        if(player.getFlipW() == -1) {
            rotateX = (int) (player.getHitbox().x + 7 * Game.SCALE - lengthLine * Math.cos(Math.toRadians(angle)));
            rotateY = (int) (player.getHitbox().y + 14 * Game.SCALE - lengthLine * Math.sin(Math.toRadians(angle)));
        }
    }

    private void updatePowerBar() {
        powerBar = (int) (power /  (float) maxPower * powerBarWidth);
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        weapon.render(g, xLvlOffset);
        player.render(g, xLvlOffset);
        magicBean.draw(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
//        time.draw(g);

        gameScore.draw(g);
        gameAngle.draw(g);
        gameTurnCount.draw(g);

        g.setColor(Color.BLUE);
        if (player.getFlipW() == -1)
            g.drawLine(rotateX, rotateY, (int) (player.getHitbox().x + 7 * Game.SCALE), (int) (player.getHitbox().y + 14 * Game.SCALE));
        else if (player.getFlipW() == 1)
            g.drawLine(rotateX, rotateY, (int) (player.getHitbox().x + 14 * Game.SCALE), (int) (player.getHitbox().y + 14 * Game.SCALE));

        gamePower.draw(g);
        g.setColor(Color.yellow);
        g.drawRect(1 * Game.TILES_SIZE, (int) (11.125 * Game.TILES_SIZE), powerBarWidth, powerBarHeight);
        g.fillRect(1 * Game.TILES_SIZE, (int) (11.125 * Game.TILES_SIZE), powerBar, powerBarHeight);

        timer.draw(g);
        gameInstruction.draw(g);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted)
            levelCompletedOverlay.draw(g);
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

        for (int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        turn = PLAYER;
        turnCount = 1;
        angle = 30;
        resetPower();
        resetScore();
        timer.resetCurrentUpdates();
        player.resetAll();
        magicBean.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    public void checkWaterTouched(Player p) {
        objectManager.checkWaterTouched(p);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver && turn == PLAYER) {
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
            else if (e.getButton() == MouseEvent.BUTTON3)
                player.powerAttack();
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else if (turn == PLAYER)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    power += powerDir;
                    if(power == 100)
                        powerDir = -1;
                    if(power == 0)
                        powerDir = 1;
                    break;
                case KeyEvent.VK_UP:
                    angle++;
                    if(angle > 90)
                        angle = 90;
                    break;
                case KeyEvent.VK_DOWN:
                    angle--;
                    if(angle < 30)
                        angle = 30;
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && turn == PLAYER)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setShoot(true);
//                    turnCount++;
                    player.resetDirBooleans();
                    System.out.println(angle);
                    System.out.println(power);
                    System.out.println("THROWING");
                    turn = THROWING;
                    timer.resetCurrentUpdates();
                    break;
            }

    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        } else
            gameOverOverlay.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        } else
            gameOverOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        } else
            gameOverOverlay.mouseMoved(e);
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if (levelCompleted)
            game.getAudioPlayer().lvlCompleted();
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;

    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
        if (turn == ENEMY) {
            resetPower();
            enemyManager.startEnemyTurn();
        }
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void increaseTurnCount() {
        turnCount++;
    }

    public Time getTimer() {
        return timer;
    }

    public void resetPower() {
        power = 0;
        powerDir = 1;
    }

    public void changeScore(int value) {
        score += value;
    }

    public int getScore(int score) {
        return score;
    }

    public void resetScore() {
        score = 0;
    }

    public void endLevelAndSave(boolean won) {
        saveResult((levelManager.getLvlIndex() + 1), score, won, turnCount);
    }
}