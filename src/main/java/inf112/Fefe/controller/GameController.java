package inf112.Fefe.controller;

import com.badlogic.gdx.InputAdapter;
import inf112.Fefe.model.GameState;

import javax.swing.Timer;
import java.awt.event.ActionEvent;

/**
 * This is the controller for the player. This is where everything is controlled
 * by buttons which are activated and deactivated.
 */
public class GameController extends InputAdapter {

    ControllableGameModel model;
    Timer timer;
    Key horAct = Key.NONE;
    Key verAct = Key.NONE;
    boolean kIsHeld = false;

    /**
     * Is used to create a new controller for the game
     * 
     * @param model is the model we will use for the game
     */
    public GameController(ControllableGameModel model) {
        this.model = model;
        timer = new Timer(1000 / 120, this::tick);
        timer.start();
    }

    @Override
    public boolean keyDown(int arg0) {
        if (model.getGameState() == GameState.PLAYING) {

            switch (arg0) {

                case 29: // left A
                    horAct = Key.LEFT;
                    break;
                case 32: // right D
                    horAct = Key.RIGHT;
                    break;
                case 51: // up W
                    verAct = Key.UP;
                    break;
                case 47: // down S
                    verAct = Key.DOWN;
                    break;
                case 62: // jump Space
                    model.jump();
                    break;
                case 39: // climb k
                    kIsHeld = true;
                    break;
                case 38: // dash j
                    model.dash(determineDashDirection());
                    break;
                case 50:
                    if (model.isDebug()) {
                        model.setDebug(false);
                        break;
                    }
                    model.setDebug(true);
                    break;
                case 111:
                    model.setGameState(GameState.PAUSE);
                    break;
            }
        } else if (model.getGameState() == GameState.PAUSE) {
            switch (arg0) {
                case 111:
                    model.setGameState(GameState.PLAYING);
            }
        } else if (model.getGameState() == GameState.INFO) {
            switch (arg0) {
                case 111:
                    model.setGameState(GameState.MAIN_MENU);
            }
        } else if (model.getGameState() == GameState.CONTROLS) {
            switch (arg0) {
                case 111:
                    model.setGameState(GameState.MAIN_MENU);
            }
        }
        return true;
    }

    private DashDir determineDashDirection() {
        switch (horAct) {
            case LEFT:
                if (verAct == Key.UP) {
                    return DashDir.NORTHWEST;
                } else if (verAct == Key.DOWN) {
                    return DashDir.SOUTHWEST;
                }
                return DashDir.WEST;
            case RIGHT:
                if (verAct == Key.UP) {
                    return DashDir.NORTHEAST;
                } else if (verAct == Key.DOWN) {
                    return DashDir.SOUTHEAST;
                }
                return DashDir.EAST;
            default:
                if (verAct == Key.UP)
                    return DashDir.NORTH;
                else if (verAct == Key.DOWN)
                    return DashDir.SOUTH;
                return DashDir.NONE;
        }
    }

    @Override
    public boolean keyUp(int arg0) {
        switch (arg0) {
            case 29: // LEFT (A)
                if (horAct == Key.LEFT)
                    horAct = Key.NONE;
                break;
            case 32: // RIGHT (D)
                if (horAct == Key.RIGHT)
                    horAct = Key.NONE;
                break;
            case 51: // UP (W)
                if (verAct == Key.UP)
                    verAct = Key.NONE;
                break;
            case 47: // DOWN(S)
                if (verAct == Key.DOWN)
                    verAct = Key.NONE;
                break;
            case 39:
                kIsHeld = false;
                break;
        }
        return true;
    }

    private void tick(ActionEvent e) {
        try {
            model.setPlayerKeys(horAct, verAct);
            if (kIsHeld)
                model.climb();
            else
                model.cancelClimb();
        } catch (NullPointerException err) {
            // ignore, player is not initialized yet
        }
    }

}
