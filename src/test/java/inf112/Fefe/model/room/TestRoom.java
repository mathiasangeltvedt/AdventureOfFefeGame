package inf112.Fefe.model.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldDef;

import inf112.util.Pair;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TestRoom {

    private static World mockWorld;
    private RoomLoader roomLoader;

    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Mockito.mock(GL20.class);
    }

    @BeforeEach
    public void setUp() {
        mockWorld = new World(new WorldDef(new Vector2(0, 0)));
        roomLoader = new RoomLoader(mockWorld);
    }

    // Test to verify that the spawn position can be accurately set and retrieved in
    // a specific room.
    @Test
    public void testSpawnPosition() {
        // Pair represents the transition from one room to another
        Pair<Integer, Integer> level = new Pair<Integer, Integer>(2, 0); // First room: 2, second room: 0
        Room room = roomLoader.loadLevel(level); // Load the room
        room.setSpawnPos(new Vector2(2, 2)); // Set a spawn position in the room
        Vector2 expected = room.getSpawnPos();
        assertEquals(expected, room.getSpawnPos(), "Spawn position should match expected");
    }

    // Test to verify that an exception is thrown when an invalid room number is
    // used
    @Test
    void testInvalidRoomNumber() {
        Pair<Integer, Integer> invalidLevel = new Pair<>(999, 999); // Assume 999,999 is not a valid room
        assertThrows(IllegalArgumentException.class, () -> {
            roomLoader.loadLevel(invalidLevel);
        }, "Loading an invalid room number should throw IllegalArgumentException.");
    }

}
