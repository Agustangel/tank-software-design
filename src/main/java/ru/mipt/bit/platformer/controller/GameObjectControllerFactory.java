package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.graphics.*;

/**
 * Factory для создания Controllers
 * SRP: отвечает ТОЛЬКО за создание Controllers
 * Все зависимости инъектируются
 */
public class GameObjectControllerFactory {
    private final float tileWidth;
    private final float tileHeight;
    private final ShapeRenderer shapeRenderer;
    
    public GameObjectControllerFactory(float tileWidth, float tileHeight,
                                  ShapeRenderer shapeRenderer) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.shapeRenderer = shapeRenderer;
    }
    
    public TankController createTankController(TextureRegion texture,
                                                  GridPoint2 position,
                                                  float speed, int maxHealth) {
        Tank model = new Tank(position, speed, maxHealth);
        GameObjectGraphics graphics = new TankGraphics(texture, position, tileWidth, tileHeight);
        return new TankController(model, graphics);
    }
    
    public BulletController createBulletController(TextureRegion texture,
                                                  Bullet model) {
        GameObjectGraphics graphics = new BulletGraphics(texture, model.getPosition(),
                                        tileWidth, tileHeight);
        return new BulletController(model, graphics);
    }
    
    public ObstacleController createObstacleController(TextureRegion texture,
                                                      GridPoint2 position,
                                                      boolean isStatic) {
        Obstacle model = new Obstacle(position, isStatic);
        GameObjectGraphics graphics = new ObstacleGraphics(texture, position, tileWidth, tileHeight);
        return new ObstacleController(model, graphics);
    }
    
    public GameObjectController createBotController(TextureRegion texture,
                                               GridPoint2 position,
                                               float speed, int maxHealth) {
        Tank model = new Tank(position, speed, maxHealth);
        GameObjectGraphics graphics = new TankGraphics(texture, position, tileWidth, tileHeight);
        return new TankController(model, graphics);
    }
}
