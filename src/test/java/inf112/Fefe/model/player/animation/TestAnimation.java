package inf112.Fefe.model.player.animation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

import inf112.Fefe.model.SpriteParser;
import inf112.Fefe.model.player.Player;

import org.mockito.Mock;
import org.mockito.Mockito;

public class TestAnimation {

    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);

        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Mockito.mock(GL20.class); // Need to use when we are using textures.
    }

    @Mock
    World mockWorld;
    Player mockPlayer;
    SpriteParser mockParser;
    Animation mockAnimation;

    @BeforeEach
    void setUpBeforeEach() {
        mockWorld = new World(new WorldDef(new Vector2(0, 0)));
        mockPlayer = new Player(mockWorld);
        mockAnimation = new Animation();
        mockParser = new SpriteParser("images/game/playerSprites/playerSprite.png");
    }

    // Test adding a sprite to the animation and verify it can be retrieved
    @Test
    void testAddSprite() {
        mockAnimation.addSprite(mockParser.getSprite(0, 0), 300); // Add a sprite with a duration of 300ms
        TextureRegion tx = mockAnimation.getSprite(false); // Retrieve the sprite
        assertNotNull(tx); // Verify that the sprite is not null
    }

    // Test that getting a sprite should not tick through the animation frames
    @Test
    void testGetSpriteShouldNotTick() {
        mockAnimation.addSprite(mockParser.getSprite(0, 0), 300); // Add a sprite with a duration of 300ms
        TextureRegion tx = mockAnimation.getSprite(false); // Retrieve the current sprite without ticking
        assertNotNull(tx); // Verify that the sprite is not null
    }

    // Test resetting the animation sprite list to its initial state
    @Test
    void testResetSpriteList() {
        // Add multiple sprites to the animation with different durations
        mockAnimation.addSprite(mockParser.getSprite(0, 0), 300);
        mockAnimation.addSprite(mockParser.getSprite(0, 1), 25);
        mockAnimation.addSprite(mockParser.getSprite(0, 2), 25);
        mockAnimation.addSprite(mockParser.getSprite(0, 3), 25);
        TextureRegion expected = mockAnimation.getSprite(false); // Get the current sprite
        for (int i = 0; i < 349; i++) { // Simulate time passing by ticking through sprites
            mockAnimation.getSprite(true);
        }
        mockAnimation.restartSprite(); // Reset the sprite list
        assertEquals(expected, mockAnimation.getSprite(false)); // Verify the sprite has reset correctly
    }

}
