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
    private static final int MAX_STRATEGIES = 3;
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
    private final CollisionStrategy baseStrategy;

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
        this.baseStrategy = new RemoveBrickStrategy(gameObjects);

    }

    public CollisionStrategy getStrategy(){
//        int strategyIndex = rnd.nextInt(NUM_OF_STRATEGIES);
//        if (strategyIndex != DOUBLE){
//            return buildStrategy(baseStrategy, strategyIndex);
//        }
//        return buildDoubleStrategy(baseStrategy, 0, 2);
        return buildStrategy(baseStrategy,1);
    }

    public CollisionStrategy buildStrategy(CollisionStrategy decoratedStrategy, int strategyType){
        switch(strategyType){
            case REGULAR:
                return decoratedStrategy;
            case MULTIPLE_BALLS:
                return new PuckBallStrategy(decoratedStrategy, gameObjects, imageReader, soundReader);
            case ADDITIONAL_PADDLE:
                return  new PaddleStrategy(decoratedStrategy, gameObjects, imageReader,
                        inputListener, paddle.getDimensions(), windowDimensions);
            case CAMERA_CHANGE:
                return new CameraChangeStrategy(decoratedStrategy, gameObjects, gameManager,
                        windowController, ball);
            case HEART_CREATION:
                return new HeartCreationStrategy(decoratedStrategy, gameObjects, heartDimensions,
                        imageReader, windowController, livesCounter);
            default:
                return null;
        }
    }

    private CollisionStrategy buildDoubleStrategy(CollisionStrategy decoratedStrategy,
                                                  int numOfStrategies, int maxStrategies){
        if (numOfStrategies == maxStrategies){
            return decoratedStrategy;
        }
        int strategyIndex = rnd.nextInt(NUM_OF_STRATEGIES - 1) + 1;
        if (strategyIndex != DOUBLE){
            numOfStrategies++;
            return buildDoubleStrategy(buildStrategy(decoratedStrategy, strategyIndex), numOfStrategies,
                    maxStrategies);
        }
        maxStrategies = Math.max(maxStrategies, MAX_STRATEGIES);
        return buildDoubleStrategy(decoratedStrategy, numOfStrategies, maxStrategies);
    }

}
