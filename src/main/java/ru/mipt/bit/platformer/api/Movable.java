package ru.mipt.bit.platformer.api;

/**
 * Интерфейс для движимых объектов
 */
public interface Movable {
    void move(GameWorld gameWorld, Direction direction);
}
