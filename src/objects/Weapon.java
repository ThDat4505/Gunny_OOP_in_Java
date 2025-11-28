//EXTRA
package objects;

import entities.Player;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;

public class Weapon {
    private Player player;
    private BufferedImage img;
    private float xDrawOffset = 10 * SCALE;
    private float yDrawOffset = 3 * SCALE;

    public Weapon(Player player) {
        this.player = player;

    }

    public void render(Graphics g, int lvlOffset) {
        img = LoadSave.GetSpriteAtlas(LoadSave.BOOMERANG_ATLAS);

        g.drawImage(img, (int) (player.getHitbox().x - xDrawOffset - lvlOffset + (player.getFlipX()/1.5))  , (int) (player.getHitbox().y - yDrawOffset), (int) player.getHitbox().width * player.getFlipW(), (int) player.getHitbox().height, null);
    }
}
