package inf112.Fefe.model.player.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class represents one Sprite element of an animation for the character
 */
public class SpriteElem {
    private int cycle, tick;
    private TextureRegion sprite;

    /**
     * This is the constructor for the Sprite element
     * 
     * @param cycle  is an integer which is how long the current sprite element
     *               should be in the cycle (in other words how long this current
     *               Texture should be visible on screen)
     * @param sprite is the actual Texture of the sprite element
     */
    public SpriteElem(int cycle, TextureRegion sprite) {
        this.cycle = cycle;
        tick = cycle;
        this.sprite = sprite;
    }

    /**
     * This method is used to retrieve the Sprite element
     * 
     * @return the Texture of the Sprite element
     */
    public TextureRegion getSprite() {
        return sprite;
    }

    /**
     * This method is used to retrieve the amount of time the sprite element has
     * left in the cycle
     * 
     * @return an integer which is the amount of time left in the cycle
     */
    public int getTick() {
        return tick;
    }

    /**
     * This method is used to update the remaining time this texture has in it's
     * current cycle.
     * If the tick is 0, we want to reset it to it's initial state.
     * 
     * @return a boolean depending on if it is still in cycle or not (as long as it
     *         is greater than 0)
     */
    public boolean tick() {
        if (--tick == 0) {
            tick = cycle;
            return true;
        }
        return false;
    }
}
