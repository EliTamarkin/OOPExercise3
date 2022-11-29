package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Heart;

/**
 * Additional hearts strategy class.
 * In charge of initiating more hearts when requested.
 * @author Eliyahu Tamarkin
 */
public class HeartCreationStrategy implements CollisionStrategy {

    private static final String HEART_IMAGE_PATH = "assets/heart.png";

    private static final int HEART_FALL_SPEED = 100;

    private static final Vector2 HEART_FALL_DIRECTION = new Vector2(0, 1);

    private final WindowController windowController;
    private final Vector2 heartDimensions;
    private final ImageRenderable heartImage;

    private final Counter livesCounter;
    private final CollisionStrategy decoratedStrategy;
    private final GameObjectCollection gameObjects;


    /**
     * Constructs a new heart strategy instance
     * @param decoratedStrategy inner strategy
     * @param gameObjects game objects
     * @param heartDimensions heart instance dimensions
     * @param imageReader image reader
     * @param windowController window controller
     * @param livesCounter lives counter
     */
    public HeartCreationStrategy(CollisionStrategy decoratedStrategy,
                                 GameObjectCollection gameObjects, Vector2 heartDimensions,
                                 ImageReader imageReader, WindowController windowController,
                                 Counter livesCounter) {
        this.decoratedStrategy = decoratedStrategy;
        this.gameObjects = gameObjects;
        this.windowController = windowController;
        this.heartDimensions = heartDimensions;
        this.heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        this.livesCounter = livesCounter;
    }

    /**
     * Creates a new heart whenever called with the parameters given at the constructor
     * @param collidedObj collided object
     * @param colliderObj collider object
     * @param bricksCounter bricks counter
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        decoratedStrategy.onCollision(collidedObj, colliderObj, bricksCounter);
        Heart heart = new Heart(Vector2.ZERO, heartDimensions, heartImage, windowController,
                gameObjects, livesCounter);
        heart.setCenter(collidedObj.getCenter());
        heart.setVelocity(HEART_FALL_DIRECTION.mult(HEART_FALL_SPEED));
        gameObjects.addGameObject(heart);
    }
}
