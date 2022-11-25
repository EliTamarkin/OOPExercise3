package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Heart;


public class HeartCreationStrategy extends CollisionStrategy {

    private static final String HEART_IMAGE_PATH = "assets/heart.png";

    private static final int HEART_FALL_SPEED = 100;

    private static final Vector2 HEART_FALL_DIRECTION = new Vector2(0, 1);

    private final WindowController windowController;
    private final Vector2 heartDimensions;
    private final ImageRenderable heartImage;

    private final Counter livesCounter;

    /**
     * Constructs a new CollisionStrategy instance.
     *
     * @param gameObjects the games objects used for adding or removing objects from the game
     */
    public HeartCreationStrategy(GameObjectCollection gameObjects, Vector2 heartDimensions,
                                 ImageReader imageReader, WindowController windowController,
                                 Counter livesCounter) {
        super(gameObjects);
        this.windowController = windowController;
        this.heartDimensions = heartDimensions;
        this.heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        this.livesCounter = livesCounter;
    }

    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        Heart heart = new Heart(Vector2.ZERO, heartDimensions, heartImage, windowController,
                gameObjects, livesCounter);
        heart.setCenter(collidedObj.getCenter());
        heart.setVelocity(HEART_FALL_DIRECTION.mult(HEART_FALL_SPEED));
        gameObjects.addGameObject(heart);
    }
}
