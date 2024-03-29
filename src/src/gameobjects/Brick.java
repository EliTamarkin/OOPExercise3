package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * The following class represents the brick of the bricker game.
 * @author Eliyahu Tamarkin
 */
public class Brick extends GameObject {
    private static final String DESTROYED_TAG = "Destroyed";
    private final CollisionStrategy collisionStrategy;
    private final Counter counter;

    /**
     * Constructs a new Brick instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionStrategy collision strategy to be played upon collision
     * @param counter       counter that determines the amount of bricks in the game
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.counter = counter;
    }

    /**
     * determines the bricks behaviour upon collision with other objects
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (this.getTag().equals(DESTROYED_TAG)){
            return;
        }
        collisionStrategy.onCollision(this, other, counter);
        this.setTag(DESTROYED_TAG);
    }
}
