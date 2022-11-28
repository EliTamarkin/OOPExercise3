package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.SecondaryPaddle;

public class PaddleStrategy implements CollisionStrategy {

    private static final String BOT_PADDLE_IMAGE_PATH = "assets/botGood.png";
    private final ImageRenderable paddleImage;
    private final Vector2 paddleDimensions;
    private final UserInputListener inputListener;
    private final Vector2 paddlePosition;
    private final Vector2 windowDimensions;
    private final CollisionStrategy decoratedStrategy;
    private final GameObjectCollection gameObjects;

    public PaddleStrategy(CollisionStrategy decoratedStrategy,
                          GameObjectCollection gameObjects, ImageReader imageReader,
                          UserInputListener inputListener, Vector2 paddleDimensions,
                          Vector2 windowDimensions){
        this.decoratedStrategy = decoratedStrategy;
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.paddleImage =  imageReader.readImage(BOT_PADDLE_IMAGE_PATH, true);
        this.paddlePosition = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
        this.paddleDimensions = paddleDimensions;
        this.inputListener = inputListener;
    }

    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        decoratedStrategy.onCollision(collidedObj, colliderObj, bricksCounter);
        if(SecondaryPaddle.numOfInstances == 0){
            SecondaryPaddle paddle = new SecondaryPaddle(Vector2.ZERO,
                    paddleDimensions, paddleImage, inputListener, windowDimensions, 1 ,
                    gameObjects);
            paddle.setCenter(paddlePosition);
            gameObjects.addGameObject(paddle);
        }
    }
}
