package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Model для препятствия
 * Содержит: позицию, является ли статичным
 */
public class Obstacle extends GameObject {
    private final boolean isStatic;
    
    public Obstacle(GridPoint2 position, boolean isStatic) {
        super(position);
        this.isStatic = isStatic;
    }
    
    public boolean isStatic() { return isStatic; }
    
    @Override
    public void update(float delta) {
        // Препятствие не обновляется
    }
}
