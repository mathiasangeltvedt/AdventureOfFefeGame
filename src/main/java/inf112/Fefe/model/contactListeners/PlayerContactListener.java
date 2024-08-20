package inf112.Fefe.model.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import inf112.Fefe.model.player.ContactablePlayer;

/**
 * This class is used to create a ContactListener for the Player
 * This is used to check if the player is on the ground and/or the player can
 * climb
 */
public class PlayerContactListener implements ContactListener {
    private ContactablePlayer player;
    private ChangeableGameModel model;
    private int body, feet, handR, handL, ground, spike, loadingZone, doubleDash, collectible, info;

    /**
     * Used to create a new ContactListener for the player
     * 
     * @param player is the player we want to create a new ContactListener for
     */
    public PlayerContactListener(ContactablePlayer player, ChangeableGameModel model) {
        this.player = player;
        this.model = model;
    }

    private int isSensor(Fixture f1, Fixture f2, int sensorId) {
        if (f1.isSensor() && f2.isSensor())
            return 0;
        if (f1.getUserData() != null && (Integer) f1.getUserData() == sensorId)
            return 1;
        if (f2.getUserData() != null && (Integer) f2.getUserData() == sensorId)
            return 1;
        return 0;
    }

    @Override
    public void beginContact(Contact contact) {

        updateContactBegin(contact);

        // check if player hits spikes
        if (spike == 1 && body == 1)
            player.setIsDead(true);

        // check if player is grounded
        if (feet > 0)
            player.setIsGrounded(true);

        // check for contacts on left and right of player
        if (handR > 0) {
            player.setCanClimb(true, ClimbDir.RIGHT);
        } else if (handL > 0) {
            player.setCanClimb(true, ClimbDir.LEFT);
        }

        // check if player bumps into wall while dashing
        if ((handR > 0 || handL > 0) && ground == 1)
            if (player.getBody().getLinearVelocity().x > 10 || player.getBody().getLinearVelocity().x < -10)
                model.playerBump();

        // check if player bumps into platform when dashing up or jumping
        if (body == 1 && ground == 1 && feet == 0 && handR == 0 && handL == 0)
            if (player.getBody().getLinearVelocity().y > 15)
                model.playerBump();

        // check if loading zone is hit
        if (loadingZone == 1 && body == 1) {
            if ((Integer) contact.getFixtureA().getUserData() == GameSensors.LOADING_ZONE.sensor)
                model.loadingZoneHit(contact.getFixtureA().getBody().getPosition());
            else
                model.loadingZoneHit(contact.getFixtureB().getBody().getPosition());
        }

        // check if doubledash powerup is collected
        if (doubleDash == 1 && body == 1) {
            player.setCanDash(true);
            model.powerupHit();
        }

        if (collectible == 1 && body == 1)
            model.collectibleHit();

        if (info > 0 && body == 1) {
            model.wantsInfo(true);
        }

    }

    @Override
    public void endContact(Contact contact) {

        updateContactEnd(contact);

        // check if feet is off the ground
        if (feet == 0)
            player.setIsGrounded(false);

        // check if neither side of the player is touching a wall
        if (handL == 0 && handR == 0) {
            player.setCanClimb(false, ClimbDir.NONE);
            player.cancelClimb();
        }
        if (info == 0) {
            model.wantsInfo(false);
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void updateContactBegin(Contact contact) {
        body = isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.BODY.sensor);
        feet += isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.FEET.sensor);
        handR += isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.HAND_R.sensor);
        handL += isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.HAND_L.sensor);
        ground = isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.GROUND.sensor);
        spike = isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.SPIKE.sensor);
        loadingZone = isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.LOADING_ZONE.sensor);
        doubleDash = isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.DOUBLE_DASH.sensor);
        collectible = isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.COLLECTIBLE.sensor);
        info += isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.INFO.sensor);
    }

    private void updateContactEnd(Contact contact) {
        feet -= isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.FEET.sensor);
        handR -= isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.HAND_R.sensor);
        handL -= isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.HAND_L.sensor);
        info -= isSensor(contact.getFixtureA(), contact.getFixtureB(), GameSensors.INFO.sensor);
    }

}
