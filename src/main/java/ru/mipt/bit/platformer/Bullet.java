package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

/**
 * Класс пули - летящий снаряд, наносящий урон
 */
public class Bullet implements GameObject, Collidable {
    private final Tank owner;
    private final Direction direction;
    private final float speed;
    private final int damage;
    
    private GridPoint2 position;
    private final Rectangle bounds;
    private final TextureRegion graphics;
    private boolean destroyed = false;
    
    private final int levelWidth;
    private final int levelHeight;
    private final float tileWidth;
    private final float tileHeight;

    // Флаг, указывающий, была ли пуля отрисована хотя бы один раз
    private boolean hasBeenRendered = false;

    public Bullet(Tank owner, Direction direction, GridPoint2 startPosition, 
                 TextureRegion graphics, float speed, int damage, 
                 int levelWidth, int levelHeight, float tileWidth, float tileHeight) {
        this.owner = owner;
        this.direction = direction;
        this.position = new GridPoint2(startPosition);
        this.graphics = graphics;
        this.speed = speed;
        this.damage = damage;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        
        // Устанавливаем нормальный размер пули (30% клетки)
        float bulletSize = 0.3f;
        this.bounds = new Rectangle(
            startPosition.x * tileWidth + tileWidth * (1 - bulletSize) / 2,
            startPosition.y * tileHeight + tileHeight * (1 - bulletSize) / 2,
            tileWidth * bulletSize,
            tileHeight * bulletSize
        );
        
        Gdx.app.log("Bullet", "Bullet created at: " + startPosition);
    }

    @Override
    public void update(float deltaTime) {
        if (destroyed) return;

        // Движение пули в направлении выстрела
        GridPoint2 newPosition = direction.applyTo(position);

        // Проверка выхода за границы уровня
        if (newPosition.x < 0 || newPosition.x >= levelWidth || 
            newPosition.y < 0 || newPosition.y >= levelHeight) {
            destroy();
            return;
        }

        position.set(newPosition);
        updateBounds();
    }

    private void updateBounds() {
        // Обновляем bounds в пиксельных координатах на основе позиции в клетках
        float bulletSize = 0.3f;
        bounds.setPosition(
            position.x * tileWidth + tileWidth * (1 - bulletSize) / 2,
            position.y * tileHeight + tileHeight * (1 - bulletSize) / 2
        );
    }

    @Override
    public void render(Batch batch) {
        if (!destroyed) {
            GraphicsManager.drawBullet(batch, this);
            // Устанавливаем флаг, что пуля была отрисована хотя бы один раз
            hasBeenRendered = true;
        }
    }

    @Override
    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }

    public void destroy() {
        this.destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Tank getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }

    public Direction getDirection() {
        return direction;
    }

    public TextureRegion getGraphics() {
        return graphics;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Возвращает true, если пуля была отрисована хотя бы один раз
     * Это предотвращает удаление пули до того, как она будет отображена на экране
     */
    public boolean hasBeenRendered() {
        return hasBeenRendered;
    }
}
