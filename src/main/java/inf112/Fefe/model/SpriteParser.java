package inf112.Fefe.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class is used to get the correct sprites corresponding to what we want
 * to draw
 */
public class SpriteParser {

    private TextureRegion[][] frames;

    /**
     * This constructor is used to parse the correct sprite corresponding to what we
     * want to draw
     * 
     * @param spriteMap is the spriteMap we want to retreive the graphics from
     */
    public SpriteParser(String path) {
        try {
            int width, height;
            Texture texture;
            if (path == "images/menu/buttons.png") {
                width = 246;
                height = 61;
            } else if (path == "images/pause/buttons.png") {
                width = 48;
                height = 42;
            } else if (path == "images/game/numbers.png") {
                width = 10;
                height = 11;
            } else if (path == "images/game/pixieDustTransform.png") {
                width = 375;
                height = 355;
            }
            // else -> playerSprite dimensions
            else {
                width = 385;
                height = 366;
            }
            texture = new Texture(Gdx.files.internal(path));
            frames = TextureRegion.split(texture, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load character sprites.");
        }
    }

    /**
     * This method is used to retreive the correct sprite corresponding to what
     * animation we want.
     * 
     * @param row is the row we want to retreive the animation from
     * @param col is the col we want to retreive the animation from
     * @return a TextureRegion which is the graphic/picture we want
     */
    public TextureRegion getSprite(int row, int col) {
        return frames[row][col];
    }

}
