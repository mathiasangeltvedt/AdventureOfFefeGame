package inf112.Fefe.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ScreenUtils;

import inf112.util.UtilityMethods;

import inf112.Fefe.Main;
import inf112.Fefe.controller.GameController;
import inf112.Fefe.model.tiles.ITile;
import inf112.Fefe.model.GameState;
import inf112.Fefe.model.tiles.TilesetParser;
import inf112.Fefe.model.SpriteParser;
import inf112.Fefe.model.player.DashValues;
import inf112.Fefe.model.player.Player;

/**
 * This class represents the graphic itself for the game. This is where
 * everything will be drawn, or where the view will be updated corresponding to
 * what GameState we are in.
 */
public class GameView extends Game {
    private static final float PLAYERSPRITE_OFFSET_X = 0.5625f, PLAYERSPRITE_OFFSET_Y = 0.6f,
            PLAYERSPRITE_DIMENSIONS = 1.5f;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private Texture bground, deathSign, info1, info2, info3, victory;
    private SpriteParser parser;
    private Map<GameState, Screen> screens = new HashMap<>();
    private GameController controller;
    private ViewableGameModel model;
    private Camera cam;
    private float posx = 3f, posy = 27.5f, sizex = 1f, sizey = 1f, posXCoin = 38f;

    /**
     * This constructor is used to create a new GameView object.
     * 
     * @param model      is the model we are using
     * @param controller is the controller we are using
     */
    public GameView(ViewableGameModel model, GameController controller) {
        this.controller = controller;
        this.model = model;
    }

    @Override
    public void create() {
        cam = new OrthographicCamera(40, 40 * Main.WINDOW_HEIGHT / Main.WINDOW_WIDTH);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        shape = new ShapeRenderer();
        shape.setProjectionMatrix(cam.combined);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        Gdx.input.setInputProcessor(controller);
        Gdx.graphics.setForegroundFPS(60);
        Gdx.gl.glLineWidth(3);
        load_textures();
        screens.put(GameState.MAIN_MENU, new MainMenuScreen(this, model));
        screens.put(GameState.PAUSE, new PauseScreen(this, model));
        screens.put(GameState.INFO, new InfoScreen(this, model));
        screens.put(GameState.CONTROLS, new ControlsScreen(this, model));
        model.initGame();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        if (screens.containsKey(model.getGameState())) {
            if (model.getGameState() == GameState.PAUSE)
                renderGame();
            setScreen(screens.get(model.getGameState()));
        } else {
            renderGame();
            model.tick();
        }
    }

    private void renderGame() {
        batch.begin();
        batch.draw(bground, 0, 0, cam.viewportWidth, cam.viewportHeight);

        renderPlayerSprite();

        if (!model.isDebug())
            renderTiles();

        if (model.isDashing())
            renderPixieDust();

        info(batch);

        batch.draw(deathSign, 1, posy, 2, 1.5f);
        int decimal = Integer.toString(model.restartCounter()).length();
        for (int i = 0; i < decimal; i++) {
            float x = decimal / 2 + posx - (i * 0.5f);
            batch.draw(dnums(UtilityMethods.findDigitVal(model.restartCounter(), i)), x, posy, sizex, sizey);
        }

        int dec = Integer.toString(model.coinCounter()).length();
        for (int i = 0; i < dec; i++) {
            float x = dec / 2 + posXCoin - (i * 0.5f);
            batch.draw(dnums(UtilityMethods.findDigitVal(model.coinCounter(), i)), x, posy, sizex, sizey);
        }
        TilesetParser p = new TilesetParser();
        batch.draw(p.parseTile(2, 9), posXCoin - 1, posy, sizex, sizey);
        if (model.getRoomNum().first == 2 && model.getRoomNum().second == 4)
            batch.draw(victory, 10, 5, 20, 20);
        batch.end();
        if (model.isDebug()) {
            shape.begin(ShapeType.Line);
            renderTileHitboxes();
            renderPlayerHitbox();
            shape.end();
        }
    }

    private TextureRegion dnums(int num) {
        return parser.getSprite(0, num);
    }

    private void renderPlayerSprite() {
        Vector2 playerPos = model.getPlayerPos();
        batch.draw(model.getPlayerSprite(), playerPos.x - PLAYERSPRITE_OFFSET_X,
                playerPos.y - PLAYERSPRITE_OFFSET_Y, PLAYERSPRITE_DIMENSIONS, PLAYERSPRITE_DIMENSIONS);
    }

    private void renderPixieDust() {
        Vector2 playerPos = model.getPlayerPos();
        DashValues pdValues = model.pixieDustTransform();
        batch.draw(pdValues.texture(), playerPos.x + pdValues.offset().x, playerPos.y + pdValues.offset().y, 1.5f,
                1.5f);
    }

    private void renderTiles() {
        for (ITile tile : model.getTiles()) {
            Body body = tile.getBody();
            Fixture fix = body.getFixtureList().get(0);
            Vector2 lowerBound = new Vector2(), tilePos = body.getPosition();
            ((ChainShape) fix.getShape()).getVertex(0, lowerBound);
            batch.draw(tile.getTexture(), tilePos.x - 0.5f, tilePos.y - 0.5f, 1, 1);
        }
    }

    private void renderTileHitboxes() {
        shape.setColor(Color.WHITE);
        for (ITile tile : model.getTiles()) {
            Body body = tile.getBody();
            Fixture fix = body.getFixtureList().get(0);
            Vector2 lowerBound = new Vector2(), upperBound = new Vector2(), tilePos = body.getPosition();
            ((ChainShape) fix.getShape()).getVertex(0, lowerBound);
            ((ChainShape) fix.getShape()).getVertex(2, upperBound);
            float w = upperBound.x - lowerBound.x, h = upperBound.y - lowerBound.y;
            shape.rect(lowerBound.x + tilePos.x, lowerBound.y + tilePos.y, w, h);
        }
    }

    private void renderPlayerHitbox() {
        Vector2 playerPos = model.getPlayerPos();
        shape.setColor(Color.GREEN);
        shape.rect(playerPos.x - Player.WIDTH / 2, playerPos.y - Player.HEIGHT /
                2, Player.WIDTH,
                Player.HEIGHT);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
    }

    private void load_textures() {
        deathSign = new Texture(Gdx.files.internal("images/game/deathSign.png"));
        bground = new Texture(Gdx.files.internal("images/game/background.png"));
        parser = new SpriteParser("images/game/numbers.png");
        victory = new Texture(Gdx.files.internal("images/game/victory.png"));
        info1 = new Texture(Gdx.files.internal("images/game/infoSigns/info1.png"));
        info2 = new Texture(Gdx.files.internal("images/game/infoSigns/info2.png"));
        info3 = new Texture(Gdx.files.internal("images/game/infoSigns/info3.png"));
    }

    private void info(SpriteBatch batch) {
        int info = model.getInfo();
        switch (info) {
            case 0:
                batch.draw(info1, 6, 2, 10, 7);
                break;
            case 1:
                batch.draw(info2, 13, 9, 10, 7);
                break;
            case 2:
                batch.draw(info3, 9, 23, 10, 7);
            default:
                break;
        }
    }
}
