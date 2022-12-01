package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.PuckBall;

import java.util.Random;

/**
 * Puck Balls strategy class.
 * In charge of initiating new puck balls whenever requested.
 * @author Eliyahu Tamarkin
 */
public class PuckBallStrategy implements CollisionStrategy {

    private static final String PUCK_BALL_IMAGE_PATH = "assets/mockBall.png";
    private static final String PUCK_BALL_COLLISION_SOUND = "assets/blop.wav";
    private static final int NUM_OF_PUCKS = 1;
    private static final int BALL_SPEED = 300;
    private final ImageRenderable ballImage;
    private final Sound collisionSound;
    private final Vector2[] directions;
    private final CollisionStrategy decoratedStrategy;
    private final GameObjectCollection gameObjects;
    private final WindowController windowController;

    /**
     * Constructs a new PuckBallStrategy instance
     * @param decoratedStrategy inner strategy
     * @param gameObjects game objects
     * @param imageReader image reader
     * @param soundReader sound reader
     */
    public PuckBallStrategy(CollisionStrategy decoratedStrategy, GameObjectCollection gameObjects,
                            WindowController windowController, ImageReader imageReader,
                            SoundReader soundReader) {
        this.decoratedStrategy = decoratedStrategy;
        this.gameObjects = gameObjects;
        this.windowController = windowController;
        ballImage =  imageReader.readImage(PUCK_BALL_IMAGE_PATH, true);
        collisionSound = soundReader.readSound(PUCK_BALL_COLLISION_SOUND);
        directions = new Vector2[]{new Vector2(1, 1), new Vector2(-1, 1),
                                    new Vector2(-1, -1), new Vector2(1, -1)};
    }

    /**
     * Generates new Puck Balls whenever requested
     * The Puck Balls are generated in a constant size and are being initialized in different directions
     * @param collidedObj collided object
     * @param colliderObj collider object
     * @param bricksCounter bricks counter
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        decoratedStrategy.onCollision(collidedObj, colliderObj, bricksCounter);
        Random rnd = new Random();
        int ballSize = (int) collidedObj.getDimensions().y();
        Vector2 ballPosition = new Vector2(collidedObj.getCenter().x(), collidedObj.getCenter().y());
        for(int i = 0; i < NUM_OF_PUCKS; i++){
            int directionIndex = rnd.nextInt(directions.length);
            PuckBall puckBall = new PuckBall(ballPosition, new Vector2(ballSize, ballSize),
                    this.ballImage, this.collisionSound, this.windowController, this.gameObjects);
            puckBall.setVelocity(directions[directionIndex].mult(BALL_SPEED));
            this.gameObjects.addGameObject(puckBall);
        }
    }
}
