package inf112.Fefe.model.player;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import inf112.Fefe.controller.DashDir;
import inf112.Fefe.controller.Key;

public interface ModdablePlayer {

    /**
     * This method is called to update the game.
     * If the player is dead -> the player respawns
     * If the player is on the ground or not
     * If the player is mid-dash
     * The gravitation of the player updates
     * In addition the method that moves the player depending on keys pressed is
     * called
     */
    public void tick();

    /**
     * This method is used for when the player presses the climbingbutton
     * If @param canClimb is true and @param climbJumpTimer <= 0 it will set the
     * horizontal velocity to 0
     * and the gravitation will also be set to 0 so the player won't be pulled to
     * the ground
     * 
     * @param isClimbing is also set to true
     * 
     * @return a boolean depending on whether the player is climbing or not
     */
    public boolean climb();

    /**
     * Call to set the gravity of the player to 1 as long as the player is not
     * climbing or dashing
     */
    public void cancelClimb();

    /**
     * This method is called when the player presses space to jump
     * 
     * @param resetJumpBuffer is a boolean which says if we want to reset the
     *                        jumpBuffer or not
     * 
     * @return boolean depending on whether the player will jump or not
     */
    public boolean jump(boolean resetJumpBuffer);

    /**
     * This method is called when the player presses the button to dash
     * 
     * @param dir is the direction the player is going to dash towards to
     */
    public boolean dash(DashDir dir);

    /**
     * This method is used to update verKey (A, D or None) and horKey (W, S or None)
     * 
     * @param verKey is what the player is currently pressing corresponding to the
     *               horizontal movement
     * @param horKey is what the player is currently pressing corresponding to the
     *               vertical movement
     */
    public void setMovementKeys(Key verKey, Key horKey);

    /**
     * This method is used whenever the player dies, to respawn the player on its
     * initial position depending on which room/level the player is in
     * After respawning the player, we update @param isDead to false
     */
    public void respawn();

    /**
     * Call to set the spawn position of the player
     * 
     * @param spawnPos the position it should be set to
     */
    public void setSpawnPos(Vector2 spawnPos);

    /**
     * Call to retrieve the Body of the player
     * 
     * @return a Body object
     */
    public Body getBody();

    /**
     * Call to retrieve whether the player is dead or not
     * 
     * @return a boolean depending on whether the player is dead or not
     */
    public boolean getIsDead();

    /**
     * Call to retrieve the dash values for the player
     * 
     * @return a new DashValues object with the dash values for the player
     */
    public DashValues getDashValues();

    /**
     * Call to retrieve the current tick for the dash
     * 
     * @return an integer which is the current dashTick for the player
     */
    public int getDashTick();

    /**
     * Call to retrieve the Texture of the player in its current state
     * 
     * @return return the texture of the player depending on the current state of
     *         the player
     */
    public TextureRegion getSprite();

    /**
     * Call to initiate sprites for the player
     * 
     * @param coin is the amount of coins the player has which sets the color of the
     *             sprite
     */
    public void initSprites(int coin);
    /**
     * Call to return the current direction of dash
     * @return
     */
    public DashDir getDashDir();
}
