package ru.mipt.bit.platformer;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;
import ru.mipt.bit.platformer.util.TileMovement;

/**
 * Класс танка - основной игровой объект.
 * Отвечает за движение, отрисовку и обработку столкновений.
 */
public class Tank implements GameObject {
    private final float movementSpeed;
    private final TextureRegion graphics;
    private final Rectangle bounds;
    private final TileMovement tileMovement;

    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;

    /**
     * Создает новый танк с конфигурируемой скоростью
     */
    public Tank(TextureRegion graphics, GridPoint2 startPosition,
                TileMovement tileMovement, float movementSpeed) {
        this.graphics = graphics;
        this.bounds = createBoundingRectangle(graphics);
        this.tileMovement = tileMovement;
        this.coordinates = new GridPoint2(startPosition);
        this.destinationCoordinates = new GridPoint2(startPosition);
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void update(float deltaTime) {
        // Обновление движения, если оно активно
        if (movementProgress < 1f) {
            tileMovement.moveRectangleBetweenTileCenters(
                bounds, coordinates, destinationCoordinates, movementProgress);
            movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);

            if (isEqual(movementProgress, 1f)) {
                coordinates.set(destinationCoordinates);
            }
        }
    }

    @Override
    public void render(Batch batch) {
        GraphicsManager.drawTank(batch, this);
    }

    // Геттеры для GraphicsManager
    public TextureRegion getGraphics() {
        return graphics;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getRotation() {
        return rotation;
    }

    /**
     * Попытка начать движение в указанном направлении
     * Возвращает true если движение началось
     */
    public boolean tryMove(Direction direction, List<Collidable> collidables) {
        if (isMoving()) {
            return false;
        }

        GridPoint2 potentialDestination = direction.applyTo(coordinates);

        if (hasCollision(potentialDestination, collidables)) {
            return false;
        }

        startMovement(direction, potentialDestination);
        return true;
    }

    private void startMovement(Direction direction, GridPoint2 destination) {
        destinationCoordinates.set(destination);
        movementProgress = 0f;
        rotation = direction.getRotation();
    }

    @Override
    public GridPoint2 getPosition() {
        return new GridPoint2(coordinates);
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    private boolean hasCollision(GridPoint2 position, List<Collidable> collidables) {
        return collidables.stream()
            .filter(Collidable::blocksMovement)
            .anyMatch(collidable -> collidable.getPosition().equals(position));
    }
}