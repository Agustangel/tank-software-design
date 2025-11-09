package ru.mipt.bit.platformer;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;
import ru.mipt.bit.platformer.util.TileMovement;

/**
 * Класс танка - основной игровой объект.
 * Отвечает за движение, отрисовку и обработку столкновений.
 */
public class Tank implements GameObject, Collidable {
    private final float movementSpeed;
    private final TextureRegion graphics;
    private final Rectangle bounds;
    private final TileMovement tileMovement;
    private final int levelWidth;
    private final int levelHeight;

    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;

    private int health;
    private final int maxHealth = 100;

    /**
     * Создает новый танк с конфигурируемой скоростью
     */
    public Tank(TextureRegion graphics, GridPoint2 startPosition,
                TileMovement tileMovement, float movementSpeed, int levelWidth, int levelHeight) {
        this.graphics = graphics;
        this.bounds = createBoundingRectangle(graphics);
        this.tileMovement = tileMovement;
        this.coordinates = new GridPoint2(startPosition);
        this.destinationCoordinates = new GridPoint2(startPosition);
        this.movementSpeed = movementSpeed;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.health = maxHealth;
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

    // Геттеры и сеттеры для здоровья
    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public void takeDamage(int damage) {
        setHealth(health - damage);
    }

    public boolean isAlive() {
        return health > 0;
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

    /**
     * Возвращает позицию назначения, если танк движется
     */
    public GridPoint2 getDestination() {
        return new GridPoint2(destinationCoordinates);
    }

    /**
     * Возвращает все клетки, занимаемые танком (текущую и целевую, если движется)
     */
    public List<GridPoint2> getOccupiedCells() {
        List<GridPoint2> occupied = new ArrayList<>();
        occupied.add(new GridPoint2(coordinates));

        if (isMoving()) {
            occupied.add(new GridPoint2(destinationCoordinates));
        }

        return occupied;
    }

    /**
     * Проверяет, занимает ли танк указанную позицию (включая движение)
     */
    public boolean occupiesPosition(GridPoint2 position) {
        return coordinates.equals(position) ||
               (isMoving() && destinationCoordinates.equals(position));
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    @Override
    public boolean blocksMovement() {
        return true;
    }

    /**
     * Проверяет столкновения с другими объектами и границами уровня
     */
    private boolean hasCollision(GridPoint2 position, List<Collidable> collidables) {
        // Проверка выхода за границы уровня
        if (position.x < 0 || position.x >= levelWidth || position.y < 0 || position.y >= levelHeight) {
            return true;
        }

        // Проверка столкновений с другими объектами
        for (Collidable collidable : collidables) {
            if (collidable != this && collidable.blocksMovement()) {
                if (collidable instanceof Tank) {
                    // Для танков проверяем все занимаемые клетки
                    Tank otherTank = (Tank) collidable;
                    if (otherTank.occupiesPosition(position)) {
                        return true;
                    }
                } else {
                    // Для остальных объектов проверяем только текущую позицию
                    if (collidable.getPosition().equals(position)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Устанавливает позицию танка (для инициализации)
     */
    public void setPosition(GridPoint2 position) {
        this.coordinates.set(position);
        this.destinationCoordinates.set(position);
        this.movementProgress = 1f;
    }

    /**
     * Возвращает текущий прогресс движения
     */
    public float getMovementProgress() {
        return movementProgress;
    }
}