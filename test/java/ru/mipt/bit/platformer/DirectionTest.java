package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

/**
 * Тесты для перечисления направлений движения
 */
class DirectionTest {
  @Test
  void testApplyTo_UpDirection() {
    // Подготовка
    GridPoint2 startPoint = new GridPoint2(2, 2);

    // Действие
    GridPoint2 result = Direction.UP.applyTo(startPoint);

    // Проверка
    assertEquals(2, result.x, "Координата X не должна измениться при движении вверх");
    assertEquals(3, result.y, "Координата Y должна увеличиться на 1 при движении вверх");
  }

  @Test
  void testApplyTo_DownDirection() {
    // Подготовка
    GridPoint2 startPoint = new GridPoint2(2, 2);

    // Действие
    GridPoint2 result = Direction.DOWN.applyTo(startPoint);

    // Проверка
    assertEquals(2, result.x, "Координата X не должна измениться при движении вниз");
    assertEquals(1, result.y, "Координата Y должна уменьшиться на 1 при движении вниз");
  }

  @Test
  void testApplyTo_LeftDirection() {
    // Подготовка
    GridPoint2 startPoint = new GridPoint2(2, 2);

    // Действие
    GridPoint2 result = Direction.LEFT.applyTo(startPoint);

    // Проверка
    assertEquals(1, result.x, "Координата X должна уменьшиться на 1 при движении влево");
    assertEquals(2, result.y, "Координата Y не должна измениться при движении влево");
  }

  @Test
  void testApplyTo_RightDirection() {
    // Подготовка
    GridPoint2 startPoint = new GridPoint2(2, 2);

    // Действие
    GridPoint2 result = Direction.RIGHT.applyTo(startPoint);

    // Проверка
    assertEquals(3, result.x, "Координата X должна увеличиться на 1 при движении вправо");
    assertEquals(2, result.y, "Координата Y не должна измениться при движении вправо");
  }

  @Test
  void testGetRotation() {
    // Проверка углов поворота для всех направлений
    assertEquals(90f, Direction.UP.getRotation(), "Поворот для UP должен быть 90°");
    assertEquals(-90f, Direction.DOWN.getRotation(), "Поворот для DOWN должен быть -90°");
    assertEquals(-180f, Direction.LEFT.getRotation(), "Поворот для LEFT должен быть -180°");
    assertEquals(0f, Direction.RIGHT.getRotation(), "Поворот для RIGHT должен быть 0°");
  }

  @Test
  void testGetDxDy() {
    // Проверка изменения координат для всех направлений
    assertEquals(0, Direction.UP.getDx(), "dx для UP должен быть 0");
    assertEquals(1, Direction.UP.getDy(), "dy для UP должен быть 1");

    assertEquals(0, Direction.DOWN.getDx(), "dx для DOWN должен быть 0");
    assertEquals(-1, Direction.DOWN.getDy(), "dy для DOWN должен быть -1");

    assertEquals(-1, Direction.LEFT.getDx(), "dx для LEFT должен быть -1");
    assertEquals(0, Direction.LEFT.getDy(), "dy для LEFT должен быть 0");

    assertEquals(1, Direction.RIGHT.getDx(), "dx для RIGHT должен быть 1");
    assertEquals(0, Direction.RIGHT.getDy(), "dy для RIGHT должен быть 0");
  }
}