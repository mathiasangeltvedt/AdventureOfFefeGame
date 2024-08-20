package inf112.Fefe.model.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import inf112.Fefe.model.contactListeners.GameSensors;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This class represents the tiles on the board
 */
public abstract class Tile implements ITile {
    public static final float DIMENSION = 1;
    protected Body body;
    protected int row, col, offsetX, offsetY;
    protected TextureRegion texture;
    private Type type;
    private boolean fixtureSensor;
    protected Fixture fixture;

    protected Tile(World world, int row, int col, int offsetX, int offsetY) {
        this(world, row, col, offsetX, offsetY, Type.TILE, false);
    }

    protected Tile(World world, int row, int col, int offsetX, int offsetY, Type type, boolean fixtureSensor) {
        this.row = row;
        this.col = col;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.fixtureSensor = fixtureSensor;
        this.type = type;
        initBody(world);
    }

    @Override
    public Body getBody() {
        return body;
    }

    protected void initBody(World world) {
        BodyDef bd = new BodyDef();
        bd.fixedRotation = true;
        bd.type = BodyType.StaticBody;
        bd.position.set(col + DIMENSION / 2, row + DIMENSION / 2);
        body = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        ChainShape shape2 = new ChainShape();
        Vector2[] arr = new Vector2[] {
                new Vector2(-DIMENSION / 2, -DIMENSION / 2),
                new Vector2(DIMENSION / 2, -DIMENSION / 2),
                new Vector2(DIMENSION / 2, DIMENSION / 2),
                new Vector2(-DIMENSION / 2, DIMENSION / 2)
        };
        shape2.createLoop(arr);
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape2;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.isSensor = fixtureSensor;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(GameSensors.GROUND.sensor);
        shape.dispose();
    }

    @Override
    public void initTexture(TilesetParser parser) {
        texture = parser.parseTile(offsetX, offsetY);
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Tile))
            return false;
        Tile t = (Tile) o;
        return row == t.row && col == t.col && type == t.type;
    }
}
