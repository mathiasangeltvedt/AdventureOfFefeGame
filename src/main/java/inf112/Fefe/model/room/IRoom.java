package inf112.Fefe.model.room;

import com.badlogic.gdx.math.Vector2;

public interface IRoom {

    /**
     * This method is used to retrieve the spawn position of the player for this
     * certain room
     * 
     * @return a Vector2 object which contains the x and y value of the player's
     *         spawn position
     */
    public Vector2 getSpawnPos();

    /**
     * This method is used to set the player's spawn position for the room the
     * player is in
     * 
     * @param spawnPos is a Vector2 object containing the x and y value for the
     *                 spawn position
     */
    public void setSpawnPos(Vector2 spawnPos);
}
