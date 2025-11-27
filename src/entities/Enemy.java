package entities;

import main.Game;
import gamestates.Playing;

import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static utilz.Constants.Direction.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.*;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkSpeed = Game.TILES_SIZE;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;
    protected float hitboxTemp = x;
    protected boolean isAction = false;



    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = SCALE * 0.35f;
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData, Playing playing) {
        float xSpeed = 0;

        if (walkDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = +walkSpeed;
        }

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                if (Math.abs(hitbox.x - hitboxTemp) > 3 * Game.TILES_SIZE) {
                    isAction = false;
                    newState(IDLE);
                    return;
                }
                return;
            }

        changeWalkDir();
    }

    public void startAction() {
        hitboxTemp = hitbox.x;
        isAction = true;
        if(currentHealth > 0)
            newState(RUNNING);
    }


    protected void turnTowardsNearestCharacter(Player player, MagicBean magicBean) {
        if((int) Math.abs(magicBean.hitbox.x - hitbox.x) >= (int) Math.abs(player.hitbox.x - hitbox.x))
            turnTowardsPlayer(player);
        else
            turnTowardsMagicBean(magicBean);
    }

    protected void turnTowardsPlayer(Player player) {
        if(player.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected void turnTowardsMagicBean(MagicBean magicBean) {
        if(magicBean.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if(playerTileY == tileY)
            if(isPlayerInRange(player)) {
                if(IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }

        return false;
    }

    protected boolean canSeeMagicBean(int[][] lvlData, MagicBean magicBean) {
        int playerTileY = (int) (magicBean.getHitbox().y / Game.TILES_SIZE);
        if(playerTileY == tileY)
            if(isMagicBeanInRange(magicBean)) {
                if(IsSightClear(lvlData, hitbox, magicBean.hitbox, tileY))
                    return true;
            }

        return false;
    }

    private boolean isMagicBeanInRange(MagicBean magicBean) {
        int absValue = (int) Math.abs(magicBean.hitbox.x - hitbox.x);
        return absValue <= Game.GAME_WIDTH;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= Game.GAME_WIDTH;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    protected boolean isMagicBeanCloseForAttack(MagicBean magicBean) {
        int absValue = (int) Math.abs(magicBean.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState) {
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if(currentHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType));
        attackChecked = true;
    }

    protected void checkMagicBeanHit(Rectangle2D.Float attackBox, MagicBean magicBean) {
        if(attackBox.intersects(magicBean.hitbox))
            magicBean.changeHealth(-GetEnemyDmg(enemyType));
        attackChecked = true;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if(aniTick > ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                aniIndex = 0;

                switch(state) {
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        hitboxTemp = x;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
        isAction = false;
    }


    public boolean isActive() {
        return active;
    }
}
