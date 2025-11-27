package objects;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import entities.MagicBean;
import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import static physic.Turn.*;
import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.*;
import static utilz.HelpMethods.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs, waterTopImgs;
    private BufferedImage waterTopImg, waterBottomImg, spikeImg, cannonBallImg, playerProjectileImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<WaterBottom> waterBottoms;
    private ArrayList<WaterTop> waterTops;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<PlayerProjectile> playerProjectiles = new ArrayList<>();

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void checkSpikesTouched(Player p) {
        for (Spike s : spikes)
            if (s.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }

    public void checkWaterTouched(Player p) {
        for (WaterTop wt : waterTops)
            if (wt.getHitbox().intersects(p.getHitbox()))
                p.kill();
        for (WaterBottom wb : waterBottoms)
            if (wb.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitbox().intersects(attackbox)) {
                    gc.setAnimation(true);
                    int type = 0;
                    if (gc.getObjType() == BARREL)
                        type = 1;
                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 2), type));
                    return;
                }
            }
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        waterBottoms = newLevel.getWaterBottoms();
        waterTops = new ArrayList<>(newLevel.getWaterTops());
        cannons = newLevel.getCannons();
        projectiles.clear();
        playerProjectiles.clear();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for (int j = 0; j < potionImgs.length; j++)
            for (int i = 0; i < potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        waterBottomImg = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);

        waterTopImgs = new BufferedImage[4];
        BufferedImage waterTopImg = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);

        for (int i = 0; i < waterTopImgs.length; i++)
            waterTopImgs[i] = waterTopImg.getSubimage(i * 32, 0, 32, 32);

        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

        for (int i = 0; i < cannonImgs.length; i++)
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
        playerProjectileImg = LoadSave.GetSpriteAtlas(LoadSave.BOOMERANG_ATLAS);

    }

    public void update(int[][] lvlData, Player player, ArrayList<Crabby> crabbies, int angle, int power, MagicBean magicBean) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();

        for (WaterTop wt : waterTops)
            wt.update();

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
        updatePlayerProjectiles(lvlData, player, crabbies, angle, power, magicBean);
    }

    private void updatePlayerProjectiles(int[][] lvlData, Player player, ArrayList<Crabby> crabbies, int angle, int power, MagicBean magicBean) {
        if(player.isShoot() && player.getAniTick() == 0) {
            int dir = 1;
            if (player.getFlipW() == -1)
                dir = -1;

            playerProjectiles.add(new PlayerProjectile((int) (player.getHitbox().x), (int) (player.getHitbox().y), dir, angle, power));
        }

        for (PlayerProjectile pp : playerProjectiles)
            for(Crabby c : crabbies)
                if (pp.isActive()) {
                    pp.updatePos();
                    player.setShoot(false);
                    if (c.getCurrentHealth() > 0)
                        if (pp.getHitbox().intersects(c.getHitbox()) && c.isActive()) {
                            pp.setActive(false);
                            pp.resetGravityTime();
                            Rectangle2D.Float explodeHitBox = new Rectangle2D.Float(pp.getHitbox().x - 20 * Game.SCALE, pp.getHitbox().y - 20 * Game.SCALE, 40 * Game.SCALE, 40 * Game.SCALE);

                            if(explodeHitBox.intersects(player.getHitbox())) {
                                playing.changeScore(20);
                                player.changeHealth(20);
                            }

                            if(explodeHitBox.intersects(magicBean.getHitbox())) {
                                playing.changeScore(20);
                                magicBean.changeHealth(20);
                            }

                            for(Crabby c2 : crabbies) {
                                if (explodeHitBox.intersects(c2.getHitbox()) && c2.isActive()) {
                                    playing.changeScore(25);
                                    c2.hurt(25);
                                }
                            }

                            System.out.println("ENEMY");
                            playing.setTurn(ENEMY);
                        } else {
                            if (IsPlayerProjectileHittingLevel(pp, lvlData)) {
                                pp.setActive(false);
                                pp.resetGravityTime();
                                Rectangle2D.Float explodeHitBox = new Rectangle2D.Float(pp.getHitbox().x - 20 * Game.SCALE, pp.getHitbox().y - 20 * Game.SCALE, 40 * Game.SCALE, 40 * Game.SCALE);

                                if(explodeHitBox.intersects(player.getHitbox())) {
                                    playing.changeScore(20);
                                    player.changeHealth(20);
                                }

                                if(explodeHitBox.intersects(magicBean.getHitbox())) {
                                    playing.changeScore(20);
                                    magicBean.changeHealth(20);
                                }

                                for (Crabby c2 : crabbies) {
                                    if (explodeHitBox.intersects(c2.getHitbox()) && c2.isActive()) {
                                        playing.changeScore(25);
                                        c2.hurt(25);
                                    }
                                }
                                System.out.println("ENEMY");
                                playing.setTurn(ENEMY);
                            }
                        }
                }
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-25);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x)
                return true;

        } else if (c.getHitbox().x < player.getHitbox().x)
            return true;
        return false;
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInfrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
                                c.setAnimation(true);

            c.update();
            if (c.getAniIndex() == 4 && c.getAniTick() == 0)
                shootCannon(c);
        }
    }

    private void shootCannon(Cannon c) {
        int dir = 1;
        if (c.getObjType() == CANNON_LEFT)
            dir = -1;

        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));

    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawWaterBottom(g, xLvlOffset);
        drawWaterTop(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawPlayerProjectiles(g,xLvlOffset);
    }

    private void drawPlayerProjectiles(Graphics g, int xLvlOffset) {
        for (PlayerProjectile pp : playerProjectiles)
            if (pp.isActive()) {
                g.drawImage(playerProjectileImg, (int) (pp.getHitbox().x - xLvlOffset), (int) (pp.getHitbox().y), (int) pp.getHitbox().width, (int) pp.getHitbox().height, null);
                g.setColor(Color.green);
                g.drawRect((int) (pp.getHitbox().x - xLvlOffset), (int) (pp.getHitbox().y), (int) pp.getHitbox().width, (int) pp.getHitbox().height);
            }
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
                g.setColor(Color.red);
                g.drawRect((int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
            }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }

            g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
        }

    }

    private void drawWaterTop(Graphics g, int xLvlOffset) {
        for (WaterTop wt : waterTops) {
            g.drawImage(waterTopImgs[wt.getAniIndex()], (int) (wt.getHitbox().x - xLvlOffset), (int) (wt.getHitbox().y - wt.yDrawOffset), WATER_WIDTH, WATER_HEIGHT, null);
        }

    }


    private void drawWaterBottom(Graphics g, int xLvlOffset) {
        for (WaterBottom wb : waterBottoms)
            g.drawImage(waterBottomImg, (int) (wb.getHitbox().x - xLvlOffset), (int) (wb.getHitbox().y - wb.getyDrawOffset()), WATER_WIDTH, WATER_HEIGHT, null);

    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : spikes)
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);

    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjType() == BARREL)
                    type = 1;
                g.drawImage(containerImgs[type][gc.getAniIndex()], (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int) (gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH,
                        CONTAINER_HEIGHT, null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT,
                        null);
            }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for (Potion p : potions)
            p.reset();
        for (GameContainer gc : containers)
            gc.reset();
        for (Cannon c : cannons)
            c.reset();
    }
}
