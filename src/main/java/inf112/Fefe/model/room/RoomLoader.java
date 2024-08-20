package inf112.Fefe.model.room;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import com.badlogic.gdx.physics.box2d.World;

import inf112.Fefe.model.GameModel;
import inf112.Fefe.model.tiles.TileFactory;
import inf112.Fefe.model.tiles.TileType;
import inf112.util.Pair;

/**
 * This class is used to load the rooms in the game
 */
public class RoomLoader implements IRoomLoader {

    private final Map<Color, TileType> tileColors = new HashMap<>();
    private TileFactory factory;
    private BufferedImage rooms;
    private Map<Pair<Integer, Integer>, Set<String>> collected;

    /**
     * This constructor is used to create a roomLoader object to be able to load
     * rooms whenever the player progresses through the game
     * 
     * @param world is the world of the game
     */
    public RoomLoader(World world) {
        factory = new TileFactory(world);
        this.collected = new HashMap<>();
        try {
            rooms = ImageIO.read(new File("src/main/resources/images/game/map.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tileColors.put(new Color(255, 0, 0), TileType.GROUND);
        tileColors.put(new Color(100, 100, 100), TileType.SPIKE_DOWN); // down
        tileColors.put(new Color(40, 40, 40), TileType.SPIKE_LEFT); // left
        tileColors.put(new Color(60, 60, 60), TileType.SPIKE_RIGHT); // right
        tileColors.put(new Color(1, 1, 1, 255), TileType.SPIKE_UP); // up
        tileColors.put(new Color(0, 11, 255), TileType.LOADING_ZONE);
        tileColors.put(new Color(255, 239, 0), TileType.DOUBLE_DASH);
        tileColors.put(new Color(255, 0, 255), TileType.COLLECTIBLE);
        tileColors.put(new Color(164, 116, 73), TileType.INFO);
    }

    @Override
    public Room loadLevel(Pair<Integer, Integer> roomPos) {
        int width = 40;
        int height = 30;
        Room room = new Room(GameModel.ROOM_ROWS, GameModel.ROOM_COLS);
        Color[][] colors;
        try {
            colors = getLevelPixels(
                    rooms.getSubimage(roomPos.second * width, roomPos.first * height, width, height));
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot load room, invalid roomPos");
        }
        for (int i = 0; i < room.getRows(); ++i) {
            for (int j = 0; j < room.getCols(); ++j) {
                int row = GameModel.ROOM_ROWS - i - 1;
                Color color = colors[i][j];
                if (color.equals(Color.decode("#0000ff"))) {
                    room.setSpawnPos(null); // Spawnposition for the player
                }
                TileType type = tileColors.get(color);
                if (type == null)
                    continue;
                if (type == TileType.COLLECTIBLE)
                    if (collected.get(roomPos) != null)
                        if (collected.get(roomPos).contains(String.format("%d%d", j, row)))
                            continue;
                room.setCell(row, j, factory.getNewTile(type, row, j));
            }
        }
        return room;
    }

    @Override
    public void addCollected(Pair<Integer, Integer> roomPos, Integer xPos, Integer yPos) {
        if (collected.get(roomPos) == null) {
            collected.put(roomPos, new HashSet<>());
        }
        collected.get(roomPos).add(String.format("%d%d", xPos, yPos));
    }

    /**
     * getLevelPixels turns colors from the designed level to actual tiles
     * in the game.
     * 
     * @param level is the current level
     *
     * @return tiles based on the color of the pixel in map.png
     * 
     */
    private static Color[][] getLevelPixels(BufferedImage level) {
        Color[][] colors = new Color[GameModel.ROOM_ROWS][GameModel.ROOM_COLS];
        for (int i = 0; i < colors.length; ++i) {
            for (int j = 0; j < colors[0].length; ++j) {
                int color = level.getRGB(j, i);
                int r = (color & 0x00ff0000) >> 16;
                int g = (color & 0x0000ff00) >> 8;
                int b = color & 0x000000ff;
                colors[i][j] = new Color(r, g, b);
            }
        }
        return colors;
    }
}
