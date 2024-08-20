package inf112.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * This class contains methods we can utilize for smaller parts of the game
 */
public class UtilityMethods {

    /**
     * A method to calculate the number on a given position in an integer
     * 
     * @param number is the given integer
     * @param pos    is the index/position in the integer we want to retreive
     * @return the number in the given position/index of the integer
     */
    public static int findDigitVal(int number, int pos) {
        return (int) (number / Math.pow(10, pos)) % 10;
    }

    /**
     * A method for converting degrees to radians
     * 
     * @param degrees the given degrees
     * @return the degrees represented in radians
     */
    public static float degreesToRadians(float degrees) {
        return degrees * (float) Math.PI / 180f;
    }

    /**
     * Call to load a json file from resources and get it as a {@link JsonValue}
     * object.
     * Must not be called until {@link Gdx} has loaded.
     * 
     * @param path path to the json file
     * @return a {@link JsonValue} object of the json file
     */
    public static JsonValue loadJson(String path) {
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal(path));
        return base;
    }

    public static boolean almostEqual(float f1, float f2) {
        return Math.abs(f1 - f2) <= 1e-5;
    }
}
