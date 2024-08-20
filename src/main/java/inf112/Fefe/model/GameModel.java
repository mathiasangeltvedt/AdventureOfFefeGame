package inf112.Fefe.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import java.util.Queue;

import inf112.Fefe.controller.ControllableGameModel;
import inf112.Fefe.controller.DashDir;
import inf112.Fefe.view.ViewableGameModel;
import inf112.util.Pair;
import inf112.util.UtilityMethods;
import inf112.util.wave.SoundManager;
import inf112.util.wave.SoundName;
import inf112.Fefe.controller.Key;
import inf112.Fefe.model.contactListeners.ChangeableGameModel;
import inf112.Fefe.model.contactListeners.PlayerContactListener;
import inf112.Fefe.model.player.ContactablePlayer;
import inf112.Fefe.model.player.DashValues;
import inf112.Fefe.model.player.ModdablePlayer;
import inf112.Fefe.model.player.Player;
import inf112.Fefe.model.room.IRoomLoader;
import inf112.Fefe.model.room.Room;
import inf112.Fefe.model.room.RoomLoader;
import inf112.Fefe.model.tiles.ITile;
import inf112.Fefe.model.tiles.TileFactory;
import inf112.Fefe.model.tiles.TileType;
import inf112.Fefe.model.tiles.Type;

/**
 * This class represents the actual model of the game, this is where all the
 * logic is sent throughout the game
 * This is the brain of the game, where all logic is parsed through the
 * different classes
 */
public class GameModel implements ViewableGameModel, ControllableGameModel, ChangeableGameModel {
    public static final int ROOM_ROWS = 30, ROOM_COLS = 40;
    private static final float GRAV_CONSTANT = -75f;
    private float musicVolume, soundVolume;
    private ModdablePlayer player;
    private Room room;
    private World world;
    private GameState gameState;
    private Pair<Integer, Integer> roomNum;
    private TileFactory tileFactory;
    private int restartCount, reloadPowerupTimer, coins, bumpTimer;
    private boolean debugMode, shouldReload = false, hasBumped = false, wantsInfo = false;
    private SoundManager soundManager;
    private IRoomLoader roomLoader;
    private PlayerContactListener contactListener;
    private JsonValue spawnPositions;
    private Queue<ITile> tilesToRemove = new LinkedList<>();
    private Queue<Pair<Integer, Integer>> powerupsToAdd = new LinkedList<>();

    @Override
    public void initGame() {
        world = new World(new WorldDef(new Vector2(0, GRAV_CONSTANT), true));
        roomLoader = new RoomLoader(world);
        roomNum = new Pair<Integer, Integer>(2, 0);
        room = roomLoader.loadLevel(roomNum);
        player = new Player(world);
        coins = 0;
        restartCount = 0;
        tileFactory = new TileFactory(world);
        contactListener = new PlayerContactListener((ContactablePlayer) player, this);
        world.setContactListener(contactListener);
        gameState = GameState.MAIN_MENU;
        debugMode = false;
        musicVolume = 0.02f;
        soundVolume = 1f;
        if (soundManager != null)
            soundManager.dispose();
        soundManager = new SoundManager();
        setMusic(gameState);
        initPlayer();
        spawnPositions = UtilityMethods.loadJson("spawnPoints.json");
    }

    @Override
    public void loadingZoneHit(Vector2 hitPos) {
        if (shouldReload)
            return;
        String s = String.format("%d,%d-", roomNum.first, roomNum.second);
        if (UtilityMethods.almostEqual(hitPos.x, 0.5f))
            --roomNum.second;
        if (UtilityMethods.almostEqual(hitPos.x, 39.5f))
            ++roomNum.second;
        if (UtilityMethods.almostEqual(hitPos.y, 0.5f))
            ++roomNum.first;
        if (UtilityMethods.almostEqual(hitPos.y, 29.5f))
            --roomNum.first;
        float[] spawnPos = spawnPositions.get(s + String.format("%d,%d", roomNum.first, roomNum.second)).asFloatArray();
        player.setSpawnPos(new Vector2(spawnPos[0], spawnPos[1]));
        shouldReload = true;
    }

    @Override
    public Vector2 getPlayerPos() {
        return player.getBody().getPosition();
    }

    @Override
    public void setPlayerKeys(Key horKey, Key verKey) {
        player.setMovementKeys(verKey, horKey);
    }

    @Override
    public void jump() {
        if (player.jump(true)) {
            soundManager.playSound(SoundName.JUMP.path, false, soundVolume);
        }
    }

    @Override
    public void tick() {
        player.tick();
        if (--reloadPowerupTimer == 0) {
            reloadPowerUp();
        }
        if (--bumpTimer == 0) {
            hasBumped = false;
        }
        if (player.getIsDead()) {
            player.respawn();
            ++restartCount;
        }
        world.step(1 / 60f, 6, 3);
        if (shouldReload) {
            reload();
            shouldReload = false;
        }
        while (!tilesToRemove.isEmpty()) {
            ITile t = tilesToRemove.poll();
            if (t == null)
                continue;
            int row = (int) t.getBody().getPosition().y, col = (int) t.getBody().getPosition().x;
            if (t.getType() == Type.POWERUP)
                powerupsToAdd.add(new Pair<Integer, Integer>(row, col));
            world.destroyBody(t.getBody());
            row = (int) t.getBody().getPosition().y;
            col = (int) t.getBody().getPosition().x;
            room.setCell(row, col, null);
        }
    }

    @Override
    public DashValues pixieDustTransform() {
        return player.getDashValues();
    }

    @Override
    public TextureRegion getPlayerSprite() {
        return player.getSprite();
    }

    @Override
    public void initPlayer() {
        player.initSprites(coinCounter());
    }

    @Override
    public Iterable<Body> getStaticBodies() {
        List<Body> staticBodies = new ArrayList<>();
        Array<Body> bodies = new Array<>();
        for (Body body : world.getBodies(bodies)) {
            if (body.getType() == BodyType.StaticBody)
                staticBodies.add(body);
        }
        return staticBodies;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        setMusic(gameState);
    }

    @Override

    public Iterable<ITile> getTiles() {
        return room;
    }

    @Override
    public int restartCounter() {
        return restartCount;
    }

    @Override
    public int coinCounter() {
        return coins;
    }

    @Override
    public void dash(DashDir dir) {
        if (player.dash(dir))
            soundManager.playSound(SoundName.DASH.path, false, soundVolume);
    }

    @Override
    public void climb() {
        player.climb();
    }

    @Override
    public void cancelClimb() {
        player.cancelClimb();
    }

    @Override
    public boolean isDashing() {
        return player.getDashTick() > 0;
    }

    @Override
    public void setDebug(boolean debug) {
        debugMode = debug;
    }

    @Override
    public boolean isDebug() {
        return debugMode;
    }

    @Override
    public void playerBump() {
        if (!hasBumped)
            soundManager.playSound(SoundName.HURT.path, false, soundVolume);
        hasBumped = true;
        bumpTimer = 5;
    }

    private void setMusic(GameState gameState) {
        switch (gameState) {
            case PLAYING:
                soundManager.playSound(SoundName.INGAME.path, true, musicVolume);
                break;
            case MAIN_MENU:
                soundManager.playSound(SoundName.MENU.path, true, musicVolume);
                break;
            default:
                break;
        }
    }

    private void reload() {
        destroyAllStaticBodies();
        room = roomLoader.loadLevel(roomNum);
        player.respawn();
        shouldReload = false;
        powerupsToAdd.clear();
        tilesToRemove.clear();
    }

    private void destroyAllStaticBodies() {
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for (Body bd : bodies) {
            if (bd.getType() == BodyType.StaticBody)
                world.destroyBody(bd);
        }
    }

    @Override
    public void powerupHit() {
        reloadPowerupTimer = 120;
        Vector2 pPos = player.getBody().getPosition();
        ITile closest = null;
        for (ITile t : room) {
            if (t.getType() == Type.POWERUP) {
                if (closest == null) {
                    closest = t;
                    continue;
                }
                float closestDist = pPos.dst(closest.getBody().getPosition()),
                        currDist = pPos.dst(t.getBody().getPosition());
                if (currDist < closestDist)
                    closest = t;
            }
        }
        if (!tilesToRemove.contains(closest))
            tilesToRemove.add(closest);

    }

    /**
     * Call to retrieve the number of the room the player is currently stationed
     * 
     * @return a Pair of integers which is the row and col index of the current room
     */
    public Pair<Integer, Integer> getRoomNum() {
        return roomNum;
    }

    @Override
    public void collectibleHit() {
        Vector2 pPos = player.getBody().getPosition();
        ITile closest = null;
        for (ITile t : room) {
            if (t.getType() == Type.COLLECTIBLE) {
                if (closest == null) {
                    closest = t;
                    continue;
                }
                float closestDist = pPos.dst(closest.getBody().getPosition()),
                        currDist = pPos.dst(t.getBody().getPosition());
                if (currDist < closestDist)
                    closest = t;
            }
        }
        if (!tilesToRemove.contains(closest)) {
            int xPos = (int) closest.getBody().getPosition().x;
            int yPos = (int) closest.getBody().getPosition().y;
            roomLoader.addCollected(roomNum, xPos, yPos);
            tilesToRemove.add(closest);
            switch (++coins) {
                case 10:
                    soundManager.playSound(SoundName.SKIN_CHANGE.path, false, soundVolume);
                    initPlayer();
                    break;
                case 50:
                    soundManager.playSound(SoundName.SKIN_CHANGE.path, false, soundVolume);
                    initPlayer();
                    break;
                case 100:
                    soundManager.playSound(SoundName.SKIN_CHANGE.path, false, soundVolume);
                    initPlayer();
                    break;
                default:
                    soundManager.playSound(SoundName.COLLECT_COIN.path, false, soundVolume);
                    break;
            }
        }
    }

    private void reloadPowerUp() {
        while (!powerupsToAdd.isEmpty()) {
            Pair<Integer, Integer> toAdd = powerupsToAdd.poll();
            int row = toAdd.first, col = toAdd.second;
            room.setCell(row, col, tileFactory.getNewTile(TileType.DOUBLE_DASH, row, col));
        }
    }

    @Override
    public int getInfo() {
        if (wantsInfo) {
            return roomNum.second;
        }
        return -1;
    }

    @Override
    public void wantsInfo(boolean wantsInfo) {
        this.wantsInfo = wantsInfo;
    }

    /**
     * 
     * Is used to get the dash direction
     * 
     * @return DashDir
     */
    public DashDir getPlayerDashDir() {
        return player.getDashDir();
    }
}
