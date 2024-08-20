package inf112.Fefe.model.tiles.powerups;

import com.badlogic.gdx.physics.box2d.World;

import inf112.Fefe.model.contactListeners.GameSensors;
import inf112.Fefe.model.tiles.Tile;
import inf112.Fefe.model.tiles.Type;

/**
 * This class represents the Double Dash powerup which lets the player dash
 * twice
 */
public class DoubleDash extends Tile {

    public DoubleDash(World world, int row, int col) {
        super(world, row, col, 1, 5, Type.POWERUP, true);
        fixture.setUserData(GameSensors.DOUBLE_DASH.sensor);
    }
}
