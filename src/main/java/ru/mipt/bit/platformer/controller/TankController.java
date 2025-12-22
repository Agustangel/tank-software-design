package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.api.Direction;
import ru.mipt.bit.platformer.api.GameWorld;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.graphics.GameObjectGraphics;
import ru.mipt.bit.platformer.graphics.Graphical;
import ru.mipt.bit.platformer.graphics.GraphicHealthDecorator;

import java.util.Optional;

/**
 * Controller для танка
 * Связывает Tank Model и Graphics
 * Обрабатывает команды из InputHandler
 */
public class TankController extends GameObjectController {
    private final Tank tankModel;
    private final GameObjectGraphics tankGraphics;
    private Optional<Bullet> lastBullet = Optional.empty();
    private GraphicHealthDecorator healthDecorator;
    
    public TankController(Tank tankModel, GameObjectGraphics tankGraphics) {
        super(tankModel, tankGraphics);
        this.tankModel = tankModel;
        this.tankGraphics = (GameObjectGraphics) tankGraphics;
    }
    
    public void setHealthDecorator(GraphicHealthDecorator decorator) {
        this.healthDecorator = decorator;
    }
    
    @Override
    public void move(GameWorld gameWorld, Direction direction) {
        tankModel.move(gameWorld, direction);
    }
    
    @Override
    public void shoot() {
        Optional<Bullet> bullet = tankModel.shoot();
        if (bullet.isPresent()) {
            lastBullet = bullet;
        }
    }
    
    @Override
    public void toggleHealthBar() {
        if (healthDecorator != null) {
            boolean currentVisible = !healthDecorator.visible;
            healthDecorator.setVisible(currentVisible);
        }
    }
    
    public Optional<Bullet> getLastBullet() {
        return lastBullet;
    }
    
    public void clearLastBullet() {
        lastBullet = Optional.empty();
    }
    
    @Override
    protected void syncGraphicsWithModel() {
        // Синхронизировать позицию
        tankGraphics.getBounds().setCenter(
            tankModel.getPosition().x * 32,
            tankModel.getPosition().y * 32);
        
        // Синхронизировать вращение
        tankGraphics.setRotation(tankModel.getRotation());
        
        // Синхронизировать здоровье
        if (healthDecorator != null) {
            healthDecorator.setCurrentHealth(tankModel.getCurHealth());
        }
    }
}
