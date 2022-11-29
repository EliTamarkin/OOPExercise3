package src.gameobjects;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The following class represents the ball of the bricker game.
 * @author Eliyahu Tamarkin
 */
public class Ball extends GameObject {
    private final Sound collisionSound;

    private int collisionNum;

    /**
     * Constructs a new Ball instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param sound         The sound the ball makes upon collision with other objects
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = sound;
        this.collisionNum = 0;
    }

    /**
     * determines the balls behaviour upon collision with other objects
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionNum++;
        setVelocity(getVelocity().flipped(collision.getNormal()));
        collisionSound.play();

    }

    /**
     * Collision counter getter
     * @return amount of collisions
     */
    public int getCollisionCount(){ return this.collisionNum; }
}
