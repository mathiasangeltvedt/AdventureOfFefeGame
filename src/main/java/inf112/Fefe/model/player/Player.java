package inf112.Fefe.model.player;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import inf112.Fefe.controller.DashDir;
import inf112.Fefe.controller.Key;
import inf112.Fefe.model.SpriteParser;
import inf112.Fefe.model.contactListeners.ClimbDir;
import inf112.Fefe.model.contactListeners.GameSensors;
import inf112.Fefe.model.player.animation.Animation;
import inf112.Fefe.model.player.animation.IAnimation;
import inf112.util.UtilityMethods;

/**
 * This class represents the player and the different things the player can do:
 * - Dash
 * - Move
 * - Climb
 * - Jump
 */
public class Player implements ContactablePlayer, ModdablePlayer {
    private static final float RUNNING_VEL = 7, JUMP_VEL = 23.0f, DASH_VEL = 4f, CLIMB_VEL = 6;

    public static final float WIDTH = 0.75f, HEIGHT = 1.2f, PIXIE_DIM_WIDTH = 1.5f, PIXIE_DIM_HEIGHT = 1.5f;
    private int jumpBuffer = 0, coyoteTime = 5, dashTick = 0, climbJumpTimer = 0, moveBuffer = 0;
    private Key verKey = Key.NONE, horKey = Key.NONE;
    private ClimbDir climbDir = ClimbDir.NONE;
    private DashDir dashDir = DashDir.NONE;
    private boolean isGrounded = false, isClimbing = false, canClimb = false, canDash = false, isDead = false;
    private Body body;

    private IAnimation idle = new Animation(), runningLeft = new Animation(), runningRight = new Animation(),
            climbingLeft = new Animation(), climbingRight = new Animation();
    private Vector2 spawnPos = new Vector2(3, 2);

    public Player(World world) {
        BodyDef bd = new BodyDef();
        bd.fixedRotation = true;
        bd.type = BodyType.DynamicBody;
        bd.position.set(2, 2);
        body = world.createBody(bd);
        verKey = Key.NONE;
        horKey = Key.NONE;

        // hitbox
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH / 2, HEIGHT / 2);
        FixtureDef hitbox = new FixtureDef();
        hitbox.shape = shape;
        hitbox.density = 1.0f;
        hitbox.friction = 0f;
        Fixture f = body.createFixture(hitbox);
        f.setUserData(GameSensors.BODY.sensor);
        shape.dispose();

        Vector2[] feetVerts = new Vector2[] {
                new Vector2(-WIDTH / 2 + 0.05f, -HEIGHT / 2), // top left
                new Vector2(WIDTH / 2 - 0.05f, -HEIGHT / 2), // top right
                new Vector2(-WIDTH / 2 + 0.05f, -HEIGHT / 2 - 0.1f), // bottom left
                new Vector2(WIDTH / 2 - 0.05f, -HEIGHT / 2 - 0.1f) // bottom right
        }, leftHandVerts = new Vector2[] {
                new Vector2(-WIDTH / 2 - 0.03f, HEIGHT / 2 - 0.05f), // top left
                new Vector2(-WIDTH / 2, HEIGHT / 2 - 0.05f), // top right
                new Vector2(-WIDTH / 2 - 0.03f, -HEIGHT / 2 + 0.05f), // bottom left
                new Vector2(-WIDTH / 2, -HEIGHT / 2 + 0.05f) // bottom right
        }, rightHandVerts = new Vector2[] {
                new Vector2(WIDTH / 2, HEIGHT / 2 - 0.05f), // top left
                new Vector2(WIDTH / 2 + 0.03f, HEIGHT / 2 - 0.05f), // top right
                new Vector2(WIDTH / 2, -HEIGHT / 2 + 0.05f), // bottom left
                new Vector2(WIDTH / 2 + 0.03f, -HEIGHT / 2 + 0.05f) // bottom right
        };
        initSensor(feetVerts, GameSensors.FEET.sensor);
        initSensor(leftHandVerts, GameSensors.HAND_L.sensor);
        initSensor(rightHandVerts, GameSensors.HAND_R.sensor);
    }

    @Override
    public void tick() {
        if (--jumpBuffer > 0 && isGrounded)
            jump(false);
        if (!isGrounded)
            --coyoteTime;
        if (--dashTick > 0) {
            body.setLinearVelocity(getDashVel());
        }
        if (dashTick == 0) {
            dashDir = DashDir.NONE;
            body.setGravityScale(1);
        }
        if (--moveBuffer <= 0) {
            move();
        }
    }

    @Override
    public void setCanClimb(boolean canClimb, ClimbDir direction) {
        this.canClimb = canClimb;
        this.climbDir = direction;
        if (!canClimb) {
            isClimbing = false;
        }
    }

    @Override
    public boolean climb() {
        if (canClimb && climbJumpTimer <= 0) {
            body.setLinearVelocity(0, 0);
            body.setGravityScale(0);
            isClimbing = true;
            return true;
        }
        return false;
    }

    @Override
    public void cancelClimb() {
        if (dashTick <= 0) {
            body.setGravityScale(1);
        }
        if (isClimbing) {
            isClimbing = false;
            body.setGravityScale(1);
        }

    }

    @Override
    public boolean jump(boolean resetJumpBuffer) {
        if (isClimbing) {
            cancelClimb();
            body.setLinearVelocity(RUNNING_VEL * (climbDir == ClimbDir.LEFT ? 1 : -1), JUMP_VEL);
            climbJumpTimer = 7;
            return true;
        } else if (isGrounded || (coyoteTime > 0 && body.getLinearVelocity().y <= 0)) {
            body.setLinearVelocity(body.getLinearVelocity().x, JUMP_VEL);
            isGrounded = false;
            return true;
        } else if (resetJumpBuffer)
            jumpBuffer = 5;
        return false;
    }

    @Override
    public boolean dash(DashDir dir) {
        if (dashTick > 0 || dir == DashDir.NONE || !canDash)
            return false;
        canDash = false;
        dashDir = dir;
        body.setGravityScale(0);
        dashTick = 14;
        body.setLinearVelocity(getDashVel());
        return true;
    }

    @Override
    public void setMovementKeys(Key verKey, Key horKey) {
        this.verKey = verKey;
        this.horKey = horKey;
    }

    @Override
    public void setIsGrounded(boolean isGrounded) {
        if (isGrounded) {
            canDash = true;
            coyoteTime = 5;
        }
        this.isGrounded = isGrounded;
    }

    @Override
    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    @Override
    public void respawn() {
        isDead = false;
        body.setTransform(spawnPos, 0);
        body.setLinearVelocity(0, 0);
        dashTick = 0;
        isGrounded = false;
        isClimbing = false;
        canClimb = false;
        canDash = true;
        isDead = false;
        moveBuffer = 20;
    }

    @Override
    public void setSpawnPos(Vector2 spawnPos) {
        this.spawnPos = spawnPos;
    }

    @Override
    public void setCanDash(boolean canDash) {
        this.canDash = canDash;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public int getDashTick() {
        return dashTick;
    }

    @Override
    public DashDir getDashDir() {
        return dashDir;
    }

    public boolean isClimbing() {
        return isClimbing;
    }

    @Override
    public boolean getIsDead() {
        return isDead;
    }

    @Override
    public DashValues getDashValues() {
        SpriteParser parser = new SpriteParser("images/game/pixieDustTransform.png");
        TextureRegion dash;
        Vector2 pos = null;
        switch (dashDir) {
            case NORTH:
                dash = parser.getSprite(0, 1);
                pos = new Vector2(-WIDTH - 0.1f, -HEIGHT - PIXIE_DIM_HEIGHT);
                break;
            case SOUTH:
                dash = parser.getSprite(0, 0);
                pos = new Vector2(-WIDTH + 0.1f, +HEIGHT / 2 + 0.5f);
                break;
            case WEST:
                dash = parser.getSprite(1, 0);
                pos = new Vector2(PIXIE_DIM_WIDTH, -0.25f);
                break;
            case EAST:
                dash = parser.getSprite(1, 1);
                pos = new Vector2(-WIDTH - PIXIE_DIM_WIDTH - 0.5f, -0.25f);
                break;
            case NORTHEAST:
                dash = parser.getSprite(3, 1);
                pos = new Vector2(-WIDTH - PIXIE_DIM_WIDTH - 0.5f, -HEIGHT - PIXIE_DIM_HEIGHT + 0.5f);
                break;
            case NORTHWEST:
                dash = parser.getSprite(2, 0);
                pos = new Vector2(WIDTH / 2, -HEIGHT - PIXIE_DIM_HEIGHT + 0.5f);
                break;
            case SOUTHEAST:
                dash = parser.getSprite(3, 0);
                pos = new Vector2(-WIDTH - PIXIE_DIM_WIDTH - 0.5f, HEIGHT / 2 + 0.5f);
                break;
            case SOUTHWEST:
                dash = parser.getSprite(2, 1);
                pos = new Vector2(WIDTH / 2, HEIGHT / 2 + 0.5f);
                break;
            default:
                throw new IllegalArgumentException("dashDir is NONE");
        }
        return new DashValues(pos, dash);
    }

    @Override
    public TextureRegion getSprite() {
        if (isClimbing) {
            if (verKey == Key.NONE)
                return climbDir == ClimbDir.RIGHT ? climbingRight.getSprite(false) : climbingLeft.getSprite(false);
            return climbDir == ClimbDir.RIGHT ? climbingRight.getSprite(true) : climbingLeft.getSprite(true);
        }

        if (Math.abs(body.getLinearVelocity().x) < 1e-5) {
            if (Math.abs(body.getLinearVelocity().y) < 1e-5) {
                return idle.getSprite(true);
            }
            idle.restartSprite();
            return idle.getSprite(false);
        }
        idle.restartSprite();
        boolean isFacingLeft = body.getLinearVelocity().x < 0;
        return isFacingLeft ? runningLeft.getSprite(true) : runningRight.getSprite(true);
    }

    private void move() {
        if (dashTick > 0)
            return;
        Vector2 pos = body.getPosition();
        Vector2 vel = body.getLinearVelocity();

        if (isClimbing) {
            switch (verKey) {
                case NONE:
                    body.setLinearVelocity(vel.x, 0);
                    break;
                case UP:
                    body.setLinearVelocity(vel.x, CLIMB_VEL);
                    break;
                case DOWN:
                    body.setLinearVelocity(vel.x, -CLIMB_VEL);
                    break;
                default:
                    break;
            }
        }
        vel = body.getLinearVelocity();
        if (--climbJumpTimer > 0)
            return;
        switch (horKey) {
            case NONE:
                if (Math.abs(vel.x / 1.5f) < 5e-1)
                    body.setLinearVelocity(0, vel.y);
                else
                    body.setLinearVelocity(vel.x / 1.5f, vel.y);
                break;
            case RIGHT:
                body.applyLinearImpulse(new Vector2(0.8f, 0), pos, true);
                break;
            case LEFT:
                body.applyLinearImpulse(new Vector2(-0.8f, 0), pos, true);
                break;
            default:
                break;
        }
        vel = body.getLinearVelocity();
        body.setLinearVelocity(Math.signum(vel.x) * Math.min(Player.RUNNING_VEL, Math.abs(vel.x)), vel.y);
    }

    private Vector2 getDashVel() {
        Matrix3 rotationMatrix = new Matrix3();
        rotationMatrix = rotationMatrix.setToRotationRad(UtilityMethods.degreesToRadians(dashDir.rotation));
        Vector2 vel = new Vector2(DASH_VEL, 0);
        return vel.mul(rotationMatrix).scl(dashTick);
    }

    private void initSensor(Vector2[] verts, int sensorId) {
        PolygonShape shape = new PolygonShape();
        shape.set(verts);
        FixtureDef feetSensor = new FixtureDef();
        feetSensor.shape = shape;
        feetSensor.density = 0f;
        feetSensor.friction = 0f;
        feetSensor.isSensor = true;
        Fixture fix = body.createFixture(feetSensor);
        fix.setUserData(sensorId);
        shape.dispose();
    }

    @Override
    public void initSprites(int coin) {
        SpriteParser parser;
        switch (coin) {
            case 10:
                parser = new SpriteParser("images/game/playerSprites/playerSprite10.png");
                break;
            case 50:
                parser = new SpriteParser("images/game/playerSprites/playerSprite50.png");
                break;
            case 100:
                parser = new SpriteParser("images/game/playerSprites/playerSprite100.png");
                break;
            default:
                parser = new SpriteParser("images/game/playerSprites/playerSprite.png");
                break;
        }
        idle.resetSprite();
        runningLeft.resetSprite();
        runningRight.resetSprite();
        climbingLeft.resetSprite();
        climbingRight.resetSprite();
        for (int i = 0; i < 4; ++i) {
            if (i == 0) {
                idle.addSprite(parser.getSprite(0, i), 300);
            } else
                idle.addSprite(parser.getSprite(0, i), 15);

        }
        for (int i = 0; i < 2; ++i) {
            runningLeft.addSprite(parser.getSprite(1, i), 15);
            runningRight.addSprite(parser.getSprite(2, i + 2), 15);

            climbingLeft.addSprite(parser.getSprite(1, i + 2), 15);
            climbingRight.addSprite(parser.getSprite(2, i), 15);

        }

    }

}
