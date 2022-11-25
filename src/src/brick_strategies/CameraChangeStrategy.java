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

public class CameraChangeStrategy extends CollisionStrategy{

    private class CameraManager extends GameObject{

        private final int collisionCount;

        /**
         * Construct a new GameObject instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         *                      the GameObject will not be rendered.
         */
        public CameraManager(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
            super(topLeftCorner, dimensions, renderable);
            collisionCount = objectToFollow.getCollisionNum();
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            int updatedCollisionsNum = objectToFollow.getCollisionNum();
            if (updatedCollisionsNum - collisionCount == MAX_OBJECT_COLLISIONS &&
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
     * Constructs a new CollisionStrategy instance.
     *
     * @param gameObjects the games objects used for adding or removing objects from the game
     */
    public CameraChangeStrategy(GameObjectCollection gameObjects, GameManager gameManager,
                                WindowController windowController, Ball objectToFollow) {
        super(gameObjects);
        this.gameManager = gameManager;
        this.windowController = windowController;
        this.objectToFollow = objectToFollow;
    }

    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        if (gameManager.getCamera() == null && colliderObj == objectToFollow){
            gameManager.setCamera(new Camera(objectToFollow, Vector2.ZERO,
                    windowController.getWindowDimensions().mult(FRAME_WIDEN_PARAMETER),
                    windowController.getWindowDimensions()));
            CameraManager cameraManager = new CameraManager(Vector2.ZERO, Vector2.ZERO, null);
            gameObjects.addGameObject(cameraManager);
        }
    }
}
