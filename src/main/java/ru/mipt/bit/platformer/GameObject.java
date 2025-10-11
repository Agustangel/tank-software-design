package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Интерфейс для всех игровых объектов, которые нужно обновлять и отрисовывать
 */
public interface GameObject {
    void update(float deltaTime);
    void render(Batch batch);
    GridPoint2 getPosition();
}