package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Brick removal strategy class.
 * In charge of removing bricks from the bricks counter whenever a brick is being hit.
 * @author Eliyahu Tamarkin
 */
public class RemoveBrickStrategy implements CollisionStrategy{
    protected final GameObjectCollection gameObjects;

    /**
     * Constructs a new RemoveBrickStrategy instance.
     * @param gameObjects   the games objects used for adding or removing objects from the game
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjects){
        this.gameObjects = gameObjects;
    }

    /**
     * Removes a brick upon collision and decrements the bricks counter
     * @param collidedObj   object that has been collided
     * @param colliderObj   object that is the collider
     * @param bricksCounter bricks counter of the game
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter){
        this.gameObjects.removeGameObject(collidedObj, Layer.STATIC_OBJECTS);
        bricksCounter.decrement();
    }
}
