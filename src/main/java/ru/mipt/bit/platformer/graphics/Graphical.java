package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Интерфейс для отрисовываемых объектов
 * Graphics НЕ ЗНАЕТ о Model и Controller!
 */
public interface Graphical {
    /**
     * Отрисовать объект
     */
    void render(Batch batch);
    
    /**
     * Получить позицию для отрисовки
     */
    GridPoint2 getPosition();
    
    /**
     * Обновить визуальное состояние
     */
    void updateVisuals();
}
