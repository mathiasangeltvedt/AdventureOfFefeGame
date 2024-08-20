package inf112.Fefe.controller;

/**
 * This class contains the different directions the player can dash toward
 */
public enum DashDir {

    NORTH(90),
    SOUTH(-90),
    WEST(180),
    EAST(0),
    NORTHEAST(45),
    NORTHWEST(135),
    SOUTHEAST(-45),
    SOUTHWEST(-135),
    NONE(0);

    public int rotation;

    private DashDir(int rotation) {
        this.rotation = rotation;
    }

}
