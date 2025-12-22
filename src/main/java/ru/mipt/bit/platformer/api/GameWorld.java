package ru.mipt.bit.platformer.api;

import java.util.Optional;

/**
 * Интерфейс для работы с игровым миром.
 * Model НЕ ЗНАЕТ о Graphics и Controller!
 */
public interface GameWorld {
    boolean isAvailableForMove(int x, int y);
    
    java.util.List<com.badlogic.gdx.math.GridPoint2> getObstacles();
    
    int getLevelWidth();
    int getLevelHeight();
}
