package objects;

public class WaterTop extends GameObject {
    public WaterTop(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitbox(32, 32);
        xDrawOffset = 0;
        yDrawOffset = 0;
        hitbox.y += yDrawOffset;
    }

    public void update() {
        updateAnimationTick();
    }
}

