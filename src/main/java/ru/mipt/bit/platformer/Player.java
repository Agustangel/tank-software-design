package ru.mipt.bit.platformer;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;
import ru.mipt.bit.platformer.util.TileMovement;

/**
 * Класс игрока, управляющего танком.
 * Отвечает за движение, отрисовку и обработку столкновений.
 */
public class Player {
  // Графика игрока
  private final TextureRegion graphics;
  // Прямоугольник для отрисовки и коллизий
  private final Rectangle bounds;
  // Механика перемещения между тайлами
  private final TileMovement tileMovement;
  // Скорость движения (время прохождения одного тайла)
  private final float movementSpeed;
  // Контроллер ввода для управления игроком
  private final InputController inputController;

  // Текущая позиция на сетке уровня
  private GridPoint2 coordinates;
  // Целевая позиция для движения
  private GridPoint2 destinationCoordinates;
  // Прогресс движения от 0 (начало) до 1 (завершение)
  private float movementProgress = 1f;
  // Текущий угол поворота спрайта
  private float rotation;

  /**
   * Создает нового игрока.
   *
   * @param graphics текстура игрока
   * @param startPosition начальная позиция на сетке
   * @param movementSpeed скорость движения
   * @param tileMovement механика перемещения
   * @param inputController контроллер ввода
   */
  public Player(TextureRegion graphics, GridPoint2 startPosition, float movementSpeed,
      TileMovement tileMovement, InputController inputController) {
    this.graphics = graphics;
    this.bounds = createBoundingRectangle(graphics);
    this.movementSpeed = movementSpeed;
    this.tileMovement = tileMovement;
    this.inputController = inputController;
    this.coordinates = new GridPoint2(startPosition);
    this.destinationCoordinates = new GridPoint2(startPosition);
  }

  /**
   * Обновляет состояние игрока каждый кадр.
   *
   * @param deltaTime время, прошедшее с последнего кадра
   * @param obstacles список позиций препятствий для проверки коллизий
   */
  public void update(float deltaTime, List<GridPoint2> obstacles) {
    // Обработка ввода возможна только когда предыдущее движение завершено
    if (isEqual(movementProgress, 1f)) {
      Direction direction = inputController.getInputDirection();
      if (direction != null) {
        // Вычисляем целевую позицию
        GridPoint2 potentialDestination = direction.applyTo(coordinates);

        // Проверяем столкновение с препятствиями
        if (!hasCollision(potentialDestination, obstacles)) {
          // Начинаем новое движение
          destinationCoordinates.set(potentialDestination);
          movementProgress = 0f;
          rotation = direction.getRotation();
        }
      }
    }

    // Обновление движения, если оно активно
    if (movementProgress < 1f) {
      // Интерполируем позицию между начальной и целевой
      tileMovement.moveRectangleBetweenTileCenters(
          bounds, coordinates, destinationCoordinates, movementProgress);
      // Увеличиваем прогресс движения
      movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);

      // Если движение завершено, фиксируем новую позицию
      if (isEqual(movementProgress, 1f)) {
        coordinates.set(destinationCoordinates);
      }
    }
  }

  /**
   * Отрисовывает игрока на экране.
   *
   * @param batch пакетный отрисовщик спрайтов
   */
  public void render(Batch batch) {
    drawTextureRegionUnscaled(batch, graphics, bounds, rotation);
  }

  /**
   * Возвращает копию текущей позиции игрока.
   */
  public GridPoint2 getCoordinates() {
    return new GridPoint2(coordinates);
  }

  /**
   * Проверяет столкновение с препятствиями.
   *
   * @param position проверяемая позиция
   * @param obstacles список препятствий
   * @return true если есть столкновение
   */
  private boolean hasCollision(GridPoint2 position, List<GridPoint2> obstacles) {
    return obstacles.stream().anyMatch(obstacle -> obstacle.equals(position));
  }
}
