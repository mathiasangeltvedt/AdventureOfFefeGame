package inf112.Fefe.controller;

import inf112.Fefe.model.GameState;

public interface ControllableGameModel {

    /**
     * Is used to update what keys the player is currently pressing, so that the
     * game knows to move the player or not
     * 
     * @param horKey are the keys that update the player's horizontal value ('A' and
     *               'D')
     * @param verKey are the keys that update the player's vertical value ('W' and
     *               'S')
     */
    public void setPlayerKeys(Key horKey, Key verKey);

    /**
     * Is called when the player presses the space button to jump
     */
    public void jump();

    /**
     * Is called when the player presses the button to make the player dash
     * 
     * @param dir is the direction which the player is going to dash in
     */
    public void dash(DashDir dir);

    /**
     * Is called when the player is going to climb the walls
     * The player will only start climbing if the player is in a position where it
     * is actually possible to climb, and the climb button is pressed
     */
    public void climb();

    /**
     * Is called when the player lets go of the climbing button or when the
     * character is at a position where it is no longer possible to climb
     */
    public void cancelClimb();

    /**
     * Is used to set the gameState of the game, for instance when the player wants
     * to start the game from the menu's, pause the game etc.
     * 
     * @param gameState is the gameState we want to set the game to
     */
    public void setGameState(GameState gameState);

    /**
     * Is used to retreive the current GameState of the game
     * 
     * @return the current GameState of the game
     */
    public GameState getGameState();

    /**
     * This method is used to set the debugMode to either true or false depending on
     * the case
     * 
     * @param debug is a boolean which represents if debugMode is going to be set to
     *              true or false
     */
    public void setDebug(boolean debug);

    /**
     * This method is used to check if debugMode is active or not
     * 
     * @return a boolean depending on if debugMode is active or not
     */
    public boolean isDebug();
}
