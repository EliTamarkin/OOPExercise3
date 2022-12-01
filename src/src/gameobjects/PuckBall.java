package src.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Puck Ball class which is in charge of representing the puck balls of the game
 * @author Eliyahu Tamarkin
 */
public class PuckBall extends Ball{
    private final WindowController windowController;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a new PuckBall instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param sound         The sound the ball makes upon collision with other objects
     * @param windowController game window controller
     */
    public PuckBall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound,
                    WindowController windowController, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, sound);
        this.windowController = windowController;
        this.gameObjects = gameObjects;
    }

    /**
     * In charge of removing the puck ball from the game whenever it is out of the screen
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
        if(this.getTopLeftCorner().y() > windowController.getWindowDimensions().y()){
            gameObjects.removeGameObject(this);
        }
    }
}
