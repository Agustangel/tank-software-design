package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Перечисление направлений движения игрока.
 * Инкапсулирует логику перемещения и поворота для каждого направления.
 */
public enum Direction {
  UP(0, 1, 90f), // Движение вверх: x=0, y=+1, поворот на 90°
  DOWN(0, -1, -90f), // Движение вниз: x=0, y=-1, поворот на -90°
  LEFT(-1, 0, -180f), // Движение влево: x=-1, y=0, поворот на -180°
  RIGHT(1, 0, 0f); // Движение вправо: x=+1, y=0, поворот на 0°

  private final int dx; // Изменение координаты X
  private final int dy; // Изменение координаты Y
  private final float rotation; // Угол поворота спрайта в градусах

  Direction(int dx, int dy, float rotation) {
    this.dx = dx;
    this.dy = dy;
    this.rotation = rotation;
  }

  /**
   * Применяет направление к заданной точке, возвращая новую позицию.
   *
   * @param point исходная позиция на сетке
   * @return новая позиция после перемещения в данном направлении
   */
  public GridPoint2 applyTo(GridPoint2 point) {
    return new GridPoint2(point.x + dx, point.y + dy);
  }

  /**
   * Возвращает угол поворота спрайта для данного направления.
   *
   * @return угол в градусах
   */
  public float getRotation() {
    return rotation;
  }

  // Геттеры для тестирования и отладки
  public int getDx() {
    return dx;
  }

  public int getDy() {
    return dy;
  }
}
