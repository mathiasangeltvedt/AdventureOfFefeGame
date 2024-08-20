package inf112.Fefe.model.tiles;

import com.badlogic.gdx.physics.box2d.World;

import inf112.Fefe.model.contactListeners.GameSensors;

public class InfoTile extends Tile {

    protected InfoTile(World world, int row, int col) {
        super(world, row, col, 5, 9, Type.TILE, true);
        fixture.setUserData(GameSensors.INFO.sensor);
    }

}
