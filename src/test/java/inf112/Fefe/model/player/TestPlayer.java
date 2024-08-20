package inf112.Fefe.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import inf112.Fefe.controller.DashDir;
import inf112.Fefe.controller.Key;
import inf112.Fefe.model.GameModel;
import inf112.Fefe.model.contactListeners.ClimbDir;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldDef;

public class TestPlayer {

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
    private World mockWorld;
    private Player mockPlayer;

    @BeforeEach
    void setUpBeforeEach() {
        mockModel = new GameModel();
        mockWorld = new World(new WorldDef(new Vector2(0, 0)));
        mockPlayer = new Player(mockWorld);
    }

    // Checking the basic functionality of the dashing
    @Test
    void dashSanityTest() {
        Vector2 positionBeforeDash = new Vector2(mockPlayer.getBody().getPosition());

        mockPlayer.setIsGrounded(true); // Ensuring the player is grounded (required for dashing)
        mockPlayer.dash(DashDir.EAST); // Initiating dash to the EAST
        mockPlayer.tick(); // Updating the player

        mockWorld.step(1 / 60f, 6, 2); // Simulating a single frame of game logic
        assertNotEquals(positionBeforeDash, mockPlayer.getBody().getPosition()); // Ensuring the player has moved
        assertNotEquals(mockPlayer.getDashTick(), 0); // Dash tick should be active indicating a dash is in progress
    }

    // Verifying that the player cannot initiate a dash while in mid-air
    @Test
    void testCantDashWhileMidair() {
        Vector2 positionBeforeDash = new Vector2(mockPlayer.getBody().getPosition());
        mockPlayer.setIsGrounded(false); // Player is mid-air, should not be able to dash
        mockPlayer.dash(DashDir.NORTHEAST); // Initiating dash to the NORTHEAST
        mockPlayer.tick();

        mockWorld.step(1 / 60f, 6, 2);
        assertEquals(positionBeforeDash.x, mockPlayer.getBody().getPosition().x); // Player should not have moved
        assertTrue(mockPlayer.getDashTick() <= 0); // Dash tick should not be active
    }

    // Verifying that the player cannot initiate a dash while already dashing
    @Test
    void testCannotDashTwice() {
        mockPlayer.setIsGrounded(true); // Ensuring the player is grounded (required for dashing)
        mockPlayer.dash(DashDir.EAST); // Initiating dash to the EAST
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertEquals(mockPlayer.getDashTick(), 13); // Dash tick should be active indicating a dash is in progress
        mockPlayer.dash(DashDir.EAST); // Initiating another dash to the EAST
        mockPlayer.tick();
        assertEquals(mockPlayer.getDashTick(), 12); // Dash tick should still be active
    }

    // Testing dash direction EAST
    @Test
    void testDashEast() {
        Vector2 positionBeforeDash = new Vector2(mockPlayer.getBody().getPosition());

        mockPlayer.setIsGrounded(true);
        mockPlayer.dash(DashDir.EAST);
        mockPlayer.tick();

        mockWorld.step(1 / 60f, 6, 2);
        assertTrue(positionBeforeDash.x < mockPlayer.getBody().getPosition().x);
        assertEquals(mockPlayer.getDashDir(), DashDir.EAST);
    }

    // Testing dash direction WEST
    @Test
    void testDashWest() {
        Vector2 positionBeforeDash = new Vector2(mockPlayer.getBody().getPosition());

        mockPlayer.setIsGrounded(true);
        mockPlayer.dash(DashDir.WEST);
        mockPlayer.tick();

        mockWorld.step(1 / 60f, 6, 2);
        assertTrue(positionBeforeDash.x > mockPlayer.getBody().getPosition().x);
        assertEquals(mockPlayer.getDashDir(), DashDir.WEST);
    }

    // Testing that the player can climb
    @Test
    void testClimb() {
        ClimbDir dir = ClimbDir.NONE;

        Vector2 position = new Vector2(mockPlayer.getBody().getPosition());

        mockPlayer.setMovementKeys(Key.UP, Key.RIGHT); // Setting the movement keys to climb
        mockPlayer.setCanClimb(true, dir); // Setting the player to be able to climb
        assertTrue(mockPlayer.climb()); // Attempting to climb
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertTrue(mockPlayer.getBody().getPosition().y > position.y); // Player should have moved upwards
    }

    // Testing that the player cannot climb in certain situations
    @Test
    void testCannotClimb() {
        ClimbDir dir = ClimbDir.NONE;

        Vector2 position = new Vector2(mockPlayer.getBody().getPosition());

        mockPlayer.setMovementKeys(Key.UP, Key.RIGHT); // Setting the movement keys to climb
        mockPlayer.setCanClimb(false, dir); // Setting the player to not be able to climb
        assertTrue(!mockPlayer.climb()); // Attempting to climb and expecting it to fail
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertEquals(mockPlayer.getBody().getPosition().y, position.y); // Player should not have moved
    }

    // Testing that the player can cancel a climb
    @Test
    void testCancelClimb() {
        ClimbDir dir = ClimbDir.NONE;

        Vector2 position = new Vector2(mockPlayer.getBody().getPosition());

        mockPlayer.setMovementKeys(Key.UP, Key.DOWN); // Setting the movement keys to climb
        mockPlayer.setCanClimb(true, dir); // Setting the player to be able to climb
        assertTrue(mockPlayer.climb()); // Attempting to climb
        mockPlayer.cancelClimb(); // Cancelling the climb
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertEquals(position, mockPlayer.getBody().getPosition()); // Player should not have moved
    }

    // Testing that the player can climb while dashing into a wall
    @Test
    void testClimbWhileDashingIntoWall() {
        mockPlayer.setIsGrounded(true); // Player needs to be grounded to initiate a dash
        mockPlayer.setCanClimb(false, ClimbDir.NONE); // Initially, player can't climb
        mockPlayer.dash(DashDir.EAST); // Dash towards the east, where we'll assume a wall is located
        // Simulate hitting the wall and the ability to climb being triggered
        mockPlayer.setCanClimb(true, ClimbDir.RIGHT); // Player can now climb

        // Attempt to climb immediately after dashing into the wall
        boolean canClimb = mockPlayer.climb();

        // Assert
        assertTrue(canClimb, "Player should be able to climb after dashing into a wall");
        assertTrue(mockPlayer.isClimbing(), "Player should be in climbing state");
    }

    // Testing that the player can jump
    @Test
    void testJump() {
        mockPlayer.setIsGrounded(true); // Ensuring the player is grounded

        Vector2 positionBeforeJump = new Vector2(mockPlayer.getBody().getPosition());

        mockPlayer.jump(true); // Initiating a jump
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertTrue(positionBeforeJump.y < mockPlayer.getBody().getPosition().y); // Player should have moved upwards

    }

    // Testing that the player can jump from a wall and perform a directional jump
    @Test
    void testJumpFromWall() {
        mockPlayer.setCanClimb(true, ClimbDir.RIGHT); // Enable climbing to the right
        mockPlayer.climb(); // Climb to the right
        Vector2 prePos = new Vector2(mockPlayer.getBody().getPosition()); // Save position before jump
        assertTrue(mockPlayer.jump(false)); // Player jumps off the wall without resetting the jump buffer
        mockWorld.step(1 / 60f, 6, 2);
        assertTrue(prePos.y < mockPlayer.getBody().getPosition().y); // Player should have moved upwards
        assertTrue(prePos.x > mockPlayer.getBody().getPosition().x); // Player should have moved to the left
    }

    // Ensure that the player cannot initiate a jump when not grounded (midair)
    @Test
    void testCannotJumpWhileMidair() {
        mockPlayer.setIsGrounded(false); // Player is not on the ground
        Vector2 positionBeforeJump = new Vector2(mockPlayer.getBody().getPosition());
        mockPlayer.jump(true); // Attempt to jump
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertTrue(positionBeforeJump.y <= mockPlayer.getBody().getPosition().y); // Should not have moved up
        assertFalse(mockPlayer.jump(false)); // Further jump attempts should fail
        assertFalse(mockPlayer.jump(true)); // Even with resetting the jump buffer, jump should fail
    }

    // Test to see that the player cannot climb when the player is on the ground.
    @Test
    void testNoClimbOnGround() {
        mockPlayer.setIsGrounded(true); // Player is on the ground
        for (int i = 0; i < 1000; i++) {
            {
                mockPlayer.tick();
                mockWorld.step(1 / 60f, 6, 2);
            }
            mockWorld.step(1 / 60f, 6, 2);

            assertFalse(mockPlayer.climb()); // Attempting to climb should fail
        }

    }

    // Test that player moves when the appropriate key is pressed
    @Test
    void playerIsMovedWhenKeyPressed() {
        Vector2 startPosition = new Vector2(mockPlayer.getBody().getPosition());
        mockPlayer.setMovementKeys(Key.NONE, Key.LEFT); // Set keys to move player left
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertNotEquals(startPosition.x, mockPlayer.getBody().getPosition().x); // Check if horizontal position has
                                                                                // changed
    }

    // Ensure the player cannot move vertically upwards by pressing up key when
    // grounded
    @Test
    void testCantMoveUp() {
        mockPlayer.setIsGrounded(true); // Player is on the ground.

        Vector2 startPosition = new Vector2(mockPlayer.getBody().getPosition());
        mockPlayer.setMovementKeys(Key.NONE, Key.UP); // Try to move up
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertEquals(startPosition, mockPlayer.getBody().getPosition()); // Position should remain unchanged
    }

    // Test that the player moves right when the right key is pressed
    @Test
    void testMoveRight() {
        Vector2 startPosition = new Vector2(mockPlayer.getBody().getPosition());
        mockPlayer.setMovementKeys(Key.NONE, Key.RIGHT);
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertTrue(startPosition.x < mockPlayer.getBody().getPosition().x);
    }

    // Test that the player moves left when the left key is pressed
    @Test
    void testMoveLeft() {
        Vector2 startPosition = new Vector2(mockPlayer.getBody().getPosition());
        mockPlayer.setMovementKeys(Key.NONE, Key.LEFT);
        mockPlayer.tick();
        mockWorld.step(1 / 60f, 6, 2);
        assertTrue(startPosition.x > mockPlayer.getBody().getPosition().x);

    }

    // Test that the player is respawned correctly at a designated spawn position
    @Test
    void testRespawn() {
        Vector2 spawnPosition = new Vector2(6, 6),
                positionBeforeRespawn = new Vector2(mockPlayer.getBody().getPosition());
        mockPlayer.setSpawnPos(spawnPosition); // Set spawn position
        mockPlayer.setIsDead(true); // Set player to be dead (required for respawn)
        mockPlayer.tick();
        mockPlayer.respawn(); // Respawn player
        mockWorld.step(1 / 60f, 6, 2);
        Vector2 positionAfterRespawn = new Vector2(mockPlayer.getBody().getPosition());
        assertNotEquals(positionBeforeRespawn, positionAfterRespawn); // Ensure player's position is changed after
                                                                      // respawn
        assertEquals(spawnPosition.x, positionAfterRespawn.x); // Ensure correct respawned position
    }

    // Test to check idle animation sprites
    @Test
    void testGetSpriteIdle() {
        mockPlayer.initSprites(0);
        TextureRegion current = mockPlayer.getSprite(); // Idle animation texture 1

        // Simulate game time to transition through different idle sprites.
        for (int i = 0; i < 400; i++) {
            if (i == 301) { // Change to idle animation texture 2
                assertNotEquals(current, mockPlayer.getSprite());
                current = mockPlayer.getSprite();
            } else if (i == 326) { // Change to idle animation texture 3
                assertNotEquals(current, mockPlayer.getSprite());
                current = mockPlayer.getSprite();
            } else if (i == 351) { // Change to idle animation texture 4
                assertNotEquals(current, mockPlayer.getSprite()); // Sprite should change to the final stage of idle
                                                                  // animation
                current = mockPlayer.getSprite();
            }
            mockPlayer.getSprite();
            mockWorld.step(1 / 60f, 6, 2);
        }

        mockPlayer.jump(false);
        mockWorld.step(1 / 60f, 6, 2);
        assertEquals(current, mockPlayer.getSprite()); // Sprite should revert to current idle animation after movement
    }

    // Test to check walking animation sprites
    @Test
    void testGetSpriteMoving() {
        mockPlayer.initSprites(0);
        mockPlayer.setMovementKeys(Key.NONE, Key.RIGHT);
        mockWorld.step(1 / 60f, 6, 2);
        TextureRegion current = mockPlayer.getSprite(); // Initial walking animation

        for (int i = 0; i < 30; i++) { // Simulate game time and ensure that the walking animation updates after a set
                                       // number of ticks
            mockPlayer.tick();
            mockWorld.step(1 / 60f, 6, 2);
            if (i == 16) { // Amount of tick per walking animation
                assertNotEquals(current, mockPlayer.getSprite()); // Walking sprite should change after 16 ticks
            }
            mockPlayer.getSprite();
        }
    }

    // Test to check climbing animation sprites
    @Test
    void testGetSpriteClimbing() {
        mockPlayer.initSprites(0);
        mockPlayer.setCanClimb(true, ClimbDir.RIGHT);
        mockPlayer.setMovementKeys(Key.UP, Key.RIGHT);
        mockPlayer.climb();
        TextureRegion current = mockPlayer.getSprite();

        for (int i = 0; i < 45; i++) { // Simulate game time and ensure climbing animation updates after a set number
                                       // of ticks.
            mockPlayer.tick();
            mockWorld.step(1 / 60f, 6, 2);
            if (i == 16) { // Amount of tick per climbing animation
                assertNotEquals(current, mockPlayer.getSprite());
                current = mockPlayer.getSprite(); // Update current sprite after change.
                mockPlayer.setMovementKeys(Key.NONE, Key.RIGHT); // Change movement keys to simulate end of active
                                                                 // climbing
            }
            if (i == 31) { // Should have changed but since verKey is NONE it should be the same
                assertEquals(current, mockPlayer.getSprite());
            }
            mockPlayer.getSprite();
        }
    }
}