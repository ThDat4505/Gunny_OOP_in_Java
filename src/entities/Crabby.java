package entities;

import main.Game;
import gamestates.Playing;

import java.awt.geom.Rectangle2D;

import static physic.Turn.*;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy {

    //AttackBox ep16.4
    private int attackBoxOffsetX;
    private int healthBarWidth;
    private int healthBarHeight;
    private int healthWidth = healthBarWidth;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox();
        healthBarWidth = CRABBY_WIDTH - 2 * CRABBY_DRAWOFFSET_X;
        healthBarHeight = (int) (2 * Game.SCALE);
        healthWidth = healthBarWidth;

    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 30);
    }

    public void update(int[][] lvlData, Player player, Playing playing, MagicBean magicBean) {
        updateHealthBar();
        updateBehavior(lvlData, player, playing, magicBean);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

//    private void updateBehavior(int[][] lvlData, Player player, Playing playing, MagicBean magicBean) {
//        if(firstUpdate)
//            firstUpdateCheck(lvlData);
//        if(inAir) {
//            updateInAir(lvlData);
//        } else {
//                switch(state) {
//                    case IDLE:
//                        newState(RUNNING);
//                        break;
//                    case RUNNING:
//                        if (playing.getTurn() == ENEMY && isAction) {
//                            if(magicBean.getCurrentHealth() > 0) {
//                                turnTowardsMagicBean(magicBean);
//                                if (canSeeMagicBean(lvlData, magicBean)) {
//                                    if (isMagicBeanCloseForAttack(magicBean))
//                                        newState(ATTACK);
//                                    else
//                                        move(lvlData, playing);
//                                } else {
//                                    move(lvlData, playing);
//                                    break;
//                                }
//                            } else {
//                                turnTowardsPlayer(player);
//                                if (canSeePlayer(lvlData, player)) {
//                                    if (isPlayerCloseForAttack(player))
//                                        newState(ATTACK);
//                                    else
//                                        move(lvlData, playing);
//                                } else {
//                                    move(lvlData, playing);
//                                }
//                            }
//                        }
//                        break;
//                    case ATTACK:
//                        if (aniIndex == 0)
//                            attackChecked = false;
//
//                        if (aniIndex == 3 && !attackChecked) {
//                            checkMagicBeanHit(attackBox, magicBean);
//                            checkPlayerHit(attackBox, player);
//                            isAction = false;
//                        }
//                        break;
//                    case HIT:
//                        break;
//                }
//        }
//    }

    //old version
    private void updateBehavior(int[][] lvlData, Player player, Playing playing, MagicBean magicBean) {
        if(firstUpdate)
            firstUpdateCheck(lvlData);
        if(inAir) {
            updateInAir(lvlData);
        } else {
            switch(state) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (playing.getTurn() == ENEMY && isAction) {
                        turnTowardsNearestCharacter(player, magicBean);
                        if (canSeePlayer(lvlData, player) || canSeeMagicBean(lvlData, magicBean)) {
                            if (isPlayerCloseForAttack(player) || isMagicBeanCloseForAttack(magicBean))
                                newState(ATTACK);
                            else
                                move(lvlData, playing);
                        } else {
                            move(lvlData, playing);
                        }
                    }
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;

                    if (aniIndex == 3 && !attackChecked) {
                        checkMagicBeanHit(attackBox, magicBean);
                        checkPlayerHit(attackBox, player);
                        isAction = false;
                    }
                    break;
                case HIT:
                    break;
            }
        }
    }

    public int flipX() {
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

    public int getHealthBarWidth() {
        return healthBarWidth;
    }

    public int getHealthBarHeight() {
        return healthBarHeight;
    }

    public int getHealthWidth() {
        return healthWidth;
    }
}
