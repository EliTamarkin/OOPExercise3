package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.ArrayList;

public class GraphicLifeCounter extends GameObject {

    private static final Vector2 HEART_SHIFT = new Vector2(30, 0);
    private final GameObject[] hearts;
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;

    /**
     * Construct a new GameObject instance.
     *
     * @param widgetTopLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions    Width and height in window coordinates.
     * @param widgetRenderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions,
                              Counter livesCounter, Renderable widgetRenderable,
                              GameObjectCollection gameObjectCollection, int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = numOfLives;
        this.hearts = new GameObject[numOfLives];
        Vector2 heartPosition = widgetTopLeftCorner;
        for(int i = 0; i < numOfLives; i++){
            GameObject heart = new GameObject(heartPosition, widgetDimensions, widgetRenderable);
            gameObjectCollection.addGameObject(heart);
            this.hearts[i] = heart;
            heartPosition = heartPosition.add(HEART_SHIFT);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(numOfLives < livesCounter.value()){
            numOfLives--;
            gameObjectCollection.removeGameObject(hearts[numOfLives]);
        }
    }
}
