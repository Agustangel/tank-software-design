package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.graphics.GameObjectGraphics;

/**
 * Controller для пули
 */
public class BulletController extends GameObjectController {
    private final Bullet bulletModel;
    private final GameObjectGraphics bulletGraphics;
    
    public BulletController(Bullet bulletModel, GameObjectGraphics bulletGraphics) {
        super(bulletModel, bulletGraphics);
        this.bulletModel = bulletModel;
        this.bulletGraphics = (GameObjectGraphics) bulletGraphics;
    }
    
    @Override
    protected void syncGraphicsWithModel() {
        bulletGraphics.getBounds().setCenter(
            bulletModel.getPosition().x * 32,
            bulletModel.getPosition().y * 32);
        
        bulletGraphics.setRotation(bulletModel.getDirection().rotation);
        bulletModel.setHasBeenRendered(true);
    }
    
    public Bullet getModel() {
        return bulletModel;
    }
}
