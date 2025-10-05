package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Obstacle
 */
class ObstacleTest {
  private Obstacle obstacle;
  private TextureRegion mockGraphics;
  private GridPoint2 position;
  private Rectangle bounds;

  @BeforeEach
  void setUp() {
    // Создание мок-объектов
    mockGraphics = mock(TextureRegion.class);
    position = new GridPoint2(3, 4);
    bounds = new Rectangle(3, 4, 1, 1);

    // Создание препятствия для тестирования
    obstacle = new Obstacle(mockGraphics, position, bounds);
  }

  @Test
  void testInitialization() {
    // Проверка корректной инициализации препятствия
    assertSame(mockGraphics, obstacle.getGraphics(),
        "Графика должна соответствовать переданной в конструкторе");
    assertEquals(position, obstacle.getPosition(),
        "Позиция должна соответствовать переданной в конструкторе");
    assertEquals(
        bounds, obstacle.getBounds(), "Границы должны соответствовать переданным в конструкторе");
  }

  @Test
  void testGetGraphics() {
    // Проверка получения графики
    TextureRegion graphics = obstacle.getGraphics();
    assertSame(mockGraphics, graphics, "Метод getGraphics должен возвращать установленную графику");
  }

  @Test
  void testGetBounds() {
    // Проверка получения границ
    Rectangle returnedBounds = obstacle.getBounds();
    assertEquals(bounds, returnedBounds, "Метод getBounds должен возвращать установленные границы");
  }

  @Test
  void testGetPosition() {
    // Проверка получения позиции
    GridPoint2 returnedPosition = obstacle.getPosition();
    assertEquals(
        position, returnedPosition, "Метод getPosition должен возвращать установленную позицию");

    // Проверка, что возвращается именно та же позиция (не копия)
    // В оригинальном коде это не копия, поэтому проверяем равенство
    assertEquals(position, returnedPosition);
  }

  @Test
  void testImmutabilityThroughGetters() {
    // Проверка, что изменение возвращенных объектов не влияет на внутреннее состояние

    // Получаем объекты через геттеры
    GridPoint2 externalPosition = obstacle.getPosition();
    Rectangle externalBounds = obstacle.getBounds();

    // Сохраняем оригинальные значения
    GridPoint2 originalPosition = new GridPoint2(externalPosition);
    Rectangle originalBounds = new Rectangle(externalBounds);

    // Изменяем внешние объекты
    externalPosition.set(999, 999);
    externalBounds.set(999, 999, 999, 999);

    // Проверяем, что внутреннее состояние не изменилось
    assertEquals(originalPosition, obstacle.getPosition(),
        "Внутренняя позиция не должна изменяться при модификации возвращенного объекта");
    assertEquals(originalBounds, obstacle.getBounds(),
        "Внутренние границы не должны изменяться при модификации возвращенного объекта");
  }
}