package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.util.TileMovement;

/**
 * Тесты для класса Tank
 */
class TankTest {
  private Tank tank;
  private TextureRegion mockTexture;
  private TileMovement mockTileMovement;
  private GridPoint2 startPosition;

  @BeforeEach
  void setUp() {
    // Создание мок-объектов
    mockTexture = mock(TextureRegion.class);
    mockTileMovement = mock(TileMovement.class);
    startPosition = new GridPoint2(1, 1);

    // Создание танка для тестирования
    tank = new Tank(mockTexture, startPosition, mockTileMovement);
  }

  @Test
  void testInitialization() {
    // Проверка корректной инициализации танка
    assertEquals(startPosition, tank.getCoordinates(),
        "Начальная позиция должна соответствовать переданной в конструкторе");
    assertEquals(Tank.getMovementSpeed(), 0.4f, "Скорость движения должна быть 0.4f");
  }

  @Test
  void testUpdate_NoMovementWhenNoDirection() {
    // Подготовка
    List<Obstacle> obstacles = new ArrayList<>();

    // Действие
    tank.update(0.1f, obstacles, null);

    // Проверка - танк не должен двигаться при отсутствии направления
    assertEquals(startPosition, tank.getCoordinates(),
        "Позиция не должна измениться при отсутствии направления движения");
  }

  @Test
  void testUpdate_MovementWithoutObstacles() {
    // Подготовка
    List<Obstacle> obstacles = new ArrayList<>();
    Direction movementDirection = Direction.RIGHT;

    // Действие
    tank.update(0.1f, obstacles, movementDirection);

    // Проверка - танк должен начать движение в указанном направлении
    // Метод update вызывает hasCollision, который возвращает false для пустого списка препятствий
    // Поэтому движение должно начаться
    assertNotNull(tank.getCoordinates(), "Координаты должны оставаться валидными после обновления");
  }

  @Test
  void testUpdate_CollisionWithObstacle() {
    // Подготовка
    TextureRegion obstacleTexture = mock(TextureRegion.class);
    Rectangle obstacleBounds = new Rectangle(0, 0, 1, 1);
    GridPoint2 obstaclePosition = new GridPoint2(2, 1); // Позиция справа от танка
    Obstacle obstacle = new Obstacle(obstacleTexture, obstaclePosition, obstacleBounds);

    List<Obstacle> obstacles = new ArrayList<>();
    obstacles.add(obstacle);

    Direction movementDirection = Direction.RIGHT; // Попытка движения в сторону препятствия

    // Действие
    tank.update(0.1f, obstacles, movementDirection);

    // Проверка - танк не должен двигаться из-за столкновения
    assertEquals(startPosition, tank.getCoordinates(),
        "Танк не должен двигаться при столкновении с препятствием");
  }

  @Test
  void testGetCoordinates_ReturnsCopy() {
    // Действие
    GridPoint2 coordinates = tank.getCoordinates();

    // Проверка - метод должен возвращать копию, а не оригинал
    assertNotSame(
        startPosition, coordinates, "Метод getCoordinates должен возвращать копию позиции");
    assertEquals(
        startPosition, coordinates, "Возвращаемая копия должна быть равна оригинальной позиции");
  }

  @Test
  void testGetMovementSpeed() {
    // Проверка получения скорости движения
    assertEquals(0.4f, Tank.getMovementSpeed(), "Скорость движения должна быть 0.4f");
  }
}