package inf112.Fefe.model.player.animation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mock;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldDef;

import inf112.Fefe.model.SpriteParser;
import inf112.Fefe.model.player.Player;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

public class TestSpriteElem {

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
    World mockWorld;
    Player mockPlayer;
    SpriteParser mockParser;
    SpriteElem mockElem;

    @BeforeEach
    void setUpBeforeEach() {
        mockWorld = new World(new WorldDef(new Vector2(0, 0)));
        mockPlayer = new Player(mockWorld);
        mockParser = new SpriteParser("images/game/playerSprites/playerSprite.png");
        mockElem = new SpriteElem(10, mockParser.getSprite(0, 0));
    }

    // Verify that the sprite retrieved from SpriteElem is not null
    @Test
    void testGetSprite() {
        assertNotNull(mockElem.getSprite());
    }

    // Test that tick count decreases properly over several updates
    @Test
    void testGetTick() {
        assertEquals(10, mockElem.getTick()); // Initial tick count should be 10
        for (int i = 0; i < 5; i++) { // Decrease tick count by 1 five times
            mockElem.tick();
        }
        assertEquals(5, mockElem.getTick()); // Tick count should now be 5
    }

    // Test that ticking correctly handles the cycle of sprite duration
    @Test
    void testSpriteElemTick() {
        for (int i = 0; i < 10; i++) { // Decrease tick count by 1 ten times
            if (i == 9)
                assertTrue(mockElem.tick()); // Last tick should return true indicating a reset
            else {
                assertFalse(mockElem.tick()); // All other ticks should return false
            }
        }
        assertEquals(10, mockElem.getTick()); // Tick count should be reset to 10

    }
}
