package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The following class represents the numerical life counter for the amount of tries remaining in the
 * bricker game.
 * @author Eliyahu Tamarkin
 */
public class NumericLifeCounter extends GameObject {

    private static final int MEDIUM_HEARTS = 2;
    private static final int LOW_HEARTS = 1;
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private final TextRenderable textRenderable;

    /**
     * Constructs a new NumericLifeCounter instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param livesCounter  counter which holds the amount of lives
     * @param gameObjectCollection the collection of the objects in the game to be used for adding or
     *      *                      removing objects
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.textRenderable = new TextRenderable(String.valueOf(livesCounter.value()));
        this.textRenderable.setColor(Color.GREEN);
        this.renderer().setRenderable(textRenderable);
    }

    /**
     * updates the number and the color of according to the value of the counter
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() == MEDIUM_HEARTS){
            textRenderable.setString(String.valueOf(livesCounter.value()));
            textRenderable.setColor(Color.YELLOW);
        }
        else if(livesCounter.value() == LOW_HEARTS){
            textRenderable.setString(String.valueOf(livesCounter.value()));
            textRenderable.setColor(Color.RED);
        }
    }
}
