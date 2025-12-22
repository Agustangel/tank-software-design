package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Базовый Decorator для Graphical
 * Decorator Pattern для добавления функциональности к Graphics
 * OCP: можем добавлять новые визуальные эффекты БЕЗ изменения существующего кода
 */
public abstract class GraphicBaseDecorator implements Graphical {
    protected final Graphical wrappedGraphical;
    
    public GraphicBaseDecorator(Graphical wrappedGraphical) {
        this.wrappedGraphical = wrappedGraphical;
    }
    
    @Override
    public void render(Batch batch) {
        wrappedGraphical.render(batch);
    }
    
    @Override
    public GridPoint2 getPosition() {
        return wrappedGraphical.getPosition();
    }
    
    @Override
    public void updateVisuals() {
        wrappedGraphical.updateVisuals();
    }
}
