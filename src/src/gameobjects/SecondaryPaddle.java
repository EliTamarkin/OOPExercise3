package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class SecondaryPaddle extends Paddle{

    public static int numOfInstances = 0;
    private static final int MAX_HIT_COUNT = 3;
    private int hitCount;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a new SecondaryPaddle instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    input listener to listen to the users key requests
     * @param windowDimensions window dimensions of the game
     * @param minDistFromEdge  minimal distance allowed to be from the games left and right borders
     */
    public SecondaryPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                           UserInputListener inputListener, Vector2 windowDimensions,
                           int minDistFromEdge, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);
        this.hitCount = 0;
        this.gameObjects = gameObjects;
        SecondaryPaddle.numOfInstances++;
    }

    /**
     * Updates the hit count of the given paddle and destroys when the maximal hit amount was met
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball){
            hitCount++;
        }
        if(hitCount >= MAX_HIT_COUNT){
            this.gameObjects.removeGameObject(this);
            numOfInstances--;
        }
    }
}
