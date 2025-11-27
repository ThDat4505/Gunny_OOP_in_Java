package objects;

public class WaterBottom extends GameObject {
    public WaterBottom(int x, int y, int objType) {
        super(x, y, objType);

        initHitbox(32, 32);
        xDrawOffset = 0;
        yDrawOffset = 0;
        hitbox.y += yDrawOffset;
    }
}
