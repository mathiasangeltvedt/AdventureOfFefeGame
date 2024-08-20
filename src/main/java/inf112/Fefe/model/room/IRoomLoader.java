package inf112.Fefe.model.room;

import inf112.util.Pair;

public interface IRoomLoader {

    /**
     * Call to load a level
     * 
     * @param levelNum the number of the level that should be loaded
     * @return the level that is loaded
     */
    public Room loadLevel(Pair<Integer, Integer> roomPos);

    /**
     * 
     * saves the x and y positions of collected coins so when the player re-enters
     * the room the coins will not reappear.
     * 
     * @param roomPos
     * @param xPos
     * @param yPos
     */
    public void addCollected(Pair<Integer, Integer> roomPos, Integer xPos, Integer yPos);
}
