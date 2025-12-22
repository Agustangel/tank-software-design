package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import ru.mipt.bit.platformer.api.Direction;
import ru.mipt.bit.platformer.api.GameWorld;
import ru.mipt.bit.platformer.util.GdxGameUtils;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Model для танка
 * Содержит: позицию, здоровье, скорость, вращение
 * НЕ ЗНАЕТ: как отрисовываться, как обрабатывать ввод
 */
public class Tank extends GameObject {
    private final GridPoint2 destination;
    private final float tankSpeed;
    private final int maxHealth;
    private final float bulletSpeed;
    
    private int rotation;
    private int curHealth;
    private float movementProgress;
    private float shootingProgress;
    
    public Tank(GridPoint2 position, float tankSpeed, int maxHealth) {
        super(position);
        this.destination = new GridPoint2(position);
        this.tankSpeed = tankSpeed;
        this.maxHealth = maxHealth;
        this.bulletSpeed = 1.5f * tankSpeed;
        this.rotation = 0;
        this.curHealth = ThreadLocalRandom.current()
            .nextInt((int)(0.8 * maxHealth), maxHealth + 1);
        this.movementProgress = 1.0f;
        this.shootingProgress = 1.0f;
    }
    
    // Getters
    public int getMaxHealth() { return maxHealth; }
    public GridPoint2 getDestination() { return destination; }
    public int getRotation() { return rotation; }
    public int getCurHealth() { return curHealth; }
    public float getMovementProgress() { return movementProgress; }
    public float getShootingProgress() { return shootingProgress; }
    public boolean isAlive() { return curHealth > 0; }
    
    // Logic
    public void decreaseHealth(int damage) {
        curHealth -= damage;
    }
    
    @Override
    public void update(float delta) {
        movementProgress = GdxGameUtils.continueProgress(
            movementProgress, delta, tankSpeed);
        shootingProgress = GdxGameUtils.continueProgress(
            shootingProgress, delta, bulletSpeed);
        
        if (MathUtils.isEqual(movementProgress, 1.0f)) {
            position.set(destination);
        }
    }
    
    public void move(GameWorld gameWorld, Direction direction) {
        if (!isReadyToAct()) return;
        
        int nx = position.x + direction.dx;
        int ny = position.y + direction.dy;
        
        if (gameWorld.isAvailableForMove(nx, ny)) {
            destination.set(nx, ny);
            movementProgress = 0.0f;
            rotation = direction.rotation;
        }
    }
    
    public Optional<Bullet> shoot() {
        if (!isReadyToAct()) return Optional.empty();
        
        Direction direction = Direction.fromRotation(rotation);
        int bulletDamage = ThreadLocalRandom.current().nextInt(1, 10);
        Bullet bullet = new Bullet(
            position, direction, bulletSpeed, bulletDamage);
        
        shootingProgress = 0.0f;
        return Optional.of(bullet);
    }
    
    private boolean isReadyToAct() {
        return MathUtils.isEqual(movementProgress, 1.0f) &&
               MathUtils.isEqual(shootingProgress, 1.0f);
    }
}
