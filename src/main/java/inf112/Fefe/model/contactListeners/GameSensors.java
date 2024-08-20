package inf112.Fefe.model.contactListeners;

public enum GameSensors {
    FEET(1),
    HAND_L(2),
    HAND_R(3),
    BODY(4),
    GROUND(5),
    SPIKE(6),
    LOADING_ZONE(7),
    DOUBLE_DASH(8),
    ANTI_GRAVITATION(9),
    COLLECTIBLE(10),
    INFO(11);

    public int sensor;

    private GameSensors(int sensor) {
        this.sensor = sensor;
    }
}
