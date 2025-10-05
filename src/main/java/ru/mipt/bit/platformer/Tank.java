package ru.mipt.bit.platformer;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

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
public class Tank {
  // Константа скорости движения
  private static final float MOVEMENT_SPEED = 0.4f;

  // Графика танка
  private final TextureRegion graphics;
  // Прямоугольник для отрисовки и коллизий
  private final Rectangle bounds;
  // Механика перемещения между тайлами
  private final TileMovement tileMovement;

  // Текущая позиция на сетке уровня
  private GridPoint2 coordinates;
  // Целевая позиция для движения
  private GridPoint2 destinationCoordinates;
  // Прогресс движения от 0 (начало) до 1 (завершение)
  private float movementProgress = 1f;
  // Текущий угол поворота спрайта
  private float rotation;

  /**
   * Создает новый танк.
   */
  public Tank(TextureRegion graphics, GridPoint2 startPosition, TileMovement tileMovement) {
    this.graphics = graphics;
    this.bounds = createBoundingRectangle(graphics);
    this.tileMovement = tileMovement;
    this.coordinates = new GridPoint2(startPosition);
    this.destinationCoordinates = new GridPoint2(startPosition);
  }

  /**
   * Обновляет состояние танка каждый кадр.
   */
  public void update(float deltaTime, List<Obstacle> obstacles, Direction movementDirection) {
    // Обработка ввода возможна только когда предыдущее движение завершено
    if (isEqual(movementProgress, 1f)) {
      if (movementDirection != null) {
        // Вычисляем целевую позицию
        GridPoint2 potentialDestination = movementDirection.applyTo(coordinates);

        // Проверяем столкновение с препятствиями
        if (!hasCollision(potentialDestination, obstacles)) {
          // Начинаем новое движение
          destinationCoordinates.set(potentialDestination);
          movementProgress = 0f;
          rotation = movementDirection.getRotation();
        }
      }
    }

    // Обновление движения, если оно активно
    if (movementProgress < 1f) {
      // Интерполируем позицию между начальной и целевой
      tileMovement.moveRectangleBetweenTileCenters(
          bounds, coordinates, destinationCoordinates, movementProgress);
      // Увеличиваем прогресс движения
      movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);

      // Если движение завершено, фиксируем новую позицию
      if (isEqual(movementProgress, 1f)) {
        coordinates.set(destinationCoordinates);
      }
    }
  }

  /**
   * Отрисовывает танк на экране.
   */
  public void render(Batch batch) {
    drawTextureRegionUnscaled(batch, graphics, bounds, rotation);
  }

  /**
   * Возвращает копию текущей позиции танка.
   */
  public GridPoint2 getCoordinates() {
    return new GridPoint2(coordinates);
  }

  /**
   * Возвращает скорость движения танка.
   */
  public static float getMovementSpeed() {
    return MOVEMENT_SPEED;
  }

  /**
   * Проверяет столкновение с препятствиями.
   */
  private boolean hasCollision(GridPoint2 position, List<Obstacle> obstacles) {
    return obstacles.stream().anyMatch(obstacle -> obstacle.getPosition().equals(position));
  }
}