package inf112.Fefe.model.tiles;

import com.badlogic.gdx.physics.box2d.World;

/**
 * This class returns tiles based on output from the tile parser
 */

public class TileFactory {
    private World world;
    private TilesetParser parser = new TilesetParser();

    /**
     * This constructor is used to create a TileFactory object to create tiles for
     * the map
     * 
     * @param world is the world in which you want to create the tiles
     */
    public TileFactory(World world) {
        this.world = world;
    }

    /**
     * This method is used create a new tile of a specific type on a specific cell
     * in the map
     * 
     * @param type is the TileType you want to create
     * @param row  is the row in which you want to create the cell
     * @param col  is the column in which you want to create the cell
     * @return a new Tile of the chosen TileType in the chosen row and col
     */
    public ITile getNewTile(TileType type, int row, int col) {
        try {
            ITile tile = type.instanciate(world, row, col);
            tile.initTexture(parser);
            return tile;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid tile type");
        }
    }
}
