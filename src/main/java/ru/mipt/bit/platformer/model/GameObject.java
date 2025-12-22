package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Базовый Model для всех сущностей
 * SRP: содержит ТОЛЬКО состояние и логику
 * НЕ ЗНАЕТ о Graphics и LibGDX!
 */
public abstract class GameObject {
    protected GridPoint2 position;
    
    public GameObject(GridPoint2 position) {
        this.position = new GridPoint2(position);
    }
    
    public GridPoint2 getPosition() {
        return position;
    }
    
    public abstract void update(float delta);
}
