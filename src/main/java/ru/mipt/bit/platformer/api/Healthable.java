package ru.mipt.bit.platformer.api;

/**
 * Интерфейс для объектов с здоровьем
 */
public interface Healthable {
    int getHealth();
    void takeDamage(int damage);
    boolean isAlive();
}
