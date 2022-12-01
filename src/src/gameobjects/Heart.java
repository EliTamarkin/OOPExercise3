package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Heart class in charge of representing heart objects at the game
 * @author Eliyahu Tamarkin
 */
public class Heart extends GameObject {

    private static final int MAX_HEARTS = 4;
    private final WindowController windowController;
    private final GameObjectCollection gameObjects;
    private final Counter livesCounter;

    /**
     * Constructs a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param windowController window controller of the game
     * @param gameObjects the current game's game objects
     * @param livesCounter current live counter of the game
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 WindowController windowController, GameObjectCollection gameObjects,
                 Counter livesCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.windowController = windowController;
        this.gameObjects = gameObjects;
        this.livesCounter = livesCounter;
    }

    /**
     * Determines which object the heart should collide with, in ther case we would only want to collide
     * with the main paddle of the game.
     * @param other The other GameObject to be collided with
     * @return true in case it should collide and false otherwise
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle && !(other instanceof SecondaryPaddle);
    }

    /**
     * Determines whether a heart that has collided with the paddle (since it is the only object it
     * may collide with) should increase the counter or not based upon the maximum amount of lives.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(livesCounter.value() < MAX_HEARTS){
            livesCounter.increment();
            gameObjects.removeGameObject(this);
        }
    }

    /**
     * Removes the heart whenever it is out of the screen's coordinates
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getTopLeftCorner().y() > windowController.getWindowDimensions().y()){
            gameObjects.removeGameObject(this);
        }
    }
}
