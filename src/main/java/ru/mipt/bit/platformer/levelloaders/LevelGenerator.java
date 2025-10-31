package ru.mipt.bit.platformer;

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