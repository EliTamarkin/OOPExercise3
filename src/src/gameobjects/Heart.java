package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Heart extends GameObject {

    private static final int MAX_HEARTS = 4;
    private final WindowController windowController;
    private final GameObjectCollection gameObjects;
    private final Counter livesCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 WindowController windowController, GameObjectCollection gameObjects,
                 Counter livesCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.windowController = windowController;
        this.gameObjects = gameObjects;
        this.livesCounter = livesCounter;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle && !(other instanceof SecondaryPaddle);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(livesCounter.value() < MAX_HEARTS){
            livesCounter.increment();
            gameObjects.removeGameObject(this);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getDimensions().y() > windowController.getWindowDimensions().y()){
            gameObjects.removeGameObject(this);
        }
    }
}
