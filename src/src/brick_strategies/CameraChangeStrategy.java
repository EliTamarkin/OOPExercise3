package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;


/**
 * Camera strategy class.
 * In charge of initiating the special camera when requested.
 * @author Eliyahu Tamarkin
 */
public class CameraChangeStrategy implements CollisionStrategy {

    private final CollisionStrategy decoratedStrategy;
    private final GameObjectCollection gameObjects;

    /**
     * Camera Manager class
     * The following class is a game object that is added to the game in order to control when camera
     * behaviour should be stopped. It checks every update whether the ball has had the requested amount
     * of hits before reverting the camera back to normal behaviour.
     */
    private class CameraManager extends GameObject{

        private final int collisionCount;

        /**
         * Construct a new CameraManager instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         *                      the GameObject will not be rendered.
         */
        public CameraManager(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
            super(topLeftCorner, dimensions, renderable);
            collisionCount = objectToFollow.getCollisionCount();
        }

        /**
         * update function to check the balls current collisions number the ball has had and reverts
         * the camera back to normal behaviour when the requested amount of hits is met.
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
            int updatedCollisionsNum = objectToFollow.getCollisionCount();
            if (updatedCollisionsNum - collisionCount - 1 >= MAX_OBJECT_COLLISIONS &&
                    gameManager.getCamera() != null){
                gameManager.setCamera(null);
                gameObjects.removeGameObject(this);
            }
        }
    }

    private static final int MAX_OBJECT_COLLISIONS = 4;

    private static final float FRAME_WIDEN_PARAMETER = 1.2f;
    private final GameManager gameManager;
    private final WindowController windowController;
    private final Ball objectToFollow;


    /**
     * Constructs a new CameraChangeStrategy instance
     * @param decoratedStrategy inner strategy
     * @param gameObjects game objects
     * @param gameManager game manager
     * @param windowController window controller
     * @param objectToFollow object to cast the camera behaviour upon
     */
    public CameraChangeStrategy(CollisionStrategy decoratedStrategy, GameObjectCollection gameObjects,
                                GameManager gameManager, WindowController windowController,
                                Ball objectToFollow) {
        this.decoratedStrategy = decoratedStrategy;
        this.gameObjects = gameObjects;
        this.gameManager = gameManager;
        this.windowController = windowController;
        this.objectToFollow = objectToFollow;
    }

    /**
     * initiates the cameras special behaviour upon being requested. Behaviour is initiated only if it
     * is not currently being initiated
     * @param collidedObj collided object
     * @param colliderObj collider object
     * @param bricksCounter bricks counter
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        decoratedStrategy.onCollision(collidedObj, colliderObj, bricksCounter);
        if (gameManager.getCamera() == null && colliderObj == objectToFollow){
            gameManager.setCamera(new Camera(objectToFollow, Vector2.ZERO,
                    windowController.getWindowDimensions().mult(FRAME_WIDEN_PARAMETER),
                    windowController.getWindowDimensions()));
            CameraManager cameraManager = new CameraManager(Vector2.ZERO, Vector2.ZERO, null);
            gameObjects.addGameObject(cameraManager);
        }
    }
}
