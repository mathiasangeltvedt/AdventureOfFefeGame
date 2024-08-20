package inf112.Fefe.view;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import inf112.Fefe.model.GameModel;

public class TestGameView {

    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);

        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Mockito.mock(GL20.class);
    }

    // Tests the call stack of the MainMenuScreen
    // This includes making sure the screen is displayed and disposed correctly
    @Test
    void testMainMenuScreenCallStack() {
        GameView view = mock(GameView.class);
        GameModel model = mock(GameModel.class);
        SpriteBatch mockedBatch = mock(SpriteBatch.class);

        // Instantiate MainMenuScreen with mocked dependencies
        Screen screen = new MainMenuScreen(view, model, mockedBatch);
        screen.show(); // Show the screen
        screen.dispose(); // Ensure resources are cleaned up correctly
    }

    // Tests the call stack of the InfoScreen
    // This includes making sure the screen is displayed and disposed correctly
    @Test
    void testInfoScreenCallStack() {
        GameView view = mock(GameView.class);
        GameModel model = mock(GameModel.class);
        SpriteBatch mockedBatch = mock(SpriteBatch.class);

        // Instantiate InfoScreen with mocked dependencies
        Screen screen = new InfoScreen(view, model, mockedBatch);
        screen.show(); // Show the screen
        screen.dispose(); // Ensure resources are cleaned up correctly
    }

    // Tests the call stack of the ControlsScreen
    // This includes making sure the screen is displayed and disposed correctly
    @Test
    void testControlsScreenCallStack() {
        GameView view = mock(GameView.class);
        GameModel model = mock(GameModel.class);
        SpriteBatch mockedBatch = mock(SpriteBatch.class);

        // Instantiate ControlsScreen with mocked dependencies
        Screen screen = new ControlsScreen(view, model, mockedBatch);
        screen.show(); // Show the screen
        screen.dispose(); // Ensure resources are cleaned up correctly
    }

    // Tests the call stack of the PauseScreen
    // This includes making sure the screen is displayed and disposed correctly
    @Test
    void testPauseScreenCallStack() {
        GameView view = mock(GameView.class);
        GameModel model = mock(GameModel.class);
        SpriteBatch mockedBatch = mock(SpriteBatch.class);

        // Instantiate PauseScreen with mocked dependencies
        Screen screen = new PauseScreen(view, model, mockedBatch);
        screen.show(); // Show the screen
        screen.dispose(); // Ensure resources are cleaned up correctly
    }
}
