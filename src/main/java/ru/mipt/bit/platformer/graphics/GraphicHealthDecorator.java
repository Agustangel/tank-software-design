package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Decorator для отображения здоровья
 * Добавляет полоску здоровья к любому Graphical
 */
public class GraphicHealthDecorator extends GraphicBaseDecorator {
    private final ShapeRenderer shapeRenderer;
    private final int maxHealth;
    private int currentHealth;
    private boolean visible;
    
    public GraphicHealthDecorator(Graphical wrappedGraphical, 
                                  ShapeRenderer shapeRenderer,
                                  int maxHealth, int currentHealth) {
        super(wrappedGraphical);
        this.shapeRenderer = shapeRenderer;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.visible = false;
    }
    
    public void setCurrentHealth(int health) {
        this.currentHealth = Math.max(0, health);
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    @Override
    public void render(Batch batch) {
        wrappedGraphical.render(batch);
        
        if (visible) {
            GridPoint2 pos = getPosition();
            drawHealthBar(pos.x, pos.y);
        }
    }
    
    private void drawHealthBar(int x, int y) {
        shapeRenderer.setProjectionMatrix(null);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Background
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y + 20, 20, 3);
        
        // Health
        shapeRenderer.setColor(Color.GREEN);
        float healthPercent = (float) currentHealth / maxHealth;
        shapeRenderer.rect(x, y + 20, 20 * healthPercent, 3);
        
        shapeRenderer.end();
    }
    
    @Override
    public void updateVisuals() {
        super.updateVisuals();
    }
}
