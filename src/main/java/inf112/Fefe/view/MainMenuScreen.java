package inf112.Fefe.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import inf112.Fefe.Main;
import inf112.Fefe.model.GameState;
import inf112.Fefe.model.SpriteParser;

/**
 * This class is used to draw everything in the Main Menu and update the game
 * corresponding to what the player does in the main menu
 */
public class MainMenuScreen extends ScreenAdapter {

    GameView view;
    ViewableGameModel model;
    SpriteBatch batch;
    TextureRegion startButton, infoButton, controls1Button, controls2Button, exitButton;
    Texture background, purpleBground, title, menuBoard, mitch;
    Rectangle startBounds, infoBounds, controls1Bounds, controls2Bounds, exitBounds;
    boolean hoveringStart, hoveringInfo, hoveringControls, hoveringExit;
    OrthographicCamera cam;
    SpriteParser parser = new SpriteParser("images/menu/buttons.png");

    /**
     * This constructor is used to create a new main menu screen
     * 
     * @param view  is the game's original view object
     * @param model is the game's original model object
     */

    public MainMenuScreen(GameView view, ViewableGameModel model) {
        this(view, model, new SpriteBatch());
    }

    public MainMenuScreen(GameView view, ViewableGameModel model, SpriteBatch batch) {
        this.view = view;
        this.model = model;
        this.batch = batch;
        background = new Texture(Gdx.files.internal("images/game/background.png"));
        title = new Texture(Gdx.files.internal("images/menu/title.png"));
        menuBoard = new Texture(Gdx.files.internal("images/menu/menuBoard.png"));
        mitch = new Texture(Gdx.files.internal("images/menu/mitch.png"));
        cam = new OrthographicCamera(30, 30 * Main.WINDOW_HEIGHT / Main.WINDOW_WIDTH);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        startBounds = new Rectangle(12, 11.5f, 7, 2);
        infoBounds = new Rectangle(12, 9, 7, 2);
        controls1Bounds = new Rectangle(8.5f, 6.5f, 7.5f, 2);
        controls2Bounds = new Rectangle(15.5f, 6.5f, 7, 2);
        exitBounds = new Rectangle(12, 4, 7, 2);
    }

    @Override
    public void show() {
        render(0);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, cam.viewportWidth, cam.viewportHeight);
        batch.draw(menuBoard, 3.9f, 0.5f, 23, 17);
        batch.draw(title, 4.8f, 16, 21, 6);
        batch.draw(mitch, 0.5f, -0.5f, 7, 6);
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(touchPos);
        updateButtons(touchPos);
        updateButtonTexture();
        drawButtons(batch);

        if (Gdx.input.isButtonJustPressed(0)) {
            if (hoveringStart) {
                model.initGame();
                view.create();
                model.setGameState(GameState.PLAYING);
            } else if (hoveringInfo) {
                model.setGameState(GameState.INFO);
            } else if (hoveringControls) {
                model.setGameState(GameState.CONTROLS);
            } else if (hoveringExit) {
                Gdx.app.exit();
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void drawButtons(SpriteBatch batch) {
        batch.draw(startButton, startBounds.x, startBounds.y, startBounds.width, startBounds.height);
        batch.draw(infoButton, infoBounds.x, infoBounds.y, infoBounds.width, infoBounds.height);
        batch.draw(controls1Button, controls1Bounds.x, controls1Bounds.y, controls1Bounds.width - 0.5f,
                controls1Bounds.height);
        batch.draw(controls2Button, controls2Bounds.x + 0.5f, controls2Bounds.y, controls2Bounds.width,
                controls2Bounds.height);
        batch.draw(exitButton, exitBounds.x, exitBounds.y, exitBounds.width, exitBounds.height);
    }

    private void updateButtons(Vector3 touchPos) {
        hoveringStart = startBounds.contains(touchPos.x, touchPos.y);
        hoveringInfo = infoBounds.contains(touchPos.x, touchPos.y);
        hoveringControls = controls1Bounds.contains(touchPos.x, touchPos.y)
                || controls2Bounds.contains(touchPos.x, touchPos.y);
        hoveringExit = exitBounds.contains(touchPos.x, touchPos.y);
    }

    private void updateButtonTexture() {
        startButton = hoveringStart ? parser.getSprite(1, 1) : parser.getSprite(1, 0);
        infoButton = hoveringInfo ? parser.getSprite(2, 1) : parser.getSprite(2, 0);
        controls1Button = hoveringControls ? parser.getSprite(4, 0) : parser.getSprite(3, 0);
        controls2Button = hoveringControls ? parser.getSprite(4, 1) : parser.getSprite(3, 1);
        exitButton = hoveringExit ? parser.getSprite(0, 1) : parser.getSprite(0, 0);
    }
}
