package inf112.Fefe.model.contactListeners;

import com.badlogic.gdx.math.Vector2;

public interface ChangeableGameModel {
    /**
     * This method is used to notify the model whenever a loading zone is hit.
     * 
     * @param hitPos the position of the loading zone tile that was hit
     */
    public void loadingZoneHit(Vector2 hitPos);

    /**
     * This method is used to update the map whenever the player hits a powerup
     * It goes through all the powerups on the map, and finds the closest powerup
     * to the player as this is the one we want to remove considering this is the
     * powerup which the player most likely hit
     */
    public void powerupHit();

    /**
     * This method is used to update the map whenever the player hits a collectible
     * It goes through all the collectibles on the map, and finds the closest
     * collectible
     * to the player as this is the one we want to remove considering this is the
     * collectible which the player most likely hit
     */
    public void collectibleHit();

    /**
     * This method is used whenever the player hits a wall/roof with high velocity
     */
    public void playerBump();

    /**
     * This method is used to update the boolean value of whether the player is
     * trying to receive info about the game or not
     * 
     * @param wantsInfo is a boolean which says if the player wants info or not
     */
    public void wantsInfo(boolean wantsInfo);
}
