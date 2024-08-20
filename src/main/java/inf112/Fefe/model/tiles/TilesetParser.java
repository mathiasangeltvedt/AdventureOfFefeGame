package inf112.Fefe.model.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Maps the picture of tiles to the right tiles
 */

public class TilesetParser {
    private TextureRegion[][] tiles;
    private static final int TILE_DIMENSION = 16;

    /**
     * This constructor is used to create a TilesetParser object to be able to
     * retreive the texture of the different tiles
     */
    public TilesetParser() {
        Texture tileset = new Texture(Gdx.files.internal("images/game/tileset/tileset.png"));
        tiles = TextureRegion.split(tileset, TILE_DIMENSION, TILE_DIMENSION);
    }

    /**
     * This method is used to parse a tile from the tileset
     * 
     * @param offsetX is the X value fo the offset in which we want to retreive the
     *                texture from
     * @param offsetY is the Y value fo the offset in which we want to retreive the
     *                texture from
     * @return
     */
    public TextureRegion parseTile(int offsetX, int offsetY) {
        return tiles[offsetY][offsetX];
    }

}
