package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The following class represents the graphical life counter for the amount of tries remaining in the
 * bricker game.
 * @author Eliyahu Tamarkin
 */
public class GraphicLifeCounter extends GameObject {

    private static final Vector2 HEART_SHIFT = new Vector2(30, 0);
    private static final int MAX_HEARTS_AMOUNT = 4;
    private final Heart[] hearts;
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private final Vector2 widgetDimensions;
    private final Renderable widgetRenderable;
    private final WindowController windowController;
    private int numOfLives;

    /**
     * Constructs a new GraphicLifeCounter instance.
     *
     * @param widgetTopLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions    Width and height in window coordinates.
     * @param livesCounter         lives counter representing the updated number of lives
     * @param widgetRenderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param gameObjectCollection the collection of the objects in the game to be used for adding or
     *                             removing objects
     * @param numOfLives           initial number of lives to be added to the game
     * @param windowController     Window controller to be used when instantiating new Heart instances
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions,
                              Counter livesCounter, Renderable widgetRenderable,
                              GameObjectCollection gameObjectCollection, int numOfLives,
                              WindowController windowController) {
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.livesCounter = livesCounter;
        this.windowController = windowController;
        this.gameObjectCollection = gameObjectCollection;
        this.widgetDimensions = widgetDimensions;
        this.widgetRenderable = widgetRenderable;
        this.numOfLives = numOfLives;
        this.hearts = new Heart[MAX_HEARTS_AMOUNT];
        Vector2 heartPosition = widgetTopLeftCorner;
        for(int i = 0; i < numOfLives; i++){
            Heart heart = new Heart(heartPosition, widgetDimensions, widgetRenderable,
                    windowController, gameObjectCollection, livesCounter);
            gameObjectCollection.addGameObject(heart, Layer.UI);
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
            removeHeart();
        }
        else if(livesCounter.value() > numOfLives && numOfLives < MAX_HEARTS_AMOUNT){
            addNewHeart();
        }
    }

    /**
     * Removes a heart from the counter
     */
    private void removeHeart(){
        numOfLives--;
        gameObjectCollection.removeGameObject(hearts[numOfLives], Layer.UI);
    }

    /**
     * Adds a heart to the counter
     */
    private void addNewHeart(){
        Vector2 heartPosition = hearts[numOfLives - 1].getTopLeftCorner().add(HEART_SHIFT);
        Heart newHeart = new Heart(heartPosition, this.widgetDimensions, widgetRenderable,
                windowController, gameObjectCollection, livesCounter);
        gameObjectCollection.addGameObject(newHeart, Layer.UI);
        hearts[numOfLives] = newHeart;
        numOfLives++;
    }
}
