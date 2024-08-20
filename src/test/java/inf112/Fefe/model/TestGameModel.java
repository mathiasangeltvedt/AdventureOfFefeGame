package inf112.Fefe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import inf112.Fefe.controller.DashDir;
import inf112.Fefe.controller.Key;
import inf112.Fefe.model.player.DashValues;
import inf112.Fefe.model.tiles.ITile;
import inf112.util.Pair;

public class TestGameModel {

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
    private GameModel mockModel;

    @BeforeEach
    void setUpBeforeEach() {
        mockModel = new GameModel();
        mockModel.initGame();
    }

    // Test to verify if the death counter updates after the player dies
    @Test
    void deathCounter() {
        Vector2 p = new Vector2(mockModel.getPlayerPos());
        mockModel.setPlayerKeys(Key.RIGHT, Key.NONE);

        for (int i = 0; i < 600; ++i) { // Simulate game ticks to represent game time passing
            mockModel.tick();
        }
        assertTrue(p.x < mockModel.getPlayerPos().x); // Player should have moved right
        mockModel.climb(); // Initiate climbing
        mockModel.setPlayerKeys(Key.NONE, Key.UP); // Player should climb up

        for (int i = 0; i < 600; ++i) {
            mockModel.tick();
        }
        assertEquals(1, mockModel.restartCounter()); // Player should have died once
    }

    // Test that all static bodies retrieved from the game model are correctly
    // identified
    @Test
    void testGetStaticBodies() {
        Iterable<Body> staticBodies = mockModel.getStaticBodies();

        for (Body body : staticBodies) {
            assertEquals(BodyType.StaticBody, body.getType()); // All bodies should be static
        }
    }

    // Test to verify the offsets applied during a dash transformation are correct
    @Test
    void testPixieDustTransformOffsetsCorrectly() {
        for (int i = 0; i < 60; i++) { // Simulate time passing to setup dash conditions
            mockModel.tick();
        }
        mockModel.setPlayerKeys(Key.RIGHT, Key.NONE);
        mockModel.dash(DashDir.EAST); // Initiate dash to EAST
        DashValues transform = mockModel.pixieDustTransform(); // Get transformation values

        assertEquals(-2.75f, transform.getOffsetX(), "X offset should be set correctly");
        assertEquals(-0.25f, transform.getOffsetY(), "Y offset should be set correctly");
        assertTrue(mockModel.isDashing(), "Player should be dashing to check correct rotation");

    }

    // Test various game state changes to ensure the game model accurately tracks
    // and updates game state
    @Test
    void gameStateChanges() {
        mockModel.setGameState(GameState.MAIN_MENU);
        assertEquals(GameState.MAIN_MENU, mockModel.getGameState());

        mockModel.setGameState(GameState.PLAYING);
        assertEquals(GameState.PLAYING, mockModel.getGameState());

        mockModel.setGameState(GameState.CONTROLS);

        assertEquals(GameState.CONTROLS, mockModel.getGameState());

    }

    // Test to verify correct room number update when a loading zone is hit.
    @Test
    void testLoadingZoneHit() {
        Pair<Integer, Integer> initialRoomNum = mockModel.getRoomNum(); // Get initial room number

        mockModel.loadingZoneHit(new Vector2(39.5f, 24.5f)); // Hit loading zone
        initialRoomNum.first++; // Increment room number
        assertEquals(initialRoomNum, mockModel.getRoomNum()); // Verify room number update
    }

    // Test to ensure that the game model correctly retrieves tiles
    @Test
    void testGetTiles() {
        Iterable<ITile> tiles = mockModel.getTiles();

        assertNotNull(tiles, "Tiles should not be null");
        assertTrue(tiles.iterator().hasNext(), "There should be at least one tile");

    }

    // Test to verify that the player sprite is correctly retrieved
    @Test
    void testGetPlayerSprite() {
        TextureRegion sprite = mockModel.getPlayerSprite();

        assertNotNull(sprite, "Player sprite should not be null");
    }

    // Test to verify that hitting a loading zone correctly increments the room
    // number
    @Test
    public void testHittingLoadingZoneIncrementsRoomNum() {
        assertTrue(mockModel.getStaticBodies().iterator().hasNext(), "Static bodies should exist before reload");
        mockModel.loadingZoneHit(new Vector2(39.5f, 24.5f)); // This should set shouldReload to true
        mockModel.tick(); // Process any updates that occur after hitting the zone
        assertEquals(mockModel.getRoomNum(), new Pair<Integer, Integer>(2, 1)); // Room number should have incremented

    }

    // Test to verify that the dashing state of the player is accurately reported
    @Test
    public void testIsDashing() {
        for (int i = 0; i < 60; i++) { // Simulate time passing to setup dash conditions
            mockModel.tick();
        }

        mockModel.setPlayerKeys(Key.RIGHT, Key.NONE); // Set keys to initiate dash
        mockModel.dash(DashDir.EAST); // Initiate dash
        mockModel.tick(); // Process dash
        assertTrue(mockModel.isDashing(), "Player should be dashing after dash command");

        for (int i = 0; i < 60; i++) { // Simulate game ticks to exceed the duration of a typical dash
            mockModel.tick();
        }
        assertFalse(mockModel.isDashing(), "Player should not be dashing after dash is complete");
    }

    // Test that the closest powerup is removed from world when a powerup is hit
    @Test
    public void testPowerupHit() {
        List<Body> staticBodiesBefore = iterableToList(mockModel.getStaticBodies());
        mockModel.loadingZoneHit(new Vector2(39.5f, 15f)); // Hit power up
        mockModel.loadingZoneHit(new Vector2(39.5f, 15f)); // Hit power up
        mockModel.powerupHit(); // Hit power up
        mockModel.tick(); // Process power up hit
        List<Body> staticBodiesAfter = iterableToList(mockModel.getStaticBodies());
        assertNotEquals(staticBodiesBefore, staticBodiesAfter);
    }

    // Test that removed powerups are respawned on the map after 120 ticks
    @Test
    public void testPowerupReloads() {
        mockModel.initGame();
        // navigate to a level with several powerups
        for (int i = 0; i < 3; ++i) {
            mockModel.loadingZoneHit(new Vector2(39.5f, 15f));
            mockModel.tick();
        }
        mockModel.powerupHit(); // Hit power up
        mockModel.tick(); // Process power up hit
        List<Body> staticBodiesBefore = iterableToList(mockModel.getStaticBodies());
        for (int i = 0; i < 120; ++i) {
            mockModel.tick(); // Run game ticks to simulate time passing
        }
        List<Body> staticBodiesAfter = iterableToList(mockModel.getStaticBodies());
        assertNotEquals(staticBodiesBefore, staticBodiesAfter);
    }

    // Test that the closest collectible is removed when a collectible is hit
    @Test
    public void testCollectibleHit() {
        List<Body> staticBodiesBefore = iterableToList(mockModel.getStaticBodies());
        mockModel.collectibleHit(); // Hit collectible
        mockModel.tick(); // Process collectible hit
        List<Body> staticBodiesAfter = iterableToList(mockModel.getStaticBodies());
        assertNotEquals(staticBodiesBefore, staticBodiesAfter); // test that collectible is removed
    }

    <E> List<E> iterableToList(Iterable<E> iter) {
        java.util.List<E> arr = new ArrayList<>();
        for (E e : iter) {
            arr.add(e);
        }
        return arr;
    }

}
