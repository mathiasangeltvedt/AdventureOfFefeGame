package inf112.Fefe.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import inf112.Fefe.Main;
import inf112.Fefe.model.GameState;
/**
 * This class represents the graphic itself for the info screen. This is where
 * everything will be drawn, or where the view will be updated corresponding to
 * what GameState we are in.
 */
public class InfoScreen extends ScreenAdapter {
    OrthographicCamera cam;
    SpriteBatch batch;
    ViewableGameModel model;
    Texture background, backButtonTextureNotHovering, backButtonTextureHovering, backButton, info;
    Rectangle backButtonBounds;
    boolean hoveringBack = false;
    GameView view;

    public InfoScreen(GameView view, ViewableGameModel model) {
        this(view, model, new SpriteBatch());
    }

    public InfoScreen(GameView view, ViewableGameModel model, SpriteBatch batch) {
        this.view = view;
        this.model = model;
        this.batch = batch;
        background = new Texture(Gdx.files.internal("images/game/background.png"));
        backButtonTextureNotHovering = new Texture(Gdx.files.internal("images/controls/backButton.png"));
        backButtonTextureHovering = new Texture(Gdx.files.internal("images/controls/backButtonHovering.png"));
        info = new Texture(Gdx.files.internal("images/menu/infoScreenText.png"));
        cam = new OrthographicCamera(30, 30 * Main.WINDOW_HEIGHT / Main.WINDOW_WIDTH);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        backButtonBounds = new Rectangle(0, 20, 4, 2);
    }

    @Override
    public void show() {
        render(0);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, cam.viewportWidth, cam.viewportHeight);
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(touchPos);
        hoveringBack = backButtonBounds.contains(touchPos.x, touchPos.y);
        backButton = hoveringBack ? backButtonTextureHovering : backButtonTextureNotHovering;
        batch.draw(backButton, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height);
        batch.draw(info, 0, 0, 30, 20);
        if (hoveringBack)
            if (Gdx.input.isButtonJustPressed(0))
                model.setGameState(GameState.MAIN_MENU);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        backButtonTextureHovering.dispose();
        backButtonTextureNotHovering.dispose();
    }

}
