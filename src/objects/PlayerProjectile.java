package objects;

import main.Game;

import java.awt.geom.Rectangle2D;

public class PlayerProjectile {
    private Rectangle2D.Float hitbox;

    private int dir;
    private boolean active = true;

    private float vx, vy;

    private int angle = 45;
    private int power = 100;
    public float gravityTime = 0f;
    private final float gravity = 300f * Game.SCALE;

    public PlayerProjectile(float x, float y, int dir, int angle, int power) {
        this.dir = dir;

        int xOffset = (int) (5 * Game.SCALE);
        int yOffset = (int) (10 * Game.SCALE);

        float rad = (float)Math.toRadians(angle);

        float speed = power * 5f * Game.SCALE;

        vx = dir * (speed * (float)Math.cos(rad));
        vy = speed * (float)Math.sin(rad);



        if(dir == 1)
            xOffset = (int) (10 * Game.SCALE);
        hitbox = new Rectangle2D.Float( x + xOffset, y + yOffset, 20 * Game.SCALE, 20 * Game.SCALE);
    }

    public void updatePos() {
        float dt = 1f / Game.UPS_SET;

        gravityTime += dt;

        // Apply gravity (affects vertical velocity)
        vy -= gravity * dt;

        // Update positions
        hitbox.x += vx * dt;
        hitbox.y -= vy * dt;
    }


    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void resetGravityTime() {
        this.gravityTime = 0;
    }

}
