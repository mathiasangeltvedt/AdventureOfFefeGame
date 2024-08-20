package inf112.util.wave;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * 
 * Plays the right sound when the right action is played.
 * 
 */

public class SoundManager {

    private Map<String, Sound> sounds = new HashMap<>();
    private Map<String, Music> music = new HashMap<>();
    private String[] soundPaths = { "dash.wav", "hurt.wav", "jump.wav", "powerup.wav", "collectCoin.wav",
            "skinChange.wav" };
    private String[] musicPaths = { "menuSound.mp3", "gameSound.mp3" };
    private Music playingSong;

    /**
     * This constructor is used to initiate a SoundManager object for the game
     */
    public SoundManager() {
        for (String path : soundPaths) {
            sounds.put(path, Gdx.audio.newSound(Gdx.files.internal("sounds/" + path)));
        }
        for (String path : musicPaths) {
            music.put(path, Gdx.audio.newMusic(Gdx.files.internal("sounds/" + path)));
        }

    }

    /**
     * This method is used to play sound/music in the game depending on different
     * actions or the state of the game
     * 
     * @param soundName  is the name of the sound/music we want to play
     * @param shouldLoop is a boolean value which decides if we want the sound/music
     *                   to
     *                   loop or not
     * @param volume     is a float value which is used to decide the volume of the
     *                   sound/music
     */
    public void playSound(String path, boolean shouldLoop, float volume) throws IllegalArgumentException {
        if (sounds.containsKey(path)) {
            sounds.get(path).setVolume(0, volume);
            sounds.get(path).play();
        } else if (music.containsKey(path)) {
            if (playingSong != null && !playingSong.equals(music.get(path)))
                playingSong.stop();
            playingSong = music.get(path);
            playingSong.setVolume(volume);
            playingSong.setLooping(shouldLoop);
            playingSong.play();

        } else
            new IllegalArgumentException(path + " is not a valid path. Perhaps the spelling is wrong?");
    }

    /**
     * This method is used to dispose of music/sounds that are no longer used
     */
    public void dispose() {
        for (Sound sound : sounds.values())
            sound.dispose();
        for (Music song : music.values())
            song.dispose();
    }
}