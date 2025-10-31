package ru.mipt.bit.platformer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Утилитарный класс для управления графикой игровых объектов.
 * Содержит методы для отрисовки различных типов объектов.
 */
public final class GraphicsManager {

    // Приватный конструктор чтобы предотвратить создание экземпляров
    private GraphicsManager() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    /**
     * Отрисовывает текстуру в заданных границах с поворотом.
     */
    public static void drawTexture(Batch batch, TextureRegion texture, Rectangle bounds, float rotation) {
        drawTextureRegionUnscaled(batch, texture, bounds, rotation);
    }

    /**
     * Отрисовывает препятствие.
     */
    public static void drawObstacle(Batch batch, Obstacle obstacle) {
        drawTextureRegionUnscaled(batch, obstacle.getGraphics(), obstacle.getBounds(), 0f);
    }

    /**
     * Отрисовывает танк с учетом его состояния.
     */
    public static void drawTank(Batch batch, Tank tank) {
        drawTextureRegionUnscaled(batch, tank.getGraphics(), tank.getBounds(), tank.getRotation());
    }
}