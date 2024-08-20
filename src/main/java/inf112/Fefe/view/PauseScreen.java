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
 * This class is used to draw everything in when the game is set to pause
 */
public class PauseScreen extends ScreenAdapter {

    GameView view;
    ViewableGameModel model;
    SpriteBatch batch;
    OrthographicCamera cam;
    TextureRegion resumeButton, restartButton, menuButton;
    Texture pauseBoard = new Texture(Gdx.files.internal("images/pause/pauseBoard.png"));
    Texture pauseTitle = new Texture(Gdx.files.internal("images/pause/title.png"));
    SpriteParser parser = new SpriteParser("images/pause/buttons.png");
    Rectangle resumeBounds, restartBounds, menuBounds;
    boolean hoveringResume, hoveringRestart, hoveringMenu;

    public PauseScreen(GameView view, ViewableGameModel model) {
        this(view, model, new SpriteBatch());
    }

    /**
     * This constructor is used to create a new PauseScreen whenever the game is set
     * to pause
     * 
     * @param view  is the game's original view object
     * @param model is the game's original model object
     */
    public PauseScreen(GameView view, ViewableGameModel model, SpriteBatch batch) {
        this.view = view;
        this.model = model;
        this.batch = batch;
    }

    @Override
    public void show() {
        cam = new OrthographicCamera(30, 30 * Main.WINDOW_HEIGHT / Main.WINDOW_WIDTH);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        resumeBounds = new Rectangle(16, 8, 2, 2);
        restartBounds = new Rectangle(14, 8, 2, 2);
        menuBounds = new Rectangle(12, 8, 2, 2);
        render(0);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(touchPos);

        updateButtons(touchPos);
        updateButtonTexture();
        drawButtons(batch);

        if (Gdx.input.isButtonJustPressed(0)) {
            if (hoveringResume) {
                model.setGameState(GameState.PLAYING);
            } else if (hoveringRestart) {
                model.initGame();
                view.create();
                model.setGameState(GameState.PLAYING);
            } else if (hoveringMenu) {
                model.setGameState(GameState.MAIN_MENU);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void drawButtons(SpriteBatch batch) {
        batch.draw(pauseTitle, menuBounds.x - 3, 12, 12, 3);
        batch.draw(pauseBoard, menuBounds.x - 2, 6, 10, 6);
        batch.draw(menuButton, menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height);
        batch.draw(restartButton, restartBounds.x, restartBounds.y, restartBounds.width, restartBounds.height);
        batch.draw(resumeButton, resumeBounds.x, resumeBounds.y, resumeBounds.width, resumeBounds.height);
    }

    private void updateButtons(Vector3 touchPos) {
        hoveringResume = resumeBounds.contains(touchPos.x, touchPos.y);
        hoveringRestart = restartBounds.contains(touchPos.x, touchPos.y);
        hoveringMenu = menuBounds.contains(touchPos.x, touchPos.y);
    }

    private void updateButtonTexture() {
        resumeButton = hoveringResume ? parser.getSprite(1, 2) : parser.getSprite(0, 2);
        restartButton = hoveringRestart ? parser.getSprite(1, 1) : parser.getSprite(0, 1);
        menuButton = hoveringMenu ? parser.getSprite(1, 0) : parser.getSprite(0, 0);
    }
}
