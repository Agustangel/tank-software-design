package ru.mipt.bit.platformer;

/**
 * Интерфейс наблюдателя для получения уведомлений об изменениях в игровом уровне
 */
public interface LevelObserver {
    void objectAdded(GameObject object);
    void objectRemoved(GameObject object);
}
