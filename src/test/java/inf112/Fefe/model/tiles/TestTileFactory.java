package inf112.Fefe.model.tiles;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldDef;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class TestTileFactory {

    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Mockito.mock(GL20.class);
    }

    @Mock
    private World mockWorld;
    private TileFactory tileFactory;

    @Mock
    private GroundTile mockGroundTile;
    @Mock
    private SpikeUpTile mockSpikeTile;

    @BeforeEach
    public void setUp() {
        mockWorld = new World(new WorldDef(new Vector2(0, 0)));
        tileFactory = new TileFactory(mockWorld);

    }

    // Test that the tile created is not null and is an instance of GroundTile
    @Test
    void testGetNewGroundTile() {
        TileType type = TileType.GROUND;

        ITile result = tileFactory.getNewTile(type, 1, 2);

        assertNotNull(result);
        assertTrue(result instanceof GroundTile, "Result should be an instance of GroundTile");
    }

    // Test that the tile created is not null and is an instance of SpikeTile
    @Test
    void testGetNewSpikeTile() {

        TileType type = TileType.SPIKE_UP;

        ITile result = tileFactory.getNewTile(type, 1, 2);

        assertNotNull(result);
        assertTrue(result instanceof SpikeUpTile, "Result should be an instance of SpikeTile");
    }

    @Test
    void testInvalidTileType() {
        assertThrows(IllegalArgumentException.class, () -> {
            tileFactory.getNewTile(null, 1, 2);
        }, "Should throw an exception when provided with a null or invalid tile type.");
    }

}
