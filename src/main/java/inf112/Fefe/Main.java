package inf112.Fefe;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import inf112.Fefe.controller.GameController;
import inf112.Fefe.model.GameModel;
import inf112.Fefe.view.GameView;

public class Main {
    public static final int WINDOW_WIDTH = 960, WINDOW_HEIGHT = 720;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        GameModel model = new GameModel();
        cfg.setTitle("FEFE");
        cfg.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        cfg.setResizable(false);
        new Lwjgl3Application(new GameView(model, new GameController(model)), cfg);
    }
}