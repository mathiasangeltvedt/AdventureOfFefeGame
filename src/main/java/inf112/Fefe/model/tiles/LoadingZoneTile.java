package inf112.Fefe.model.tiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import inf112.Fefe.model.contactListeners.GameSensors;

/**
 * This class represents the spikes that the player can die on.
 */

public class LoadingZoneTile extends Tile {

    /**
     * This constructor is used to create a new LoadingZone tile which is the tile
     * that lets the player move between rooms
     * 
     * @param world is the world where we want to add the tile
     * @param row   is the row-number of the cell where we want the tile
     * @param col   is the col-number of the cell where we want the tile
     */
    public LoadingZoneTile(World world, int row, int col) {
        super(world, row, col, 3, 2);
    }

    @Override
    protected void initBody(World world) {
        BodyDef bd = new BodyDef();
        bd.fixedRotation = true;
        bd.type = BodyType.StaticBody;
        bd.position.set(col + DIMENSION / 2, row + DIMENSION / 2);
        body = world.createBody(bd);

        ChainShape shape = new ChainShape();
        Vector2[] arr = new Vector2[] {
                new Vector2(-DIMENSION / 2, DIMENSION / 2),
                new Vector2(DIMENSION / 2, DIMENSION / 2),
                new Vector2(DIMENSION / 2, DIMENSION / 4),
                new Vector2(-DIMENSION / 2, DIMENSION / 4)
        };
        shape.createLoop(arr);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.isSensor = true;
        Fixture f = body.createFixture(fixtureDef);
        f.setUserData(GameSensors.LOADING_ZONE.sensor);
        shape.dispose();
    }
}
