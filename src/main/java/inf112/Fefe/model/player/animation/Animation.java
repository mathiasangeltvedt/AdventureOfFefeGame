package inf112.Fefe.model.player.animation;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation implements IAnimation {

    private Queue<SpriteElem> sprites = new LinkedList<>();
    private Queue<SpriteElem> reset = new LinkedList<>();

    @Override
    public void addSprite(TextureRegion sprite, int cycle) {
        sprites.add(new SpriteElem(cycle, sprite));
        reset.add(new SpriteElem(cycle, sprite));
    }

    @Override
    public TextureRegion getSprite(boolean shouldTick) {
        if (shouldTick && sprites.peek().tick()) {
            sprites.add(sprites.poll());
        }
        return sprites.peek().getSprite();
    }

    @Override
    public void restartSprite() {
        sprites = new LinkedList<>(reset);
    }

    /**
     * This method is used to reset the sprite list to the original state
     */
    @Override
    public void resetSprite() {
        sprites = new LinkedList<>();
        reset = new LinkedList<>();
    }
}
