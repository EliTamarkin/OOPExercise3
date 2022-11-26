package src;

import danogl.collisions.Layer;
import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.CollisionStrategyFactory;
import src.gameobjects.*;

import java.awt.event.KeyEvent;

/**
 * The following class represents the manager of the bricker game.
 * @author Eliyahu Tamarkin
 */
public class BrickerGameManager extends GameManager {

    private static final String WINDOW_TITLE = "Bricker";
    private static final Vector2 WINDOW_DIMENSIONS = new Vector2(700, 500);

    //background
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    //walls
    private static final int WALL_OFFSET = 1;
    private static final int WALL_DIM = 1;
    private static final Vector2 TOP_LEFT_OFFSET_CORNER = new Vector2(0, WALL_OFFSET);
    private static final Vector2 TOP_RIGHT_OFFSET_RIGHT = new Vector2(WINDOW_DIMENSIONS.x(),
            WALL_OFFSET);

    //ball
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_COLLISION_SOUND_PATH = "assets/blop.wav";
    private static final Vector2 BALL_DIMENSIONS = new Vector2(30, 30);
    private static final int BALL_SPEED = 300;

    //paddle
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final Vector2 PADDLE_DIMENSIONS =  new Vector2(200, 20);
    private static final int PADDLE_MIN_DIST_FROM_EDGE = 30;

    //brick
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final Vector2 BRICK_DIMENSIONS =  new Vector2(90, 20);
    private static final Vector2 BRICKS_INITIAL_POSITION = new Vector2(20,5 );
    private static final Vector2 DISTANCE_BETWEEN_BRICKS =  new Vector2(95, 0);
    private static final int BRICK_ROWS_AMOUNT = 8;
    private static final int BRICK_IN_ROW_AMOUNT = 7;

    //graph life counter
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final int NUM_OF_LIVES = 3;
    private static final Vector2 HEART_DIMENSIONS =  new Vector2(25, 25);
    private static final Vector2 HEARTS_INITIAL_POSITION =
            new Vector2(0, WINDOW_DIMENSIONS.y() - 25);

    //numeric life counter
    private static final Vector2 LIVES_COUNTER_DIMENSIONS = HEART_DIMENSIONS;


    //messages
    private static final String WIN_MESSAGE ="You Won! ";
    private static final String LOSE_MESSAGE ="You Lost! ";
    private static final String PLAY_AGAIN_MESSAGE ="Play again?";



    private Ball ball;

    private Paddle userPaddle;
    private WindowController windowController;
    private Counter numOfBricks;

    private final Counter numOfLives = new Counter(NUM_OF_LIVES);
    private UserInputListener userInputListener;


    /**
     * Constructs a new BrickerGameManager instance.
     * @param windowTitle title to be displayed upon the window
     * @param windowDimensions the window dimensions to be set
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
    }

    /**
     * initializes the bricker game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.userInputListener = inputListener;
        windowController.setTargetFramerate(80);
        initializeBackground(imageReader);
        initializeWalls();
        initializeBall(imageReader, soundReader);
        initializePaddle(inputListener, imageReader);
        initializeBricks(imageReader, soundReader);
        initializeGraphLifeCounter(imageReader);
        initializeNumericLifeCounter();
    }

    /**
     * updates the games fields and state
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForInvalidation();
        checkForGameEnd();
    }

    /**
     * checks whether the user has been invalidated and updates the required fields
     */
    private void checkForInvalidation(){
        double ballHeight = ball.getCenter().y();
        if (ballHeight > WINDOW_DIMENSIONS.y()){
            numOfLives.decrement();
            ball.setCenter(WINDOW_DIMENSIONS.mult(0.5F));
            ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        }
    }

    /**
     * checks whether game has ended and displays a dialogue whether to close the game or play again
     */
    private void checkForGameEnd() {
        String prompt = "";
        if (checkForWin()){
            prompt = WIN_MESSAGE;
        }
        if (checkForLose()){
            prompt = LOSE_MESSAGE;
        }
        if (!prompt.isEmpty()){
            prompt += PLAY_AGAIN_MESSAGE;
            if (windowController.openYesNoDialog(prompt)){
                numOfLives.reset();
                numOfLives.increaseBy(NUM_OF_LIVES);
//                numOfBricks.reset();
//                numOfBricks.increaseBy(56);
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * The following function checks whether one of the win conditions is met
     * @return true if any win condition holds and false otherwise
     */
    private boolean checkForWin(){
        return userInputListener.isKeyPressed(KeyEvent.VK_W) || numOfBricks.value() <= 0;
    }

    /**
     * The following function checks whether one of the losing conditions is met
     * @return true if any lose condition holds and false otherwise
     */
    private boolean checkForLose(){
        return numOfLives.value() <= 0;
    }

    public static void main(String[] args) {
        new BrickerGameManager(WINDOW_TITLE, WINDOW_DIMENSIONS).run();
    }

    /**
     * initializes the games background
     * @param imageReader reader to read the background image
     */
    private void initializeBackground(ImageReader imageReader){
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH,
                false);
        this.gameObjects().addGameObject(new GameObject(Vector2.ZERO, WINDOW_DIMENSIONS,
                backgroundImage), Layer.BACKGROUND);
    }

    /**
     * initializes the games walls
     */
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

    /**
     * initializes the games ball
     * @param imageReader reader to read the balls image
     * @param soundReader reader to read the sound the ball will make upon collision
     */
    private void initializeBall(ImageReader imageReader, SoundReader soundReader){
        Renderable ballImage =  imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS, ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        ball.setCenter(WINDOW_DIMENSIONS.mult(0.5F));
        this.gameObjects().addGameObject(ball);
    }

    /**
     * initializes the games paddle
     * @param inputListener listener to listen for any key press events in order to move the paddle
     *                      according to the requested key
     * @param imageReader reader to read the paddle's image
     */
    private void initializePaddle(UserInputListener inputListener, ImageReader imageReader){
        Renderable paddleImage =  imageReader.readImage(PADDLE_IMAGE_PATH, false);
        this.userPaddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSIONS, paddleImage, inputListener,
                WINDOW_DIMENSIONS, PADDLE_MIN_DIST_FROM_EDGE);
        userPaddle.setCenter(new Vector2(WINDOW_DIMENSIONS.x() / 2,
                (int) WINDOW_DIMENSIONS.y() - 30));
        this.gameObjects().addGameObject(userPaddle);
    }

    /**
     * initializes the games bricks
     * @param imageReader reader to read the brick's image
     */
    private void initializeBricks(ImageReader imageReader, SoundReader soundReader){
        numOfBricks = new Counter(0);
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        CollisionStrategyFactory collisionStrategyFactory = new CollisionStrategyFactory(gameObjects(),
                imageReader, soundReader, userInputListener, this, windowController,
                WINDOW_DIMENSIONS, userPaddle, ball, numOfLives, HEART_DIMENSIONS);
        Vector2 brickPosition = BRICKS_INITIAL_POSITION;
        for (int i = 0; i < BRICK_ROWS_AMOUNT; i++) {
            for (int j = 0; j < BRICK_IN_ROW_AMOUNT; j++) {
                CollisionStrategy strategy = collisionStrategyFactory.getStrategy();
                Brick brick = new Brick(brickPosition, BRICK_DIMENSIONS, brickImage, strategy,
                        numOfBricks);
                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickPosition = brickPosition.add(DISTANCE_BETWEEN_BRICKS);
                numOfBricks.increment();
            }
            brickPosition = BRICKS_INITIAL_POSITION.add(new Vector2(0, 20).mult(i + 1));
        }
    }

    /**
     * initializes the graphic lives counter which displays the amount of hears left
     * @param imageReader reader to read the heart's image
     */
    private void initializeGraphLifeCounter(ImageReader imageReader){
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(HEARTS_INITIAL_POSITION,
                HEART_DIMENSIONS, numOfLives, heartImage, this.gameObjects(), NUM_OF_LIVES,
                windowController);
        gameObjects().addGameObject(graphicLifeCounter, Layer.UI);
    }

    /**
     * initializes the numeric lives counter which displays the amount of lives left as a number
     */
    private void initializeNumericLifeCounter(){
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(numOfLives,
                Vector2.ZERO, LIVES_COUNTER_DIMENSIONS, windowController, HEART_DIMENSIONS);
        gameObjects().addGameObject(numericLifeCounter, Layer.UI);
    }


}
