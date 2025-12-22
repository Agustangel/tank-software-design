package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.api.Direction;

/**
 * Model для пули
 * Содержит: позицию, направление, скорость, урон
 */
public class Bullet extends GameObject {
    private final Direction direction;
    private final float speed;
    private final int damage;
    private boolean destroyed;
    private boolean hasBeenRendered;
    
    public Bullet(GridPoint2 position, Direction direction, 
                      float speed, int damage) {
        super(position);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
        this.destroyed = false;
        this.hasBeenRendered = false;
    }
    
    public Direction getDirection() { return direction; }
    public float getSpeed() { return speed; }
    public int getDamage() { return damage; }
    public boolean isDestroyed() { return destroyed; }
    public boolean hasBeenRendered() { return hasBeenRendered; }
    
    public void setHasBeenRendered(boolean rendered) {
        this.hasBeenRendered = rendered;
    }
    
    public void destroy() {
        destroyed = true;
    }
    
    @Override
    public void update(float delta) {
        // Пуля движется каждый кадр
        position.x += direction.dx;
        position.y += direction.dy;
    }
}
