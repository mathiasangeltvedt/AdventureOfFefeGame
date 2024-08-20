package inf112.Fefe.model.player;

import com.badlogic.gdx.physics.box2d.Body;

import inf112.Fefe.model.contactListeners.ClimbDir;

public interface ContactablePlayer {

    /**
     * This method is used to update whether the player can climb or not
     * 
     * @param canClimb  is a boolean which says if the player can climb or not
     * @param direction is the directin the player will climb in
     */
    public void setCanClimb(boolean canClimb, ClimbDir direction);

    /**
     * This method is called on when the player is either outside range to be able
     * to climb, or the player
     * releases the climbingbutton
     * If @param dashTick is less or equal to 0 the gravitation for the player is
     * set to standard
     * If @param isClimbing is true, it will be set to false, and the gravitation of
     * the player is set to standard
     */
    public void cancelClimb();

    /**
     * This method is used to update whether the player is on the ground or not.
     * If the player is on the ground and @param jumpBuffer is greater than 0, the
     * jump method is called
     * 
     * @param isGrounded is a boolean which whether the player is on the ground or
     *                   not
     */
    public void setIsGrounded(boolean isGrounded);

    /**
     * Call to set {@code canDash}
     * 
     * @param canDash whether the player can dash
     */
    public void setCanDash(boolean canDash);

    /**
     * Call to set {@code isDead}
     * 
     * @param isDead boolean of whether player is dead or not
     */
    public void setIsDead(boolean isDead);

    /**
     * Call to retrieve the Body of the player
     * 
     * @return a Body object
     */
    public Body getBody();

}
