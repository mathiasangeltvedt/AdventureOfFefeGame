package inf112.Fefe.model.tiles.powerups;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import inf112.Fefe.model.contactListeners.GameSensors;
import inf112.Fefe.model.player.animation.Animation;
import inf112.Fefe.model.tiles.Type;
import inf112.Fefe.model.tiles.Tile;
import inf112.Fefe.model.tiles.TilesetParser;
/**
 * This class makes the coins of the game.
 * 
 */
public class Collectible extends Tile {
    private Animation coins = new Animation();

    public Collectible(World world, int row, int col) {
        super(world, row, col, 1, 5, Type.COLLECTIBLE, true);
        TilesetParser parser = new TilesetParser();
        for (int i = 2; i < 5; i++) {
            coins.addSprite(parser.parseTile(i, 9), 15);
        }
        fixture.setUserData(GameSensors.COLLECTIBLE.sensor);
    }

    @Override
    public TextureRegion getTexture() {
        return coins.getSprite(true);
    }

}
