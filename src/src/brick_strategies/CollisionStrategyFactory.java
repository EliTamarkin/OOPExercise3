package src.brick_strategies;

import danogl.GameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.Paddle;

import java.util.Random;

public class CollisionStrategyFactory {

    private static final int NUM_OF_STRATEGIES = 6;
    private static final int REGULAR = 0;
    private static final int MULTIPLE_BALLS = 1;
    private static final int ADDITIONAL_PADDLE = 2;
    private static final int CAMERA_CHANGE = 3;
    private static final int HEART_CREATION = 4;
    private static final int DOUBLE = 5;
    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final GameManager gameManager;
    private final WindowController windowController;
    private final Vector2 windowDimensions;
    private final Paddle paddle;
    private final Ball ball;
    private final Counter livesCounter;
    private final Vector2 heartDimensions;


    private final Random rnd = new Random();

    public CollisionStrategyFactory(GameObjectCollection gameObjects, ImageReader imageReader,
                                    SoundReader soundReader, UserInputListener inputListener,
                                    GameManager gameManager, WindowController windowController,
                                    Vector2 windowDimensions, Paddle paddle, Ball ball,
                                    Counter livesCounter, Vector2 heartDimensions){
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.gameManager = gameManager;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        this.paddle = paddle;
        this.ball = ball;
        this.livesCounter = livesCounter;
        this.heartDimensions = heartDimensions;

    }

    public int getStrategy(){
        return 4;
    }

    public CollisionStrategy buildStrategy(int strategyType){
        CollisionStrategy strategy;
        switch(strategyType){
            case REGULAR:
                strategy = new CollisionStrategy(gameObjects);
                break;
            case MULTIPLE_BALLS:
                strategy = new PuckBallStrategy(gameObjects, imageReader, soundReader);
                break;
            case ADDITIONAL_PADDLE:
                strategy = new PaddleStrategy(gameObjects, imageReader, inputListener,
                        paddle.getDimensions(), windowDimensions);
                break;
            case CAMERA_CHANGE:
                strategy = new CameraChangeStrategy(gameObjects, gameManager, windowController, ball);
                break;
            case HEART_CREATION:
                strategy = new HeartCreationStrategy(gameObjects, heartDimensions, imageReader,
                        windowController, livesCounter);
                break;
            default:
                strategy = null;
        }
        return strategy;

    }

}
