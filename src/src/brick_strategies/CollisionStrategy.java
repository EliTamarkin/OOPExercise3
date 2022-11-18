package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class CollisionStrategy {
    private GameObjectCollection gameObjects;

    public CollisionStrategy(GameObjectCollection gameObjects){

        this.gameObjects = gameObjects;
    }

    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter){
        this.gameObjects.removeGameObject(collidedObj);
        bricksCounter.decrement();

    }
}
