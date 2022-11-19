package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {

    private static final Vector2 HEART_SHIFT = new Vector2(30, 0);
    private final GameObject[] hearts;
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;

    /**
     * Constructs a new GraphicLifeCounter instance.
     *
     * @param widgetTopLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions    Width and height in window coordinates.
     * @param widgetRenderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param gameObjectCollection the collection of the objects in the game to be used for adding or
     *                             removing objects
     * @param numOfLives           initial number of lives to be added to the game
     * @param livesCounter         lives counter representing the updated number of lives
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
            gameObjectCollection.addGameObject(heart, Layer.BACKGROUND);
            this.hearts[i] = heart;
            heartPosition = heartPosition.add(HEART_SHIFT);
        }
    }

    /**
     * updates the number of lives displayed according to the value of the counter
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
        if(livesCounter.value() < numOfLives){
            numOfLives--;
            gameObjectCollection.removeGameObject(hearts[numOfLives], Layer.BACKGROUND);
        }
    }
}
