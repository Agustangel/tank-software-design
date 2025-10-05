package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

/**
 * Класс препятствия на игровом поле.
 * Инкапсулирует позицию и графику препятствия.
 */
public class Obstacle {
  private final TextureRegion graphics;
  private final Rectangle bounds;
  private final GridPoint2 position;

  public Obstacle(TextureRegion graphics, GridPoint2 position, Rectangle bounds) {
    this.graphics = graphics;
    this.position = position;
    this.bounds = bounds;
  }

  public TextureRegion getGraphics() {
    return graphics;
  }

  public Rectangle getBounds() {
    return bounds;
  }

  public GridPoint2 getPosition() {
    return position;
  }
}