package src.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

/**
 * Collision strategy interface that all other strategies implement.
 * @author Eliyahu Tamarkin
 */
public interface CollisionStrategy {
    /**
     * The following function decides the behaviour of the strategy which needs to be acted
     * @param collidedObj collided object
     * @param colliderObj collider object
     * @param bricksCounter bricks counter
     */
    void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter);
}
