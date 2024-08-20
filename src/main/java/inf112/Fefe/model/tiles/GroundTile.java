package inf112.Fefe.model.tiles;

import com.badlogic.gdx.physics.box2d.World;

/**
 * 
 * 
 * This class makes ground tiles for the game.
 */

public class GroundTile extends Tile {

    /**
     * This constructor is used to create a new GroundTile object
     * 
     * @param world is the world in which you want to create the tile in
     * @param row   is the certain row in the world you want to create the tile on
     * @param col   is the certain column in the world you want to create the tile
     *              on
     */
    public GroundTile(World world, int row, int col) {
        super(world, row, col, 0, 0);
    }
}
