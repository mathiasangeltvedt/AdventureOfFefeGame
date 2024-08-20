package inf112.Fefe.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import inf112.Fefe.model.GameState;
import inf112.Fefe.model.player.DashValues;
import inf112.Fefe.model.tiles.ITile;
import inf112.util.Pair;

/**
 * This class is used to make methods in the model available for the view to
 * draw the game
 */
public interface ViewableGameModel {

    /**
     * Is used to retreive the player's current position
     * 
     * @return the position of the player in form of a Vector2 object
     */
    public Vector2 getPlayerPos();

    /**
     * Is used to retreive the sprite of the player, which means the different
     * models of the player's animation
     * 
     * @return a TextureRegion with the drawing of the player
     */
    public TextureRegion getPlayerSprite();

    /**
     * Is called to initiate the player and the sprite of the player so the player
     * is drawn correctly and in the correct position.
     */
    public void initPlayer();

    /**
     * Is called on to update the game, let the map update, let the player's
     * position update etc.
     */
    public void tick();

    /**
     * Is used to retreive the different static bodies of the game so that they can
     * be drawn to the game-screen
     * 
     * @return and iterable which contains the different static bodies on the map
     */
    public Iterable<Body> getStaticBodies();

    /**
     * Is used to retreive the tiles on the board
     * 
     * @return an iterable containing the tiles on the board
     */
    public Iterable<ITile> getTiles();

    /**
     * Is used to set the gameState of the game when it is supposed to change, for
     * instance when the player starts the game from the menus, pauses the game etc.
     * 
     * @param gameState is the gamestate we want to set the game to
     */
    public void setGameState(GameState gameState);

    /**
     * Is used to get the current gamestate of the game
     * 
     * @return the current gameState of the game
     */
    public GameState getGameState();

    /**
     * Is used to retreive the amount of restarts the player has done
     * 
     * @return the amount of restarts
     */
    public int restartCounter();

    /**
     * Is used to check if the player is currently dashing or not
     * 
     * @return a boolean which tells if the player is currently dashing or not
     */
    public boolean isDashing();

    /**
     * This is used to calculate where we want to draw the dust from the player's
     * dashing
     * 
     * @return a Vector3-object with the position of the dust
     */
    public DashValues pixieDustTransform();

    /**
     * This method is used to initiate the game, and is also used when we want to
     * restart the game
     */
    public void initGame();

    /**
     * This method is used to check if debugMode is active or not
     * 
     * @return a boolean representing if debugMode is active or not
     */
    public boolean isDebug();

    /**
     * This method is used to retrieve the amount of coins the player has collected
     * 
     * @return a integer representing the amount of coins the player has collected
     */
    public int coinCounter();

    /**
     * This method is used to check if the player is standing next to a sign to get
     * info about the game
     *
     * @return an integer depending on which level he wants the info on
     */
    public int getInfo();

    /**
     * Call this method to retrieve the current room-number the player is in
     * 
     * @return a Pair with Integers which is the row and col number of the current
     *         level from map.png
     */
    public Pair<Integer, Integer> getRoomNum();
}
