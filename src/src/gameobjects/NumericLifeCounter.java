package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {

    private static final int MEDIUM_HEARTS = 2;
    private static final int LOW_HEARTS = 1;
    private Counter livesCounter;
    private GameObjectCollection gameObjectCollection;
    private final TextRenderable textRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() == MEDIUM_HEARTS){
            textRenderable.setString(String.valueOf(livesCounter.value()));
            textRenderable.setColor(Color.YELLOW);
            return;
        }
        textRenderable.setString(String.valueOf(livesCounter.value()));
        textRenderable.setColor(Color.GREEN);
    }
}
