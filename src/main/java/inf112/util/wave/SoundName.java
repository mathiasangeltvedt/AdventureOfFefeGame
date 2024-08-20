package inf112.util.wave;

/**
 * This is the different types of sound the game has for the different actions.
 */
public enum SoundName {
    DASH("dash.wav"),
    HURT("hurt.wav"),
    JUMP("jump.wav"),
    POWERUP("powerup.wav"),
    COLLECT_COIN("collectCoin.wav"),
    SKIN_CHANGE("skinChange.wav"),
    MENU("menuSound.mp3"),
    INGAME("gameSound.mp3");

    public String path;

    private SoundName(String path) {
        this.path = path;
    }
}
