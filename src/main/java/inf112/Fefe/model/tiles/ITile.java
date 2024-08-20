package inf112.Fefe.model.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public interface ITile {

    /**
     * This method is used to retreive the body of the Tile
     * 
     * @return the body of the Tile
     */
    public Body getBody();

    /**
     * This method is used to initiate the textures of the different tiles so they
     * are retreivable when trying to create the view of the tiles
     * 
     * @param parser is the parser for the TileSet
     */
    public void initTexture(TilesetParser parser);

    /**
     * This method is used to get the texture of a tile
     * 
     * @return the texture of the tile
     */
    public TextureRegion getTexture();

    /**
     * Call to retrieve the type of the tile
     * 
     * @return a Type object which is the type of the tile
     */
    public Type getType();

}
