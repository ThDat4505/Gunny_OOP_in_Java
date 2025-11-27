package entities;

import main.Game;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static physic.Turn.ENEMY;
import static physic.Turn.PLAYER;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();
    private boolean[] isCrabbiesAction;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();

    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
        isCrabbiesAction = new boolean[crabbies.size()];
        System.out.println("Size of crabs: " + crabbies.size());
    }

    public void update(int[][] lvlData, Player player, MagicBean magicBean) {
        boolean isAnyActive = false;
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                c.update(lvlData, player, playing, magicBean);
                isAnyActive = true;
            }
        }

        if (playing.getTurn() == ENEMY) {
            for (int i = 0; i < crabbies.size(); i++) {
                Crabby c = crabbies.get(i);
                isCrabbiesAction[i] = c.isActive() && c.isAction;
            }
            if (isDoneActions(isCrabbiesAction)) {
                System.out.println(PLAYER);
                playing.setTurn(PLAYER);
            }
        }

        if ((!isAnyActive) || (magicBean.getCurrentHealth() >= 100))
            playing.setLevelCompleted(true);
    }

    public void startEnemyTurn() {
        playing.increaseTurnCount();
        for (int i = 0; i < crabbies.size(); i++) {
            Crabby c = crabbies.get(i);
            if (c.isActive()) {
                c.startAction();
            }
            isCrabbiesAction[i] = c.isActive() && c.isAction;
        }
    }

    private boolean isDoneActions(boolean[] isCrabbiesAction) {
        boolean done = true;
        for (boolean a : isCrabbiesAction)
            if (a) {
                done = false;
                break;
            }
        return done;
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    public void drawCrabsHealthBar(Graphics g, int xLvlOffset, Crabby c) {
        g.setColor(Color.red);
        g.fillRect((int) (c.getHitbox().x - xLvlOffset), (int) (c.getHitbox().y - 10 * Game.SCALE), c.getHealthWidth(), c.getHealthBarHeight());
        g.drawRect((int) (c.getHitbox().x - xLvlOffset), (int) (c.getHitbox().y - 10 * Game.SCALE), c.getHealthBarWidth(), c.getHealthBarHeight());
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for(Crabby c : crabbies)
            if(c.isActive()) {
                g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()], (int) (c.getHitbox().x) - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(), (int) (c.getHitbox().y) - CRABBY_DRAWOFFSET_Y, CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
//                c.drawHitbox(g, xLvlOffset);
//                c.drawAttackBox(g, xLvlOffset);

                drawCrabsHealthBar(g, xLvlOffset, c);

            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for(Crabby c : crabbies)
            if (c.getCurrentHealth() > 0)
                if(c.isActive())
                    if (attackBox.intersects(c.getHitbox())) {
                        c.hurt(10);
                        return;
                    }
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for(int j = 0; j < crabbyArr.length; j++)
            for( int i = 0; i < crabbyArr[j].length; i ++)
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
    }

    public void resetAllEnemies() {
        for(Crabby c : crabbies)
            c.resetEnemy();
    }
}
