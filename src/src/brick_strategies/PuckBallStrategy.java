package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;

import java.util.Random;

public class PuckBallStrategy extends CollisionStrategy {

    private static final String PUCK_BALL_IMAGE_PATH = "assets/mockBall.png";
    private static final String PUCK_BALL_COLLISION_SOUND = "assets/blop.wav";
    private static final int NUM_OF_PUCKS = 1;
    private static final int BALL_SIZE_RATIO = 3;

    private static final int BALL_POSITION_RATIO = 2;
    private static final int BALL_SPEED = 500;
    private final ImageRenderable ballImage;
    private final Sound collisionSound;
    private final Vector2[] directions;

    public PuckBallStrategy(GameObjectCollection gameObjects, ImageReader imageReader,
                            SoundReader soundReader) {
        super(gameObjects);
        ballImage =  imageReader.readImage(PUCK_BALL_IMAGE_PATH, true);
        collisionSound = soundReader.readSound(PUCK_BALL_COLLISION_SOUND);
        directions = new Vector2[]{new Vector2(1, 1), new Vector2(1, -1),
                new Vector2(-1, -1), new Vector2(-1, 1)};
    }

    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        Random rnd = new Random();
        int ballSize = (int) collidedObj.getDimensions().x() / BALL_SIZE_RATIO;
        Vector2 ballPosition = new Vector2(collidedObj.getCenter().x() / BALL_POSITION_RATIO,
                collidedObj.getCenter().y());
        for(int i = 0; i < NUM_OF_PUCKS; i++){
            int directionIndex = rnd.nextInt(directions.length);
            Ball puckBall = new Ball(ballPosition, new Vector2(ballSize, ballSize),
                    this.ballImage, this.collisionSound);
            puckBall.setVelocity(directions[directionIndex].mult(BALL_SPEED));
            this.gameObjects.addGameObject(puckBall);
        }
    }
}
