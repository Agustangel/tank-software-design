package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.GdxGameUtils;

/**
 * Базовый Graphics для всех сущностей
 * SRP: отвечает ТОЛЬКО за отрисовку
 */
public abstract class GameObjectGraphics implements Graphical {
    protected final TextureRegion texture;
    protected final GridPoint2 position;
    protected final Rectangle bounds;
    protected float rotation;
    
    public GameObjectGraphics(TextureRegion texture, GridPoint2 position, 
                     float tileWidth, float tileHeight) {
        this.texture = texture;
        this.position = new GridPoint2(position);
        this.bounds = GdxGameUtils.createBoundingRectangle(texture);
        this.rotation = 0;
    }
    
    public TextureRegion getTexture() { return texture; }
    public Rectangle getBounds() { return bounds; }
    public void setRotation(float rotation) { this.rotation = rotation; }
    
    @Override
    public GridPoint2 getPosition() {
        return position;
    }
    
    @Override
    public void render(Batch batch) {
        GdxGameUtils.drawTextureRegionUnscaled(
            batch, texture, bounds, rotation);
    }
    
    @Override
    public void updateVisuals() {
        // Переопределить в подклассах
    }
}
