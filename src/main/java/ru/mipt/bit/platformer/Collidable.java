package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Интерфейс для объектов, с которыми можно сталкиваться
 */
public interface Collidable {
    GridPoint2 getPosition();
    boolean blocksMovement();
}