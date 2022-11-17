package src;

import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;

public class BrickerGameManager extends GameManager {

    private static final String WINDOW_TITLE = "Bricker";
    private static final Vector2 WINDOW_DIMENSIONS = new Vector2(700, 500);

    //background
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    //walls
    private static final int WALL_OFFSET = 30;
    private static final int WALL_DIM = 1;
    private static final Vector2 TOP_LEFT_OFFSET_CORNER = new Vector2(0, WALL_OFFSET);
    private static final Vector2 TOP_RIGHT_OFFSET_RIGHT = new Vector2(WINDOW_DIMENSIONS.x(),
            WALL_OFFSET);

    //ball
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_COLLISION_SOUND_PATH = "assets/blop.wav";

    private static final Vector2 BALL_DIMENSIONS = new Vector2(50, 50);
    private static final int BALL_SPEED = 300;

    //paddle
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final Vector2 PADDLE_DIMENSIONS =  new Vector2(200, 20);
    private static final int PADDLE_MIN_DIST_FROM_EDGE = 0;

    //brick
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final Vector2 BRICK_DIMENSIONS =  new Vector2(90, 20);
    private static final Vector2 BRICKS_INITIAL_POSITION =
            new Vector2(0, 30).add(new Vector2(5,0 ));

    private static final Vector2 DISTANCE_BETWEEN_BRICKS =  new Vector2(100, 0);
    private static final int BRICK_ROWS_AMOUNT = 8;
    private static final int BRICK_IN_ROW_AMOUNT = 7;

    //graph life counter
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final Vector2 HEART_DIMENSIONS =  new Vector2(25, 25);

    //numeric life counter
    private static final Vector2 LIVES_COUNTER_DIMENSIONS = HEART_DIMENSIONS;
    private static final Vector2 NUMERIC_COUNTER_POSITION = new Vector2(90, -5);


    private Ball ball;
    private WindowController windowController;
    private Counter numOfBricks;
    private static final int NUM_OF_LIVES = 3;
    private Counter numOfLives = new Counter(NUM_OF_LIVES);


    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        windowController.setTargetFramerate(80);
        initializeBackground(imageReader);
        initializeWalls();
        initializeBall(imageReader, soundReader);
        initializePaddle(inputListener, imageReader);
        initializeBricks(imageReader);
        initializeGraphLifeCounter(imageReader);
        initializeNumericLifeCounter();
    }

    public static void main(String[] args) {
        new BrickerGameManager(WINDOW_TITLE, WINDOW_DIMENSIONS).run();
    }

    private void initializeBackground(ImageReader imageReader){
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH,
                false);
        this.gameObjects().addGameObject(new GameObject(Vector2.ZERO, WINDOW_DIMENSIONS,
                backgroundImage));
    }

    private void initializeWalls(){
        //top wall
        this.gameObjects().addGameObject(new GameObject(TOP_LEFT_OFFSET_CORNER,
                new Vector2(WINDOW_DIMENSIONS.x(), WALL_DIM), null));
        //left wall
        this.gameObjects().addGameObject(new GameObject(TOP_LEFT_OFFSET_CORNER,
                new Vector2(WALL_DIM, WINDOW_DIMENSIONS.y()), null));
        //right wall
        this.gameObjects().addGameObject(new GameObject(TOP_RIGHT_OFFSET_RIGHT,
                new Vector2(WALL_DIM, WINDOW_DIMENSIONS.y()), null));
    }

    private void initializeBall(ImageReader imageReader, SoundReader soundReader){
        Renderable ballImage =  imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS, ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        ball.setCenter(WINDOW_DIMENSIONS.mult(0.5F));
        this.gameObjects().addGameObject(ball);
    }

    private void initializePaddle(UserInputListener inputListener, ImageReader imageReader){
        Renderable paddleImage =  imageReader.readImage(PADDLE_IMAGE_PATH, false);
        GameObject userPaddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSIONS, paddleImage, inputListener,
                WINDOW_DIMENSIONS, PADDLE_MIN_DIST_FROM_EDGE);
        userPaddle.setCenter(new Vector2(WINDOW_DIMENSIONS.x() / 2,
                (int) WINDOW_DIMENSIONS.y() - 30));
        this.gameObjects().addGameObject(userPaddle);
    }

    private void initializeBricks(ImageReader imageReader){
        numOfBricks = new Counter(0);
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        CollisionStrategy collisionStrategy = new CollisionStrategy(this.gameObjects());
        Vector2 brickPosition = BRICKS_INITIAL_POSITION;
        for (int i = 0; i < BRICK_ROWS_AMOUNT; i++) {
            for (int j = 0; j < BRICK_IN_ROW_AMOUNT; j++) {
                Brick brick = new Brick(brickPosition, BRICK_DIMENSIONS, brickImage, collisionStrategy,
                        new Counter(56));
                this.gameObjects().addGameObject(brick);
                numOfBricks.increment();
                brickPosition = brickPosition.add(DISTANCE_BETWEEN_BRICKS);
            }
            brickPosition = BRICKS_INITIAL_POSITION.add(new Vector2(0, 30).mult(i + 1));
        }
    }

    private void initializeGraphLifeCounter(ImageReader imageReader){
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(Vector2.ZERO, HEART_DIMENSIONS,
                numOfLives, heartImage, this.gameObjects(), NUM_OF_LIVES);
        gameObjects().addGameObject(graphicLifeCounter);
    }

    private void initializeNumericLifeCounter(){
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(numOfLives,
                NUMERIC_COUNTER_POSITION, LIVES_COUNTER_DIMENSIONS, this.gameObjects());
        gameObjects().addGameObject(numericLifeCounter);
    }


}
