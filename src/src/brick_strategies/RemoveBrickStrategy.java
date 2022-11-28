package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;
import src.gameobjects.Brick;

/**
 * The following class represents the collision strategy acted upon collision between objects.
 * @author Eliyahu Tamarkin
 */
public class RemoveBrickStrategy implements CollisionStrategy{
    protected final GameObjectCollection gameObjects;

    /**
     * Constructs a new CollisionStrategy instance.
     * @param gameObjects   the games objects used for adding or removing objects from the game
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjects){
        this.gameObjects = gameObjects;
    }

    /**
     * action to be done upon collision
     * @param collidedObj   object that has been collided
     * @param colliderObj   object that is the collider
     * @param bricksCounter bricks counter of the game
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter){
        this.gameObjects.removeGameObject(collidedObj, Layer.STATIC_OBJECTS);
        bricksCounter.decrement();
    }
}
