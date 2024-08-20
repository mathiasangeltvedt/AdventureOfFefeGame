package inf112.Fefe.model.room;

import com.badlogic.gdx.math.Vector2;

import inf112.Fefe.model.tiles.ITile;
import inf112.util.grid.Grid;

/**
 * This class represents a Room in the game
 */
public class Room extends Grid<ITile> implements IRoom {

    private Vector2 spawnPos;

    /**
     * This is a constructor for a Room in the game
     * 
     * @param rows is the amount of rows/horizontal cells in the room
     * @param cols is the amount of cols/vertical cells in the room
     */
    public Room(int rows, int cols) {
        super(rows, cols);
    }

    @Override
    public Vector2 getSpawnPos() {
        return spawnPos;
    }

    @Override
    public void setSpawnPos(Vector2 spawnPos) {
        this.spawnPos = spawnPos;
    }
}
