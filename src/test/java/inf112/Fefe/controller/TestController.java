package inf112.Fefe.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

import com.badlogic.gdx.graphics.GL20;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;

import inf112.Fefe.model.GameModel;
import inf112.Fefe.model.GameState;
import java.util.Arrays;

public class TestController {

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
    private GameController controller;

    @BeforeEach
    void setUpBeforeEach() {
        mockModel = new GameModel();
        mockModel.initGame();
        controller = new GameController(mockModel);
        mockModel.setGameState(GameState.PLAYING);

    }

    // Test key press and release functionality for various keys used in the game
    @Test
    void testKeyDown() {
        assertTrue(controller.keyDown(29));
        assertTrue(controller.keyDown(32));
        assertTrue(controller.keyUp(29));
        assertTrue(controller.keyUp(32));

        assertTrue(controller.keyDown(51));
        assertTrue(controller.keyDown(47));
        assertTrue(controller.keyUp(51));
        assertTrue(controller.keyUp(47));

        assertTrue(controller.keyDown(62));
        assertTrue(controller.keyDown(39));
        assertTrue(controller.keyUp(62));
        assertTrue(controller.keyUp(39));

        assertTrue(controller.keyDown(50));
        assertTrue(controller.keyUp(50));

    }

    // Simulate dashing south by pressing and releasing the left arrow key
    @Test
    void determineDashDirection() {

        List<Integer> keyVal = Arrays.asList(29, 32, 51, 47);
        List<DashDir> dirVal = Arrays.asList(DashDir.WEST, DashDir.EAST, DashDir.NORTH, DashDir.SOUTH);

        for (int i = 0; i < 4; i++) {
            mockModel = new GameModel();
            mockModel.initGame();
            controller = new GameController(mockModel);
            mockModel.setGameState(GameState.PLAYING);
            for (int j = 0; j < 100; ++j) {
                mockModel.tick();
            }
            controller.keyDown(keyVal.get(i));
            controller.keyDown(38);
            assertEquals(dirVal.get(i), mockModel.getPlayerDashDir());
            controller.keyUp(keyVal.get(i));

        }

    }

    // Testing unexpected controller input
    @Test
    void unexpectedControllerInput() {
        for (int i = 0; i < 1000; ++i) {
            mockModel.tick();
        }
        List<Integer> keys = Arrays.asList(29, 32, 51, 47, 62, 39, 38, 50, 111);

        for (int i = 0; i < 50; i++) {
            if (!keys.contains(i))
                controller.keyDown(i);
            assertEquals(DashDir.NONE, mockModel.getPlayerDashDir());
            assertEquals(GameState.PLAYING, mockModel.getGameState());
            assertEquals(false, mockModel.isDebug());
            assertEquals(false, controller.kIsHeld);
            assertEquals(Key.NONE, controller.verAct);
            assertEquals(Key.NONE, controller.horAct);
        }

    }
}
