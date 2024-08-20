package inf112.Fefe.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import inf112.Fefe.Main;
import inf112.Fefe.model.GameState;
/**
 * this class graphics for the control screen
 * 
 */
public class ControlsScreen extends ScreenAdapter {
    Camera cam;
    SpriteBatch batch;
    Texture background, overview, backButtonTextureNotHovering, backButtonTextureHovering, backButton;
    Rectangle backButtonBounds;
    boolean hoveringBack = false;
    ViewableGameModel model;
    GameView view;

    public ControlsScreen(GameView view, ViewableGameModel model) {
        this(view, model, new SpriteBatch());
    }

    public ControlsScreen(GameView view, ViewableGameModel model, SpriteBatch batch) {
        this.view = view;
        this.model = model;
        this.batch = batch;
        this.background = new Texture(Gdx.files.internal("images/game/background.png"));
        this.overview = new Texture(Gdx.files.internal("images/controls/controls.png"));
        this.backButtonTextureNotHovering = new Texture(Gdx.files.internal("images/controls/backButton.png"));
        this.backButtonTextureHovering = new Texture(Gdx.files.internal("images/controls/backButtonHovering.png"));
    }

    @Override
    public void show() {
        cam = new OrthographicCamera(30, 30 * Main.WINDOW_HEIGHT / Main.WINDOW_WIDTH);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        backButtonBounds = new Rectangle(0, 20, 4, 2);
        render(0);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, cam.viewportWidth, cam.viewportHeight);
        batch.draw(overview, 5, 3, 20, 15);
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(touchPos);
        hoveringBack = backButtonBounds.contains(touchPos.x, touchPos.y);
        backButton = hoveringBack ? backButtonTextureHovering : backButtonTextureNotHovering;
        batch.draw(backButton, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height);
        if (hoveringBack && Gdx.input.isButtonJustPressed(0))
            model.setGameState(GameState.MAIN_MENU);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        overview.dispose();
        backButtonTextureHovering.dispose();
        backButtonTextureNotHovering.dispose();
    }

}
