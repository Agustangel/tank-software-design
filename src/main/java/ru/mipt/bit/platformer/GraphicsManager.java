package ru.mipt.bit.platformer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Менеджер графики для отрисовки игровых объектов.
 */
public class GraphicsManager {
  /**
   * Отрисовывает текстуру в заданных границах с поворотом.
   */
  public void drawTexture(Batch batch, TextureRegion texture, Rectangle bounds, float rotation) {
    // Используем существующий метод из GdxGameUtils
    drawTextureRegionUnscaled(batch, texture, bounds, rotation);
  }

  /**
   * Отрисовывает препятствие.
   */
  public void drawObstacle(Batch batch, Obstacle obstacle) {
    drawTextureRegionUnscaled(batch, obstacle.getGraphics(), obstacle.getBounds(), 0f);
  }
}