package inf112.Fefe.model.sprite;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

import inf112.Fefe.model.GameModel;
import inf112.Fefe.model.SpriteParser;

public class TestSpriteParser {

    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationAdapter listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);

        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Mockito.mock(GL20.class); // Need to use when we are using textures.
    }

    @Mock
    private GameModel mockModel;

    @BeforeEach
    void setUpBeforeEach() {
        mockModel = new GameModel();
    }

    // Test to verify that the sprite can be accurately retrieved from a specific
    // path
    @Test
    void testGetSprite() {
        SpriteParser parser = new SpriteParser("images/game/playerSprites/playerSprite.png");
        parser.getSprite(0, 0);
    }

    // Test to verify that attempting to retrieve a sprite using invalid indices
    // throws an expected exception
    @Test
    void testSpriteIllegalPos() {
        SpriteParser parser2 = new SpriteParser("images/pause/buttons.png");

        // Expecting an exception to be thrown when invalid sprite indices are used.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            parser2.getSprite(-30, -20);
        });
    }
}