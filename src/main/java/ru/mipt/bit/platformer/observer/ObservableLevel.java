package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Наблюдаемый игровой уровень - уведомляет наблюдателей об изменениях
 */
public class ObservableLevel {
    private final List<LevelObserver> observers = new ArrayList<>();
    
    public void addObserver(LevelObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void removeObserver(LevelObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObjectAdded(GameObject object) {
        for (LevelObserver observer : observers) {
            observer.objectAdded(object);
        }
    }
    
    public void notifyObjectRemoved(GameObject object) {
        for (LevelObserver observer : observers) {
            observer.objectRemoved(object);
        }
    }
}
