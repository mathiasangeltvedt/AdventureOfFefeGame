package inf112.Fefe.model.player.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface IAnimation {

    /**
     * This method is used to add sprites to the list of sprites for the animation
     * 
     * @param sprite is the TextureRegion which is part of the animation
     * @param cycle  is the amount of time we want this specific sprite to be in
     *               cycle
     */
    public void addSprite(TextureRegion sprite, int cycle);

    /**
     * This method is used to retrieve the current sprite of the animation
     * 
     * @param shouldTick is a boolean value which says if the sprite is part of a
     *                   cycle or not
     * @return the Texture for the animation
     */
    public TextureRegion getSprite(boolean shouldTick);

    /**
     * This method is used to reset a sprite to it's original state whenever the
     * animation is cut off for some reason
     */
    public void restartSprite();

    /**
     * This method clears the linked list with animation
     * in case there is a need to change animation
     * is used when the player changes skin.
     */
    public void resetSprite();
}
