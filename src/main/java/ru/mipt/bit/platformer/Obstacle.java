package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

/**
 * Класс препятствия на игровом поле.
 * Инкапсулирует позицию и графику препятствия.
 */
public class Obstacle implements GameObject, Collidable {
    private final TextureRegion graphics;
    private final Rectangle bounds;
    private final GridPoint2 position;
    private final boolean blocksMovement;

    public Obstacle(TextureRegion graphics, GridPoint2 position,
                   Rectangle bounds, boolean blocksMovement) {
        this.graphics = graphics;
        this.position = position;
        this.bounds = bounds;
        this.blocksMovement = blocksMovement;
    }

    public Obstacle(TextureRegion graphics, GridPoint2 position, Rectangle bounds) {
        this(graphics, position, bounds, true);
    }

    @Override
    public void update(float deltaTime) {
      // Препятствия статичны, не требуют обновления
    }

    @Override
    public void render(Batch batch) {
        GraphicsManager.drawObstacle(batch, this);
    }

    @Override
    public GridPoint2 getPosition() {
        return position;
    }

    @Override
    public boolean blocksMovement() {
        return blocksMovement;
    }

    public TextureRegion getGraphics() {
        return graphics;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}