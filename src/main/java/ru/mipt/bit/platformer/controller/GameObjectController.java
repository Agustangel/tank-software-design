package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.api.Direction;
import ru.mipt.bit.platformer.api.GameWorld;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.graphics.Graphical;

/**
 * Базовый Controller для всех сущностей
 * SRP: связывает Model и Graphics
 * Model ←→ Controller ←→ Graphics
 */
public abstract class GameObjectController {
    protected final GameObject model;
    protected final Graphical graphics;
    
    public GameObjectController(GameObject model, Graphical graphics) {
        this.model = model;
        this.graphics = graphics;
    }
    
    public GameObject getModel() { return model; }
    public Graphical getGraphics() { return graphics; }
    
    /**
     * Обновить состояние
     */
    public void update(float delta) {
        model.update(delta);
        syncGraphicsWithModel();
    }
    
    /**
     * Отрисовать
     */
    public void render() {
        graphics.updateVisuals();
    }
    
    /**
     * Синхронизировать Graphics с Model
     */
    protected abstract void syncGraphicsWithModel();
    
    /**
     * Движение
     */
    public void move(GameWorld gameWorld, Direction direction) {
        // По умолчанию ничего не делает
    }
    
    /**
     * Стрельба
     */
    public void shoot() {
        // По умолчанию ничего не делает
    }
    
    /**
     * Включить/выключить здоровье
     */
    public void toggleHealthBar() {
        // По умолчанию ничего не делает
    }
}
