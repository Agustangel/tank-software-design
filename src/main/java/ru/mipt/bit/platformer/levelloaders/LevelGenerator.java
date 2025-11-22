package ru.mipt.bit.platformer.levelloaders;

import ru.mipt.bit.platformer.Level;

/**
 * Интерфейс для генераторов уровней.
 */
public interface LevelGenerator {

    /**
     * Генерирует данные уровня 
     */
    Level generateLevel();

    /**
     * Возвращает название генератора для отладки и логирования
     */
    String getName();
}
